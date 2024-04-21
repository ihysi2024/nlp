
import java.util.List;
import java.util.Map;

import controller.ViewFeatures;
import model.sunday.Event;
import model.interfaces.IEvent;
import model.interfaces.IUser;
import model.sunday.NUPlanner;
import model.interfaces.PlannerSystem;
import model.sunday.Time;
import view.IEventView;
import view.IScheduleTextView;
import view.ScheduleTextView;

/**
 * Represents a mock of an event view for testing the controller.
 */
public class MockEventView implements IEventView {

  private IScheduleTextView view;
  private StringBuilder out;

  /**
   * Represents how the mock will be displaying information.
   * @param out where the delegation can be checked.
   */
  public MockEventView(StringBuilder out) {
    this.out = out;
    PlannerSystem model = new NUPlanner("Prof. Lucia");
    this.view = new ScheduleTextView(model, out);
  }

  /**
   * Allow the user to interact with the calendar through the features present
   * in the event view.
   * @param features functionality that the user has access to through the event view.
   */
  public void addFeatures(ViewFeatures features) {
    // unnecessary for the mock.
  }

  /**
   * Close the event view so it stops being visible.
   */
  public void closeEvent() {
    out.delete(0, out.length());
    out.append("Closing an event");
  }

  /**
   * Set the event fields on the panel to the given event's fields.
   * Visualizes a user's entry for an event in the event panel text fields.
   * @param event event to visualize in the event panel.
   */
  public void populateEventContents(IEvent event) {
    out.delete(0, out.length());
    out.append("Populating the view with the following event fields: \n");
    out.append(view.eventToString(event));
  }

  /**
   * Get the user's input for the event name.
   * @return a String[] of the event name
   */
  public String[] getEventNameInput() {
    return null;
  }

  /**
   * Get the user's input for the event time.
   * @return a String[] of the event time
   */
  public String[] getTimeInput() {
    return null;
  }

  /**
   * Get the user's input for the event location.
   * @return a String[] of the location
   */
  public String[] getLocationInput() {
    return null;
  }

  /**
   * Get the user's input for the event list of users.
   * @return a String[] of the location
   */
  public String[] getUsersInput() {
    return null;
  }

  /**
   * Modify an event with the user's new input to the event panel.
   * @param event represents the updated event
   */
  public void modifyEvent(IEvent event) {
    out.delete(0, out.length());
    out.append("Modifying the event with the following event fields: \n");
    out.append(view.eventToString(event));
  }

  /**
   * Resets the panel to its originally empty fields. Useful for trying to create a new event
   * after an event has already been created.
   */
  public void resetPanel() {
    out.delete(0, out.length());
    out.append("Resetting the panel");
  }

  @Override
  public void openEvent(String host) {
    out.delete(0, out.length());
    out.append("Opening an event");
  }

  /**
   * Resets the panel to its originally empty fields. Useful for trying to create a new event
   * after an event has already been created.
   * @param host of the event to open.
   */

  @Override
  public void openBlankEvent(String host) {
    out.delete(0, out.length());
    out.append("Opening an event");
  }

  @Override
  public void displayCreateError() {
    out.delete(0, out.length());
    out.append("Error in creating an event");
  }

  /**
   * Store the current event's inputs as a map of String -> String[]
   * Useful for modifying an event with the current panel inputs.
   * @return a map of strings to string[]
   */
  public Map<String, String[]> storeOpenedEventMap() {
    out.delete(0, out.length());
    out.append("Storing an opened event");
    return null;
  }

  /**
   * Store the user's input as an event that is added to their schedule.
   * @return the event that was created, null otherwise
   */
  public IEvent createEvent() {
    out.delete(0, out.length());
    out.append("Creating an event");
    return new Event("movie",
            new Time(Time.Day.FRIDAY, 21, 15),
            new Time(Time.Day.FRIDAY, 23, 30),
            true,
            "home",
            List.of("Student Anon"));
  }

  /**
   * Updates list of users in event view.
   */
  public void updateUserList() {
    // unnecessary for the mock
  }

  @Override
  public void displayRemoveError(Map<String, String[]> eventToRemove) {
    out.delete(0, out.length());
    out.append("Error in removing an event");
  }

  @Override
  public void displayModifyError(IUser host) {
    out.delete(0, out.length());
    out.append("Error in modifying an event");
  }

}
