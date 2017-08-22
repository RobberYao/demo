package com.siebre.util;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 数字转中文大写工具类
 */
public class CurrencyFormatUtils {

	private static HashMap<String, String> numberMap = new HashMap<String, String>();
	private static HashMap<String, String> digitalMap = new HashMap<String, String>();

	static {
		numberMap.put("0", "零");
		numberMap.put("1", "壹");
		numberMap.put("2", "贰");
		numberMap.put("3", "叁");
		numberMap.put("4", "肆");
		numberMap.put("5", "伍");
		numberMap.put("6", "陆");
		numberMap.put("7", "柒");
		numberMap.put("8", "捌");
		numberMap.put("9", "玖");

		digitalMap.put("-2", "分");
		digitalMap.put("-1", "角");
		digitalMap.put("0", "元");
		digitalMap.put("1", "拾");
		digitalMap.put("2", "佰");
		digitalMap.put("3", "仟");
		digitalMap.put("4", "万");
		digitalMap.put("5", "拾");
		digitalMap.put("6", "佰");
		digitalMap.put("7", "千");
		digitalMap.put("8", "亿");
		digitalMap.put("9", "十");
		digitalMap.put("10", "佰");
	}
	
	/**
	 * double类型数据格式化
	 */
	public static double format(double wantformat) {
		return (double) Math.round(wantformat * 100) / 100;
	}

	/**
	 * BigDecimal类型转换中文大写
	 */
	public static String toUppercase(BigDecimal amount) {
		return toUppercase(amount.doubleValue());
	}
	
	/**
	 * double类型转换中文大写
	 */
	public static String toUppercase(double money) {
		if (money <= 0)
			return "零元整";
		String uMoney = "";
		long tMoney = (long) (money * 100);
		int index = -2;
		do {
			int digital = (int) (tMoney - tMoney / 10 * 10);
			String tuMoney = "";
			if (digital != 0)
				tuMoney = (String) numberMap.get("" + digital);
			if (digital != 0 || index == 0 || index == 4 || index == 8)
				tuMoney += (String) digitalMap.get("" + index);
			uMoney = tuMoney + uMoney;
			index++;
			tMoney /= 10;
		} while (tMoney > 9);
		if (tMoney > 0) {
			uMoney = (String) numberMap.get("" + tMoney) + (String) digitalMap.get("" + index) + uMoney;
		}
		return uMoney + "整";
	}
	
	public static void main(String[] args) {
		System.out.println(toUppercase(45645641156.00));
	}
}
