import java.util.*;

public class SearchAlgorithms{
    /**
	 * Method that implements bread first algorithm
	 */
	public boolean BreadtFirst(Graph g){
		PriorityQueue<Node> nextToVisit =new PriorityQueue<>(new Comparator<Node>(){
			@Override
			public int compare(Node fNode, Node sNode){											//put the smallest in front first
				return fNode.cost - sNode.cost;													// put sNode fist: compare a big against a smaller will give a bigger value than 0
																								//leave fNode first: compare a small number against a bigger will give a smaller number than 0
			}
		});
		List<Node> visitedPos = new ArrayList<>();
		nextToVisit.add(g.map[g.startx][g.starty]);

		while(!nextToVisit.isEmpty()){
			//Next position to visit by the algorithm
			Node current = nextToVisit.remove();
			
			//Current position now added to previously visited
			visitedPos.add(current);								
			
			//Check if we arrived to the goal
			if(current.equals(g.map[g.endx][g.endy])){	
				System.out.println("The path was found" );
				g.printInfo(g.map[g.endx][g.endy]);
				System.out.println("Number of nodes expanded: " + (visitedPos.size()+nextToVisit.size()));				
				return true;
			}
			g.generateSuccessors(current);
			Node[] successorsOfCurrent = current.successors;
			for(int i=0; i< successorsOfCurrent.length;i++){
				if(!visitedPos.contains(successorsOfCurrent[i]) && successorsOfCurrent[i] !=null ){
					nextToVisit.add(successorsOfCurrent[i]);	//add the next node that will need to visit
					visitedPos.add(successorsOfCurrent[i]);		//add the nodes that are in the nexttoVisit already so we dont repeat
					successorsOfCurrent[i].comingFrom = current;	//set the nodes where they are coming from
				}
			}
			
		}	
		System.out.println("Number of nodes expanded: " + (visitedPos.size()+nextToVisit.size()));
		return false;
	}

	

	/**
	 * Method to implement limited depth search
	 */
	public boolean depthLimited(int depthLimit, Graph g){
		//Stack for depth limited
		Deque<Node> nextToVisit = new LinkedList<Node>();
		int depth = 0;
		List<Node> visitedPos = new ArrayList<>();
		nextToVisit.addFirst(g.map[g.startx][g.starty]);

		//While we have nodes to visit and we have not reached the depth limit
		while(!nextToVisit.isEmpty()){
			//Next position to visit
			Node current = nextToVisit.removeFirst();
			current.setDepth(depth);
			while(current.depth==depthLimit){
				current = nextToVisit.removeFirst();
			}
				
			//Add to already visited to avoid revisiting
			visitedPos.add(current);	

			if(current.equals(g.map[g.endx][g.endy])){
				System.out.println("The path was found" );
				g.printInfo(g.map[g.endx][g.endy]);
				System.out.println("Number of nodes expanded: " + (visitedPos.size()+nextToVisit.size()));
				return true;
			}
			g.generateSuccessors(current);
			Node[] currSuccessors = current.successors;
			for(int i=0; i< currSuccessors.length;i++){
				if(!visitedPos.contains(currSuccessors[i]) && currSuccessors[i] !=null ){
					currSuccessors[i].setDepth(++depth);
					nextToVisit.addFirst(currSuccessors[i]);	//add the next node that we will need to visit
					currSuccessors[i].comingFrom = current;	//set the nodes where they are coming from
				}
			}

		}
		System.out.println("Number of nodes expanded: " + (visitedPos.size()+nextToVisit.size()));
		return false;
	}

	/**
	 * Method to implement iterative deepening
	 */
	public boolean iterativeDeep(Graph g){
		boolean solutionFound = false;
		for(int i = 0; !solutionFound ;i++){
			solutionFound = depthLimited(i,g);
		}
		return solutionFound;
	}

    /**
     * Implements manhattan distance between two nodes
     * @param source
     * @param target
     * @return
     */
    public double manhDist(Node source, Node target){
        return Math.abs(source.coordenateX-target.coordenateX) + Math.abs(source.coordenateY-target.coordenateY);
	}
	/**
	 * Generic method for heuristic application
	 * @param source
	 * @param target
	 * @return
	 */
	public double heur(Node source, Node target){
		//Can substitute any heuristic
		return manhDist(source, target);
	}

	/**
	 * 
	 * @param g
	 * @return
	 */
    public boolean aStar(Graph g){
        PriorityQueue<Node> nextToVisit =new PriorityQueue<>(new Comparator<Node>(){
			@Override
			public int compare(Node fNode, Node sNode){	
                if(fNode.cost+heur(fNode, g.map[g.endx][g.endy])<sNode.cost+heur(sNode, g.map[g.endx][g.endy]))
                    return -1;
                else if(fNode.cost+heur(fNode, g.map[g.endx][g.endy]) == sNode.cost+heur(sNode, g.map[g.endx][g.endy]))
                    return 0;
                else
                    return 1;
                
			}
        });
        List<Node> visitedPos = new ArrayList<>();
		nextToVisit.add(g.map[g.startx][g.starty]);
        while(!nextToVisit.isEmpty()){
			Node current = nextToVisit.remove();
			
			//Add to already visited to avoid revisiting
			visitedPos.add(current);	

			if(current.equals(g.map[g.endx][g.endy])){
				System.out.println("The path was found" );
				g.printInfo(g.map[g.endx][g.endy]);
				System.out.println("Number of nodes expanded: " + (visitedPos.size()+nextToVisit.size()));
				return true;
            }
            g.generateSuccessors(current);
            Node[] currSuccessors = current.successors;
			for(int i=0; i< currSuccessors.length;i++){
				if(!visitedPos.contains(currSuccessors[i]) && currSuccessors[i] !=null ){
					nextToVisit.add(currSuccessors[i]);	//add the next node that we will need to visit
					currSuccessors[i].comingFrom = current;	//set the nodes where they are coming from
				}
			}
		}
		System.out.println("Number of nodes expanded: " + (visitedPos.size()+nextToVisit.size()));
        return false;


    }

}