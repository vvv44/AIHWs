import java.util.Stack;

public class SearchAlgorithmBacktracking {

  // Your search algorithm should return a solution in the form of a valid
  // schedule before the deadline given (deadline is given by system time in ms)
  public Schedule solve(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    // YOUR CODE HERE

    return solution;
  }

  // This is a very naive baseline scheduling strategy
  // It should be easily beaten by any reasonable strategy
  public Schedule naiveBaseline(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    for (int i = 0; i < problem.courses.size(); i++) {
      Course c = problem.courses.get(i);
      boolean scheduled = false;
      for (int j = 0; j < c.timeSlotValues.length; j++) {
        if (scheduled) break;
        if (c.timeSlotValues[j] > 0) {
          for (int k = 0; k < problem.rooms.size(); k++) {
            if (solution.schedule[k][j] < 0) {
              solution.schedule[k][j] = i;
              scheduled = true;
              break;
            }
          }
        }
      }
    }

    return solution;
  }

  public Schedule backTracking(SchedulingProblem problem, long deadline) {
    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();
    Stack traverse = new Stack<Option>();

    Room room;
    Course course;
    int roomIndex;
    int courseIndex;

    Option option;

    for(int i = 0; i < problem.rooms.size(); i++){
      int students = (course = problem.courses.get(0)).enrolledStudents;
      if((room = problem.rooms.get(i)).capacity >= students){             //no need to check time slots, they're all open
        traverse.push(new Option(course, room));                           //if room is larger than enrollment, add
      }
    }

    while(!traverse.isEmpty()){
      option = (Option) traverse.pop();

      room = option.R;
      course = option.C;
      roomIndex = getRoomIndex(problem, room);
      courseIndex = getCourseIndex(problem, course);

      for (int i = 0; i < problem.NUM_TIME_SLOTS; i++) {
        if (option.C.timeSlotValues[i] > 0) {
          if (solution.schedule[roomIndex][i] < 0) {
            solution.schedule[roomIndex][i] = courseIndex;                //schedule in a fitting slot
            break;
          }
        }
      }

      System.out.println(solution.schedule);                              //print solution so far

      if(courseIndex == problem.courses.size()-1) break;                  //check if this is the last course

      boolean valid = false;                                              //valid parts of solutions have children
      for(int i = 0; i < problem.rooms.size(); i++){                      //go through all possible rooms
        int students = (course = problem.courses.get(courseIndex+1)).enrolledStudents;
        if((room = problem.rooms.get(i)).capacity >= students){
          for (int j = 0; j < problem.NUM_TIME_SLOTS; j++) {              //find one with appropriate slot and size
            if (course.timeSlotValues[j] > 0) {
              if (solution.schedule[roomIndex][i] < 0) {
                traverse.push(new Option(course, room));
                valid = true;
                break;
              }
            }
          }
        }
      }

      //heuristic goes here

      if(!valid){
        System.out.println("backtracking");
        Course lastChoiceMade = ((Option) traverse.peek()).C;
        courseIndex = getCourseIndex(problem, lastChoiceMade);
        for(int i = 0; i < solution.schedule.length; i++){
          for(int j = 0; j < solution.schedule[0].length; j++){
            if (solution.schedule[i][j] >= courseIndex){
              solution.schedule[i][j] = -1;
            }
          }
        }
      }
    }

    return solution;
  }

  public int getRoomIndex(SchedulingProblem problem, Room room) {
    int roomIndex = -1;
    for (int i = 0; i < problem.rooms.size(); i++) {
      if (roomIndex >= 0) break;
      if (room.equals(problem.rooms.get(i))) {
        roomIndex = i;
      }
    }
    return roomIndex;
  }

  public int getCourseIndex(SchedulingProblem problem, Course course){
    int courseIndex = -1;
    for(int i = 0; i < problem.courses.size(); i++) {
      if (courseIndex >= 0) break;
      if (course.equals(problem.courses.get(i))) {
        courseIndex = i;
      }
    }
    return courseIndex;
  }
}
