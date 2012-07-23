package com.cattsoft.coolsql.pub.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cattsoft.coolsql.pub.component.DateSelector;
import com.cattsoft.coolsql.pub.display.NumberStringCache;
import com.cattsoft.coolsql.pub.parse.PublicResource;


/**
 */
public class StringUtil
{
	public static final String REGEX_CRLF = "(\r\n|\n\r|\r|\n)";
	public static final Pattern PATTERN_CRLF = Pattern.compile(REGEX_CRLF);
	public static final Pattern PATTERN_NON_LF = Pattern.compile("(\r\n|\n\r|\r)");

	public static final String LINE_TERMINATOR = System.getProperty("line.separator");
	public static final String EMPTY_STRING = "";

	public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
	public static final String ISO_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String ISO_TZ_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS z";
	
	public static final SimpleDateFormat ISO_TIMESTAMP_FORMATTER = new SimpleDateFormat(ISO_TIMESTAMP_FORMAT);
	public static final SimpleDateFormat ISO_TZ_TIMESTAMP_FORMATTER = new SimpleDateFormat(ISO_TZ_TIMESTAMP_FORMAT);

	public static final StringBuilder emptyBuffer() { return new StringBuilder(0); }
	
	public static final String getCurrentTimestampWithTZString()
	{
		return ISO_TZ_TIMESTAMP_FORMATTER.format(now());
	}

	private static final java.util.Date now()
	{
		return new java.util.Date(System.currentTimeMillis());
	}
	
	public static String getPathSeparator()
	{
		return System.getProperty("path.separator");
	}
	
	public static int hashCode(CharSequence val)
	{
		int len = val.length();
		int hash = 0;
		
		for (int i = 0; i < len; i++)
		{
			hash = 31*hash + val.charAt(i);
		}
		return hash;
	}
	
	public static String makePlainLinefeed(String input)
	{
		Matcher m = PATTERN_NON_LF.matcher(input);
		return m.replaceAll("\n");
	}
	
	public static boolean endsWith(CharSequence s, char c)
	{
		if (s == null) return false;
		int len = s.length();
		if (len == 0) return false;
		return s.charAt(len - 1) == c;
	}
		
	public static boolean lineStartsWith(CharSequence text, int lineStartPos, String compareTo)
	{
		if (isWhitespaceOrEmpty(compareTo)) return false;
		int textLength = text.length();
		
		int len = compareTo.length();
		for (int i=0; i < len; i++)
		{
			char thisChar = 0;
			char otherChar = compareTo.charAt(i);
			if (lineStartPos + i < textLength)
			{
				thisChar = text.charAt(lineStartPos + i);
			}
			if (thisChar != otherChar) return false;
		}
		return true;
	}
	
	/**
	 * Returns the length of the line without any line ending characters
	 */
	public static int getRealLineLength(String line)
	{
		int len = line.length();
		if (len == 0) return 0;
		
		char c = line.charAt(len - 1);

		while ((len > 0) && (c == '\r' || c == '\n')) 
		{
			len--;
			if (len > 0) c = line.charAt(len - 1);
		}
		return len;
	}
	
	public static boolean isWhitespace(CharSequence s)
	{
		if (s == null) return false;
		int len = s.length();
		if (len == 0) return false;
		int pos = 0;
		while (pos < len)
		{
			char c = s.charAt(pos);
			if (!Character.isWhitespace(c)) return false;
			pos++;
		}
		return true;
	}
	
	public static String rtrim(String s)
	{
		if (s == null) return s;
		int pos = s.length();
		if (pos == 0) return s;
		
		while (pos > 0 && Character.isWhitespace(s.charAt(pos - 1)))
		{
			pos --;
		}
		
		return s.substring(0, pos);
	}
	
	public static int indexOf(CharSequence value, char c)
	{
		return indexOf(value, c, 1);
	}
	
	public static int indexOf(CharSequence value, char c, int occurance)
	{
		if (value == null) return -1;
		if (occurance <= 0) occurance = 1;
		int numFound = 0;
		
		for (int i=0; i < value.length(); i++)
		{
			if (value.charAt(i) == c)
			{
				numFound++;
				if (numFound == occurance) return i;
			}
		}
		return -1;
	}
					
	public static void trimTrailingWhitespace(StringBuilder value)
	{
		if (value == null || value.length() == 0) return;
		int len = value.length(); 
		int pos = len - 1;
		char c = value.charAt(pos);
		if (!Character.isWhitespace(c)) return;
		
		while (Character.isWhitespace(c))
		{
			pos --;
			c = value.charAt(pos);
		}
		value.delete(pos + 1, len);
	}
	
	public static boolean isMixedCase(String s)
	{
		return !isUpperCase(s) && !isLowerCase(s);
	}

	public static boolean isLowerCase(String s)
	{
		if (s == null) return false;
		int l = s.length();
		for (int i = 0; i < l; i++)
		{
			char c = s.charAt(i);
			if (Character.isUpperCase(c)) return false;
		}
		return true;
	}
	
	public static boolean isUpperCase(String s)
	{
		if (s == null) return false;
		int l = s.length();
		for (int i = 0; i < l; i++)
		{
			char c = s.charAt(i);
			if (Character.isLowerCase(c)) return false;
		}
		return true;
	}
	
	public static Comparator<String> getCaseInsensitiveComparator()
	{
		return new Comparator<String>()
		{
			public int compare(String value1, String value2)
			{
				if (value1 == null && value2 == null) return 0;
				if (value1 == null) return -1;
				if (value2 == null) return 1;
				return value1.compareToIgnoreCase(value2);
			}
		};
	}

