package view;

import java.util.Map;

import javax.swing.JFrame;

import controller.ViewFeatures;

import model.allInterfaces.IEvent;

import model.allInterfaces.IUser;
import model.allInterfaces.ReadOnlyPlanner;

/**
 * Frame for the event pop-out window, where users will be able to see the functionality
 * related to creating new events, modifying existing events, and removing events.
 */

public class EventView extends JFrame implements IEventView {
  private final EventPanel panel;

  /**
   * Creates a view of the Simon game.
   *
   * @param model desired model to represent Simon game
   */
  public EventView(ReadOnlyPlanner model) {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.panel = new EventPanel(model);
    this.add(panel);
    this.setVisible(false);
    this.pack();
  }

  /**
   * Store the user's input as an event that is added to their schedule.
   * Delegate to the panel.
   */
  public IEvent createEvent() {
    return panel.createEvent();
  }

  /**
   * Set the event fields on the panel to the given event's fields.
   * Visualizes a user's entry for an event in the event panel text fields.
   * Delegate to the panel.
   * @param event event to visualize in the event panel.
   */
  public void populateEventContents(IEvent event) {
    this.panel.populateEventContents(event);
  }

  /**
   * Get the user's input for the event name.
   * Delegate to the panel.
   * @return a String[] of the event name
   */
  @Override
  public String[] getEventNameInput() {
    return panel.getEventNameInput();
  }

  /**
   * Get the user's input for the event time.
   * Delegate to the panel.
   * @return a String[] of the event time
   */
  @Override
  public String[] getTimeInput() {
    return panel.getTimeInput();
  }

  /**
   * Get the user's input for the event location.
   * Delegate to the panel.
   * @return a String[] of the location
   */
  @Override
  public String[] getLocationInput() {
    return panel.getLocationInput();
  }

  /**
   * Resets the panel to its originally empty fields. Useful for trying to create a new event
   * Delegate to the panel.
   * after an event has already been created.
   */
  public void resetPanel() {
    panel.resetPanel();
  }

  /**
   * Get the user's input for the event list of users.
   * Delegate to the panel.
   * @return a String[] of the location
   */
  @Override
  public String[] getUsersInput() {
    return panel.getUsersInput();
  }

  /**
   * Store the current event's inputs as a map of String -> String[]
   * Useful for modifying an event with the current panel inputs.
   * Delegate to the panel.
   *
   * @return a map of strings to string[]
   */
  @Override
  public Map<String, String[]> storeOpenedEventMap() {
    return panel.storeOpenedEventMap();
  }

  /**
   * Allow the user to interact with the calendar through the features present
   * in the event view.
   * Delegate to the panel.
   *
   * @param features functionality that the user has access to through the event view.
   */
  @Override
  public void addFeatures(ViewFeatures features) {
    panel.addFeatures(features);
  }

  /**
   * Close the event view so it stops being visible.
   */
  public void closeEvent() {
    this.setVisible(false);
  }

  /**
   * Open the event view for the user to see.
   * Delegate to the panel.
   *
   * @param host host of this event
   */
  public void openEvent(String host) {
    this.panel.openEvent(host);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Display error in creating the event.
   */
  public void displayCreateError() {
    panel.displayCreateError();
  }

  /**
   * Display error in removing the event.
   * @param eventToRemove event to remove in a map.
   */

  public void displayRemoveError(Map<String, String[]> eventToRemove) {
    panel.displayRemoveError(eventToRemove);
  }

  /**
   * Open the event view for the user to see.
   * Delegate to the panel.
   *
   * @param host host of this event
   */

  public void openBlankEvent(String host) {
    this.panel.openBlankEvent(host);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Updates list of users in event view.
   */

  @Override
  public void updateUserList() {
    panel.updateUserList();
  }

  /**
   * Displays the error that arises when a user tries to modify an event.
   * @param host the user of the event
   */

  public void displayModifyError(IUser host) {
    panel.displayModifyError(host);
  }
}
