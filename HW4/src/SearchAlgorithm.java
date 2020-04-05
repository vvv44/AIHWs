import java.util.ArrayList;
import java.util.Stack;

public class SearchAlgorithm {
/*----------------SIMULATED ANNEALING IMPLEMENTED-------------------*/
  public Schedule simulatedAnnealing(SchedulingProblem problem, long deadline){
    //Empty schedule to fill
    Schedule solution = problem.getEmptySchedule();
    //Schedules that will be used for the annealing process.
    Schedule current = solution;
    Schedule next = solution;
    //Temperature value that will help simulate the annealing.
    double T = 5000;
    //Starting time 
    long startTime = System.nanoTime();

    //Initial Solution
    current = randomSolution(current,problem,deadline,startTime);
    double scoreDelta;
    for(;T>0;T--){
      if(System.nanoTime()>(startTime+(deadline*1000000000))){
        System.out.println("Time limit reached, returning currently achieved solution");
        return solution;
      }

      next = randomSuccessor(current,problem,deadline,startTime);
      //Change in score
      scoreDelta = problem.evaluateSchedule(next)-problem.evaluateSchedule(current);
      if(scoreDelta>0){
        current = next;
      }else{
        if(Math.exp(scoreDelta/T)>Math.random()){
          current = next;
        }
      }
    }
    return current;
  }

  /**
   * Method that will generate a neighbor from the current solution given, by either randomly permutating the solution, or
   * modifying a few of the assignments.
   * @param current
   * @param problem
   * @return
   */
  private Schedule randomSuccessor(Schedule current, SchedulingProblem problem, long deadline,long startTime) {
    if(System.nanoTime()>(startTime+(deadline*1000000000))){
      System.out.println("Time limit reached, returning currently achieved solution");
      return current;
    }
    //We will then shift those classes to another time slot and/or room
    for(int i = 0;i<current.schedule.length;i++){
      int j = 0;
      while(j<current.schedule[i].length && current.schedule[i][j]<0){
        j++;
      }
      if(j==current.schedule[i].length){
        continue;
      }
      //Here we are in the assigned class to the room i and time slot j.
      /*We will shift the class to a new random room and timeslot that works for the class*/
      //int newRoom = (int)(Math.random()*current.schedule.length);

      /**We can also choose the best room out of a random selection of rooms (best as in closest to desired buuilding by class)*/
      int newRoom = bestRandomRoom(problem, current.schedule[i][j]);
      //FIXME: Does not end when class is unassignable
      while(newRoom<current.schedule.length){
        //We traverse the slots available in the new room
        int newSlot = 0;
        while(newSlot<current.schedule[newRoom].length){
          //If class can be assigned to new time slot and slot is not taken yet we assign it
          if(problem.courses.get(current.schedule[i][j]).timeSlotValues[newSlot]>0 && current.schedule[newRoom][newSlot]<0 /*&& problem.courses.get(current.schedule[i][j]).enrolledStudents < problem.rooms.get(newRoom).capacity*/){
            current.schedule[newRoom][newSlot] = current.schedule[i][j];
            current.schedule[i][j] = -1;
            break;
          }
          newSlot++;
        }
        //If class assigned we move to next class
        if(current.schedule[i][j] == -1)
          break;
        //newRoom = (int)(Math.random()*current.schedule.length);
        newRoom = bestRandomRoom(problem, current.schedule[i][j]);
      }
    }
    return current;
  }

  /**
   * Method that will generate a few random rooms to choose from, and it will evaluate the one that is in closest to (or in the building) preferred
   * by the class given
   * @param problem
   * @return
   */
  private int bestRandomRoom(SchedulingProblem problem, int currClass) {
    //The number of rooms we will generate
    int numOfRooms = (int)(Math.random()*problem.rooms.size());
    int bestRoom = 0;
    int currRoom = 0;
    Building preferred = problem.courses.get(currClass).preferredLocation;
    //We will iterate generating random rooms and keep the one that is closest to current class preferred building
    for(int i = 0;i<numOfRooms;i++){
      currRoom = (int)(Math.random()*problem.rooms.size());
      //Check if it is better than current best room.
      if(distanceOfBuilding(preferred,problem.rooms.get(currRoom).b)<distanceOfBuilding(preferred, problem.rooms.get(bestRoom).b)){
        bestRoom = currRoom;
      }
    }

    return bestRoom;
  }

