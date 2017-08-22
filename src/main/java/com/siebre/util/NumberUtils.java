/*
 * Created on 2003-9-9
 *
 */
package com.siebre.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumberUtils {
	protected static NumberFormat formatter = NumberFormat.getInstance();

	protected static int DEFAULT_MAX_INTERGER_DIGITS = 10;
	protected static int DEFAULT_MIN_INTERGER_DIGITS = 1;
	protected static int DEFAULT_MAX_FRACTION_DIGITS = 2;
	protected static int DEFAULT_MIN_FRACTION_DIGITS = 2;

	public static String format(double value, int maxIntegerDigits, int minIntegerDigits, int maxFractionDigits, int minFractionDigits) {
		formatter.setMaximumIntegerDigits(maxIntegerDigits);
		formatter.setMinimumIntegerDigits(minIntegerDigits);
		formatter.setMaximumFractionDigits(maxFractionDigits);
		formatter.setMinimumFractionDigits(minFractionDigits);
		return formatter.format(value);
	}

	public static String format(double value) {
		return format(value, DEFAULT_MAX_INTERGER_DIGITS, DEFAULT_MIN_INTERGER_DIGITS, DEFAULT_MAX_FRACTION_DIGITS, DEFAULT_MIN_FRACTION_DIGITS);
	}

	public static String format(double value, int maxFractionDigits) {
		return format(value, DEFAULT_MAX_INTERGER_DIGITS, DEFAULT_MIN_INTERGER_DIGITS, maxFractionDigits, DEFAULT_MIN_FRACTION_DIGITS);
	}

	public static String format(double value, int maxFractionDigits, int minFractionDigits) {
		return format(value, DEFAULT_MAX_INTERGER_DIGITS, DEFAULT_MIN_INTERGER_DIGITS, maxFractionDigits, minFractionDigits);
	}
	
	public static BigDecimal getRound(double Num, int scale) {
		BigDecimal result;
		BigDecimal value = new BigDecimal(Num);
		result = value.setScale(scale, RoundingMode.HALF_EVEN);
		return result;
	}

	public static BigDecimal getRound(double Num) {
		return getRound(Num, 2);
	}
}
