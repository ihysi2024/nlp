import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import model.Event;
import model.IEvent;
import model.IUser;
import model.NUPlanner;
import model.PlannerSystem;
import model.Schedule;
import model.Time;

import view.IEventView;
import view.IPlannerView;
import strategies.IScheduleStrategy;
import view.IScheduleView;
import strategies.ScheduleAnyTime;
/**
 * Test the controller and that it can correctly delegate to the right views.
 */

public class TestController {
  private IEvent morningLec;
  private IEvent morningSnack;

  /**
   * Set up examples to use in the corresponding tests.
   */

  @Before
  public void setUp() {
    PlannerSystem modelForTextView = new NUPlanner(new ArrayList<>());

    Schedule emptySchedule = new Schedule(new ArrayList<>());
    this.morningLec = new Event("CS3500 Morning Lecture",
            new Time( Time.Day.TUESDAY, 9, 50),
            new Time(Time.Day.TUESDAY, 11, 30),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));

    Event newMorningLec = new Event("CS3500 Morning Lecture",
            new Time(Time.Day.TUESDAY, 13, 35),
            new Time(Time.Day.TUESDAY, 16, 00),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));

    // same time as the morning lecture
    Event morningLecSameTime = new Event("same time morning lecture",
            new Time(Time.Day.TUESDAY, 9, 50),
            new Time(Time.Day.TUESDAY, 11, 30),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList(
                    "Student Anon")));
    // overlapping time as the morning lecture
    Event morningLecOverlapping = new Event("overlapping morning lecture ",
            new Time(Time.Day.TUESDAY, 8, 30),
            new Time(Time.Day.TUESDAY, 10, 30),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));
    // start time same as end time of morning lecture
    Event morningLecEndTime = new Event("same start time as end time",
            new Time(Time.Day.TUESDAY, 11, 30),
            new Time(Time.Day.TUESDAY, 12, 15),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));
    // same time as the morning lecture
    morningLecSameTime = new Event("same time morning lecture",
            new Time( Time.Day.TUESDAY, 9, 50),
            new Time(Time.Day.TUESDAY, 11, 30),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));
    // overlapping time as the morning lecture
    morningLecOverlapping = new Event("overlapping morning lecture ",
            new Time( Time.Day.TUESDAY, 8, 30),
            new Time(Time.Day.TUESDAY, 10, 30),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));
    // start time same as end time of morning lecture
    morningLecEndTime = new Event("same start time as end time",
            new Time( Time.Day.TUESDAY, 11, 30),
            new Time(Time.Day.TUESDAY, 12, 15),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Student Anon",
                    "Chat")));
    IEvent afternoonLec = new Event("CS3500 Afternoon Lecture",
            new Time(Time.Day.TUESDAY, 13, 35),
            new Time(Time.Day.TUESDAY, 15, 15),
            false,
            "Churchill Hall 101",
            new ArrayList<>(Arrays.asList("Prof. Lucia",
                    "Chat")));

    Schedule studentAnonSchedule = new Schedule(new ArrayList<>(Arrays.asList(morningLec)));
    Schedule chatSchedule = new Schedule(new ArrayList<>(Arrays.asList(morningLec, afternoonLec)));
    this.morningSnack = new Event("snack",
            new Time(Time.Day.TUESDAY, 8, 30),
            new Time(Time.Day.TUESDAY, 8, 45),
            false,
            "Churchill Hall 101",
            List.of("Student Anon"));
  }

  /**
   * Test that the controller correctly delegates to the event view.
   */

  @Test
  public void testEventFunctionality() {
    IEvent movie = new Event("movie",
            new Time(Time.Day.FRIDAY, 21, 15),
            new Time(Time.Day.FRIDAY, 23, 30),
            true,
            "home",
            List.of("Student Anon"));
    List<IUser> users = new ArrayList<>();
    PlannerSystem plannerSystem = new NUPlanner(users);
    IScheduleStrategy strategyAnyTime = new ScheduleAnyTime();
    Controller controller = new Controller(plannerSystem, strategyAnyTime);
    StringBuilder outEvent = new StringBuilder("");
    StringBuilder outPlanner = new StringBuilder("");
    IEventView mockView = new MockEventView(outEvent);
    IPlannerView mockPlanner = new MockPlannerView(outPlanner, plannerSystem);
    controller.setEventView(mockView);
    controller.setPlannerView(mockPlanner);

    controller.openEventView("Prof. Lucia");

    Assert.assertEquals("Opening an event", outEvent.toString());

    controller.openBlankEventView("Prof. Lucia");

    Assert.assertEquals("Opening an event", outEvent.toString());


    controller.resetPanelView();

    Assert.assertEquals("Resetting the panel", outEvent.toString());

    controller.populateEvent(this.morningSnack);

    Assert.assertEquals("Populating the view with the following event fields: \n"
            + "name: snack\n" +
            "time: Tuesday: 08:30->Tuesday: 08:45\n" +
            "location: Churchill Hall 101\n" +
            "online: false\n" +
            "users: Student Anon", outEvent.toString());

    controller.modifyEvent(this.morningSnack, movie);

    Assert.assertEquals("Populating the view with the following event fields: \n" +
            "name: snack\n" +
            "time: Tuesday: 08:30->Tuesday: 08:45\n" +
            "location: Churchill Hall 101\n" +
            "online: false\n" +
            "users: Student Anon", outEvent.toString());

    controller.createEvent();

    Assert.assertEquals("Creating an event", outEvent.toString());

    controller.storeEvent();

    Assert.assertEquals("Storing an opened event", outEvent.toString());

    controller.closeEventView();

    Assert.assertEquals("Closing an event", outEvent.toString());

    Map<String, String[]> dummyMap = new HashMap<>();

    controller.displayEventCreateErrors();

    Assert.assertEquals("Error in creating an event", outEvent.toString());

    controller.displayEventRemoveErrors(dummyMap);

    Assert.assertEquals("Error in removing an event", outEvent.toString());

    controller.displayEventModifyErrors();

    Assert.assertEquals("Error in modifying an event", outEvent.toString());

  }

  /**
   * Test that the controller correctly delegates to the planner view.
   */
  @Test
  public void testPlannerFunctionality() {
    List<IUser> users = new ArrayList<>();
    PlannerSystem plannerSystem = new NUPlanner(users);
    IScheduleStrategy strategyAnyTime = new ScheduleAnyTime();

    Controller controller = new Controller(plannerSystem, strategyAnyTime);
    StringBuilder outEvent = new StringBuilder("");
    StringBuilder outPlanner = new StringBuilder("");
    StringBuilder outSched = new StringBuilder("");
    IEventView mockView = new MockEventView(outEvent);
    IPlannerView mockPlanner = new MockPlannerView(outPlanner, plannerSystem);
    IScheduleView mockSchedule = new MockScheduleView(outSched, plannerSystem);
    controller.setEventView(mockView);
    controller.setPlannerView(mockPlanner);
    controller.setScheduleView(mockSchedule);

    controller.setCurrentUser();

    Assert.assertEquals("Setting the current user", outPlanner.toString());

    controller.findEvent(new Time(Time.Day.TUESDAY, 14, 0));

    Assert.assertEquals("Finding event at time: Tuesday: 14:00", outPlanner.toString());

    controller.selectUserSchedule("Prof. Lucia");

    Assert.assertEquals("Displaying the schedule for \n" +
            "User: Prof. Lucia\n" +
            "Sunday: \n" +
            "Monday: \n" +
            "Tuesday: \n" +
            "name: CS3500 Morning Lecture\n" +
            "time: Tuesday: 09:50->Tuesday: 11:30\n" +
            "location: Churchill Hall 101\n" +
            "online: false\n" +
            "users: Prof. Lucia\n" +
            "Student Anon\n" +
            "Chat          \n" +
            "name: CS3500 Afternoon Lecture\n" +
            "time: Tuesday: 13:35->Tuesday: 15:15\n" +
            "location: Churchill Hall 101\n" +
            "online: false\n" +
            "users: Prof. Lucia\n" +
            "Chat          \n" +
            "Wednesday: \n" +
            "Thursday: \n" +
            "Friday: \n" +
            "name: Sleep\n" +
            "time: Friday: 18:00->Sunday: 12:00\n" +
            "location: Home\n" +
            "online: true\n" +
            "users: Prof. Lucia          \n" +
            "Saturday: \n", outPlanner.toString());

    controller.openPlannerView();

    Assert.assertEquals("Opening planner view", outPlanner.toString());

    controller.removeEvent(this.morningLec);

    Assert.assertEquals("Getting the current user", outPlanner.toString());

    controller.goLaunchPlanner();

    Assert.assertEquals("Displaying the planner view", outPlanner.toString());

    controller.createEvent();

    Assert.assertEquals("Opening planner view", outPlanner.toString());

    controller.addCalendar();

    Assert.assertEquals("Add this user: \n" +
            "User: Chat\n" +
            "Sunday: \n" +
            "Monday: \n" +
            "Tuesday: \n" +
            "name: CS3500 Afternoon Lecture\n" +
            "time: Tuesday: 13:35->Tuesday: 15:15\n" +
            "location: Churchill Hall 101\n" +
            "online: false\n" +
            "users: Prof. Lucia\n" +
            "Chat          \n" +
            "Wednesday: \n" +
            "Thursday: \n" +
            "Friday: \n" +
            "Saturday: \n", outPlanner.toString());


    controller.closePlannerView();

    Assert.assertEquals("Closing the planner view", outPlanner.toString());
  }

  /**
   * Test that the controller correctly delegates to the schedule event view.
   */
  @Test
  public void testScheduleFunctionality() {
    List<IUser> users = new ArrayList<>();
    PlannerSystem plannerSystem = new NUPlanner(users);
    IScheduleStrategy strategyAnyTime = new ScheduleAnyTime();
    Controller controller = new Controller(plannerSystem, strategyAnyTime);
    StringBuilder outEvent = new StringBuilder("");
    StringBuilder outPlanner = new StringBuilder("");
    StringBuilder outSched = new StringBuilder("");
    IEventView mockView = new MockEventView(outEvent);
    IPlannerView mockPlanner = new MockPlannerView(outPlanner, plannerSystem);
    IScheduleView mockSchedule = new MockScheduleView(outSched, plannerSystem);
    controller.setEventView(mockView);
    controller.setPlannerView(mockPlanner);
    controller.setScheduleView(mockSchedule);

    controller.openScheduleView();

    Assert.assertEquals("Opening schedule panel view", outSched.toString());

    controller.scheduleEventInPlanner();

    Assert.assertEquals("Opening schedule panel view", outSched.toString());

    controller.closeScheduleView();

    Assert.assertEquals("Closing schedule panel view", outSched.toString());

    controller.resetSchedulePanelView("Prof. Lucia");

    Assert.assertEquals("Resetting the schedule panel", outSched.toString());

    controller.displayScheduleErrors();

    Assert.assertEquals("Displaying the error in scheduling an event", outSched.toString());
  }
}
