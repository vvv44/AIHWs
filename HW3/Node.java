
public class Node {
	int cost;														//The cost of the node
	int coordenateX;												//easier axes to the coordenate X of this node
	int coordenateY;												//easier axes to the coordenate Y of this node
	boolean impassable;												//check if this this node is accessible
	Node[] successors;										    	//keep track of the successors [below, left, right, top]
	Node comingFrom;												//keep track of where it comes from
	
	/*Here we will contain the cost, the x-axis, y-axis (to make it easier in some cases) and will create the array of the neighbors*/
	public Node(int c,int x, int y){
		this.cost = c;
		this.coordenateX = x;
		this.coordenateY = y;
		successors = new Node[4];
		
		if(this.cost == 0){											//Set the places that can or cannot go through
			this.impassable = true;
		}else{
			this.impassable = false;
		}
	}
	
	/* This metod will set the cost and impassable (check if it has 0)*/
	public void setCostImpassable(int c){
		this.cost = c;
		
		if(this.cost == 0){											//Set the places that can or cannot go through
			this.impassable = true;
		}else{
			this.impassable = false;
		}
	}
	
	/*This four methods are used to keep track of the successors*/
	public void setBelowSuccessor(Node b){
		this.successors[0] = b;
	}
	public void setLeftSuccessor(Node l){								//left
		this.successors[1] = l;
	}
	public void setRightSuccessor(Node r){								//right
		this.successors[2] = r;
	}
	public void setTopSuccessor(Node t){								//top
		this.successors[3] = t;
	}
}
