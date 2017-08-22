package com.siebre.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AgeUtil {

	/**
	 * 根据日期计算年龄(实岁)
	 */
	public static int getAge(Date birthDate) {
		if (birthDate == null)
			return -1;

		return getAgeIternal(birthDate, new Date(), false);
	}

	/**
	 * 根据日期计算年龄(虚岁)
	 */
	public static int getNominalAge(Date birthDate) {
		return getAgeIternal(birthDate, new Date(), true);
	}

	/**
	 * 根据日期计算年龄(带小数)
	 */
	public static double getRealAge(Date birthday) {
		double age = 0D;

		age = getRealAge(birthday, new Date());

		return age;
	}
	
	/**
	 * 根据日期和年龄计算日期
	 */
	public static Date getAgeDay(Date birthday, double age) {
		// Create a calendar object with the date of birth
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(birthday);

		// Create a calendar object with today's date
		Calendar calcDay = new GregorianCalendar();
		calcDay.setTime(birthday);
		calcDay.add(Calendar.YEAR, (int) age);
		Date calcDate = calcDay.getTime();

		// Create a calendar object for get whole day in this year
		// Calendar wholeDay = new GregorianCalendar();
		// wholeDay.setTime(calcDate);
		// wholeDay.set(Calendar.MONTH,Calendar.DECEMBER);
		// wholeDay.set(Calendar.DAY_OF_MONTH,31);

		int interDays = (int) (calcDay.getMaximum(Calendar.DAY_OF_YEAR) * (age - (int) age));
		calcDay.add(Calendar.DAY_OF_YEAR, interDays);
		return calcDay.getTime();
	}
	
	/**
	 * 判别两日期是否相等
	 */
	public static boolean isSame(Date date1, Date date2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		date1 = calendar.getTime();
		calendar.setTime(date2);
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		date2 = calendar.getTime();
		return date1.equals(date2);
	}
	
	/**
	 * 根据日期天数计算年龄
	 */
	public static int getAgeInDays(Date birthDate) {
		return getAgeInDays(birthDate, new Date());
	}
	
	/**
	 * 根据日期月份计算年龄
	 */
	public static double getAgeInMonths(Date birthday, Date calcDate) {
		return getRealAge(birthday, calcDate) * 12;
	}
	
	/**
	 * 根据年龄计算最小日期
	 */
	public static Date getMinBirthDate(int age){
		Calendar cal = Calendar.getInstance();
		cal.roll(Calendar.YEAR, -age);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 根据年龄计算最大日期
	 */
	public static Date getMaxBirthDate(int age){
		Date temp = getMinBirthDate(age - 1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}
	
	
	private static int getAgeIternal(Date birthday, Date calcDate, boolean isNominal) {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(birthday);

		Calendar calcDay = new GregorianCalendar();
		calcDay.setTime(calcDate);

		int age = calcDay.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

		dateOfBirth.add(Calendar.YEAR, age);

		if (!isNominal && calcDay.before(dateOfBirth)) {
			age--;
		}

		if (age < 0)
			age = 0;

		return age;
	}

	public static double getRealAge(Date birthday, Date calcDate) {
		// Create a calendar object with the date of birth
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(birthday);

		// Create a calendar object with today's date
		Calendar calcDay = new GregorianCalendar();
		calcDay.setTime(calcDate);

		// Create a calendar object for get whole day in this year
		Calendar wholeDay = new GregorianCalendar();
		wholeDay.setTime(calcDate);
		wholeDay.set(Calendar.MONTH, Calendar.DECEMBER);
		wholeDay.set(Calendar.DAY_OF_MONTH, 31);

		// Get age based on year
		int age = calcDay.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

		// Add the tentative age to the date of birth to get this year's birthday
		dateOfBirth.add(Calendar.YEAR, age);

		// If this year's birthday has not happened yet, subtract one from age
		// if (calcDay.before(dateOfBirth)) {
		// age--;
		// }

		// Get age based on day
		long millisAge = calcDay.getTimeInMillis() - dateOfBirth.getTimeInMillis();
		millisAge = millisAge / (1000L * 60 * 60 * 24);

		double result = ((double) millisAge) / wholeDay.get(Calendar.DAY_OF_YEAR) + age;

		if (result < 0)
			result = 0;
		return result;
	}

	public static int getAgeInDays(Date birthDate, Date current) {
		if (birthDate != null && current != null) {
			return (int) ((current.getTime() - birthDate.getTime()) / (1000L * 60 * 60 * 24));
		}

		return -1;
	}
	
}