  /**
   * Method that provides distance between two buildings
   * @param preferred
   * @param b
   * @return
   */
  private double distanceOfBuilding(Building b1, Building b2) {
    double xDist = (b1.xCoord - b2.xCoord) * (b1.xCoord - b2.xCoord);
    double yDist = (b1.yCoord - b2.yCoord) * (b1.yCoord - b2.yCoord);
    return Math.sqrt(xDist + yDist);
  }

  /**
   * Method that provides an initial random solution to start with. Will be
   * randomly generated but still follow the restrictions and constraints, so it
   * will be a valid solution.
   * 
   * @param current
   * @param problem
   * @return
   */
  private Schedule randomSolution(Schedule current, SchedulingProblem problem, long deadline,long startTime) {
    /**
     * We will assign classes to random rooms and time slots that are valid
     */

    for(int i = 0;i<problem.courses.size();i++){
      if(System.nanoTime()>(startTime+(deadline*1000000000))){
        System.out.println("Time limit reached, returning currently achieved solution");
        return current;
      }
      boolean assigned = false;
      while(!assigned){
        //FIXME: Does not end when class is unnasignable
        int randomRoom = (int)(Math.random()*current.schedule.length);
        int randomTime = (int)(Math.random()*current.schedule[0].length);
        if(problem.courses.get(i).timeSlotValues[randomTime]>0 /*&& problem.courses.get(i).enrolledStudents < problem.rooms.get(randomRoom).capacity*/){
          current.schedule[randomRoom][randomTime] = i;
          assigned = true;
        }
      }
    }
    return current;
  }
  /*-------------------SIMULATED ANNEALING IMPLEMENTED-----------------------------*/

  /*-------------------BACKTRACKING ALGORITHM IMPLEMENTED--------------------------*/
  
