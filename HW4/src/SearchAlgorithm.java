public class SearchAlgorithm {

  public Schedule simulatedAnnealing(SchedulingProblem problem, long deadline){
    //Empty schedule to fill
    Schedule solution = problem.getEmptySchedule();
    //Schedules that will be used for the annealing process.
    Schedule current = solution;
    Schedule next = solution;
    //Temperature value that will help simulate the annealing.
    double T = 1;
    //Limit to the annealing
    long limit = 50;
    //Initial Solution
    current = randomSolution(current,problem);
    //Change in score
    double scoreDelta = problem.evaluateSchedule(next)-problem.evaluateSchedule(current);
    System.out.println("reached for loop");
    for(;T<limit;T++){
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
    //We will randomly decide how many classes to move to another time slot
    int classesToModify = (int)Math.random()*10;
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
      int newRoom = (int)(Math.random()*current.schedule.length);
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
