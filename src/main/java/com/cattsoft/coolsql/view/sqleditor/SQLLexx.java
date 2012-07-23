package com.cattsoft.coolsql.view.sqleditor;

import java.util.Vector;

import com.cattsoft.coolsql.view.log.LogProxy;
/**
 * 
 * @author liu_xlin
 *
 */
public class SQLLexx {

	public SQLLexx() {
	}

	public static Vector parse(String text) {
		Vector tokens = new Vector();
		StringPointer p = new StringPointer(text);
		try {
			while (!p.isDone()) {
				int offset = p.getOffset();
				char c = p.getNext();
				if (c == '\n')
					tokens.addElement(new Token(Token.END_OF_LINE, '\n', offset));
				else if (CheckCondition(c, 1)) {
					StringBuffer value = AddTokenWhile(p, c, 1);
					tokens.addElement(new Token(Token.WHITESPACE, value.toString(), offset,
							offset + value.length()));
				} else if (CheckCondition(c, 3)) {
					StringBuffer value = AddTokenWhile(p, c, 2);
					tokens.addElement(new Token(Token.IDENTIFIER, value.toString(), offset,
							offset + value.length()));
				} else if (CheckCondition(c, 4)) {
					StringBuffer value = AddTokenUntil(p, c, 4);
					tokens.addElement(new Token(Token.LITERAL, value.toString(), offset,
							offset + value.length()));
				} else if (CheckCondition(c, 5)) {
					StringBuffer value = AddTokenUntil(p, c, 4);
					tokens.addElement(new Token(Token.LITERAL, value.toString(), offset,
							offset + value.length()));
				} else if (Character.isDigit(c)) {
					StringBuffer value = AddTokenWhile(p, c, 6);
					tokens.addElement(new Token(Token.NUMERIC, value.toString(), offset,
							offset + value.length()));
				} else if (c == '-') {
					if (p.isDone()) {
						tokens.addElement(new Token(Token.SYMBOL, (new Character('-'))
								.toString(), offset, offset + 1));
					} else {
						char next = p.peek();
						if (next == '-') {
							StringBuffer value = AddTokenUntil(p, '-', 7);
							tokens.addElement(new Token(Token.COMMENT, value.toString(),
									offset, offset + value.length()));
						} else {
							tokens.addElement(new Token(Token.SYMBOL,
									(new Character('-')).toString(), offset,
									offset + 1));
						}
					}
				} else if (c == '\\') {
					if (p.peek() == ';') {
						p.getNext();
						tokens.addElement(new Token(Token.SYMBOL, (new Character(';'))
								.toString(), offset, offset + 2));
					} else {
						tokens.addElement(new Token(Token.SYMBOL, (new Character('\\'))
								.toString(), offset, offset + 1));
					}
				} else if (c == ';') {
					tokens.addElement(new Token(Token.SEPARATOR, (new Character(';'))
							.toString(), offset, offset + 1));
					if (!p.isDone()) {
						StringBuffer value = AddTokenUntil(p, "", 7);
						offset++;
						tokens.addElement(new Token(Token.COMMENT, value.toString(),
								offset, offset + value.length()));
					}
				} else if (c == '/') {
					if (p.peek() == '*')
						tokens.addElement(tokenizeComment(p, offset));
					else
						tokens.addElement(new Token(Token.SYMBOL, new String(
								new char[] { c }), offset, offset + 1));
				} else {
					tokens.addElement(new Token(Token.SYMBOL, new String(
							new char[] { c }), offset, offset + 1));
				}
			}
		} catch (RuntimeException e) {
			LogProxy.errorReport(e);
		}
		return tokens;
	}

	private static StringBuffer AddTokenUntil(StringPointer p, String s,
			int condition) {
		StringBuffer value = new StringBuffer(s);
		if (p.isDone())
			return value;
		char c;
		do {
			c = p.getNext();
			if (c != '\n')
				value.append(c);
		} while (!CheckCondition(c, condition) && c != '\n' && !p.isDone());
		return value;
	}

	private static StringBuffer AddTokenUntil(StringPointer p, char c,
			int condition) {
		return AddTokenUntil(p, (new Character(c)).toString(), condition);
	}

	private static StringBuffer AddTokenWhile(StringPointer p, String s,
			int condition) {
		StringBuffer value;
		label0: {
			value = new StringBuffer(s);
			if (p.isDone())
				return value;
			do {
				char c = p.getNext();
				if (!CheckCondition(c, condition))
					break;
				value.append(c);
				if (p.isDone())
					break label0;
			} while (true);
			p.back();
		}
		return value;
	}

	private static StringBuffer AddTokenWhile(StringPointer p, char c,
			int condition) {
		return AddTokenWhile(p, (new Character(c)).toString(), condition);
	}

	private static boolean CheckCondition(char c, int condition) {
		switch (condition) {
		case 1: // '\001'
			return Character.isWhitespace(c);

		case 3: // '\003'
			return Character.isLetter(c) || c == '$' || c == '#';

		case 2: // '\002'
			return Character.isLetter(c) || Character.isDigit(c) || c == '_'
					|| c == '$' || c == '#';

		case 4: // '\004'
			return c == '\'';

		case 5: // '\005'
			return c == '"';

		case 6: // '\006'
			return Character.isDigit(c) || c == '.';

		case 7: // '\007'
			return c == '\n';
		}
		return false;
	}

	private static Token tokenizeComment(StringPointer p, int offset) {
		StringBuffer value = new StringBuffer();
		char c = p.getNext();
		value.append('/');
		for (; (c != '*' || p.peek() != '/') && !p.isDone(); c = p.getNext())
			value.append(c);

		if (!p.isDone()) {
			value.append(c);
			c = p.getNext();
			value.append(c);
		}
		return new Token('C', value.toString(), offset, offset + value.length());
	}

	private static final char CHAR_EOL = 10;

	private static final char CHAR_DASH = 45;

	private static final char CHAR_ESCAPE = 92;

	private static final char CHAR_SEPARATOR = 59;

	private static final int CONDITION_WHITESPACE = 1;

	private static final int CONDITION_IDENTIFIER = 2;

	private static final int CONDITION_IDENTIFIER_INITIAL = 3;

	private static final int CONDITION_LITERAL_SIMPLE_QUOTE = 4;

	private static final int CONDITION_LITERAL_DOUBLE_QUOTE = 5;

	private static final int CONDITION_NUMERIC = 6;

	private static final int CONDITION_EOL = 7;
}
