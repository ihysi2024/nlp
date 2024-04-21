package model.sunday;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.allInterfaces.IEvent;
import model.allInterfaces.IUser;
import model.allInterfaces.PlannerSystem;

import static controller.UtilsXML.readXML;
import static model.sunday.User.interpretXML;

/**
 * Planner system that contains a set of users and their corresponding schedules.
 * Allows a user to display their schedule, create new events, modify existing events, and
 * remove old events. Also allows users to upload their schedules in the form of an XML file
 * or export their schedules as XML.
 */
public class NUPlanner implements PlannerSystem {

  /**
   * Planner system that contains a list of users and their corresponding schedules.
   * Assumptions to be made is that all users that will ever be invited to an event
   * are already in the system and that every user has exactly one schedule. Duplicate
   * users cannot be added to the system by the inclusion of a set of users
   * instead of a list.
   */
  private List<model.allInterfaces.IUser> users;

  private String host;

  /**
   * Initialize a planner system with a given set of users.
   *
   * @param users non-duplicate user list in the system
   */
  public NUPlanner(List<model.allInterfaces.IUser> users, String host) {
    this.users = new ArrayList<>(users);
    this.host = host;
  }

  public NUPlanner(List<model.allInterfaces.IUser> users) {
    this.users = new ArrayList<>(users);
    this.host = "None";
  }

  /**
   * Initialize a planner system with an empty list of users.
   */
  public NUPlanner(String host) {
    this.users = new ArrayList<>();
    this.host = host;
  }

  /**
   * Observe the users in the system.
   * @return a set of users
   */

  public List<model.allInterfaces.IUser> getUsers() {
    return this.users;
  }

  /**
   * Returns the current host whose planner is open.
   * @return the current host whose planner is open
   */
  public String getHost() {
    return this.host;
  }

  /**
   * Adds a user to the planner system with the schedule provided in the file path's XML.
   * @param filePath file path to read XML schedule from
   */

  @Override
  public void importScheduleFromXML(String filePath) {
    this.users.add(interpretXML(readXML(filePath))); // adding new user to planner system

    // adding this user's events to each existing schedule
    int numUsers = this.users.size();
    List<model.allInterfaces.IEvent> newUserEvents = this.users.get(numUsers - 1).getSchedule().getEvents();

    ArrayList<model.allInterfaces.IEvent> arrNewUserEvents = new ArrayList<>(newUserEvents);

    for (model.allInterfaces.IEvent eventToAdd : arrNewUserEvents) {
      this.addEventForRelevantUsers(eventToAdd);
    }
  }

  /**
   * Retrieves the events in this user's schedule.
   *
   * @param user desired user for whom to retrieve the schedule
   * @return a list of this user's events
   */
  @Override
  public List<model.allInterfaces.IEvent> retrieveUserEvents(model.allInterfaces.IUser user) {
    System.out.println("num events: " + user.getSchedule().getEvents().size());
    return user.getSchedule().getEvents();
  }

  /**
   * Write each user's schedule in the system to an XML file and store it.
   * @param filePathToSave where to save the XML file
   */
  public void exportScheduleAsXML(String filePathToSave) {
    for (model.allInterfaces.IUser user: this.users) {
      user.userSchedToXML(filePathToSave);
    }
  }

  /**
   * return event in a user's schedule at a given time.
   *
   * @param user      the user to examine
   * @param givenTime the time to look at event within
   * @return an event. return null if no events at that time
   * @throws IllegalArgumentException if user doesn't exist or doesn't have a schedule
   */
  @Override
  public model.allInterfaces.IEvent retrieveUserScheduleAtTime(model.allInterfaces.IUser user, model.allInterfaces.ITime givenTime) {
    return user.getSchedule().eventOccurring(givenTime);
  }

