package com.siebre.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DateUtils {
	public static int getYearOfDate(Date date) {
		Calendar gc = prepare(date);
		return gc.get(Calendar.YEAR);
	}

	public static int getMonthOfDate(Date date) {
		Calendar gc = prepare(date);
		return gc.get(Calendar.MONTH);
	}

	public static int getDayOfDate(Date date) {
		Calendar gc = prepare(date);
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	public static Date addYear(Date date, int year) {
		Calendar gc = prepare(date);
		gc.add(Calendar.YEAR, year);
		return gc.getTime();
	}

	public static Date addMonth(Date date, int month) {
		Calendar gc = prepare(date);
		gc.add(Calendar.MONTH, month);
		return gc.getTime();
	}

	public static Date addDay(Date date, int day) {
		Calendar gc = prepare(date);
		gc.add(Calendar.DAY_OF_MONTH, day);
		return gc.getTime();
	}

	private static Calendar prepare(Date date) {
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		return gc;
	}

	public static Date rollingDate(Date date, int field, int amount) {
		Calendar gc = prepare(date);
		gc.roll(field, amount);
		return gc.getTime();
	}

	/**
	 * Limited version of Calendar.set.
	 * <p>
	 * Set the specified field to min(actual maximum value of this field,
	 * specified new value).
	 * </p>
	 * 
	 * @param date
	 *            base date.
	 * @param field
	 *            field want to modify.
	 * @param value
	 *            the new value of specified field.
	 * @return a new Date instance holding the new value.
	 */
	public static Date setFieldInLimit(Date date, int field, int value) {
		Calendar gc = prepare(date);
		gc.set(field, Math.min(gc.getActualMaximum(field), value));
		return gc.getTime();
	}

	public static Date parseDate(String value) {
		Date date = java.sql.Date.valueOf(value);
		return date;
	}

	public static Date getDayStart(Date currentDate) {
		Date result = null;

		Calendar current = Calendar.getInstance();
		current.setTime(currentDate);
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.clear(Calendar.MILLISECOND);
		current.clear(Calendar.MINUTE);
		current.clear(Calendar.SECOND);
		result = current.getTime();

		return result;
	}

	public static Date getDayEnd(Date day) {
		Date result = null;

		Calendar date = Calendar.getInstance();
		date.setTime(day);
		date.set(Calendar.HOUR_OF_DAY, 24);
		date.clear(Calendar.MINUTE);
		date.clear(Calendar.SECOND);
		date.set(Calendar.MILLISECOND, -1);
		result = date.getTime();

		return result;
	}

	public static Date getTodayStart() {
		return getDayStart(new Date());
	}

	public static Date getTodayEnd() {
		return getDayEnd(new Date());
	}

	public static Date getWeekStart() {
		Date result = null;

		Calendar current = Calendar.getInstance();
		current.set(Calendar.HOUR_OF_DAY, 0);
		current.clear(Calendar.MILLISECOND);
		current.clear(Calendar.MINUTE);
		current.clear(Calendar.SECOND);
		current.set(Calendar.DAY_OF_WEEK, current.getFirstDayOfWeek());
		result = current.getTime();

		return result;
	}

	public static Date getWeekEnd() {
		Date result = null;

		Calendar current = Calendar.getInstance();
		current.setTime(getWeekStart());
		current.add(Calendar.WEEK_OF_YEAR, 1);
		current.set(Calendar.MILLISECOND, -1);
		result = current.getTime();

		return result;
	}

	public static Date getMonthStart(Date currentDate) {
		Date result = null;

		Calendar current = Calendar.getInstance();
		current.setTime(getDayStart(currentDate));
		current.set(Calendar.DAY_OF_MONTH, 1);
		result = current.getTime();

		return result;
	}

	public static Date getYearStart(Date date) {
		int year = getYearOfDate(date);
		Calendar result = Calendar.getInstance();
		result.set(year, 0, 1);
		return result.getTime();
	}

	/**
	 * Return a Date representing the start of the specified month.
	 * 
	 * @param year
	 *            Year value.
	 * @param month
	 *            Month value. Starts from 0.
	 * @return
	 */
	public static Date getMonthStart(int year, int month) {
		Calendar sampleDate = Calendar.getInstance();
		sampleDate.set(Calendar.YEAR, year);
		sampleDate.set(Calendar.MONTH, month);
		return getMonthStart(sampleDate.getTime());
	}

	public static Date getMonthEnd(Date currentDate) {
		Date result = null;

		Calendar current = Calendar.getInstance();
		current.setTime(getMonthStart(currentDate));
		current.add(Calendar.MONTH, 1);
		current.add(Calendar.MILLISECOND, -1);
		result = current.getTime();

		return result;
	}

	/**
	 * Return a Date representing the end of the specified month.
	 * 
	 * @param year
	 *            Year value.
	 * @param month
	 *            Month value. Starts from 0.
	 * @return
	 */
	public static Date getMonthEnd(int year, int month) {
		Calendar sampleDate = Calendar.getInstance();
		sampleDate.set(Calendar.YEAR, year);
		sampleDate.set(Calendar.MONTH, month);
		return getMonthEnd(sampleDate.getTime());
	}

	public static int calculateDisparity(Date beforeDate, Date afterDate, int calendar_time_type, int roundingMode) {
		int result = 0;

		if (beforeDate == null || afterDate == null) {
			throw new IllegalArgumentException("beforeDate and afterDate can not be null.");
		}
		if (calendar_time_type == Calendar.DATE)
			throw new IllegalArgumentException("Calendar.Date is not supported now.");

		Calendar after = prepare(afterDate);
		Calendar before = prepare(beforeDate);

		// int yearDisparity = after.get(Calendar.YEAR) -
		// before.get(Calendar.YEAR);
		// int monthDisparity = after.get(Calendar.MONTH) -
		// before.get(Calendar.MONTH);
		// switch (calendar_time_type) {
		// case Calendar.YEAR:
		// result = yearDisparity + monthDisparity / 12;
		// break;
		// case Calendar.MONTH:
		// result = yearDisparity * 12 + monthDisparity;
		// break;
		// default:
		// // throw exception
		// }

		if (calendar_time_type == Calendar.DATE) {// TODO not completed.
			result -= before.get(Calendar.DAY_OF_YEAR);
			while (before.before(after)) {
				result += before.getActualMaximum(Calendar.DAY_OF_YEAR);
				before.add(Calendar.YEAR, 1);
			}
			result += before.get(Calendar.DAY_OF_YEAR);
		} else {
			if (roundingMode == BigDecimal.ROUND_DOWN) {
				result = -1;
				while (!before.after(after)) {
					result++;
					before.add(calendar_time_type, 1);
				}
			} else if (roundingMode == BigDecimal.ROUND_UP) {
				result = 0;
				while (before.before(after)) {
					result++;
					before.add(calendar_time_type, 1);
				}
			}
		}

		return result;
	}

	public static Date mergeDate(Date day, Date time) {
		Calendar result = Calendar.getInstance();

		result.setTime(time);
		Calendar dayHelper = Calendar.getInstance();
		dayHelper.setTime(day);
		result.set(dayHelper.get(Calendar.YEAR), dayHelper.get(Calendar.MONTH), dayHelper.get(Calendar.DATE));

		return result.getTime();
	}

	/**
	 * Get a Iterator of a Date collection which is [start, end].
	 * 
	 * @param start
	 * @param end
	 * @return a Date Iterator.
	 */
	public static Iterator dateIterator(Date start, Date end) {
		return new DateIterator(start, end);
	}

	/**
	 * Get a Iterator of a Calendar collection which is [start, end].
	 * 
	 * @param start
	 * @param end
	 * @return a Calendar Iterator.
	 */
	public static Iterator calendarIterator(Calendar start, Calendar end) {
		return new CalendarIterator(start, end);
	}

	/**
	 * from org.apache.commons.lang.time.DateUtils.DateIterator. Renamed.
	 */
	static class CalendarIterator implements Iterator {
		private final Calendar endFinal;

		private final Calendar spot;

		/**
		 * Constructs a DateIterator that ranges from one date to another.
		 * 
		 * @param startFinal
		 *            start date (inclusive)
		 * @param endFinal
		 *            end date (not inclusive)
		 */
		CalendarIterator(Calendar startFinal, Calendar endFinal) {
			super();
			this.endFinal = endFinal;
			spot = startFinal;
			spot.add(Calendar.DATE, -1);
		}

		/**
		 * Has the iterator not reached the end date yet?
		 * 
		 * @return <code>true</code> if the iterator has yet to reach the end
		 *         date
		 */
		public boolean hasNext() {
			return spot.before(endFinal);
		}

		/**
		 * Return the next calendar in the iteration
		 * 
		 * @return Object calendar for the next date
		 */
		public Object next() {
			if (spot.equals(endFinal)) {
				throw new NoSuchElementException();
			}
			spot.add(Calendar.DATE, 1);
			return spot.clone();
		}

		/**
		 * Always throws UnsupportedOperationException.
		 * 
		 * @throws UnsupportedOperationException
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Date version.
	 */
	static class DateIterator extends CalendarIterator implements Iterator {

		/**
		 * Constructs a DateIterator that ranges from one date to another.
		 * 
		 * @param startFinal
		 *            start date (inclusive)
		 * @param endFinal
		 *            end date (not inclusive)
		 */
		DateIterator(Date startFinal, Date endFinal) {
			super(prepare(startFinal), prepare(endFinal));
		}

		/**
		 * Return the next calendar in the iteration
		 * 
		 * @return Object calendar for the next date
		 */
		public Object next() {
			return ((Calendar) super.next()).getTime();
		}

	}

	public static boolean isInThePeriod(Date asOfDate, Date start, Date end) {
		if (!asOfDate.before(start) && !asOfDate.after(end))
			return true;
		return false;
	}

	/**
	 * input param formate like "2005-10-1"
	 * 
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static int getLastDayOfMonth(String date) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(formatter.parse(date));
		Calendar c = formatter.getCalendar();
		int day = c.getActualMaximum(Calendar.DATE);
		return day;
	}

	public static boolean isSameDay(Date one, Date two) {
		if (getYearOfDate(one) == getYearOfDate(two) && getMonthOfDate(one) == getMonthOfDate(two) && getDayOfDate(one) == getDayOfDate(two)) {
			return true;
		}
		return false;
	}

	public static boolean isFirstDateOfMonth(Date date) {
		int day = date.getDate();
		if (day == 1)
			return true;
		return false;
	}

	public static boolean isFirstDayFromThisMonth(Date date) {
		Date today = new Date();

		if ((today.before(date) || today.equals(date)) && isFirstDateOfMonth(date))
			return true;

		return false;
	}

	public static boolean isFirstDayFromNextMonth(Date date) {
		Date today = new Date();

		if (today.before(date) && isFirstDateOfMonth(date)) {
			if (today.getMonth() == date.getMonth())
				return false;
			else
				return true;
		}

		return false;
	}

	public static boolean laterThanToday(Date effectiveDate) {
		Date today = DateUtils.getTodayStart();
		if (today.before(effectiveDate) || today.equals(effectiveDate))
			return true;
		return false;
	}

	public static boolean laterThanFirstDayOfThisMonth(Date effectiveDate) {
		Date firstDayOfThisMonth = getMonthStart(new Date());
		if (firstDayOfThisMonth.before(effectiveDate) || firstDayOfThisMonth.equals(effectiveDate))
			return true;
		return false;
	}

	/**
	 * 判断日期字符串是否合法,制定格式为”yyyyMM“,主要用于导入数据日期格式校验。
	 * 
	 * @param externalReference
	 * @return
	 */
	public static boolean isValidFormat(String externalReference) {
		boolean isValid = true;

		Integer year = new Integer(0);
		Integer month = new Integer(0);
		if (externalReference.length() == 6) {
			try {
				year = Integer.valueOf(externalReference.substring(0, 4));
				month = Integer.valueOf(externalReference.substring(4));
			} catch (Exception e) {
				isValid = false;
			}
		} else {
			isValid = false;
		}
		if (year.intValue() < 1970 || year.intValue() > 2099 || month.intValue() > 12 || month.intValue() < 1) {
			isValid = false;
		}

		return isValid;
	}

}
