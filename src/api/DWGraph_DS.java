package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class represents a directional weighted graph
 * and set of operations applicable on a directional weighted graph
 *
 */
public class DWGraph_DS implements directed_weighted_graph , Serializable{
	
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, node_data> nodesInGraph;
	private HashMap<Integer,HashMap<Integer,EdgeData>> edges;
	private HashMap<Integer, Collection<node_data>> neighbors;	
	private HashMap<Integer, Collection<node_data>> pointers;	
	private int edgeSiz=0;
	private int mcCounter=0;
	
//	private long counter2=0;
	/**
	 *  empty constructor
	 */
	public DWGraph_DS()
	{
		this.pointers = new HashMap<Integer, Collection<node_data>> ();
		this.nodesInGraph = new HashMap<Integer, node_data>();
		this.edges = new HashMap<Integer, HashMap<Integer,EdgeData>>();
		this.neighbors = new HashMap<Integer, Collection<node_data>>();
	}

	public void setEdgeSize(int size)
	{
		this.edgeSiz = size;
	}
	
	public void setMC(int size)
	{
		this.mcCounter = size;
	}
	/**
	 *@return String displays the graph
	 */
	public String toString()
	{
		String ans = "";
		for(node_data a: nodesInGraph.values())
		{
			if(neighbors.get(a.getKey()).size()>0)
			{
				for(node_data b: neighbors.get(a.getKey()))
				{
					ans=ans+ a.getKey() + " " + b.getKey()+" " +  edges.get(a.getKey()).get(b.getKey()).getWeight() + "\n";
					
				}
			}
			else
			{
				ans = ans + a.getKey() + "\n";
			}	
		}
		return ans;
	}
	/**
	 *  returns the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		if(nodesInGraph.containsKey(key))
		{
			return nodesInGraph.get(key);
		}
		return null;
	}
	/**
	 * Return a collection of the nodes that the node with the given ID is the dest
	 *  node of the connecting edge between them
	 * @param key the ID of the node
	 * @return collection of the nodes
	 */
   public Collection<node_data> getPointers(int key){
	   if(pointers.containsKey(key))
	   return pointers.get(key);
	   return null;
   }

	/**
	 * returns the data of the edge (src,dest), null if none.
	 * @param src
	 * @param dest
	 * @return the data of the edge , null if none
	 */
	@Override
	public edge_data getEdge(int src, int dest)
	{		
		if(edges.containsKey(src) && edges.get(src).containsKey(dest))
		{			
			return edges.get(src).get(dest);
		}
		return null;
	}
	/**
	 * adds a new node to the graph with the given node_data.
	 * @param n the node to add
	 */
	@Override
	public void addNode(node_data n) 
	{		
		if(!nodesInGraph.containsKey(n.getKey()))
		{
			nodesInGraph.put(n.getKey(), n);
			pointers.put(n.getKey(), new LinkedList<node_data>());
			neighbors.put(n.getKey(), new LinkedList<node_data>());
			mcCounter++;
		}
	}

	/**
	 * @return the edges of the graph
	 */
	public Collection<EdgeData> getEdges()
	{
		ArrayList<EdgeData> ans = new ArrayList<EdgeData>();
		for(Integer i : edges.keySet())
		{
			for(Integer j: edges.get(i).keySet())
			{
				ans.add(edges.get(i).get(j));
			}
		}
		return ans;
	}
	/**
	 * Connects an edge with weight w between node src to node dest.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost between src-->dest.
	 */
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
				//HashMap<Integer, EdgeData> innerHash = new HashMap<Integer, EdgeData>();
						EdgeData temp = new EdgeData();
						temp.setDest(dest);
						temp.setSrc(src);
						temp.setWeight(w);
				//		innerHash.put(dest, temp);
						edges.get(src).put(dest, temp);
						pointers.get(dest).add(nodesInGraph.get(src));
						neighbors.get(src).add(nodesInGraph.get(dest));
						edgeSiz++;
						mcCounter++;
					}
					
					return;
				}
				//the source node don't have neighbor
				HashMap<Integer, EdgeData> innerHash = new HashMap<Integer, EdgeData>();
				EdgeData temp = new EdgeData();
				temp.setDest(dest);
				temp.setSrc(src);
				temp.setWeight(w);
				innerHash.put(dest, temp);
				edges.put(src, innerHash);
				pointers.get(dest).add(nodesInGraph.get(src));
				neighbors.get(src).add(nodesInGraph.get(dest));
				edgeSiz++;
				mcCounter++;
			}
		}
	}
	/**
	 * This method returns a pointer for the
	 * collection representing all the nodes in the graph.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_data> getV()
	{
		return nodesInGraph.values();
	}
	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the edges getting out of
	 * the given node (all the edges starting (source) at the given node).
	 * @return Collection<edge_data>
	 */
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
	/**
	 * Deletes the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * @return the data of the removed node (null if none).
	 * @param key
	 */
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
				Iterator<node_data> a = pointers.get(key).iterator();
				while(a.hasNext())
				{
					removeEdge(a.next().getKey(), key);				
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
	/**
	 * Deletes the edge from the graph,
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
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
				
				/*
				 * Second we should remove the src from the 
				 * 			 collection of the dest pointers.
				 * The HashMap should look like this:
				 * (From)4 ----> {6,5}  ==> (To) 4 ----> {5}
				 */
				pointers.get(dest).remove(nodesInGraph.get(src));
				neighbors.get(src).remove(nodesInGraph.get(dest));
				edgeSiz--;
				mcCounter++;
				return temp;
			}			
		}
//	}		
		return null;
	}
	/**
	 * Checks if there is a edge between two src(ID) node, to dest(ID) node
	 * @param src the ID of the src node
	 * @param dest the ID of the dest node
	 * @return true/false
	 */
	public boolean hasEdge(int src, int dest)
	{
		boolean ans = false;
		if(nodesInGraph.containsKey(src) && nodesInGraph.containsKey(dest))
		{
			if(edges.containsKey(src))
			{
				if(edges.get(src).containsKey(dest))
				{
					ans=true;
				}			
			}		
		}		
		return ans;
	}
	/**
	 * Returns the collection of neighbors of the node with the ID
	 * @param src the ID of the node
	 * @return collection of neighbors of the node with the ID
	 */
	public Collection<node_data> getNi(int src)
	{
		return neighbors.get(src);
	}
	/**
	 * @return the number of nodes in the graph.
	 */
	@Override
	public int nodeSize() 
	{
		return nodesInGraph.size();
	}
	/**
	 * @return the number of edges.
	 */
	@Override
	public int edgeSize() 
	{
		return edgeSiz;
	}
	/**
	 * @return the Mode Count - for testing changes in the graph.
	 */
	@Override
	public int getMC() 
	{
		return mcCounter;
	}

	/**
	 * Turns the edges directions
	 */
	public void changeDirections()
	{
		ArrayList<edge_data> edgeses = new ArrayList<edge_data>();
		for(edge_data a : getEdges())
		{
			edgeses.add(a);
		}
		for(edge_data a:edgeses )
		{
			int src1 = a.getSrc();
			int dest1 = a.getDest();
			double w = a.getWeight();
			removeEdge(src1, dest1);
			connect(dest1, src1, w);
		}
	}	
}