  /**
   * Add events for the users listed in the event's invitee list.
   *
   * @param eventToAdd event to add to the relevant user schedule
   */
  public void addEventForRelevantUsers(model.allInterfaces.IEvent eventToAdd) {
    for (model.allInterfaces.IUser currUser : this.users) {
      boolean eventAtTime = currUser.getSchedule().getEvents().contains(eventToAdd);
      if (eventToAdd.getUsers().contains(currUser.getName()) && !eventAtTime) {
        try {
          // add event to current user's schedule
          currUser.addEventForUser(eventToAdd); // make sure event isn't being added for ira again
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Cannot add event for: "
                  + currUser.getName() + e.getMessage());
          // event is not added because it overlaps
        }
      }
    }
  }

  /**
   * Adding a user to the planner system. A user has a schedule.
   *
   * @param userToAdd user to add to Planner
   */
  @Override
  public void addUser(model.allInterfaces.IUser userToAdd) {
    this.users.add(userToAdd);
  }

  /**
   * Events can only be modified if all users can attend the event.
   * @param prevEvent event to be modified
   * @param newEvent what the previous event should be modified to
   * @throws IllegalArgumentException if user listed cannot attend the modified event
   **/
  public void modifyEvent(model.allInterfaces.IEvent prevEvent, model.allInterfaces.IEvent newEvent) {
    model.allInterfaces.IUser host = null;
    for (model.allInterfaces.IUser user: this.users) {
      if (user.getName().equals(prevEvent.getUsers().get(0))) {
        host = new User(user.getName(), user.getSchedule());
      }
    }

    // only allow modification if the old event and updated event
    // still have the same host
    if (newEvent.getUsers().contains(prevEvent.getUsers().get(0))) {
      try {
        // remove the previous event from the user's schedule
        removeEventForRelevantUsers(prevEvent, host);
        // add the new event to the user's schedule
        addEventForRelevantUsers(newEvent);

      }
      catch (IllegalArgumentException e) {
        // if an exception is thrown, leave the list of events alone to avoid
        // causing conflict in any user's schedule
        //addEventForRelevantUsers(prevEvent);
      }
    }
  }

  /**
   * Remove an event from planner system for relevant users.
   * If host (first user in invitee list) is removing event, remove for all users.
   * If any other user, only remove event from their schedule.
   *
   * @param eventToRemove event to remove from planner system
   * @param userRemovingEvent user removing the event
   */
  public void removeEventForRelevantUsers(model.allInterfaces.IEvent eventToRemove, model.allInterfaces.IUser userRemovingEvent) {
    Iterator<model.allInterfaces.IUser> iterUsers = this.users.iterator();

    String hostOfEvent = eventToRemove.getUsers().get(0);

    if (userRemovingEvent.getName().equals(hostOfEvent)) { // host removing event, remove for all
      while (iterUsers.hasNext()) {
        model.allInterfaces.IUser currUser = iterUsers.next();
        if (eventToRemove.getUsers().contains(currUser.getName())) {
          try {
            // remove the event from the current user's schedule
            currUser.removeEventForUser(eventToRemove);
          }
          // given event is not in schedule
          catch (IllegalArgumentException e) {
            // ignoring exception because event will not be removed for this user
            throw new IllegalArgumentException("given event not part of this user's schedule");
          }
        }
      }
    }
    // just an invitee trying to remove an event, removing event + updating invitee list for all
    else {
      userRemovingEvent.removeEventForUser(eventToRemove);
      removeUserFromEventList(eventToRemove, userRemovingEvent);
    }
  }

  /**
   * Remove a user from the event list for every other person in planner system
   * that is also invited to the given event.
   *
   * @param event event to updated in planner system
   * @param userToRemove user being removed from the event
   */
  @Override
  public void removeUserFromEventList(model.allInterfaces.IEvent event, model.allInterfaces.IUser userToRemove) {
    // loop through all of the users
    // if the given event is part of their schedule, remove this user from their user list
    // can you just edit the actual event's user list??
    for (IUser currUser : this.users) {
      if (event.getUsers().contains(currUser.getName())) {
        for (int eventIdx = 0; eventIdx < currUser.getSchedule().getEvents().size(); eventIdx++) {
          IEvent currEvent = currUser.getSchedule().getEvents().get(eventIdx);
          if (event.getEventName().equals(currEvent.getEventName())) {
            currEvent.removeUserFromList(userToRemove.getName());
          }
        }
      }
    }
  }


}
