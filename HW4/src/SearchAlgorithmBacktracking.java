import java.util.ArrayList;
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
    //determines heuristic to be used, 2 orders courses largest to smallest, 1 orders rooms smallest to largest, 0 is no heuristic
    //heuristic 3 orders both so as to visually check for solutions. It is the most efficient
    int heuristic = 3;

    double[][] distanceTable = setDistances(problem.buildings);

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();
    Stack traverse = new Stack<Option>();

    Option option;
    Room room;
    Course course;
    int roomIndex;
    int courseIndex;

    if(heuristic == 1){
      orderRooms(problem.rooms);
    } else if(heuristic == 2){
      orderCourses(problem.courses);
    } else if(heuristic == 3){
      orderCourses(problem.courses);
      orderRooms(problem.rooms);
    }

    for(int i = 0; i < problem.courses.size(); i++){
      System.out.print(problem.courses.get(i).enrolledStudents + " ");
    }
    System.out.println();
    for(int i = 0; i < problem.rooms.size(); i++){
      System.out.print(problem.rooms.get(i).capacity + " ");
    }
    System.out.println();

    for(int i = 0; i < problem.rooms.size(); i++){
      int students = (course = problem.courses.get(0)).enrolledStudents;
      if((room = problem.rooms.get(i)).capacity >= students){             //no need to check time slots, they're all open
        traverse.push(new Option(course, room));                           //if room is larger than enrollment, add
      }
    }

    while(!traverse.isEmpty()){
      if(System.currentTimeMillis() == deadline) break;

      option = (Option) traverse.pop();

      room = option.R;
      course = option.C;
      roomIndex = getRoomIndex(problem.rooms, room);
      courseIndex = getCourseIndex(problem.courses, course);

      for (int i = 0; i < problem.NUM_TIME_SLOTS; i++) {
        if (course.timeSlotValues[i] > 0) {
          if (solution.schedule[roomIndex][i] < 0) {
            solution.schedule[roomIndex][i] = courseIndex;                //schedule in a fitting slot
            break;
          }
        }
      }

      //System.out.println(solution);                                     //print solution so far

      if(courseIndex == problem.courses.size()-1) break;                  //check if this is the last course

      boolean nextExists = false;                                         //valid parts of solutions have children
      for(int i = 0; i < problem.rooms.size(); i++){                      //go through all possible rooms
        int students = (course = problem.courses.get(courseIndex+1)).enrolledStudents;
        if((room = problem.rooms.get(i)).capacity >= students){
          for (int j = 0; j < problem.NUM_TIME_SLOTS; j++) {              //find one with appropriate slot and size
            if (course.timeSlotValues[j] > 0) {
              if (solution.schedule[i][j] < 0) {
                traverse.push(new Option(course, room));
                nextExists = true;
                break;                                                    //just one empty slot makes the solution valid
              }
            }
          }
        }
      }

      //reorder according to building priority
      orderLocation(traverse, distanceTable, courseIndex, problem.buildings, problem.courses);

      //backtracking occurs here
      if(!nextExists && !traverse.isEmpty()){
        //System.out.println("backtracking");
        Course lastChoiceMade = ((Option) traverse.peek()).C;
        courseIndex = getCourseIndex(problem.courses, lastChoiceMade);
        for(int i = 0; i < solution.schedule.length; i++){
          for(int j = 0; j < solution.schedule[0].length; j++){
            if (solution.schedule[i][j] >= courseIndex){
              solution.schedule[i][j] = -1;
            }
          }
        }
      }
    }

    if(traverse.isEmpty()){
      System.out.println("No solution exists");
    }
    System.out.println(solution);
    return solution;
  }

  public int getRoomIndex(ArrayList<Room> rooms, Room room) {
    int roomIndex = -1;
    for (int i = 0; i < rooms.size(); i++) {
      if (roomIndex >= 0) break;
      if (room.equals(rooms.get(i))) {
        roomIndex = i;
      }
    }
    return roomIndex;
  }

  public int getCourseIndex(ArrayList<Course> courses, Course course){
    int courseIndex = -1;
    for(int i = 0; i < courses.size(); i++) {
      if (courseIndex >= 0) break;
      if (course.equals(courses.get(i))) {
        courseIndex = i;
      }
    }
    return courseIndex;
  }

  public int getBuildingIndex(ArrayList<Building> buildings, Building building){
    int buildingIndex = -1;
    for(int i = 0; i < buildings.size(); i++) {
      if (buildingIndex >= 0) break;
      if (building.equals(buildings.get(i))) {
        buildingIndex = i;
      }
    }
    return buildingIndex;
  }


  public void orderCourses(ArrayList<Course> courses){
    Course temp;
    for(int i = 0; i < courses.size(); i++){
      for(int j = i; j < courses.size(); j++){
        if(courses.get(i).enrolledStudents < courses.get(j).enrolledStudents){
          temp = courses.get(i);
          courses.set(i, courses.get(j));
          courses.set(j, temp);
        }
      }
    }
  }

  public void orderRooms(ArrayList<Room> rooms){
    Room temp;
    for(int i = 0; i < rooms.size(); i++){
      for(int j = i; j < rooms.size(); j++){
        if(rooms.get(j).capacity < rooms.get(i).capacity){
          temp = rooms.get(i);
          rooms.set(i, rooms.get(j));
          rooms.set(j, temp);
        }
      }
    }
  }

  public double[][] setDistances(ArrayList<Building> buildings){
    double[][] table = new double[buildings.size()][buildings.size()];

    double var1, var2;
    Building buildingi, buildingj;

    for(int i = 0; i < buildings.size(); i++){
      table[i][i] = 0;
      buildingi = buildings.get(i);
      for(int j = 0; j < buildings.size(); j++){
        buildingj = buildings.get(j);
        var1 = (buildingi.xCoord - buildingj.xCoord) * (buildingi.xCoord - buildingj.xCoord);
        var2 = (buildingi.yCoord - buildingj.yCoord) * (buildingi.yCoord - buildingj.yCoord);
        table[i][j] = Math.sqrt(var1 + var2);
      }
    }
    return table;
  }

  public void orderLocation(Stack<Option> traverse, double[][] distanceTable, int courseIndex, ArrayList<Building> buildings, ArrayList<Course> courses){
    ArrayList temp = new ArrayList<Option>();
    Option option;

    while(!traverse.isEmpty() && getCourseIndex(courses, ((Option) traverse.peek()).C) != courseIndex){
      temp.add(traverse.pop());
    }

    int currentLocationi, currentLocationj, preferredLocation;
    for(int i = 0; i < temp.size(); i++){
      currentLocationi = getBuildingIndex(buildings, ((Option)temp.get(i)).R.b);
      for(int j = 0; j < temp.size(); j++){
        preferredLocation = getBuildingIndex(buildings, courses.get(courseIndex).preferredLocation);
        currentLocationj = getBuildingIndex(buildings, ((Option)temp.get(j)).R.b);
        if(distanceTable[preferredLocation][currentLocationi] < distanceTable[preferredLocation][currentLocationj]){
          option = (Option) temp.get(i);
          temp.set(i, temp.get(j));
          temp.set(j, option);
        }
      }
    }

    for(int i = 0; i < temp.size(); i++){
      traverse.push((Option) temp.get(i));
    }
  }
}
