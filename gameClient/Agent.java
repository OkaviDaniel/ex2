package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

public class Agent {

	public static final double EPS = 0.0001;
	private static int count = 0;
	private static int seed = 3331; //?
	private int id; 
//	private long key;
	private geo_location pos;
	private double speed;
	private edge_data curr_edge;
	private node_data curr_node;
	private directed_weighted_graph gg;
	private CL_Pokemon curr_fruit;
	private long sg_dt;
	private double value;
	//private int src,dest;
	
	
	public Agent(directed_weighted_graph g, int start_node) 
	{
		gg = g;
		setMoney(0);
		this.curr_node = gg.getNode(start_node);
		pos = curr_node.getLocation();
		id = -1;
		setSpeed(0);
	}
	
	
	
	public void update(String json) //ID?
	{
		
		
		
	}
	
	private void setSpeed(int i) {
		// TODO Auto-generated method stub
		
	}


	private void setMoney(int i) {
		// TODO Auto-generated method stub
		
	}
	
	
}
