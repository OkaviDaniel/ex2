package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class DWGraph_Algo implements dw_graph_algorithms, Serializable{
	
	private static final long serialVersionUID = 1L;
	private directed_weighted_graph g;
	private int dijkstraCounter=0;
	private static long counter1 = 1;

	//Default constructor
	public DWGraph_Algo()
	{
		this.g = new DWGraph_DS();
	}	

	@Override
	public void init(directed_weighted_graph g)
	{
		this.g = (DWGraph_DS)g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return this.g;
	}

	@Override
	public directed_weighted_graph copy() {
		DWGraph_DS graphTo = new  DWGraph_DS();
		List<node_data> oldNodes = new ArrayList<node_data>(g.getV());
		Iterator<node_data> iterator = oldNodes.iterator();

		//create all nodes and add to the graph 
		while(iterator.hasNext()){

			NodeData newNode=new NodeData(iterator.next());///need to import NodeData
			graphTo.addNode(newNode);
		}

		//for each node, add friends
		iterator = oldNodes.iterator();
		while(iterator.hasNext())
		{
			node_data currentNode = iterator.next();
			List<node_data>	adjecents=new ArrayList<node_data>(((DWGraph_DS)g).getNi(currentNode.getKey()));
			Iterator<node_data> iteratorAdjecents = adjecents.iterator();
			while(iteratorAdjecents.hasNext()) {
				node_data next=iteratorAdjecents.next();
				graphTo.connect(currentNode.getKey(), next.getKey(),(g.getEdge(currentNode.getKey(), next.getKey()).getWeight()));
			}
		}
		graphTo.setEdgeSize(g.edgeSize());
		graphTo.setMC(g.getMC());
		return graphTo;
	}



	private void Dijkstra(directed_weighted_graph graph, node_data source) {

		Collection<node_data> nodeInfoArrayList = graph.getV();

		// compare by lambda function
		Comparator<node_data> nodeSorter=Comparator.comparing((node_data node) -> node.getWeight());
		PriorityQueue<node_data> nodeInfoPriorityOueue=new PriorityQueue<>(nodeSorter);

		//init all array and insert to queue
		for (node_data node : nodeInfoArrayList) {
			node.setWeight( Integer.MAX_VALUE);//set max value on all nodes 
			node.setTag(-1);
			node.setInfo("w");
			nodeInfoPriorityOueue.add(node);//add all nodes to queue
		}

		//set source distance to 0
		source.setWeight(0);
		//for every node in queue ,pull him out,get all adjecents wasn't visited and decide minimum distance from source
		while(nodeInfoPriorityOueue.size()>0) {

			node_data polledNode=nodeInfoPriorityOueue.poll();
			ArrayList<node_data>adjecents=new ArrayList<node_data>(((DWGraph_DS)graph).getNi(polledNode.getKey()));
			for (int i = 0; i < adjecents.size(); i++) {
				node_data adj=adjecents.get(i);
				if(adj.getInfo().equals("w")) {
					double totalWeight=polledNode.getWeight()+(g.getEdge(polledNode.getKey(), adj.getKey())).getWeight();
					if(totalWeight < adj.getWeight()) {
						nodeInfoPriorityOueue.remove(adj);
						adj.setWeight(totalWeight);
						adj.setTag(polledNode.getKey());
						nodeInfoPriorityOueue.add(adj);
					}
				}
			}
			polledNode.setInfo("b");
			dijkstraCounter++;

		}	
	}

	@Override
	public boolean isConnected()
	{	
		ArrayList<node_data>allNodes=new ArrayList<node_data>(g.getV());
		if(allNodes.size()==0 || allNodes.size()==1) {
			return true;
		}
		Dijkstra(g,allNodes.get(0));

		if(dijkstraCounter!=allNodes.size()){ //for(int i=0;i<allNodes.size();i++) {
			return false;                     //	node_data n=allNodes.get(i);
		}                                     // if(n.getWeight()==Integer.MAX_VALUE) {
		dijkstraCounter=0;                    //             return false;
		return true;                          //  }                             
	}		                                  //}                                     


	@Override
	public double shortestPathDist(int src, int dest) {

		node_data srcNode=g.getNode(src);
		node_data destNode=g.getNode(dest);
		if(srcNode==null || destNode==null)
			return -1;
		if(srcNode==destNode)//are the same node
			return 0;
		Dijkstra(g, srcNode);
		if(destNode.getWeight()==Integer.MAX_VALUE) {
			return -1;
		}
		return destNode.getWeight();
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {

		node_data srcNode=g.getNode(src);
		node_data destNode=g.getNode(dest);

		if(srcNode==null || destNode==null)
			return null;
		if(srcNode==destNode) {//are the same node
			LinkedList<node_data> myList=new LinkedList<node_data>();
			myList.add(srcNode);
			return myList;
		}
		LinkedList<node_data> myList=new LinkedList<node_data>();
		Dijkstra(g, srcNode);
		node_data iterator=destNode;
		while(iterator!=srcNode) {
			myList.add(iterator) ;                   	
			iterator=g.getNode(iterator.getTag());
		}
		myList.add(iterator);
		Collections.reverse(myList);
		return myList;
	}

	@Override
	public boolean save(String file) {
		String json1 = "{";
		json1 = json1 + "\"Edges\":[";
		for(EdgeData e: ((DWGraph_DS)g).getEdges())
		{
			json1 = json1 + e.toJson() + ",";
		}
		//Getting rid of the last ','
		int tmp1 = json1.length();
		String json2 = "";
		for(int i = 0; i<tmp1-1;i++)
		{
			json2 = json2 + json1.charAt(i);
		}
		//and then adding the '],'
		json2 = json2 + "],";
		
		//For nodes
		String json3 = "\"Nodes\":[";
		for(node_data n: g.getV())
		{
			json3 = json3 + ((NodeData)n).toJson()+ ",";
		}
		int tmp2=json3.length();
		String json4 = "";
		for (int i = 0; i < tmp2-1; i++) {
			json4 = json4 + json3.charAt(i);
		}
		json4 = json4 + "]}";
		String ans = json2 + json4;
		
		try {
			PrintWriter aw = new PrintWriter(new File(file));
			aw.write(ans);
			aw.close();
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}			
	}

	@Override
	public boolean load(String file)
	{			
		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(DWGraph_DS.class, new graphDeserializer());
			Gson gson = builder.create();
			
			FileReader reader = new FileReader(file);
		//	System.out.println(reader);
			DWGraph_DS a = gson.fromJson(reader,DWGraph_DS.class);
			this.g = a;
			//System.out.println(a);
			return true;
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean connectedOrNot()
	{
		if(g.getV().size()==0 || g.getV().size()==1)
		{
			return true;
		}
		else
		{
			//Choose a random node
			node_data a = g.getV().iterator().next();
			//Run the bfs algo
			bfs(a);
			// counter1 is a static variable that count the number of the nodes that the bfs algo met on his run
			if(counter1 == g.getV().size())
			{
				// reset the counter to 1
				counter1 = 1;
				//Change the direction of the edges on the graph (O(n) when n is the number of vertices on the graph)
				((DWGraph_DS)g).changeDirections();
				// restore the nodes to their default state
				restoreNodes();
				//run the bfs algo on the same node
				bfs(a);
				//if the counter is still equals to the number of verices on the graph then:
				if(counter1 == g.getV().size())
				{
					// restore the nodes and the counter to their default state
					restoreNodes();
					counter1 = 1;
					// change back the edges on the graph
					((DWGraph_DS)g).changeDirections();
					return true;
				}
				else
				{
					//If we got here then the bfs algo on the changed graph didn't met all the nodes on the graph
					//So restore all the nodes and the edges and the counter to their default state
					restoreNodes();
					counter1 = 1;
					((DWGraph_DS)g).changeDirections();
					restoreNodes();
					return false;
				}
			}
			else
			{
				//In this case the first run of the bfs algo didn't met all the nodes on the graph
				counter1 = 1;
				restoreNodes();
				return false;
			}
		}
	}
	
	
	private void bfs(node_data src)
	{
		if(g.getV().contains(src))
		{
			LinkedList<node_data> q = new LinkedList<node_data>();
			src.setInfo("Visited");
			q.add(src);		
			while(!q.isEmpty()) {
				node_data temp = q.removeFirst();			
				Collection<node_data> ni = ((DWGraph_DS)g).getNi(temp.getKey());
				for(node_data f : ni)
				{
					if(!f.getInfo().equals("Visited"))
					{						
						f.setInfo("Visited");
						q.add(f);	
						counter1++;
					}
				}
			}
		}
		//System.out.println("This is the counter: " + counter1);
		
	}
	private void restoreNodes()
	{
		if(g.getV().size()>0)
		{
			Iterator<node_data> f = g.getV().iterator();
			while(f.hasNext())
			{
				node_data temp = f.next();
				if(!temp.getInfo().equals("NotVisited"))
				{
					temp.setInfo("NotVisited");
				}
			}
		}
	}
	
	


}

