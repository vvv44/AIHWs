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
		System.out.println("Traverse Cost:");
		for(int i=0; i< map.length;i++){
			for(int j=0; j<map[i].length; j++){
				if(!map[i][j].impassable){
					System.out.print(map[i][j].cost+" ");
				}else{
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Method to generate successors for a node, based on position, successors are adjacent Nodes, (only in four directions)
	 */
	public void generateSuccessors(Node n){
		if( ((n.coordenateY-1)>=0) && ((n.coordenateY-1)<map.length) && (map[n.coordenateY-1][n.coordenateX].impassable==false) ){					//lower
			n.setlowerSuccessor(map[n.coordenateY-1][n.coordenateX]);
		}
		if( ((n.coordenateX-1)>=0) && ((n.coordenateX-1)<map[n.coordenateY].length) && (map[n.coordenateY][n.coordenateX-1].impassable==false) ){					//left
			n.setLeftSuccessor(map[n.coordenateY][n.coordenateX-1]);
		}
		if( ((n.coordenateX+1)>=0) && ((n.coordenateX+1)<map[n.coordenateY].length) && (map[n.coordenateY][n.coordenateX+1].impassable==false) ){					//right
			n.setRightSuccessor(map[n.coordenateY][n.coordenateX+1]);
		}
		if( ((n.coordenateY+1)>=0) && ((n.coordenateY+1)<map.length) && (map[n.coordenateY+1][n.coordenateX].impassable==false) ){					//Upper
			n.setUpperSuccessor(map[n.coordenateY+1][n.coordenateX]);
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
			System.out.print("("+node.coordenateX+","+node.coordenateY+"), ");
			node = node.comingFrom;
			totCost+=node.cost;
		}
		System.out.println();
		System.out.println("Total cost: " + totCost);
						
	}
	

	
	
}