	public static final boolean arraysEqual(String[] one, String[] other)
	{
		if (one == null && other != null) return false;
		if (one != null && other == null) return false;
		if (one.length != other.length) return false;
		for (int i = 0; i < one.length; i++)
		{
			if (!equalString(one[i],other[i])) return false;
		}
		return true;
	}
	
	public static final boolean hasOpenQuotes(String data, char quoteChar)
	{
		if (isEmptyString(data)) return false;
		int chars = data.length();
		boolean inQuotes = false;
		for (int i = 0; i < chars; i++)
		{
			if (data.charAt(i) == quoteChar) inQuotes = !inQuotes;
		}
		return inQuotes;
	}
	
	/**
	 * Capitalize a single word.
	 * (write the first character in uppercase, the rest in lower case)
	 * This does not loop through the entire string to capitalize every word.
	 */
	public static final String capitalize(String word)
	{
		if (word == null) return null;
		if (word.length() == 0) return word;
		StringBuilder s = new StringBuilder(word.toLowerCase());
		char c = s.charAt(0);
		s.setCharAt(0, Character.toUpperCase(c));
		return s.toString();
	}
	
	/**
	 * Remove all characters that might not be allowed in a filename from the input string.
	 * @param input the value to be used as a filename
	 * @return input value without any characters that might not be allowed for a filename
	 */
	public static final String makeFilename(String input)
	{
		return input.replaceAll("[\t\\:\\\\/\\?\\*\\|<>\"'\\{\\}$\\[\\]\\^|\\&]", "");
	}

	/**
	 * Replaces all occurances of aValue in aString with aReplacement and appends the resulting
	 * String to the passed target.
	 * @param target the buffer to append the result to. If target is null a new StringBuilder will be created
	 * @param haystack the String in which to search the value
	 * @param needle the value to search for
	 * @param replacement the value with which needle is replaced
	 * @return the resulting buffer
	 */
	public static final StringBuilder replaceToBuffer(StringBuilder target, String haystack, String needle,String replacement)
	{
		if (target == null)
		{
			target = new StringBuilder((int)(haystack.length() * 1.1));
		}

		int pos = haystack.indexOf(needle);
		if (pos == -1)
		{
			target.append(haystack);
			return target;
		}

		int lastpos = 0;
		int len = needle.length();
		while (pos != -1)
		{
			target.append(haystack.substring(lastpos, pos));
			if (replacement != null) target.append(replacement);
			lastpos = pos + len;
			pos = haystack.indexOf(needle, lastpos);
		}
		if (lastpos < haystack.length())
		{
			target.append(haystack.substring(lastpos));
		}
		return target;
	}

	/**
	 * Replacement for StringBuilder.lastIndexOf() which does
	 * a lot of object creation and copying to achieve this.
	 * This implementation should be a lot faster for StringBuilder
	 * and StringBuffer, and will basically be the same for String
	 * objects.
	 * 
	 * @param s the string to search in 
	 * @param c the character to look for 
	 * @return -1 if c was not found, the position of c in s otherwise
	 */
	public static final int lastIndexOf(CharSequence s, char c)
	{
		int len = s.length();
		if (s == null || len == 0) return -1;
		
		for (int i=(len - 1); i > 0; i--)
		{
			if (s.charAt(i) == c) return i;
		}
		return -1;
	}
	
	public static final String replace(String haystack, String needle, String replacement)
	{
		if (replacement == null) return haystack;
		if (needle == null) return haystack;
		if (haystack == null) return null;
		
		int pos = haystack.indexOf(needle);
		if (pos == -1) return haystack;

		StringBuilder temp = replaceToBuffer(null,haystack,needle, replacement);

		return temp.toString();
	}

	static final int[] limits =
	{
		9,99,999,9999,99999,999999,9999999,99999999,999999999,Integer.MAX_VALUE
	};

	/**
	 * Returns the number of Digits of the value
	 * @param x the value to check
	 * @return the number of digits that x consists of
	 */
	public static int numDigits(int x )
	{
		for (int i = 0; i < limits.length; i++)
		{
			if ( x <= limits[i])
			{
				return i+1;
			}
		}
		return limits.length + 1;
	}
	
	public static boolean isInteger(String value)
	{
		if (isEmptyString(value)) return false;
		String s = value.trim();
		int l = s.length();
		for (int i=0; i < l; i++)
		{
			if (!Character.isDigit(s.charAt(i))) return false;
		}
		return true;
	}
	
