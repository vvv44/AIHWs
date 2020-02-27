import java.util.*;


public class Graph {
	int startx;
	int starty;
	int endx;
	int endy;
	Node[][] map;
	
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
	public void setMap(Node[][] graph){
		this.map = graph;
	}
	
	/**
	 * Method to display graph visually with the costs and everything.
	 */
	public void displayGraph(){												
		System.out.println("Start Coordinates ("+startx+","+starty+")");
		System.out.println("End Coordinates ("+endx+","+endy+")");
	}
	
	/**
	 * Method to generate successors for a node, based on position, successors are adjacent Nodes, (only in four directions)
	 */
	public void generateSuccessors(Node n){
		if( ((n.coordinateY-1)>=0) && ((n.coordinateY-1)<map.length) && (map[n.coordinateY-1][n.coordinateX].impassable==false) ){					//lower
			n.setlowerSuccessor(map[n.coordinateY-1][n.coordinateX]);
		}
		if( ((n.coordinateX-1)>=0) && ((n.coordinateX-1)<map[n.coordinateY].length) && (map[n.coordinateY][n.coordinateX-1].impassable==false) ){					//left
			n.setLeftSuccessor(map[n.coordinateY][n.coordinateX-1]);
		}
		if( ((n.coordinateX+1)>=0) && ((n.coordinateX+1)<map[n.coordinateY].length) && (map[n.coordinateY][n.coordinateX+1].impassable==false) ){					//right
			n.setRightSuccessor(map[n.coordinateY][n.coordinateX+1]);
		}
		if( ((n.coordinateY+1)>=0) && ((n.coordinateY+1)<map.length) && (map[n.coordinateY+1][n.coordinateX].impassable==false) ){					//Upper
			n.setUpperSuccessor(map[n.coordinateY+1][n.coordinateX]);
		}	
	}
	
	/**
	 * Method to find path found by the algorithm
	 * @param node
	 */
	public void printInfo(Node node){
		System.out.print("Reverse path: ");
		int totCost = 0;
		while(node.comingFrom !=null){
			System.out.print("("+node.coordinateX+","+node.coordinateY+"), ");
			node = node.comingFrom;
			totCost+=node.cost;
		}
		System.out.println();
		System.out.println("Path Total Cost: " + totCost);
						
	}
	
}
