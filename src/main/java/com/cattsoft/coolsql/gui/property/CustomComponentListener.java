package com.cattsoft.coolsql.gui.property;

import java.awt.Component;
/**
 * This class based on wizard allows user to custom component process.
 * @author ��Т��(kenny liu)
 *
 * 2008-5-4 create
 */
public abstract class CustomComponentListener {
	/**
	 * Indicates that this CustomComponentListener will take responsibility for
	 * noticing events from the passed component, and that the WizardPage should
	 * not try to automatically listen on it (which it can only do for standard
	 * Swing components and their children).
	 * <p>
	 * Note that this method may be called frequently and any test it does
	 * should be fast.
	 * <p>
	 * <b>Important:</b> The return value from this method should always be the
	 * same for any given component, for the lifetime of the WizardPage.
	 * 
	 * @param c
	 *            A component
	 * @return Whether or not this CustomComponentListener will listen on the
	 *         passed component. If true, the component will later be passed to
	 *         <code>startListeningTo()</code>
	 */
	public abstract boolean accept(Component c);
	/**
	 * Begin listening for events on the component. When an event occurs, call
	 * the <code>eventOccurred()</code> method on the passed
	 * <code>CustomComponentNotifier</code>.
	 * 
	 * @param c
	 *            The component to start listening to
	 * @param n
	 *            An object that can be called to update the settings map when
	 *            an interesting event occurs on the component
	 */
	public abstract void startListeningTo(Component c);
	/**
	 * Stop listening for events on a component.
	 * 
	 * @param c
	 *            The component to stop listening to
	 */
	public abstract void stopListeningTo(Component c);
	/**
	 * Determine if the passed component is a container whose children may need
	 * to be listened on. Returns false by default.
	 * 
	 * @param c
	 *            A component which might be a container
	 */
	public boolean isContainer(Component c) {
		return false;
	}
	/**
	 * Get the map key for this component's value. By default, returns the
	 * component's name. Will only be passed components which the
	 * <code>accept()</code> method returned true for.
	 * <p>
	 * <b>Important:</b> The return value from this method should always be the
	 * same for any given component, for the lifetime of the WizardPage.
	 * 
	 * @param c
	 *            the component, which the accept method earlier returned true
	 *            for
	 * @return A string key that should be used in the Wizard's settings map for
	 *         the name of this component's value
	 */
	public String keyFor(Component c) {
		return c.getName();
	}
	/**
	 * Get the value currently set on the passed component. Will only be passed
	 * components which the <code>accept()</code> method returned true for,
	 * and which <code>keyFor()</code> returned non-null.
	 * 
	 * @param c
	 *            the component
	 * @return An object representing the current value of this component. For
	 *         example, if it were a <code>JTextComponent</code>, the value
	 *         would likely be the return value of
	 *         <code>JTextComponent.getText()</code>
	 */
	public abstract Object valueFor(Component c);
}