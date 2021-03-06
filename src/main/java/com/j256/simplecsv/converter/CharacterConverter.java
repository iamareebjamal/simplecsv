package com.j256.simplecsv.converter;

import com.j256.simplecsv.common.CsvColumn;
import com.j256.simplecsv.processor.ColumnInfo;
import com.j256.simplecsv.processor.ParseError;
import com.j256.simplecsv.processor.ParseError.ErrorType;

/**
 * Converter for the Java String type.
 * 
 * <p>
 * The {@link CsvColumn#converterFlags()} parameter can be set to {@link #PARSE_ERROR_IF_MORE_THAN_ONE_CHAR} to throw a
 * parse error if the input has more than one character.
 * </p>
 * 
 * @author graywatson
 */
public class CharacterConverter implements Converter<Character, Boolean> {

	/**
	 * Use this flag if you want a parse error generated when the input has more than one character. Default is to just
	 * take the first character.
	 */
	public static final long PARSE_ERROR_IF_MORE_THAN_ONE_CHAR = 1 << 1;

	private static final CharacterConverter singleton = new CharacterConverter();

	/**
	 * Get singleton for class.
	 */
	public static CharacterConverter getSingleton() {
		return singleton;
	}

	@Override
	public Boolean configure(String format, long flags, ColumnInfo<Character> fieldInfo) {
		boolean parseErrorOnMoreThanOne = ((flags & PARSE_ERROR_IF_MORE_THAN_ONE_CHAR) != 0);
		return parseErrorOnMoreThanOne;
	}

	@Override
	public boolean isNeedsQuotes(Boolean parseErrorOnMoreThanOne) {
		return true;
	}

	@Override
	public boolean isAlwaysTrimInput() {
		return false;
	}

	@Override
	public String javaToString(ColumnInfo<Character> columnInfo, Character value) {
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}

	@Override
	public Character stringToJava(String line, int lineNumber, int linePos, ColumnInfo<Character> columnInfo,
			String value, ParseError parseError) {
		Boolean parseErrorOnMoreThanOne = (Boolean) columnInfo.getConfigInfo();
		if (value.isEmpty()) {
			return null;
		} else if (value.length() > 1 && parseErrorOnMoreThanOne) {
			parseError.setErrorType(ErrorType.INVALID_FORMAT);
			parseError.setMessage("More than one character specified");
			parseError.setLinePos(linePos);
			return null;
		} else {
			return value.charAt(0);
		}
	}
}
