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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class DWGraph_Algo implements dw_graph_algorithms, Serializable{
	
	private static final long serialVersionUID = 1L;
	private directed_weighted_graph g;
	private int dijkstraCounter=0;
	private static long counter1 = 1;
	
	//For tarjanAlgorithm only--v--
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
		//this.g = (DWGraph_DS)g;
	
		this.g = g;
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


	public void Dijkstra(directed_weighted_graph graph, node_data source) {

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
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		nodeInfoPriorityOueue.remove(source);
		nodeInfoPriorityOueue.add(source);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		while(nodeInfoPriorityOueue.size()>0) {

			node_data polledNode=nodeInfoPriorityOueue.poll();
			//System.out.println(nodeInfoPriorityOueue);
			ArrayList<node_data>adjecents=new ArrayList<node_data>(((DWGraph_DS)graph).getNi(polledNode.getKey()));
			//System.out.println("Curr node:"+polledNode+" , list: "+ adjecents);
			for (int i = 0; i < adjecents.size(); i++) {
				node_data adj=adjecents.get(i);
				if(adj.getInfo().equals("w")) {
					double totalWeight=polledNode.getWeight()+(g.getEdge(polledNode.getKey(), adj.getKey())).getWeight();
					if(totalWeight < adj.getWeight()) {
						nodeInfoPriorityOueue.remove(adj);
						adj.setWeight(totalWeight);
						//System.out.println(polledNode.getKey());
						adj.setTag(polledNode.getKey());
						nodeInfoPriorityOueue.add(adj);
					}
				}
			}
			polledNode.setInfo("b");
		//	System.out.println("key: " +polledNode.getKey()+",tag: " +polledNode.getTag());
			dijkstraCounter++;

		}	
	}
	
	@Override
	public double shortestPathDist(int src, int dest) {
		node_data srcNode=g.getNode(src);
		node_data destNode=g.getNode(dest);
		if(srcNode==null || destNode==null)
		{
			//System.out.println("hello");
			return -1;
		}			
		if(srcNode==destNode)//are the same node
			{return 0;}
		//System.out.println("src node in shortestPathDist: " +  src);

		Dijkstra(g, srcNode);
		if(destNode.getWeight()==Integer.MAX_VALUE) {
			//System.out.println("Hello2");
			return -1;
		}
		return destNode.getWeight();
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {

		node_data srcNode=g.getNode(src);
		node_data destNode=g.getNode(dest);

		if(srcNode==null || destNode==null)
		{
			//System.out.println("Hello3");
			return null;
		}
			
		if(srcNode==destNode) {//are the same node
			LinkedList<node_data> myList=new LinkedList<node_data>();
			myList.add(srcNode);
			//System.out.println("Hello2");
			return myList;
		}
		LinkedList<node_data> myList=new LinkedList<node_data>();
		Dijkstra(g, srcNode);
		node_data iterator=destNode;
		while(iterator!=srcNode) 
		{
			
			myList.add(iterator) ;                   	
			if(g.getNode(iterator.getTag())!=null)
			{
				//System.out.println("Curr iterator: " + iterator);
				iterator=g.getNode(iterator.getTag());
			}
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
		String json2 = "";
		if(!(json1.charAt(json1.length()-1)=='['))
		{
			int tmp1 = json1.length();
			
			for(int i = 0; i<tmp1-1;i++)
			{
				json2 = json2 + json1.charAt(i);
			}
			json2 = json2 + "],";
		}
		else
		{
			//There are no edges, then just add "],"
			json2 = json1 + "],";
		}
			
		
		
		//For nodes
		String json3 = "\"Nodes\":[";
		for(node_data n: g.getV())
		{
			json3 = json3 + ((NodeData)n).toJson()+ ",";
		}
		int tmp2=json3.length();
		String json4 = "";
		
		if(!(json3.charAt(json3.length()-1)=='['))
		{
			for (int i = 0; i < tmp2-1; i++) {
				json4 = json4 + json3.charAt(i);
			}
			json4 = json4 + "]}";
		}
		else
		{
			//There are no nodes, then just add "]}"
			json4 = json3 + "]}";
		}
		
		String ans = json2 + json4;
		
		try {
			PrintWriter aw = new PrintWriter(new File(file));
			aw.write(ans);
			//System.out.println(ans);
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

	/**
	 * This method check if the current graph is strongly connected (Using bfs algorithm).
	 * 
	 */
	public boolean isConnected()
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
	
	
	/**
	 * 
	 * @param src The source node that the algorithm is going to start from
	 */
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
	
	
	private void dfs(node_data u)
	{
		lowlink[u.getKey()] = time++;
		u.setInfo("Visited");
		stack.add(u);
		boolean uIsComponentRoot = true;
		for(node_data v : ((DWGraph_DS)g).getNi(u.getKey()))
		{
			if(!v.getInfo().equals("Visited"))
			{
				dfs(v);
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
	
	
	public List<List<Integer>> tarjan()
	{
		stack = new Stack<>();
		time = 0;
		lowlink = new int[g.getV().size()];
		components = new ArrayList<>();
		for(node_data u : g.getV())
		{
			if(!u.getInfo().equals("Visited"))
			{
				dfs(u);
			}
		}
		if(components.size()==1)
		{
			
			restoreNodes();
			return components;
		}
		else
		{
			restoreNodes();
			return components;
		}
		
	}
	
	public List<List<Integer>> getComp()
	{	
			List<List<Integer>> tmp = tarjan();
			restoreNodes();
			return tmp;		
	}
	
	
	public directed_weighted_graph fromJsonToGraph(String json)
	{
		JSONObject jsonObj;
		try 
		{
			DWGraph_DS g = new DWGraph_DS();
			jsonObj = new JSONObject(json);
			JSONArray nodesEl = (JSONArray) jsonObj.get("Nodes");
			for(int i = 0; i<nodesEl.length();i++)
			{
				JSONObject tmp1 = nodesEl.getJSONObject(i);
				int id = tmp1.getInt("id");
				NodeData n1 = new NodeData(id);
				String geoTmp = tmp1.getString("pos");
				String[] geoTmp2 = geoTmp.split(",");
				double x = Double.parseDouble(geoTmp2[0]);
				double y = Double.parseDouble(geoTmp2[1]);
				double z = Double.parseDouble(geoTmp2[2]);
				n1.setLocation(new GeoLocation(x, y, z));				
				g.addNode(n1);
				
			}			
			
			JSONArray edgesEl = (JSONArray) jsonObj.get("Edges");
		
			for(int i = 0; i<edgesEl.length();i++)
			{
				JSONObject tmp2 = edgesEl.getJSONObject(i);
				int src = tmp2.getInt("src");
				int dest = tmp2.getInt("dest");
				double weight = tmp2.getDouble("w");
				g.connect(src, dest, weight);		
			}
			return g;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return null;
		}	
	}
	
	public double oneCallShrtPathD(int src, int dest) {
		node_data srcNode=g.getNode(src);
		node_data destNode=g.getNode(dest);
		if(srcNode==null || destNode==null)
			return -1;
		if(srcNode==destNode)
			return 0;		
		if(destNode.getWeight()==Integer.MAX_VALUE) {
			return -1;
		}
		return destNode.getWeight();
	}
	
	
	public List<node_data> shortCurrPath(int src, int dest) {

		node_data srcNode=g.getNode(src);
		node_data destNode=g.getNode(dest);

		if(srcNode==null || destNode==null) {
			return null;
		}
		if(srcNode==destNode) {
			LinkedList<node_data> myList=new LinkedList<node_data>();
			myList.add(srcNode);			
			return myList;
		}
		LinkedList<node_data> myList=new LinkedList<node_data>();
		node_data iterator=destNode;
		while(iterator!=srcNode) 
		{		
			myList.add(iterator) ;
			iterator=g.getNode(iterator.getTag());						
		}
		myList.add(iterator);
		Collections.reverse(myList);
		return myList;
	}
}

