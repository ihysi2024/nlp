package model.sunday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.allInterfaces.IEvent;
import model.allInterfaces.ISchedule;
import model.allInterfaces.ITime;

/**
 * Represents a schedule in the planner system.
 */
public class Schedule implements ISchedule {
  private final List<model.allInterfaces.IEvent> events;


  public Schedule(List<model.allInterfaces.IEvent> events) {
    this.events = Objects.requireNonNull(events);
  }

  /**
   * Adds a new event to this schedule. Event can only be added to the
   * schedule if it does not overlap with any other events in this schedule.
   *
   * @param event the event to be added
   * @throws IllegalArgumentException if event overlaps another event
   */

  public void addEvent(model.allInterfaces.IEvent event) {
    int countOverlapping = 0;
    for (model.allInterfaces.IEvent existingEvent: this.events) {
      if (existingEvent.overlappingEvents(event)) {
        countOverlapping++;
      }
    }
    System.out.println(countOverlapping);
    if (countOverlapping != 0) {
      throw new IllegalArgumentException("Event coincides with another event");
    }
    else {
      this.events.add(event);
    }
  }

  /**
   * Remove event from this schedule. Only removes an event that exactly matches an event
   * already in the system.
   *
   * @param otherEvent the event to be removed
   */

  public void removeEvent(model.allInterfaces.IEvent otherEvent) {
    this.events.removeIf(thisEvent -> thisEvent.equals(otherEvent));
  }

  /**
   * Observes the events present in a given schedule. Necessary to
   * allow the user to observe the events in their schedule.
   * @return the list of the schedule's events.
   */
  public List<model.allInterfaces.IEvent> getEvents() {
    return this.events;
  }

  /**
   * Creating an association between day of the week and events occurring that day.
   *
   * @return a HashMap relating each day of the week to a list of events
   */
  public Map<Time.Day, List<model.allInterfaces.IEvent>> dayToEventsMappping() {
    HashMap<Time.Day, List<model.allInterfaces.IEvent>> dayToEvent = new LinkedHashMap<>();
    dayToEvent.put(Time.Day.SUNDAY, new ArrayList<>());
    dayToEvent.put(Time.Day.MONDAY, new ArrayList<>());
    dayToEvent.put(Time.Day.TUESDAY, new ArrayList<>());
    dayToEvent.put(Time.Day.WEDNESDAY, new ArrayList<>());
    dayToEvent.put(Time.Day.THURSDAY, new ArrayList<>());
    dayToEvent.put(Time.Day.FRIDAY, new ArrayList<>());
    dayToEvent.put(Time.Day.SATURDAY, new ArrayList<>());

    for (model.allInterfaces.IEvent eventToSchedule: this.events) {
      dayToEvent.get(eventToSchedule.getStartTime().getDate()).add(eventToSchedule);
    }
    return dayToEvent;
  }

  /**
   * Convert the schedule to proper XML format for exportation.
   *
   * @return a string compatible with XML formats
   */
  public String scheduleToXMLFormat() {
    StringBuilder scheduleXML = new StringBuilder();
    for (model.allInterfaces.IEvent event: this.events) {
      scheduleXML.append(event.eventToXMLFormat() + "\n");
    }
    return scheduleXML.toString();
  }

  /**
   * Return the event occurring at a given time. Schedule can only have one event at any given time
   * @param time the time to search for events occurring during
   * @return the event at the given time. returns null if no event is occurring
   */
  public model.allInterfaces.IEvent eventOccurring(model.allInterfaces.ITime time) {

    for (IEvent event: this.events) {
      ITime tempEndTime = event.getEndTime();
      // event goes to following week, so artificially ending at end of this week
      if (event.getStartTime().compareTimes(event.getEndTime()) >= 0) {
        tempEndTime = new Time(Time.Day.SATURDAY, 23, 59);
      }
      if (event.getStartTime().compareTimes(time) <= 0
              && tempEndTime.compareTimes(time) >= 0) {
        return event;
      }
    }
    return null;
  }

}
