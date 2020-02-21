import java.util.*;


public class Graph {
	int startx;
	int starty;
	int endx;
	int endy;
	Node[][] g;
	
	public Graph(){
	}
	public void setStartx(int sx){
		this.startx = sx;
	}
	public void setStarty(int sy){
		this.starty = sy;
	}
	public void setEndx(int ex){
		this.endx = ex;
	}
	public void setEndy(int ey){
		this.endy = ey;
	}
	public void setGraph(Node[][] graph){
		this.g = graph;
	}
	
	public void displayGraph(){												//This will display the graph/cost of all the nodes. the empty spaces are the places where you can not go through
		System.out.println("Start Coordinates ("+startx+","+starty+")");
		System.out.println("End Coordinates ("+endx+","+endy+")");
		System.out.println("Traverse Cost:");
		for(int i=0; i< g.length;i++){
			for(int j=0; j<g[i].length; j++){
				if(!g[i][j].impassable){
					System.out.print(g[i][j].cost+" ");
				}else{
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
	
	/*this method will generate the successors*/
	
	public void successor(){
		for(int i=0; i<g.length;i++){
			for(int j=0; j<g[i].length;j++){
				
				if( ((i-1)>=0) && ((i-1)<g.length) && (g[i-1][j].impassable==false) ){					//below
					g[i][j].setBelowSuccessor(g[i-1][j]);
				}
				if( ((j-1)>=0) && ((j-1)<g[i].length) && (g[i][j-1].impassable==false) ){					//left
					g[i][j].setLeftSuccessor(g[i][j-1]);
				}
				if( ((j+1)>=0) && ((j+1)<g[i].length) && (g[i][j+1].impassable==false) ){					//right
					g[i][j].setRightSuccessor(g[i][j+1]);
				}
				if( ((i+1)>=0) && ((i+1)<g.length) && (g[i+1][j].impassable==false) ){					//top
					g[i][j].setTopSuccessor(g[i+1][j]);
				}
				
			}
		}
	}
	/*prove that the successors are working correctly*/
	public void print_successors(int i, int j){
		System.out.println("successors:");
		System.out.print("current:"+g[i][j].cost);
		if(g[i][j].successors[3] != null){															//top
			System.out.print(" top: "+g[i][j].successors[3].cost+" ");
		}else{
			System.out.print(" top: "+g[i][j].successors[3]+" ");
		}
		if(g[i][j].successors[0] != null){															//below
			System.out.print(" below: "+g[i][j].successors[0].cost+" ");
		}else{
			System.out.print(" below: "+g[i][j].successors[0]+" ");
		}
		if(g[i][j].successors[1] != null){															//left
			System.out.print(" left: "+g[i][j].successors[1].cost+" ");
		}else{
			System.out.print(" left: "+g[i][j].successors[1]+" ");
		}
		if(g[i][j].successors[2] != null){															//right
			System.out.print(" right: "+g[i][j].successors[2].cost+" ");
		}else{
			System.out.print(" right: "+g[i][j].successors[2]+" ");
		}
		System.out.println();
	}
	
	public void findPath(Node graph){
		System.out.print("Reverse path: ");
		while(graph.comingFrom !=null){
			System.out.print(graph.cost+" ");
			graph = graph.comingFrom;
		}
		System.out.println(graph.cost);
	}
	
	/* breadth-first search algorithm*/
	public void BreadtFirst(){
		PriorityQueue<Node> nexttoVisit =new PriorityQueue<>(new Comparator<Node>(){
			@Override
			public int compare(Node fNode, Node sNode){											//put the smallest in front first
				return fNode.cost - sNode.cost;													// put sNode fist: compare a big against a smaller will give a bigger value than 0
																								//leave fNode first: compare a small number against a bigger will give a smaller number than 0
			}
		});
		int totalCost = 0;
		
		//for(int i=0; i<g[1][3].successors.length; i++){										//check if this queue works
		//	q.add(g[1][3].successors[i]);
		//}
		//while(!q.isEmpty()){
		//	System.out.println(q.remove().cost);
		//}
		List<Node> visitedPos = new ArrayList<>();
		
		nexttoVisit.add(g[startx][starty]);
		
		while(!nexttoVisit.isEmpty()){
			
																	//remove the node to continue with the node
			Node currentPos = nexttoVisit.remove();
			
			visitedPos.add(currentPos);								//now the current position will count as visited
			
			if(currentPos.equals(g[endx][endy])){					//Check if we arrived to the goal
				System.out.println("The path was found" );
				findPath(g[endx][endy]);
				return;
			}
			Node[] successorsOfCurrent = currentPos.successors;
			for(int i=0; i< successorsOfCurrent.length;i++){
				//if(successorsOfCurrent[i] == null){
				//	System.out.println("Position: "+currentPos.cost+" fixing "+successorsOfCurrent[i]);
				//}else{
				//	System.out.println("Position: "+currentPos.cost+" fixing "+successorsOfCurrent[i].cost);
				//}
				if(!visitedPos.contains(successorsOfCurrent[i]) && successorsOfCurrent[i] !=null ){
					nexttoVisit.add(successorsOfCurrent[i]);		//add the next node that will need to visit
					visitedPos.add(successorsOfCurrent[i]);			//add the nodes that are in the nexttoVisit already so we dont repeat
					successorsOfCurrent[i].comingFrom = currentPos;	//set the nodes where they are coming from
					
				}
			}
			
			
		}
		
	}
	
	
}
