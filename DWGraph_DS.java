package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class DWGraph_DS implements directed_weighted_graph {
	
	private HashMap<Integer, node_data> nodesInGraph;
	private HashMap<Integer,HashMap<Integer,EdgeData>> edges;
	private HashMap<Integer, Collection<node_data>> neighbors;
	private HashMap<Integer, Collection<Integer>> pointers;				
	private int edgeSiz=0;
	private int mcCounter=0;
	
	public DWGraph_DS()
	{
		this.pointers = new HashMap<Integer, Collection<Integer>>();
		this.nodesInGraph = new HashMap<Integer, node_data>();
		this.edges = new HashMap<Integer, HashMap<Integer,EdgeData>>();
		this.neighbors = new HashMap<Integer, Collection<node_data>>();
	}
	
	@Override
	public node_data getNode(int key) {
		if(nodesInGraph.containsKey(key))
		{
			return nodesInGraph.get(key);
		}
		return null;
	}

	@Override
	public edge_data getEdge(int src, int dest)
	{		
		if(edges.containsKey(src) && edges.get(src).containsKey(dest))
		{			
			return edges.get(src).get(dest);
		}
		return null;
	}

	@Override
	public void addNode(node_data n) 
	{		
		if(!nodesInGraph.containsKey(n.getKey()))
		{
			nodesInGraph.put(n.getKey(), n);
			pointers.put(n.getKey(), new LinkedList<Integer>());
			neighbors.put(n.getKey(), new LinkedList<node_data>());
			mcCounter++;
		}
	}

	@Override
	public void connect(int src, int dest, double w) 
	{		
		 
		if(src!=dest)
		{
		//the nodes are exist and the weight is positive
			if(nodesInGraph.get(src)!=null && nodesInGraph.get(dest)!=null && w>0)
			{
				//the source node have neighbor
				if(edges.containsKey(src))
				{
					//the neighbor and the edge is exist 
					if(edges.get(src).get(dest)!=null)	
					{
						// the weight different
						if(edges.get(src).get(dest).getWeight()!=w)
						{
							edges.get(src).get(dest).setWeight(w);
							mcCounter++;
						}						
					}
					//There are neighbors but dest(node) is not one of them
					else
					{
						HashMap<Integer, EdgeData> innerHash = new HashMap<Integer, EdgeData>();
						EdgeData temp = new EdgeData();
						temp.setDest(dest);
						temp.setSrc(src);
						temp.setWeight(w);
						innerHash.put(dest, temp);
						pointers.get(dest).add(nodesInGraph.get(src).getKey());
						// Add the destination node to the "neighbors" collection
						neighbors.get(src).add(nodesInGraph.get(dest));
						edgeSiz++;
						mcCounter++;
					}					
				}
				else 
				{
				//the source node don't have neighbor
				HashMap<Integer, EdgeData> innerHash = new HashMap<Integer, EdgeData>();
				EdgeData temp = new EdgeData();
				temp.setDest(dest);
				temp.setSrc(src);
				temp.setWeight(w);
				innerHash.put(dest, temp);
				edges.put(src, innerHash);
				pointers.get(dest).add(nodesInGraph.get(src).getKey());
				neighbors.get(src).add(nodesInGraph.get(dest));
				edgeSiz++;
				mcCounter++;
				}
			}
		}
	}

	@Override
	public Collection<node_data> getV()
	{
		return nodesInGraph.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) 
	{
		if(edges.get(node_id) != null)
		{
			Iterator<Integer> i = edges.get(node_id).keySet().iterator();
			LinkedList<edge_data> tmp = new LinkedList<>();
			while(i.hasNext())
			{				
				tmp.add(edges.get(node_id).get(i.next()));
			}
			return tmp;
		}		
		return null;
	}

	@Override
	public node_data removeNode(int key) 
	{	
		// If the node exist in the graph
		if(nodesInGraph.containsKey(key))
		{
			// If this node have a directed edge to another node
			if(edges.get(key)!=null) 
			{
				Iterator<Integer> a = edges.get(key).keySet().iterator();
				while(a.hasNext())
				{
					removeEdge(key,a.next());				
				}
			}	
			
			//If this node is the destination of a directed edge.
			if(pointers.get(key)!=null)
			{
				Iterator<Integer> a = pointers.get(key).iterator();
				while(a.hasNext())
				{
					removeEdge(a.next(), key);				
				}
			}
			node_data temp = nodesInGraph.get(key);
			nodesInGraph.remove(key);	
			neighbors.keySet().remove(key);
			pointers.keySet().remove(key);
			edges.keySet().remove(key);
			mcCounter++;
			// maybe we should add "nodesInGraph.keySet().remove(key)"?
			return temp;
		}
		return null;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		
//	if(nodesInGraph.containsKey(src))
//	{				
		if(edges.containsKey(src))
		{
			if(edges.get(src).containsKey(dest) && edges.get(src).get(dest)!=null)
			{
				edge_data temp = edges.get(src).get(dest);
				/*
				 * First we should remove the directed edge
				 * 		6
				 * 		|
				 * 		\
				 * 		|
				 * 		v
				 * 		4 <--------5
				 */
				edges.get(src).remove(dest);
				if(edges.get(src).size()==0)
				{
					edges.keySet().remove(src);
				}
				/*
				 * Second we should remove the src from the 
				 * 			 collection of the dest pointers.
				 * The HashMap should look like this:
				 * (From)4 ----> {6,5}  ==> (To) 4 ----> {5}
				 */
				pointers.get(dest).remove(src);
				
				//Remove the destination node from the collection of the "neighbors"
				neighbors.get(src).remove(nodesInGraph.get(dest));
				edgeSiz--;
				mcCounter++;
				return temp;
			}			
		}
//	}		
		return null;
	}

	public Collection<node_data> getNi(int src)
	{
		return neighbors.get(src);
	}
	@Override
	public int nodeSize() 
	{
		return nodesInGraph.size();
	}

	@Override
	public int edgeSize() 
	{
		return edgeSiz;
	}

	@Override
	public int getMC() 
	{
		return mcCounter;
	}

/////////////////////////internal class NodeData///////////////////////////////
	public class NodeData implements node_data{
		private int key;
		private GeoLocation geoL;
		private double weight;
		private String info;
		private int tag;
		
		
		
		public NodeData()
		{
			this.key = 0;
			this.geoL = new GeoLocation();
			this.weight = 0; // Maybe it should be infinite
			this.info = "";
			this.tag = 0;
		}
		
		public NodeData(int key)
		{
			this.key = key;
			this.geoL = new GeoLocation();
			this.weight = 0; // Maybe it should be infinite
			this.info = "";
			this.tag = 0;
		}
		
		public String toString()
		{
			return "This key: " + key;
		}
		
		@Override
		public int getKey() {
			return this.key;
		}

		@Override
		public geo_location getLocation() {
			return geoL;
		}

		@Override
		public void setLocation(geo_location p) {
			this.geoL = (GeoLocation)p;
		}

		@Override
		public double getWeight() {
			return this.weight;
		}

		@Override
		public void setWeight(double w) {
			this.weight=w;
			
		}

		@Override
		public String getInfo() {
			
			return this.info;
		}

		@Override
		public void setInfo(String s) {
			this.info=s;
			
		}

		@Override
		public int getTag() {
			
			return this.tag;
		}

		@Override
		public void setTag(int t) {
			this.tag=t;
			
		}
		
	}
////////////////internal class GeoLocation/////////////////////////////////////
	
	
}