	public static boolean isNumber(String value)
	{
		try
		{
			 Double.parseDouble(value);
			 return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Checks if the given parameter is "empty", i.e: either null, length == 0 or 
	 * only whitespace
	 */
	public static final boolean isWhitespaceOrEmpty(CharSequence value)
	{
		if (isEmptyString(value)) return true;
		return isWhitespace(value);
	}
	
	public static final boolean isEmptyString(CharSequence value)
	{
		if (value == null) return true;
		if (value.length() == 0) return true;
		return false;
	}
	
	public static final String getStartingWhiteSpace(final String aLine)
	{
		if (aLine == null) return null;
		int pos = 0;
		int len = aLine.length();
		if (len == 0) return "";

		char c = aLine.charAt(pos);
		while (c <= ' ' && pos < len - 1)
		{
			pos ++;
			c = aLine.charAt(pos);
		}
		String result = aLine.substring(0, pos);
		return result;
	}

	public static double getDoubleValue(String aValue, double aDefault)
	{
		if (aValue == null) return aDefault;

		double result = aDefault;
		try
		{
			result = Double.parseDouble(aValue.trim());
		}
		catch (NumberFormatException e)
		{
			// Ignore
		}
		return result;
	}

	public static int getIntValue(String aValue)
	{
		return getIntValue(aValue, 0);
	}

	public static int getIntValue(String aValue, int aDefault)
	{
		if (aValue == null) return aDefault;

		int result = aDefault;
		try
		{
			result = Integer.parseInt(aValue.trim());
		}
		catch (NumberFormatException e)
		{
			// Ignore
		}
		return result;
	}

	public static long getLongValue(String aValue, long aDefault)
	{
		if (aValue == null) return aDefault;

		long result = aDefault;
		try
		{
			result = Long.parseLong(aValue.trim());
		}
		catch (NumberFormatException e)
		{
			// Ignore
		}
		return result;
	}

	/**
	 * Checks if both Strings are equals considering null values. 
	 * a null String and an empty String (length==0 or all whitespace) are
	 * considered equal as well (because both are "empty")
	 * @see #isWhitespaceOrEmpty(CharSequence)
	 */
	public static final boolean equalStringOrEmpty(String one, String other)
	{
		if (isWhitespaceOrEmpty(one) && isWhitespaceOrEmpty(other)) return true;
		return equalString(one, other);
	}
	
	public static final boolean equalString(String one, String other)
	{
		return compareStrings(one, other, false) == 0;
	}

	public static int compareStrings(String value1, String value2, boolean ignoreCase)
	{
		if (value1 == null && value2 == null) return 0;
		if (value1 == null) return -1;
		if (value2 == null) return 1;
		if (ignoreCase) return value1.compareToIgnoreCase(value2);
		return value1.compareTo(value2);
	}
	
	public static final boolean equalStringIgnoreCase(String one, String other)
	{
		if (one == null && other == null) return true;
		if (one == null || other == null) return false;
		return one.equalsIgnoreCase(other);
	}

	public static final List<String> stringToList(String aString)
	{
		return stringToList(aString, ",");
	}
	
	public static final List<String> stringToList(String aString, String aDelimiter)
	{
		return stringToList(aString, aDelimiter, false, false);
	}

	public static final List<String> stringToList(String aString, String aDelimiter, boolean removeEmpty)
	{
		return stringToList(aString, aDelimiter, removeEmpty, false);
	}
	
	public static final List<String> stringToList(String aString, String aDelimiter, boolean removeEmpty, boolean trimEntries)
	{
		return stringToList(aString, aDelimiter, removeEmpty, trimEntries, false);
	}
	/**
	 * Parses the given String and creates a List containing the elements
	 * of the string that are separated by <tt>aDelimiter</aa>
	 *
	 * @param aString the value to be parsed
	 * @param aDelimiter the delimiter to user
	 * @param removeEmpty flag to remove empty entries
	 * @param trimEntries flag to trim entries (will be applied beore checking for empty entries)
   * @param checkBrackets flag to check for opening and closing brackets (delimiter inside brackets will not be taken into account)
	 * @return A List of Strings
	 */
	public static final List<String> stringToList(String aString, String aDelimiter, boolean removeEmpty, boolean trimEntries, boolean checkBrackets)
	{
		if (isEmptyString(aString)) return Collections.emptyList();
		OStringTokenizer tok = new OStringTokenizer(aString, aDelimiter);
		tok.setDelimiterNeedsWhitspace(false);
		tok.setCheckBrackets(checkBrackets);
		List<String> result = new LinkedList<String>();
		while (tok.hasMoreTokens())
		{
			String element = tok.nextToken();
			if (element == null) continue;
			if (trimEntries) element = element.trim();
			if (removeEmpty && isEmptyString(element)) continue;
			result.add(element);
		}
		return result;
	}

	/**
	 * Returns all DB Object names from the comma separated list.
	 * This is different to stringToList() as it keeps any quotes that 
	 * are present in the list.
	 * 
	 * @param list a comma separated list of elements (optionally with quotes)
	 * @return a List of Strings as defined by the input string
	 */
	public static final List<String> getObjectNames(String list)
	{
		if (isEmptyString(list)) return Collections.emptyList();
		OStringTokenizer tok = new OStringTokenizer(list, ",");
		tok.setDelimiterNeedsWhitspace(false);
		tok.setCheckBrackets(false);
		tok.setKeepQuotes(true);
		List<String> result = new LinkedList<String>();
		while (tok.hasMoreTokens())
		{
			String element = tok.nextToken();
			if (element == null) continue;
			element = element.trim();
			if (element.length() > 0)
			{
				result.add(element);
			}
		}
		return result;
	}

	
	public static final String[] toArray(Collection<String> strings)
	{
		if (strings == null) return null;
		if (strings.size() == 0) return new String[0];

		int i = 0;
		String[] result = new String[strings.size()];
		for (String s : strings)
		{
			result[i++] = s;
		}
		return result;
	}
	
	/**
	 * Create a String from the given Collection, where the elements are delimited
	 * with the supplied delimiter
	 * 
	 * @return The elements of the Collection as a String
	 * @param aList The list to process
	 * @param aDelimiter The delimiter to use
	 */
	public static final String listToString(Collection aList, char aDelimiter)
	{
		return listToString(aList, aDelimiter, false);
	}
	
	/**
	 * Create a String from the given list, where the elements are delimited
	 * with the supplied delimiter
	 * 
	 * @return The elements of the Collection as a String
	 * @param aList The list to process
	 * @param aDelimiter The delimiter to use
	 * @param quoteEntries if true, all entries are quoted with a double quote
	 */
	public static final String listToString(Collection aList, char aDelimiter, boolean quoteEntries)
	{
		if (aList == null || aList.size() == 0) return "";
		int numElements = 0;
		StringBuilder result = new StringBuilder(aList.size() * 50);
		Iterator itr = aList.iterator();
		for (Object o : aList)
		{
			if (o == null) continue;
			if (numElements > 0)
			{
				result.append(aDelimiter);
			}
			if (quoteEntries) result.append('"');
			result.append(o.toString());
			if (quoteEntries) result.append('"');
			numElements ++;
		}
		return result.toString();
	}


	public static final String makeJavaString(String sql, String prefix, boolean includeNewLines)
	{
		if (sql == null) return "";
		if (prefix == null) prefix = "";
		StringBuilder result = new StringBuilder(sql.length() + prefix.length() + 10);
		result.append(prefix);
		if (prefix.endsWith("=")) result.append(" ");
		int k = result.length();
		StringBuilder indent = new StringBuilder(k);
		for (int i=0; i < k; i++) indent.append(' ');
		BufferedReader reader = new BufferedReader(new StringReader(sql));
		boolean first = true;
		try
		{
			String line = reader.readLine();
			while (line != null)
			{
				line = replace(line, "\"", "\\\"");
				if (first) first = false;
				else result.append(indent);
				result.append('"');
				if (line.endsWith(";"))
				{
					line = line.substring(0, line.length() - 1);
				}
				result.append(line);

				line = reader.readLine();
				if (line != null)
				{
					if (includeNewLines)
					{
						result.append(" \\n\"");
					}
					else
					{
						result.append(" \"");
					}
					result.append(" + \n");
				}
				else
				{
					result.append("\"");
				}
			}
			result.append(';');
		}
		catch (Exception e)
		{
			result.append("(Error when creating Java code, see logfile for details)");
			e.printStackTrace();
		}
		finally
		{
			FileUtil.closeQuitely(reader);
		}
		return result.toString();
	}

	
	public static final String cleanJavaString(String aString)
	{
		if (isEmptyString(aString)) return "";
		// a regex to find escaped newlines in the literal
		Pattern newline = Pattern.compile("\\\\n|\\\\r");
		String lines[] = PATTERN_CRLF.split(aString);
		StringBuilder result = new StringBuilder(aString.length());
		int count = lines.length;
		for (int i=0; i < count; i ++)
		{
			//String l = (String)lines.get(i);
			String l = lines[i];
			if (l == null) continue;
			if (l.trim().startsWith("//"))
			{
				l = l.replaceFirst("//", "--");
			}
			else
			{
				l = l.trim();
				//if (l.startsWith("\"")) start = 1;
				int start = l.indexOf("\"");
				int end = l.lastIndexOf("\"");
				if (end == start) start = 1;
				if (end == 0) end = l.length() - 1;
				if (start > -1) start ++;
				if (start > -1 && end > -1)
				{
					l = l.substring(start, end);
				}
			}
			Matcher m = newline.matcher(l);
			l = m.replaceAll("");
			l = replace(l,"\\\"", "\"");
			result.append(l);
			if (i < count - 1) result.append('\n');
		}
		return result.toString();
	}

	public static final String trimQuotes(String input)
	{
		if (input == null) return null;
		if (input.length() == 0) return EMPTY_STRING;
		if (input.length() == 1) return input;

		String result = input.trim();
		int len = result.length();
		if (len == 0) return EMPTY_STRING;
		if (len == 1) return input;

		char firstChar = result.charAt(0);
		char lastChar = result.charAt(len - 1);

		if ( (firstChar == '"' && lastChar == '"') ||
		     (firstChar == '\'' && lastChar == '\''))
		{
			return result.substring(1, len - 1);
		}
		return input;
	}

	public static final String escapeXML(String s)
	{
		StringBuilder result = null;

		for(int i = 0, max = s.length(), delta = 0; i < max; i++)
		{
			char c = s.charAt(i);
			String replacement = null;

			switch (c)
			{
				case '&': replacement = "&amp;"; break;
				case '<': replacement = "&lt;"; break;
				case '\r': replacement = "&#13;"; break;
				case '\n': replacement = "&#10;"; break;
				case '>': replacement = "&gt;"; break;
				case '"': replacement = "&quot;"; break;
				case '\'': replacement = "&apos;"; break;
				case (char)0: replacement = ""; break;
			}

			if (replacement != null)
			{
				if (result == null)
				{
					result = new StringBuilder(s);
				}
				result.replace(i + delta, i + delta + 1, replacement);
				delta += (replacement.length() - 1);
			}
		}
		if (result == null)
		{
			return s;
		}
		return result.toString();

	}

	public static boolean stringToBool(String aString)
	{
		if (aString == null) return false;
		return ("true".equalsIgnoreCase(aString) || "1".equals(aString) || "y".equalsIgnoreCase(aString) || "yes".equalsIgnoreCase(aString) );
	}

	public static final StringBuilder getLines(String s, int lineCount)
	{
		StringBuilder result = new StringBuilder(lineCount * 100);
		try
		{
			BufferedReader r = new BufferedReader(new StringReader(s));
			int lines = 0;
			String line = r.readLine();
			while (line != null && lines < lineCount)
			{
				result.append(line);
				result.append('\n');
				lines ++;
				line = r.readLine();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static final String getMaxSubstring(String s, int maxLen, String add)
	{
		if (maxLen < 1) return s;
		if (s == null) return null;
		if (s.length() < maxLen) return s;
		if (add == null)
		{
			return s.substring(0, maxLen);
		}
		else
		{
			return s.substring(0, maxLen) + add;
		}
	}
	
	public static final String getMaxSubstring(String s, int maxLen)
	{
		return getMaxSubstring(s, maxLen, "...");
	}

	public static final String REGEX_SPECIAL_CHARS = "\\[](){}.*+?$^|";

	/**
	 * 	Quote the characters in a String that have a special meaning
	 *  in regular expression.
	 */
	public static String quoteRegexMeta(String str)
	{
		if (str == null) return null;
		if (str.length() == 0)
		{
			return "";
		}
		int len = str.length();
		StringBuilder buf = new StringBuilder(len + 5);
		for (int i = 0; i < len; i++)
		{
			char c = str.charAt(i);
			if (REGEX_SPECIAL_CHARS.indexOf(c) != -1)
			{
				buf.append('\\');
			}
			buf.append(c);
		}
		return buf.toString();
	}

	public static int findPreviousWhitespace(String data, int pos)
	{
		if (data == null) return -1;
		int count = data.length();
		if (pos > count || pos <= 1) return -1;
		for (int i=pos; i > 0; i--)
		{
			if (Character.isWhitespace(data.charAt(i))) return i;
		}
		return -1;
	}

	public static int findWordBoundary(String data, int pos, String wordBoundaries)
	{
		if (wordBoundaries == null) return findPreviousWhitespace(data, pos);
		if (data == null) return -1;
		int count = data.length();
		if (pos > count || pos <= 1) return -1;
		for (int i=pos; i > 0; i--)
		{
			char c = data.charAt(i);
			if (wordBoundaries.indexOf(c) > -1 || Character.isWhitespace(c)) return i;
		}
		return -1;
	}
	
	/**
	 * Find the first non-quoted whitespace in the given String.
	 * 
	 * @param data the data in which to search 
	 * @return the position of the first whitespace or -1 if no whitespace was found.
	 */
	public static int findFirstWhiteSpace(CharSequence data)
	{
		if (data == null) return -1;
		int count = data.length();
		boolean inQuotes = false;
		for (int i=0; i < count; i++)
		{
			char c = data.charAt(i);
			if (c == '"') 
			{
				inQuotes = !inQuotes;
			}
			if (!inQuotes)
			{
				if (Character.isWhitespace(data.charAt(i))) return i;
			}
		}
		return -1;
	}

	public static final String getWordLeftOfCursor(String text, int pos, String wordBoundaries)
	{
		try
		{
			if (pos < 0) return null;
			int len = text.length();
			int testPos = -1;
			if (pos >= len) 
			{
				testPos = len - 1;
			}
			else
			{
				testPos = pos - 1;
			}
			if (testPos < 1) return null;

			if (Character.isWhitespace(text.charAt(testPos))) return null;

			String word = null;
			int startOfWord = findWordBoundary(text, testPos, wordBoundaries);
			if (startOfWord > 0)
			{
				word = text.substring(startOfWord+1, Math.min(pos,len));
			}
			return word;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	public static int findPattern(String regex, String data, int startAt)
	{
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		return findPattern(p, data, startAt);
	}
	
	public static int findPattern(Pattern p, String data, int startAt)
	{
		if (startAt < 0) return -1;
		Matcher m = p.matcher(data);
		int result = -1;
		if (m.find(startAt)) result = m.start();
		return result;
	}

	public static String decodeUnicode(String theString)
	{
		if (theString == null) return null;
		
		char aChar;
		int len = theString.length();
		if (len == 0) return theString;
		StringBuilder outBuffer = new StringBuilder(len);

		for (int x=0; x < len ; )
		{
			aChar = theString.charAt(x++);
			if (aChar == '\\' && x < len)
			{
				aChar = theString.charAt(x++);
				
				if (aChar == 'u')
				{
					// Read the xxxx
					int value = -1;
					int i=0;
					for (i=0; i<4; i++)
					{
						if ( x + i >= len) 
						{
							value = -1;
							break;
						}
						
						aChar = theString.charAt(x + i);
						switch (aChar)
						{
							case '0': case '1': case '2': case '3': case '4':
							case '5': case '6': case '7': case '8': case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a': case 'b': case 'c':
							case 'd': case 'e': case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A': case 'B': case 'C':
							case 'D': case 'E': case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								// Invalid ecape sequence
								value = -1;
								break;
						}
						if (value == -1) break;
					}
					
					if ( value != -1)
					{
						outBuffer.append((char)value);
					}
					else
					{
						// Invalid encoded unicode character
						// do not convert the stuff, but copy the 
						// characters into the result buffer
						outBuffer.append("\\u");
						if (i == 0 && x < len) 
						{
							outBuffer.append(aChar);
						}
						else
						{
							for (int k=0; k < i; k++) outBuffer.append(theString.charAt(x+k));
						}
						x++;
					}
					x += i;
				}
				else
				{
					// The character after the backslash was not a 'u'
					// so we are not dealing with a uXXXX value
					// This applies popular "encodings" for non-printable characters
					if (aChar == 't') aChar = '\t';
					else if (aChar == 'r') aChar = '\r';
					else if (aChar == 'n') aChar = '\n';
					else if (aChar == 'f') aChar = '\f';
					else if (aChar == '\\') aChar = '\\';
					else outBuffer.append('\\'); 
					outBuffer.append(aChar); 
				}
			}
			else
			{
				outBuffer.append(aChar);
			}

		}
		return outBuffer.toString();
	}

	public static void dump(String value)
	{
		int size = value.length();
		for (int i = 0; i < size; i++)
		{
			int c = value.charAt(i);
			String s = Integer.toHexString(c);
			if (s.length() == 1) System.out.print("0");
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println("");
	}
	
	public static String escapeUnicode(String value)
	{
		return escapeUnicode(value, null, null, false);
	}
	
	public static String escapeUnicode(String value, CharacterRange range)
	{
		return escapeUnicode(value, null, range, false);
	}
	
	/*
	 * Converts unicodes to encoded &#92;uxxxx
	 * and writes out any of the characters in specialSaveChars
	 * with a preceding slash.
	 * This has partially been "borrowed" from the Properties class, because the code
	 * there is not usable from the outside.
	 * Backslash, CR, LF, Tab and FormFeed (\f) will always be replaced.
	 */
	public static String escapeUnicode(String value, String specialSaveChars, CharacterRange range)
	{
		return escapeUnicode(value, specialSaveChars, range, false);
	}
	
	public static String escapeUnicode(String value, String specialSaveChars, CharacterRange range, boolean alwaysUnicode)
	{
		if (value == null) return null;
		if (range == null || range == CharacterRange.RANGE_NONE) return value;
		
		int len = value.length();
		StringBuilder outBuffer = new StringBuilder(len*2);

		for(int x=0; x<len; x++)
		{
			char aChar = value.charAt(x);
			boolean replaced = false;
			if (!alwaysUnicode)
			{
				switch(aChar)
				{
					case '\\':
						outBuffer.append('\\');
						outBuffer.append('\\');
						replaced = true;
						break;
					case '\t':
						outBuffer.append('\\');
						outBuffer.append('t');
						replaced = true;
						break;
					case '\n':
						outBuffer.append('\\');
						outBuffer.append('n');
						replaced = true;
						break;
					case '\r':
						outBuffer.append('\\');
						outBuffer.append('r');
						replaced = true;
						break;
					case '\f':
						outBuffer.append('\\');
						outBuffer.append('f');
						replaced = true;
						break;
				default:
						replaced = false;
				}
			}
			
			if (!replaced)
			{
					if ( (range != null && range.isOutsideRange(aChar)) ||
						   (specialSaveChars != null && specialSaveChars.indexOf(aChar) > -1))
					{
						outBuffer.append('\\');
						outBuffer.append('u');
						appendUnicode(outBuffer, aChar);
					}
					else
					{
						outBuffer.append(aChar);
					}
			}
		}
		return outBuffer.toString();
	}

	public static String padRight(String input, int length)
	{
		if (input == null) return null;
		if (input.length() >= length) return input;
		StringBuilder result = new StringBuilder(length);
		result.append(input);
		while (result.length() < length) 
		{
			result.append(' ');
		}
		return result.toString();
	}
	public static String padStringLeft(String input,int length,char fillChar)
	{
		input=trim(input);
		int currentLength=input.length();
		
		if(length<=currentLength)
			return input;
		int gapLength=length-currentLength;
		
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<gapLength;i++)
		{
			sb.append(fillChar);
		}
		return sb.append(input).toString();
	}
	public static String padStringRight(String input,int length,char fillChar)
	{
		input=trim(input);
		int currentLength=input.length();
		
		if(length<=currentLength)
			return input;
		int gapLength=length-currentLength;
		
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<gapLength;i++)
		{
			sb.append(fillChar);
		}
		return input+sb.toString();
	}
	public static String formatNumber(int value, int length, boolean fillRight)
	{
		String s = NumberStringCache.getNumberString(value);
		int l = s.length();
		if (l >= length) return s;
		StringBuilder result = new StringBuilder(length);
		if (fillRight)
		{
			result.append(s);
		}
		for (int k = l; k < length; k++)
		{
			result.append(' ');
		}
		if (!fillRight)
		{
			result.append(s);
		}
		return result.toString();
	}
	
	private static void appendUnicode(StringBuilder buffer, char c)
	{
		buffer.append(hexDigit(c >> 12));
		buffer.append(hexDigit(c >>  8));
		buffer.append(hexDigit(c >>  4));
		buffer.append(hexDigit(c));
	}
	
	private static char hexDigit(int nibble)
	{
		return hexDigit[(nibble & 0xF)];
	}

	private static final char[] hexDigit = {
		'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	};
	   /**
     * ���ָ���ַ��Ƿ�Ϊ�մ�
     * 
     * @param content
     * @return
     */
    public static boolean isSpaceString(String content) {
        if (content == null)
            return false;
        if (trim(content).equals("")) {
            return true;
        } else
            return false;
    }
	/**
	 * Return <tt>true</tt> if the passed string is <tt>null</tt> or empty.
	 *
	 * @param	str		String to be tested.
	 *
	 * @return	<tt>true</tt> if the passed string is <tt>null</tt> or empty.
	 */
	public static boolean isEmpty(String str)
	{
		return str == null || str.trim().length() == 0;
	}
    /**
     * ���ַ����Ŀո��ȥ,���Ϊnull�򷵻ؿմ�
     * 
     * @param s
     * @return
     */
    public static String trim(String s) {
        if (s == null)
            return "";
        else
            return s.trim();
    }

    /**
     * ������r�Ͳ���c�ϲ����ַ��ַ�ĳ�����5λ��2+3��
     * @return
     */
    public static String compose(int r, int c) {
        String str1 = String.valueOf(c);
        if (str1.length() < 2) {
            str1 = "0" + str1;
        }
        String str2 = String.valueOf(r);
        int len = str2.length();
        if (len == 1) {
            str2 = "00" + str2;
        } else if (len == 2) {
            str2 = "0" + str2;
        }
        return str1 + str2;
    }
    /**
     * Return cell location information.
     * the first element is row index, and the second is colum index
     * @param str composed str that can specify a table cell.
     */
    public static int[] resolveString(String str)
    {
    	str=trim(str);
    	if(str.equals("")||str.length()<3)
    		return null;
    	int[] result=new int[2];
    	int column=Integer.parseInt(str.substring(0,2));
    	int row=Integer.parseInt(str.substring(2));
    	
    	result[1]=column;
    	result[0]=row;
    	
    	return result;
    }
    public static String substituteString(String original, String placeHolder,
            String replacementValue) {
        StringBuffer buffer = new StringBuffer();
        char originalChars[] = original != null ? original.toCharArray()
                : new char[0];
        char placeHolderChars[] = placeHolder.toCharArray();
        char placeHolderComparisonArray[] = new char[placeHolderChars.length];
        int i = 0;
        for (int length = originalChars.length; i < length; i++)
            if (i <= length - placeHolderChars.length) {
                System.arraycopy(originalChars, i, placeHolderComparisonArray,
                        0, placeHolderComparisonArray.length);
                if (Arrays.equals(placeHolderComparisonArray, placeHolderChars)) {
                    i += placeHolderComparisonArray.length - 1;
                    buffer.append(replacementValue);
                } else {
                    buffer.append(originalChars[i]);
                }
            } else {
                buffer.append(originalChars[i]);
            }

        return original != null ? buffer.toString() : null;
    }

    /**
     * Format the given message text by wrapping prettily
     * @param ms the text that should be formated.
     * @param width the size of the length of each row.
     */
    public static String formatMessage(String ms, int width) {
    	if (ms == null) {
    		return "";
    	}
    	
        if (width < 1)
            return ms;
        StringBuffer buffer = new StringBuffer();
        int length = ms.length();
        int i = 0;
        int count = 0;
        char ch;
        while (i < length) {
            ch = ms.charAt(i);
            count++;
            buffer.append(ch);

            if (count >= width) {
                buffer.append("\n");
                count = 0;
            }
            i++;
        }
        return buffer.toString();
    }
    /**
     * �����ļ�������
     * @param file
     * @return
     */
    public static String getFileType(String file)
    {
        int index = -1;
        if ((index = file.lastIndexOf(".")) != -1) 
            return file.substring(index + 1, file.length());
        else
            return null;
    }
    /**
     * ����ȥ�����ļ����͵��ַ�
     * @param file
     * @return
     */
    public static String getNoTypeFileStr(String file)
    {
        int index = -1;
        if ((index = file.lastIndexOf(".")) != -1) 
            return file.substring(0,index);
        else
            return file;
    }
    /**
     * �����ڶ���ת����yyyy-MM-dd HH:mm:ss��ʽ���ַ�
     * @param date
     * @return
     */
    public static String transDate(Date date)
    {
        if(date==null)
            return "";
        String format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formater=new SimpleDateFormat(format);
        return formater.format(date);
    }
    /**
     * ���ָ���ĸ�ʽ����date����ת�����ַ�
     * @param date  --��Ҫת����Date����
     * @param s  --ת�����ַ��ʽ
     * @return --ת�����String����
     */
    public static String transDate(Date date,String s)
    {
        SimpleDateFormat formater=new SimpleDateFormat(s);
        return formater.format(date);
    }
    /**
     * �жϸ���ַ�,�Ƿ����������
     * @param str  --ָ�����ַ�
     * @return  --������������,����true  ���򷵻�false
     */
    public static boolean isDigital(String str)
    {
        if(str==null||str.equals(""))
            return false;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(0);
            if(!Character.isDigit(ch))
                return false;
        }
        return true;
    }
    /**
     * У�����ڸ�ʽ����ȷ��
     * @param date  --�������ַ�
     * @return  --����ʽ��ȷ���ؿմ�:""  ,���򷵻ؾ���Ĵ�����Ϣ
     */
    public static String checkDateFormat(String date)
    {
        String tmpDate=StringUtil.trim(date);
        if(tmpDate.length()!=10)  //������ڳ���
            return PublicResource.getUtilString("date.format.lengtherror");
        
        int count=0;
        /**
         * У���ַ�'-'�ĸ���
         */
        for(int i=0;i<tmpDate.length();i++)
        {
            char tmpCh=tmpDate.charAt(i);
            if(tmpCh=='-')
            {
                count++;
            }
        }
        if(count!=2)
            return PublicResource.getUtilString("date.format.error");
        
        /**
         * У������ַ�ĺϷ���
         */
        String yy=tmpDate.substring(0,4);
        String mm=tmpDate.substring(5,7);
        String dd=tmpDate.substring(8,10);
        if(!isDigital(yy))
            return PublicResource.getUtilString("date.format.notnumber");
        if(!isDigital(mm))
            return PublicResource.getUtilString("date.format.notnumber");
        if(!isDigital(dd))
            return PublicResource.getUtilString("date.format.notnumber");
        
        /**
         * У����ݷ��صĺϷ���
         */
        int year=Integer.parseInt(yy);
        int month=Integer.parseInt(mm);
        int day=Integer.parseInt(dd);
        if(year<1900||year>3000)
            return PublicResource.getUtilString("date.format.yearerror");
        if(month<1||month>12)
            return PublicResource.getUtilString("date.format.montherror");
        if(day<0||day>31)
            return PublicResource.getUtilString("date.format.dayerror");
        
        if((month==4||month==6||month==9||month==11)&&day>30)
        {
            return PublicResource.getUtilString("date.format.maxmonth30");
        }
    	if(month==2){
    		if(isLeapYear(year)){
    			if(day>29){
    			    return PublicResource.getUtilString("date.format.maxmonth29");
    			}
    		}else if(day>28){
    		    return PublicResource.getUtilString("date.format.maxmonth28");
    		}
    	}
    	return "";
    }
    /**
     * �жϸ�����Ƿ�Ϊ����
     * @param year  --��ݵ�����ֵ
     * @return      --true ����; false ƽ��
     */
    public static boolean isLeapYear(int year)
    {
    	if(year%4==0){
    		if(year%100==0){
    			if(year%400==0){
    				return true;	
    			}else
    				return false;	
    		}
    		return true;	
    	}else 
    		return false;
    }
    /**
     * ��ȡ��ǰ���ڣ����ڸ�ʽΪyyyy-MM-dd
     * @return  --���ص����ϵͳ����
     */
    public static String getCurrentDate()
    {
        return transDate(new Date(),"yyyy-MM-dd");
    }
    /**
     * �����ڵ�ǰһ��
     * @param date
     * @return
     */
    public static String getPreviousDay(Date date)
    {
        return getOffsetDay(date,-1);        
    }
    /**
     * �����ڵĺ�һ��
     * @param date
     * @return
     */
    public static String getNextDay(Date date)
    {
        return getOffsetDay(date,1);       
    }
    /**
     * ��ݸ����ڣ�Ȼ����ǰ�������׷��days��
     * @param date  --��׷�ݵ�����  ���Ϊ������ô��ǰ׷�ݣ����Ϊ�������׷��
     * @param days  --׷�ݵ�����
     * @return   --׷�ݺ�����ڣ�String��
     */
    public static String getOffsetDay(Date date,int days)
    {
        long timeGap = 24 * 60 * 60 * 1000*days;
        Date result=new Date(date.getTime()+timeGap);
        return transDate(result,"yyyy-MM-dd");
    }
    /**
     * ��ݸ��sql�ֶ����ͣ���ȡʱ���ʽ
     * @param type  --sql�ֶ�����
     * @return  --����ʱ���ʽ,�䶨����IDateSelector��ͬ
     */
    public static String getTimeStypeBySQLType(int type)
    {
        if(type==Types.DATE)
        {
            return DateSelector.STYLE_DATE1;
        }else if(type==Types.TIME)
        {
            return DateSelector.STYLE_DATETIME1;
        }else
            return null;
    }
    /**
     * ��ȡָ�������hash���֡�
     * @param ob  
     * @return  --������hash��Ͷ������Ӧ��������ɵ��ַ�
     */
    public static String getHashNameOfObject(Object ob)
    {
        return ob.hashCode()+"("+ob.getClass().getName()+")";
    }
    /**
     * �Ը��ַ�����Ƿ�Ϊ��ֵ��У�飬�������ֵ�ͣ����ز��Ϸ�����ʾ��Ϣ
     * @param str  --��ҪУ����ַ����
     * @param size  --��ֵ����󳤶�
     * @param scal  --��ֵС��ֳ���
     * @return  --���Ϊ��ֵ�ͣ�����null  ���������ֵ�ͣ�������Ӧ�Ĵ�����Ϣ
     */
    public static String isNumberString(String str,int size,int scale)
    {
        try {
            if(str.equals("-"))  //���Ϸ�
                return PublicResource.getUtilString("editortable.cellcheck.number.nonum");
            if (!str.equals("") && !str.equals("-")) {
                double d = Double.parseDouble(str);              
            }
        } catch (Exception e) {
            
            return PublicResource.getUtilString("editortable.cellcheck.number.illegalchar");
        }
        str = str.replaceFirst("-", ""); //
        int decPos = str.indexOf(".");  //С��㲿��
        
        String strIntPart="";//�����
        String strDecPart=""; //С���
        if (decPos > -1) {
            strIntPart = str.substring(0, decPos);
            strDecPart = str.substring(decPos + 1);
        } else {
            strIntPart = str;
        }
        if (strIntPart.length() > (size - scale)) {
            
            return PublicResource.getUtilString("editortable.cellcheck.number.overint")+(size-scale);
        }else if(strDecPart.length() > scale)
        {
            return PublicResource.getUtilString("editortable.cellcheck.number.overdigit")+scale;
        }
        return null;
    }
    /**
     * �÷�����isNumberString(String str,int size,int scale)��������ͬ��У�����ֻ����÷������᷵����Ӧ����ʾ��Ϣ�������ٶ��ϽϿ졣
     * @param str  --��ҪУ����ַ����
     * @param size  --��ֵ����󳤶�
     * @param scale  --��ֵС��ֳ���
     * @return  --�������ֵ�͹��򣬷���true�����򷵻�false
     */
    public static boolean isNumberStr(String str,int size,int scale)
    {
        try {
            if(str.equals("-"))  //���Ϸ�
                return false;
            if (!str.equals("") && !str.equals("-")) {
                double d = Double.parseDouble(str);              
            }
        } catch (Exception e) {
            
            return false;
        }
        str = str.replaceFirst("-", ""); //
        int decPos = str.indexOf(".");  //С��㲿��
        
        String strIntPart="";//�����
        String strDecPart=""; //С���
        if (decPos > -1) {
            strIntPart = str.substring(0, decPos);
            strDecPart = str.substring(decPos + 1);
        } else {
            strIntPart = str;
        }
        if (strIntPart.length() > (size - scale)||strDecPart.length() > scale) {
            return false;
        }
        return true;
    }
    /**
     * У���ַ����Ƿ��ڸ�ķ�Χ�ڣ���У���Ϊ���ֽںͰ��ַ����ַ�ʽ���г��ȼ��㡣
     * @param str  --��ҪУ����ַ�
     * @param len  --�ַ���
     * @param isByte  --true�����ֽڼ����ַ��� false�����ַ���㳤��
     * @return  true���ڸ��ȷ�Χ�ڣ�false:���ڸ��ȷ�Χ��
     */
    public static boolean checkLength(String str,int len,boolean isByte)
    {
        if(str==null)
            return false;
        if(isByte)
        {
            if(str.getBytes().length>len)
                return false;
            else
                return true;
        }else
        {
            if(str.length()>len)
                return false;
            else
                return true;
        }
    }
    /**
     * Remove the suffix from the passed file name.
     *
     * @param	fileName	File name to remove suffix from.
     *
     * @return	<TT>fileName</TT> without a suffix.
     *
     * @throws	IllegalArgumentException	if <TT>null</TT> file name passed.
     */
    public static String removeFileNameSuffix(String fileName)
    {
       if (fileName == null)
       {
          throw new IllegalArgumentException("file name == null");
       }
       int pos = fileName.lastIndexOf('.');
       if (pos > 0 && pos < fileName.length() - 1)
       {
          return fileName.substring(0, pos);
       }
       return fileName;
    }
}
