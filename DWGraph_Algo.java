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
	private HashMap<Integer, Integer> firstTime;
	private HashMap<Integer, Integer> lastTime;
	private HashMap<Integer, node_data> prev;
	private int time;
	private List<List<Integer>> components;
	private int[] lowlink;
	private Stack<node_data> stack;


	//Default constructor
	public DWGraph_Algo()
	{
		this.g = new DWGraph_DS();
	}	

	@Override
	public void init(directed_weighted_graph g)
	{
		this.g = (DWGraph_DS)g;
		this.firstTime = new HashMap<Integer, Integer>();
		this.lastTime  = new HashMap<Integer, Integer>();
		this.prev 	   = new HashMap<Integer, node_data>();
	//	time = 0;
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




	public boolean connectedOrNot()
	{
		stack = new Stack<>();
		time = 0;
		lowlink = new int[g.getV().size()];
		components = new ArrayList<>();
		for(node_data u : g.getV())
		{
			if(!u.getInfo().equals("Visited"))
			{
				dfsNew(u);
			}
		}
		if(components.size()==1)
		{
			
			return true;
		}
		else
		{
			System.out.println(components);
			return false;
		}
		
	}
	
	
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
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(g);
		try {
			PrintWriter aw = new PrintWriter(new File(file));
			aw.write(json);
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
		//	builder.registerTypeAdapter(NodeData.class, new NodeDataInstanceCreator());
		//	System.out.println("I've been here");
			Gson gson = builder.create();
			
			FileReader reader = new FileReader(file);
			DWGraph_DS a = gson.fromJson(reader,DWGraph_DS.class);
			this.g = a;
			System.out.println(a);
			return true;
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
		
	
//	private void dfs()
//	{
//		for(node_data u : g.getV())
//		{
//			u.setInfo("White");
//			prev.put(u.getKey(), null);
//			this.time=0;
//		}
//		for(node_data u : g.getV())
//		{
//			if(u.getInfo().equals("White"))
//			{
//				dfsVisit(u);
//			}
//		}
//	}
	
//	private void dfsVisit(node_data u)
//	{
//		u.setInfo("Grey");
//		time = time +1;
//		firstTime.put(u.getKey(), time);
//		for(node_data i : ((DWGraph_DS)g).getNi(u.getKey()))
//		{
//			if(i.getInfo().equals("White"))
//			{
//				prev.put(i.getKey(), u);
//				dfsVisit(i);
//			}
//		}
//		u.setInfo("Black");
//		lastTime.put(u.getKey(), ++time);
//	}
	
	
	private void dfsNew(node_data u)
	{
		lowlink[u.getKey()] = time++;
		u.setInfo("Visited");
		stack.add(u);
		boolean uIsComponentRoot = true;
		for(node_data v : ((DWGraph_DS)g).getNi(u.getKey()))
		{
			if(!v.getInfo().equals("Visited"))
			{
				dfsNew(v);
			}
			if(lowlink[u.getKey()] > lowlink[v.getKey()])
			{
				lowlink[u.getKey()] = lowlink[v.getKey()];
				uIsComponentRoot = false;
			}		
		}
		
		if(uIsComponentRoot)
		{
			List<Integer> component = new ArrayList<>();
			while(true)
			{
				int x = stack.pop().getKey();
				component.add(x);
				lowlink[x] = Integer.MAX_VALUE;
				if (x == u.getKey())
					break;
			}
			components.add(component);
		}
	}
}

