
public class Node {
	int cost;														//The cost of the node
	int coordinateX;												//easier axes to the coordinate X of this node
	int coordinateY;												//easier axes to the coordinate Y of this node
	boolean impassable;												//check if this this node is accessible
	Node[] successors;										    	//keep track of the successors [lower, left, right, Upper]
	Node comingFrom;												//keep track of where it comes from
	int depth;

	/*Here we will contain the cost, the x-axis, y-axis (to make it easier in some cases) and will create the array of the neighbors*/
	public Node(int c,int x, int y){
		this.cost = c;
		this.coordinateX = x;
		this.coordinateY = y;
		successors = new Node[4];
		
		if(this.cost == 0){											//Set the places that can or cannot go through
			this.impassable = true;
		}else{
			this.impassable = false;
		}
	}

	public void setDepth(int depth){
		this.depth = depth;
	}
	
	/* This method will set the cost and impassable (check if it has 0)*/
	public void setCostImpassable(int c){
		this.cost = c;
		
		if(this.cost == 0){											//Set the places that can or cannot go through
			this.impassable = true;
		}else{
			this.impassable = false;
		}
	}
	
	/*This four methods are used to keep track of the successors*/
	public void setlowerSuccessor(Node low){
		this.successors[0] = low;
	}
	public void setLeftSuccessor(Node left){								//left
		this.successors[1] = left;
	}
	public void setRightSuccessor(Node right){								//right
		this.successors[2] = right;
	}
	public void setUpperSuccessor(Node Upper){								//Upper
		this.successors[3] = Upper;
	}
}
