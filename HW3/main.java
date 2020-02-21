import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class main {
	public static Graph ReadFile(String fileName) throws IOException {

		Graph execGraph = new Graph();													//Store the graph, goals, and source between others
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));		//read
			String line;
			//System.out.print(reader.read());

			line = reader.readLine();
			String[] GraphDimension = line.split(" ");
			Node[][] Graph = new Node[Integer.parseInt(GraphDimension[0])][Integer.parseInt(GraphDimension[1])];	

			line = reader.readLine();																				//Putting start in Graph
			String[] setGraphClass = line.split(" ");
			execGraph.setStartx(Integer.parseInt(setGraphClass[0])); 												//Start point coordinates
			execGraph.setStarty(Integer.parseInt(setGraphClass[1]));

			line = reader.readLine();																				
			setGraphClass = line.split(" ");
			execGraph.setEndx(Integer.parseInt(setGraphClass[0]));													//End point coordinates
			execGraph.setEndy(Integer.parseInt(setGraphClass[1]));

			String[] cost;
			int i = 0;
			while((line = reader.readLine()) !=null){
				cost = line.split(" ");
				for(int j=0; j< cost.length;j++){
					Graph[i][j] = new Node(Integer.parseInt(cost[j]),i,j);
				}
				//System.out.println();
				i++;
			}
			
			execGraph.g = Graph;																					//The graph of nodes

			//for(i=0; i< Graph.length;i++){
			//	for(int j=0; j<Graph[i].length; j++){
			//		System.out.print(Graph[i][j].cost+" ");
			//	}
			//	System.out.println();
			//}

		}catch(FileNotFoundException e){
			System.out.println("not working");
		}

		return execGraph;
	}
	public static void main (String[] args) throws IOException {
		System.out.println("Hello");
		Scanner scan = new Scanner(System.in);
		String file = scan.next();
		//String file2 = "C:\\Users\\User\\Documents\\Java Programs\\SearchAlgorithms\\src\\"+file;
		Graph g = ReadFile(file);          //read the file and 
		g.displayGraph();
		g.successor();
		g.print_successors(1,3);
		g.BreadtFirst();
		
	}
}
