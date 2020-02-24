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
	public void generateSuccessor(){
		for(int i=0; i<map.length;i++){
			for(int j=0; j<map[i].length;j++){
				if( ((i-1)>=0) && ((i-1)<map.length) && (map[i-1][j].impassable==false) ){					//lower
					map[i][j].setlowerSuccessor(map[i-1][j]);
				}
				if( ((j-1)>=0) && ((j-1)<map[i].length) && (map[i][j-1].impassable==false) ){					//left
					map[i][j].setLeftSuccessor(map[i][j-1]);
				}
				if( ((j+1)>=0) && ((j+1)<map[i].length) && (map[i][j+1].impassable==false) ){					//right
					map[i][j].setRightSuccessor(map[i][j+1]);
				}
				if( ((i+1)>=0) && ((i+1)<map.length) && (map[i+1][j].impassable==false) ){					//Upper
					map[i][j].setUpperSuccessor(map[i+1][j]);
				}
				
			}
		}
	}
	
	/**
	 * Method to find path found by the algorithm
	 * @param graph
	 */
	public void findPath(Node graph){
		System.out.print("Reverse path: ");
		while(graph.comingFrom !=null){
			System.out.print(graph.cost+" ");
			graph = graph.comingFrom;
		}
		System.out.println(graph.cost);
	}
	

	
	
}
