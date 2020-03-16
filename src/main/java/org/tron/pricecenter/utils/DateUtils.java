package org.tron.pricecenter.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

  public static Date getPreviou7Day() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, -7);
    return c.getTime();
  }

  public static Date getPreviouDayStart() {
    Calendar dayStart = Calendar.getInstance();
    dayStart.add(Calendar.DATE, -1);
    dayStart.set(Calendar.HOUR_OF_DAY, 0);
    dayStart.set(Calendar.MINUTE, 0);
    dayStart.set(Calendar.SECOND, 0);
    dayStart.set(Calendar.MILLISECOND, 0);
    return dayStart.getTime();
  }

  public static Date getTodayStart() {
    Calendar dayEnd = Calendar.getInstance();
    dayEnd.set(Calendar.HOUR_OF_DAY, 0);
    dayEnd.set(Calendar.MINUTE, 0);
    dayEnd.set(Calendar.SECOND, 0);
    dayEnd.set(Calendar.MILLISECOND, 0);
    return dayEnd.getTime();
  }
}
