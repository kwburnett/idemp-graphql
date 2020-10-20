package org.bandahealth.idempiere.graphql.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.compiere.util.CLogger;

public class DateUtil {

	public final static String DATE_FORMAT = "yyyy-MM-dd";
	private final static String DEFAULT_FORMAT = "yyyy-MM-dd hh:mm:ss";
	private final static String QUEUE_DATE_FORMAT = "E, dd MMMM - HH:mm";

	private static SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
	private static CLogger log = CLogger.getCLogger(DateUtil.class);

	public static String parse(Timestamp timestamp) {
		if (timestamp != null) {
			return sdf.format(timestamp);
		}

		return null;
	}

	public static String parseDateOnly(Timestamp timestamp, String dateFormat) {
		if (timestamp != null) {
			return new SimpleDateFormat(dateFormat).format(timestamp);
		}

		return null;
	}

	public static String parseDateOnly(Timestamp timestamp) {
		if (timestamp != null) {
			return new SimpleDateFormat(DATE_FORMAT).format(timestamp);
		}

		return null;
	}

	/**
	 * Parse Visit Queue Date
	 *
	 * @param timestamp
	 * @return
	 */
	public static String parseQueueTime(Timestamp timestamp) {
		if (timestamp != null) {
			return new SimpleDateFormat(QUEUE_DATE_FORMAT).format(timestamp);
		}

		return null;

	}

	/**
	 * Parse a YYYY-MM-DD (with or without the timestamp) to a Timestamp
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp getTimestamp(String date) {
		if (date != null) {
			try {
				return new Timestamp(sdf.parse(date).getTime());
			} catch (ParseException e) {
				try {
					return new Timestamp(new SimpleDateFormat(DATE_FORMAT).parse(date).getTime());
				} catch (ParseException e1) {
					log.severe(e.getMessage());
				}
			}
		}

		return null;
	}

	public static Date parseDate(String date) {
		if (date != null) {
			try {
				return new SimpleDateFormat(DATE_FORMAT).parse(date);
			} catch (ParseException ex) {
				log.severe(ex.getMessage());
			}
		}

		return null;
	}

	/**
	 * Adds a day to the passed-in timestamp
	 *
	 * @param currentDay The timestamp to get a day from
	 * @return A timestamp exactly 1 day ahead
	 */
	public static Timestamp getTheNextDay(Timestamp currentDay) {
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(currentDay);
		endDateCalendar.add(Calendar.DATE, 1);
		return new Timestamp(endDateCalendar.getTimeInMillis());
	}
}
