import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

public class main {
	public static Graph ReadFile(String fileName) throws IOException {
		Graph execGraph = new Graph();										//Store the graph, goals, and source between others
		try{
			File file = new File(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(file));		//read
			String line;
			//System.out.print(reader.read());

			/**Get graph dimensions */
			line = reader.readLine();
			String[] GraphDimension = line.split(" ");
			Node[][] Graph = new Node[Integer.parseInt(GraphDimension[0])][Integer.parseInt(GraphDimension[1])];	

			/**Get start coordinates */
			line = reader.readLine();																	
			String[] graphStartEnd = line.split(" ");
			execGraph.setStartx(Integer.parseInt(graphStartEnd[0])); 									
			execGraph.setStarty(Integer.parseInt(graphStartEnd[1]));

			/**Get goal coordinates */
			line = reader.readLine();																				
			graphStartEnd = line.split(" ");
			execGraph.setEndx(Integer.parseInt(graphStartEnd[0]));									
			execGraph.setEndy(Integer.parseInt(graphStartEnd[1]));

			/**Get cost of each node for the map*/
			String[] cost;
			int i = 0;
			while((line = reader.readLine()) !=null){
				cost = line.split(" ");
				for(int j=0; j< cost.length;j++){
					Graph[i][j] = new Node(Integer.parseInt(cost[j]),i,j);
				}
				i++;
			}
			
			/**Setting the map in the graph class */
			execGraph.map = Graph;																		
			reader.close();
		}catch(FileNotFoundException e){
			System.out.println("File Not Found, try again");
		}

		return execGraph;
	}


	public static void main (String[] args) throws IOException {
		Graph map = ReadFile(args[0]);
		SearchAlgorithms searcher =  new SearchAlgorithms();
		map.displayGraph();
		map.generateSuccessor();
		
		long startTime = System.nanoTime();
		if(args[1].equalsIgnoreCase("BFS"))
			if(!searcher.BreadtFirst(map))
				System.out.println("Cost: -1" + "   Path: Null");
		else if(args[1].equalsIgnoreCase("IDS"))
			if(!searcher.depthLimited(6,map))
				System.out.println("Cost: -1" + "   Path: Null");
		else if(args[1].equalsIgnoreCase("AS"))
			if(!searcher.aStar(map))
				System.out.println("Cost: -1" + "   Path: Null");
		else
			System.out.println("Invalid Input");
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Running time in Miliseconds: " + totalTime/1000);
	}
}
