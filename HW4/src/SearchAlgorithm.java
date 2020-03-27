public class SearchAlgorithm {

  public Schedule simulatedAnnealing(SchedulingProblem problem, long deadline){
    //Empty schedule to fill
    Schedule solution = problem.getEmptySchedule();
    //Schedules that will be used for the annealing process.
    Schedule current = solution;
    Schedule next = solution;
    //Temperature value that will help simulate the annealing.
    double T = 5000;
    //Limit to the annealing
    //Initial Solution
    current = randomSolution(current,problem);
    //Change in score
    double scoreDelta = problem.evaluateSchedule(next)-problem.evaluateSchedule(current);
    System.out.println("reached for loop");
    for(;T>0;T--){
      next = randomSuccessor(current,problem);
      if(scoreDelta>0){
        current = next;
      }else{
        if(Math.exp(scoreDelta/T)>Math.random()){
          current = next;
        }
      }
    }
    return solution;
  }

  /**
   * Method that will generate a neighbor from the current solution given, by either randomly permutating the solution, or
   * modifying a few of the assignments.
   * @param current
   * @param problem
   * @return
   */
  private Schedule randomSuccessor(Schedule current, SchedulingProblem problem) {
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

      while(newRoom<current.schedule.length){
        //We traverse the slots available in the new room
        int newSlot = 0;
        while(newSlot<current.schedule[newRoom].length){
          //If class can be assigned to new time slot and slot is not taken yet we assign it
          if(problem.courses.get(current.schedule[i][j]).timeSlotValues[newSlot]>0 && current.schedule[newRoom][newSlot]<0 && problem.courses.get(current.schedule[i][j]).enrolledStudents < problem.rooms.get(newRoom).capacity){
            current.schedule[newRoom][newSlot] = current.schedule[i][j];
            current.schedule[i][j] = -1;
            break;
          }
          newSlot++;
        }
        //If class assigned we move to next class
        if(current.schedule[i][j] == -1)
          break;
        newRoom = (int)(Math.random()*current.schedule.length);
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
  private Schedule randomSolution(Schedule current, SchedulingProblem problem) {
    /**
     * We will assign classes to random rooms and time slots that are valid
     */
    System.out.println("Before random solution for");
    for(int i = 0;i<problem.courses.size();i++){
      System.out.println("Inside for random solution");
      boolean assigned = false;
      while(!assigned){
        int randomRoom = (int)(Math.random()*current.schedule.length);
        int randomTime = (int)(Math.random()*current.schedule[0].length);
        if(problem.courses.get(i).timeSlotValues[randomTime]>0 && problem.courses.get(i).enrolledStudents < problem.rooms.get(randomRoom).capacity){
          current.schedule[randomRoom][randomTime] = i;
          assigned = true;
        }
      }
    }
    return current;
  }

  public Schedule backtrackSearch(SchedulingProblem problem, long deadline) {
    //Empty schedule to fill
    Schedule solution = problem.getEmptySchedule();
    return backtrackRecursive(solution, problem, deadline);
  }
  
  /**
   * Function that performs recursive backtracking.
   * @param solution
   * @param problem
   * @param deadline
   * @return
   */
  public Schedule backtrackRecursive(Schedule solution,SchedulingProblem problem, long deadline){
    return  solution;
  }


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
