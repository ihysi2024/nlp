import java.util.List;

import controller.ViewFeatures;
import model.allInterfaces.PlannerSystem;
import view.IScheduleView;

/**
 * Represents the mock view that allows an event to be scheduled as early as possible given
 * user time constraints.
 */

public class MockScheduleView implements IScheduleView {

  private StringBuilder out;

  /**
   * Represents the mock view of a schedule panel.
   * @param out where the user can read the mock's outputs.
   * @param model what planner system the mock is based on.
   */

  public MockScheduleView(StringBuilder out, PlannerSystem model) {
    this.out = out;
  }

  /**
   * Opens up the current user's schedule.
   */

  @Override
  public void openScheduleView() {
    out.delete(0, out.length());
    out.append("Opening schedule panel view");
  }

  /**
   * Closes the current schedule view.
   */

  @Override
  public void closeScheduleView() {
    out.delete(0, out.length());
    out.append("Closing schedule panel view");
  }

  /**
   * Adds feature listeners available on this panel, including the button clicks for
   * creating and scheduling events, adding/saving calendars, and selecting a user.
   *
   * @param features available features
   */

  @Override
  public void addFeatures(ViewFeatures features) {
    // unnecessary for the mock
  }

  /**
   * Get the user's input for the event name.
   * @return a String of the event name
   */

  @Override
  public String getEventNameInput() {
    return null;
  }

  /**
   * Get the user's input for the event name.
   * @return a String of the event name
   */

  @Override
  public String getLocationInput() {
    return null;
  }

  /**
   * Get the user's input for the event list of users.
   * @return a String[] of the location
   */

  @Override
  public List<String> getUsersInput() {
    return null;
  }

  /**
   * Observes the user's input for whether the event is online or not.
   * @return whether the event is online
   */

  @Override
  public boolean getOnline() {
    return false;
  }


  /**
   * Observes how long the event is.
   * @return the length of the event
   */

  @Override
  public int getDuration() {
    return 90;
  }

  /**
   * Empties the fields in the panel for the user to enter their own inputs.
   * @param host the host of the event.
   */

  @Override
  public void resetSchedulePanel(String host) {
    out.delete(0, out.length());
    out.append("Resetting the schedule panel");
  }

  /**
   * Display errors that may arise should the user provide invalid inputs to the panel.
   */

  @Override
  public void displayError() {
    out.delete(0, out.length());
    out.append("Displaying the error in scheduling an event");
  }
}
