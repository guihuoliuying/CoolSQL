package com.cattsoft.coolsql.system;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cattsoft.coolsql.pub.display.PropertyStorage;
import com.cattsoft.coolsql.pub.util.EncodingUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;

public class ParamProperties extends Properties implements PropertyStorage {

	private static final long serialVersionUID = 1L;
	private Map<String,List<PropertyChangeListener>> changeListeners = new HashMap<String,List<PropertyChangeListener>>();
	private Object changeNotificationSource = null;

//	private int distinctSections;
	protected ParamProperties() {
		this(null, 2);
	}

	public ParamProperties(Object notificationSource) {
		this(notificationSource,2);
	}
	public ParamProperties(Object notificationSource, int num) {
		this.changeNotificationSource = (notificationSource == null
				? this
				: notificationSource);
//		this.distinctSections = num;
	}

	public int getIntProperty(String property, int defaultValue) {
		String value = this.getProperty(property, null);
		if (value == null)
			return defaultValue;
		return StringUtil.getIntValue(value, defaultValue);
	}
	public long getLongProperty(String property, long defaultValue) {
		String value = this.getProperty(property, null);
		if (value == null)
			return defaultValue;
		return StringUtil.getLongValue(value, defaultValue);
	}
	public boolean getBoolProperty(String property, boolean defaultValue) {
		String value = this.getProperty(property, null);
		if (value == null)
			return defaultValue;
		return StringUtil.stringToBool(value);
	}

	public Object setProperty(String property, int value) {
		return this.setProperty(property, Integer.toString(value));
	}

	public Object setProperty(String property, boolean value) {
		return this.setProperty(property, Boolean.toString(value));
	}
	public Object setProperty(String name, String value) {
		return setProperty(name, value, true);
	}

	public Object setProperty(String name, String value, boolean firePropChange) {
		if (name == null)
			return null;

		String oldValue = null;

		synchronized (this) {
			if (value == null) {
				super.remove(name);
				return null;
			}
			oldValue = (String) super.setProperty(name, value);
		}

		if (firePropChange && !StringUtil.equalString(oldValue, value)) {
			this.firePropertyChanged(name, oldValue, value);
		}
		return oldValue;
	}
	/**
	 * Adds a property definition in the form key=value Lines starting with #
	 * are ignored Lines that do not contain a = character are ignored Any text
	 * after a # sign in the value is ignored
	 */
	public void addPropertyDefinition(String line) {
		if (line == null)
			return;
		if (line.trim().length() == 0)
			return;
		if (line.startsWith("#"))
			return;
		int pos = line.indexOf("=");
		if (pos == -1)
			return;
		String key = line.substring(0, pos);
		String value = line.substring(pos + 1);
		pos = value.indexOf('#');
		if (pos > -1) {
			value = value.substring(0, pos);
		}
		this.setProperty(key, value.trim());
	}

	public void loadTextFile(String filename) throws IOException {
		loadTextFile(filename, null);
	}

	/**
	 * Read the content of the file into this properties object. This method
	 * does not support line continuation, but supports an encoding (as opposed
	 * to the original properties class)
	 * 
	 */
	public void loadTextFile(String filename, String encoding)
			throws IOException {
		BufferedReader in = null;
		File f = new File(filename);
		if (encoding == null) {
			encoding = EncodingUtil.getDefaultEncoding();
		}
		try {
			in = EncodingUtil.createBufferedReader(f, encoding);
			String line = in.readLine();
			while (line != null) {
				this.addPropertyDefinition(StringUtil.decodeUnicode(line));
				line = in.readLine();
			}
		} finally {
			try {
				in.close();
			} catch (Throwable th) {
			}
		}
	}
	public void addPropertyChangeListener(PropertyChangeListener aListener,
			String property) {
		addPropertyChangeListener(aListener, new String[]{property});
	}
	public void addPropertyChangeListener(PropertyChangeListener aListener,
			String[] properties) {
		synchronized (this.changeListeners) {
			for (int i = 0; i < properties.length; i++) {
				String prop = properties[i];
				if(prop==null)
					continue;
				List<PropertyChangeListener> listeners = this.changeListeners.get(prop);
				if (listeners == null) {
					listeners = new ArrayList<PropertyChangeListener>();
					this.changeListeners.put(prop, listeners);
				}
				listeners.add(aListener);
			}
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener aListener) {
		synchronized (this.changeListeners) {
			List listeners = (List) changeListeners.values();
			for (int i = 0; i < listeners.size(); i++) {
				List tmp = (List) listeners.get(i);
				if (tmp != null) {
					tmp.remove(aListener);
				}
			}
		}
	}

	private void firePropertyChanged(String name, String oldValue,
			String newValue) {
		List listeners = (List) this.changeListeners.get(name);
		if (listeners == null || listeners.size() == 0)
			return;

		// Making a shallow copy of the list prevents a
		// ConcurrentModificationException
		List l2 = new ArrayList(listeners);
		PropertyChangeEvent evt = new PropertyChangeEvent(this, name, oldValue,
				newValue);

		for (int i = 0; i < l2.size(); i++) {
			PropertyChangeListener l = (PropertyChangeListener) l2.get(i);
			if (l != null) {
				l.propertyChange(evt);
			}
		}
	}
	public synchronized void saveToFile(String filename) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filename);
			this.save(out);
		} finally {
			out.close();
		}
	}
	public synchronized void save(OutputStream out) throws IOException {
		Object[] keys = this.keySet().toArray();
		Arrays.sort(keys);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		String value = null;
//		String lastKey = null;
		String key = null;
		for (int i = 0; i < keys.length; i++) {
			key = (String) keys[i];

//			if (lastKey != null) {
//				String k1 = null;
//				String k2 = null;
//				k1 = getSections(lastKey, this.distinctSections); // getFirstTwoSections(lastKey);
//				k2 = getSections(key, this.distinctSections); // getFirstTwoSections(key);
//				if (!k1.equals(k2)) {
//					bw.newLine();
//				}
//			}
			Object v = this.get(key);
			if (v != null) {
				value = v.toString();
				value = StringUtil.replace(value, "\\", "\\\\");
				if (value.indexOf('\n') > -1) {
					value = value.replaceAll("\n", "\\\\n");
				}
				if (value.length() > 0) {
					bw.write(key + "=" + value);
					bw.newLine();
				} else {
					bw.write(key + "=");
					bw.newLine();
				}
			} else {
				bw.write(key + "=");
				bw.newLine();
			}
//			lastKey = key;
		}
		bw.flush();
	}
	private String getSections(String aString, int aNum) {
		int pos = aString.indexOf(".");
		String result = null;
		for (int i = 1; i < aNum; i++) {
			int pos2 = aString.indexOf('.', pos + 1);
			if (pos2 > -1) {
				pos = pos2;
			} else {
				if (i == (aNum - 1)) {
					pos = aString.length();
				}
			}
		}
		result = aString.substring(0, pos);
		return result;
	}
}
