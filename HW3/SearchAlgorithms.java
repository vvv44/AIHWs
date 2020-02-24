import java.util.*;

public class SearchAlgorithms{
    /**
	 * Method that implements bread first algorithm
	 */
	public void BreadtFirst(Graph g){
		PriorityQueue<Node> nexttoVisit =new PriorityQueue<>(new Comparator<Node>(){
			@Override
			public int compare(Node fNode, Node sNode){											//put the smallest in front first
				return fNode.cost - sNode.cost;													// put sNode fist: compare a big against a smaller will give a bigger value than 0
																								//leave fNode first: compare a small number against a bigger will give a smaller number than 0
			}
		});
		List<Node> visitedPos = new ArrayList<>();
		nexttoVisit.add(g.map[g.startx][g.starty]);

		while(!nexttoVisit.isEmpty()){
			//Next position to visit by the algorithm
			Node currentPos = nexttoVisit.remove();
			
			//Current position now added to previously visited
			visitedPos.add(currentPos);								
			
			//Check if we arrived to the goal
			if(currentPos.equals(g.map[g.endx][g.endy])){					
				System.out.println("The path was found" );
				g.findPath(g.map[g.endx][g.endy]);
				return;
			}
			Node[] successorsOfCurrent = currentPos.successors;
			for(int i=0; i< successorsOfCurrent.length;i++){
				if(!visitedPos.contains(successorsOfCurrent[i]) && successorsOfCurrent[i] !=null ){
					nexttoVisit.add(successorsOfCurrent[i]);	//add the next node that will need to visit
					visitedPos.add(successorsOfCurrent[i]);		//add the nodes that are in the nexttoVisit already so we dont repeat
					successorsOfCurrent[i].comingFrom = currentPos;	//set the nodes where they are coming from
				}
			}
			
		}	
	}

	/**
	 * Method to implement limited depth search
	 */
	public boolean depthLimited(int depthLimit, Graph g){
		//Stack for depth limited
		Deque<Node> nextToVisit = new LinkedList<Node>();

		List<Node> visitedPos = new ArrayList<>();
		nextToVisit.addFirst(g.map[g.startx][g.starty]);
		int depth = 0;
		//While we have nodes to visit and we have not reached the depth limit
		while(!nextToVisit.isEmpty() && depth!=depthLimit){
			Node current = nextToVisit.removeFirst();
			if(current.equals(g.map[g.endx][g.endy])){
				System.out.println("The path was found" );
				g.findPath(g.map[g.endx][g.endy]);
				return true;
			}
			Node[] currSuccessors = current.successors;
			for(int i=0; i< currSuccessors.length;i++){
				if(!visitedPos.contains(currSuccessors[i]) && currSuccessors[i] !=null ){
					nextToVisit.addFirst(currSuccessors[i]);	//add the next node that we will need to visit
					currSuccessors[i].comingFrom = current;	//set the nodes where they are coming from
				}
			}

			depth++;
		}
		return false;
	}

	/**
	 * Method to implement iterative deepening
	 */
	public void iterativeDeep(Graph g){
		boolean solutionFound = false;
		for(int i = 0; !solutionFound ;i++){
			solutionFound = depthLimited(i,g);
		}
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

    public boolean aStar(Graph g, int depthLimit){
        PriorityQueue<Node> nextToVisit =new PriorityQueue<>(new Comparator<Node>(){
			@Override
			public int compare(Node fNode, Node sNode){											//put the smallest in front first
                if(fNode.cost+manhDist(fNode, g.map[g.endx][g.endy])<sNode.cost+manhDist(sNode, g.map[g.endx][g.endy]))
                    return -1;
                else if(fNode.cost+manhDist(fNode, g.map[g.endx][g.endy]) == sNode.cost+manhDist(sNode, g.map[g.endx][g.endy]))
                    return 0;
                else
                    return 1;
                
			}
        });
        List<Node> visitedPos = new ArrayList<>();
		nextToVisit.add(g.map[g.startx][g.starty]);
        int depth = 0;
        while(!nextToVisit.isEmpty() && depth!=depthLimit){
            Node current = nextToVisit.remove();
			if(current.equals(g.map[g.endx][g.endy])){
				System.out.println("The path was found" );
				g.findPath(g.map[g.endx][g.endy]);
				return true;
            }
            
            Node[] currSuccessors = current.successors;
			for(int i=0; i< currSuccessors.length;i++){
				if(!visitedPos.contains(currSuccessors[i]) && currSuccessors[i] !=null ){
					nextToVisit.add(currSuccessors[i]);	//add the next node that we will need to visit
					currSuccessors[i].comingFrom = current;	//set the nodes where they are coming from
				}
			}
        }
        return false;


    }

}