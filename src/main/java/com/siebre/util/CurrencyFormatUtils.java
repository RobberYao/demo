package com.siebre.util;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * ����ת���Ĵ�д������
 */
public class CurrencyFormatUtils {

	private static HashMap<String, String> numberMap = new HashMap<String, String>();
	private static HashMap<String, String> digitalMap = new HashMap<String, String>();

	static {
		numberMap.put("0", "��");
		numberMap.put("1", "Ҽ");
		numberMap.put("2", "��");
		numberMap.put("3", "��");
		numberMap.put("4", "��");
		numberMap.put("5", "��");
		numberMap.put("6", "½");
		numberMap.put("7", "��");
		numberMap.put("8", "��");
		numberMap.put("9", "��");

		digitalMap.put("-2", "��");
		digitalMap.put("-1", "��");
		digitalMap.put("0", "Ԫ");
		digitalMap.put("1", "ʰ");
		digitalMap.put("2", "��");
		digitalMap.put("3", "Ǫ");
		digitalMap.put("4", "��");
		digitalMap.put("5", "ʰ");
		digitalMap.put("6", "��");
		digitalMap.put("7", "ǧ");
		digitalMap.put("8", "��");
		digitalMap.put("9", "ʮ");
		digitalMap.put("10", "��");
	}
	
	/**
	 * double�������ݸ�ʽ��
	 */
	public static double format(double wantformat) {
		return (double) Math.round(wantformat * 100) / 100;
	}

	/**
	 * BigDecimal����ת�����Ĵ�д
	 */
	public static String toUppercase(BigDecimal amount) {
		return toUppercase(amount.doubleValue());
	}
	
	/**
	 * double����ת�����Ĵ�д
	 */
	public static String toUppercase(double money) {
		if (money <= 0)
			return "��Ԫ��";
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
		return uMoney + "��";
	}
	
	public static void main(String[] args) {
		System.out.println(toUppercase(45645641156.00));
	}
}