  /**
   * Method that implements backtracking search algorithm 
   * @param problem
   * @param deadline
   * @return
   */
  public Schedule backTracking(SchedulingProblem problem, long deadline) {
    double[][] distanceTable = setDistances(problem.buildings);

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();
    //Stack to traverse for the backtracking
    Stack<Next> traverse = new Stack<Next>();

    Next Next;
    Room room;
    Course course;
    int roomIndex;
    int courseIndex;

    /**Order rooms and courses by distance to provide the best solution possible 
     * This is the heuristic
    */
    orderCourses(problem.courses);
    orderRooms(problem.rooms);


    for(int i = 0; i < problem.rooms.size(); i++){
      int students = (course = problem.courses.get(0)).enrolledStudents;
      if((room = problem.rooms.get(i)).capacity >= students){             //no need to check time slots, they're all open
        traverse.push(new Next(course, room));                           //if room size is more than the enrollment of a course
      }
    }

    while(!traverse.isEmpty()){
      if(System.currentTimeMillis() == deadline) break;

      Next = (Next) traverse.pop();

      room = Next.R;
      course = Next.C;
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


      if(courseIndex == problem.courses.size()-1) break;                  //check if this is the last course

      boolean nextExists = false;                                         //valid parts of solutions have children
      for(int i = 0; i < problem.rooms.size(); i++){                      //go through all possible rooms
        int students = (course = problem.courses.get(courseIndex+1)).enrolledStudents;
        if((room = problem.rooms.get(i)).capacity >= students){
          for (int j = 0; j < problem.NUM_TIME_SLOTS; j++) {              //find one with appropriate slot and size
            if (course.timeSlotValues[j] > 0) {
              if (solution.schedule[i][j] < 0) {
                traverse.push(new Next(course, room));
                nextExists = true;
                break;                                                    //just one empty slot makes the solution valid
              }
            }
          }
        }
      }

      
      //reorder according to building priority
      orderLocation(traverse, distanceTable, courseIndex, problem.buildings, problem.courses);

      //Here we backtrack if there is no next option (we reached the deepest area)
      if(!nextExists && !traverse.isEmpty()){
        Course lastNextMade = ((Next) traverse.peek()).C;
        courseIndex = getCourseIndex(problem.courses, lastNextMade);
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


  /**
   * Method to get the index of the provided room
   * @param rooms
   * @param room
   * @return
   */
  public int getRoomIndex(ArrayList<Room> rooms, Room room) {
    int roomIndex = -1;
    for (int i = 0; i < rooms.size(); i++) {
      if (room.equals(rooms.get(i))) {
        return i;
      }
    }
    return roomIndex;
  }

  /**
   * Get index of the provided course
   * @param courses
   * @param course
   * @return
   */
  public int getCourseIndex(ArrayList<Course> courses, Course course){
    int courseIndex = -1;
    for(int i = 0; i < courses.size(); i++) {
      if (course.equals(courses.get(i))) {
        return i;
      }
    }
    return courseIndex;
  }

  /**
   * Get index of provided index
   * @param buildings
   * @param building
   * @return
   */
  public int getBuildingIndex(ArrayList<Building> buildings, Building building){
    int buildingIndex = -1;
    for(int i = 0; i < buildings.size(); i++) {
      if (building.equals(buildings.get(i))) {
        return i;
      }
    }
    return buildingIndex;
  }

/**
 * Order courses based on size of enrollment (descendent)
 * @param courses
 */
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

  /**
   * Order rooms based on capacity (ascendent)
   * @param rooms
   */
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

  /**
   * Method that sets distances between buildings
   * @param buildings
   * @return
   */
  public double[][] setDistances(ArrayList<Building> buildings){
    double[][] table = new double[buildings.size()][buildings.size()];

    double var1, var2;
    Building building1, building2;

    for(int i = 0; i < buildings.size(); i++){
      table[i][i] = 0;
      building1 = buildings.get(i);
      for(int j = 0; j < buildings.size(); j++){
        building2 = buildings.get(j);
        var1 = (building1.xCoord - building2.xCoord) * (building1.xCoord - building2.xCoord);
        var2 = (building1.yCoord - building2.yCoord) * (building1.yCoord - building2.yCoord);
        table[i][j] = Math.sqrt(var1 + var2);
      }
    }
    return table;
  }

  /**
   * Orders location to consider preferred building of the courses
   * @param traverse
   * @param distanceTable
   * @param courseIndex
   * @param buildings
   * @param courses
   */
  public void orderLocation(Stack<Next> traverse, double[][] distanceTable, int courseIndex, ArrayList<Building> buildings, ArrayList<Course> courses){
    ArrayList<Next> temp = new ArrayList<Next>();
    Next Next;

    while(!traverse.isEmpty() && getCourseIndex(courses, ((Next) traverse.peek()).C) != courseIndex){
      temp.add(traverse.pop());
    }

    int currentLocationi, currentLocationj, preferredLocation;
    for(int i = 0; i < temp.size(); i++){
      currentLocationi = getBuildingIndex(buildings, ((Next)temp.get(i)).R.b);
      for(int j = 0; j < temp.size(); j++){
        preferredLocation = getBuildingIndex(buildings, courses.get(courseIndex).preferredLocation);
        currentLocationj = getBuildingIndex(buildings, ((Next)temp.get(j)).R.b);
        if(distanceTable[preferredLocation][currentLocationi] < distanceTable[preferredLocation][currentLocationj]){
          Next = (Next) temp.get(i);
          temp.set(i, temp.get(j));
          temp.set(j, Next);
        }
      }
    }

    for(int i = 0; i < temp.size(); i++){
      traverse.push((Next) temp.get(i));
    }
  }
/*---------------BACKTRACKING ALGORITHM IMPLEMENTED----------------------------*/

/*---------------NAIVE SOLUTION----------------------------------------------- */
  /**
   * 
   * @param problem
   * @param deadline
   * @return
   */
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
}
