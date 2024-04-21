package model;

import java.util.List;

/**
 * Represents a time, which includes a day of the week and the time up to minute granularity.
 * The day of the week is relative to this current week and is not associated with a specific
 * day of a month/year. Some of the main operations are comparing two times and converting
 * between Time and String (and vice versa).
 */
public class Time implements ITime {

  private final int hours;
  private final int minutes;
  private final Day date;

  /**
   * Represents a day of the week. Only 7 possible values so using an Enum.
   * In this representation, Sunday is considered the first day of the week.
   */
  public enum Day {
    SUNDAY(0, "Sunday"),
    MONDAY(1, "Monday"),
    TUESDAY(2, "Tuesday"),
    WEDNESDAY(3, "Wednesday"),
    THURSDAY(4, "Thursday"),
    FRIDAY(5, "Friday"),
    SATURDAY(6, "Saturday");
    private final int dayIdx;
    private final String dayString;

    Day(int dayIdx, String dayString) {
      this.dayIdx = dayIdx;
      this.dayString = dayString;
    }

    public String getDayString() {
      return dayString;
    }

    public int getDayIdx() {
      return dayIdx;
    }


  }

  /**
   * Creates a Time object.
   * @param date date of this Time
   * @param hours hours of this Time in 24 hour time (0-24)
   * @param minutes minutes of this Time
   */
  public Time(Day date, int hours, int minutes) {
    if (hours < 0 || hours > 24) {
      throw new IllegalArgumentException("Invalid time");
    }
    if (minutes < 0 || minutes > 59) {
      throw new IllegalArgumentException("Invalid time");
    }

    this.hours = hours;
    this.minutes = minutes;
    this.date = date;
  }

  /**
   * Getting this Time's hours.
   * @return this Time's hours.
   */
  public int getHours() {
    return this.hours;
  }

  /**
   * Getting this Time's minutes.
   * @return this Time's minutes.
   */
  public int getMinutes() {
    return this.minutes;
  }

  /**
   * Calculates the # of minutes since midnight that have passed prior to event beginning.
   * If event starts at midnight, returns 0.
   *
   * @return # of minutes since midnight until beginning of event
   */
  public int minutesSinceMidnight() {
    return 60 * this.getHours() + this.getMinutes();
  }

  /**
   * Getting this Time's day of the week.
   * @return this Time's day of the week.
   */
  public Day getDate() {
    return this.date;
  }

  /**
   * Compare two times and determine what their relation is to each other.
   * @param refTime other time to compare to
   * @return 0 if they are the same time
   *        -1 if this time comes before that time
   *         1 if this time comes after that time
   */
  public int compareTimes(ITime refTime) {
    if (this.date.dayIdx < refTime.getDate().dayIdx) {
      return -1;
    }
    else if (this.date.dayIdx > refTime.getDate().dayIdx) {
      return 1;
    }
    else { // same day
      if (this.hours < refTime.getHours()) {
        return -1;
      }
      else if (this.hours > refTime.getHours()) {
        return 1;
      }
      else { // same day + hour, so comparing minutes
        return Integer.compare(this.minutes, refTime.getMinutes());
      }
    }
  }

  /**
   * Convert given String into a Time object. Time String must be in format HHMM.
   *
   * @param day String day to be converted into a Day
   * @param time time in format 0000 (1st two values are hours, 2nd two are minutes)
   * @return Time representation of given String
   * @throws IllegalArgumentException if the given day and/or time values are invalid
   *         ex. day does not exist, or time isn't in proper format
   */
  public static Time stringToTime(String day, String time) {
    List<String> daysofTheWeek =
            List.of("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");
    Day tempDay = Day.SATURDAY;
    int tempHours = 0;
    int tempMin = 0;
    // throw exception if given day isn't in Day Enum
    if (!daysofTheWeek.contains(day.toLowerCase())) {
      throw new IllegalArgumentException("invalid day");
    }

    for (Day constDay : Day.values()) {
      if (day.equalsIgnoreCase(constDay.getDayString())) {
        tempDay = constDay;
      }
    }
    if (time.length() != 4) {
      throw new IllegalArgumentException("invalid time input: " + time);
    }

    tempHours = Integer.parseInt(time.charAt(0) + String.valueOf(time.charAt(1)));
    tempMin = Integer.parseInt(time.charAt(2) + String.valueOf(time.charAt(3)));

    try {
      return new Time(tempDay, tempHours, tempMin);
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("invalid hours and/or minutes");
    }
  }

  /**
   * Convert given String into a Time object. Time String must be in format HHMM.
   *
   * @param day String day to be converted into a Day
   * @param time time in format 0000 (1st two values are hours, 2nd two are minutes)
   * @return Time representation of given String in format 00:00
   * @throws IllegalArgumentException if the given day and/or time values are invalid
   *         ex. day does not exist, or time isn't in proper format
   */
  public static Time stringToTime(int day, String time) {
    Day tempDay = Day.SATURDAY;
    int tempHours;
    int tempMin;
    for (Day constDay : Day.values()) {
      if (day == constDay.getDayIdx()) {
        tempDay = constDay;
      }
    }
    if (time.length() != 5) {
      throw new IllegalArgumentException("invalid time input: " + time);
    }

    tempHours = Integer.parseInt(time.charAt(0) + String.valueOf(time.charAt(1)));
    tempMin = Integer.parseInt(time.charAt(3) + String.valueOf(time.charAt(4)));

    try {
      return new Time(tempDay, tempHours, tempMin);
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("invalid hours and/or minutes");
    }
  }


  /**
   * Creates a time from the given day index and time.
   *
   * @param dayIndex day of the week
   * @param totMinutes minutes of the time
   * @return Time with given day and time
   */
  public static Time indexToTime(int dayIndex, int totMinutes) {
    Day tempDay = Day.SUNDAY;
    for (Day constDay : Day.values()) {
      if (dayIndex == constDay.getDayIdx()) {
        tempDay = constDay;
      }
    }

    int hours = totMinutes / 60;
    int minutes = totMinutes % 60;
    return new Time(tempDay, hours, minutes);
  }

}