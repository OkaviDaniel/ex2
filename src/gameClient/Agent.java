package gameClient;

import org.json.JSONObject;

import api.GeoLocation;
import api.NodeData;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;

/**
 * This class represent an agent from the pokemon series
 */
public class Agent {
	
	public static final double EPS = 0.0001;
	//private static int count = 0;
	//private static int seed = 3331; //?
	private int id; 
	private geo_location pos;
	private double speed;
	private edge_data curr_edge;
	private node_data curr_node;
	private directed_weighted_graph gg;
	private Pokemon curr_fruit;
	private long sg_dt;
	private double value;


	/**
	 * empty constructor
	 */
	public Agent()
	{
		gg = null;
		this.value = 0; // val == money
		this.curr_node = new NodeData();
		pos = curr_node.getLocation();
		id = -1;
		this.speed = 0;
	}

	/**
	 * constructor
	 * @param g the graph we want to place the agent
	 * @param start_node	The node we want to plcae the agent on the graph
	 */
	public Agent(directed_weighted_graph g, int start_node) 
	{
		gg = g;
		this.value = 0; // val == money
		this.curr_node = gg.getNode(start_node);
		pos = curr_node.getLocation();
		id = -1;
		this.speed = 0;
		
	}	
	
	/**
	 * Update the current agent from the given data in Json format
	 * @param json	String with Json format
	 */
	public void update(String json) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			JSONObject agentObj = jsonObj.getJSONObject("Agent");
			int id1 = agentObj.getInt("id");
			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {this.id = id1;}
				double speed = agentObj.getDouble("speed");
				int src = agentObj.getInt("src");
				int dest = agentObj.getInt("dest");
				double value = agentObj.getDouble("value");
				String p = agentObj.getString("pos");
				Point3D pp = new Point3D(p);
				GeoLocation tmp = new GeoLocation(pp.x(), pp.y(), pp.z());
				pos = tmp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Convert the current agent into a String with Json format
	 * @return  String with Json format
	 */
	public String toJSON() 
	{
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this.id+","
				+ "\"value\":"+this.value+","
				+ "\"src\":"+this.curr_node.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.speed+","
				+ "\"pos\":\""+pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;	
	}

	/**
	 * A getter for the source node.
	 * @return int the value of the src node
	 */
	public int getSrcNode() 
	{
		return this.curr_node.getKey();
	}

	/**
	 * Set the money for the current agent.
	 * @param i	The wanted value
	 */
	public void setMoney(double i) {
		this.value = i;
	}
	
	/**
	 * Set the destination node of the current agent.
	 * @param dest	The destination node key
	 * @return boolean 	Returns true if and only if the next node is a neighbor of the current node
	 */
	public boolean setNextNode(int dest)
	{
		boolean ans = false;
		int src = this.curr_node.getKey();
		this.curr_edge = gg.getEdge(src, dest);
		if(curr_edge!=null) 
		{
			ans=true;
		}
		else 
		{
			curr_edge = null;
		}
		return ans;
	}
	
	
	public void setCurrNode(int src) {
		this.curr_node = gg.getNode(src);
	}
	
	/**
	 * Check if the current agent is moving or not on the edge.
	 * @return boolean
	 */
	public boolean isMoving() {
		return this.curr_edge!=null;
	}
	
	
	/**
	 * Get the destination node of the agent
	 * @return	int the key value of the node
	 */
	public int getNextNode() {
		int ans = -2;
		if(this.curr_edge==null) 
		{
			ans = -1;
		}
		else
		{
			ans = this.curr_edge.getDest();
		}
		return ans;
	}

	/**
	 * return String with Json format
	 * @return String
	 */
	public String toString() {
		return toJSON();
	}
	
	public String toString1() {
		String ans=""+this.getID()+","+pos+", "+isMoving()+","+this.getValue();	
		return ans;
	}
	public int getID() {
		return this.id;
	}

	
	public geo_location getLocation() {
		return pos;
	}

	
	public double getValue() {
		return this.value;
	}


	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double v) {
		this.speed = v;
	}
	
	public Pokemon get_curr_fruit() {
		return curr_fruit;
	}
	
	public void set_curr_fruit(Pokemon curr_fruit) {
		this.curr_fruit = curr_fruit;
	}
	
	/**
	 * get a default number and calculate the time it takes to the agent to move to the next node.
	 * @param ddtt  A default number
	 */
	public void set_SDT(long ddtt) 
	{
		long ddt = ddtt;
		if(this.curr_edge!=null)
		{
			double w = get_curr_edge().getWeight();
			geo_location dest = gg.getNode(get_curr_edge().getDest()).getLocation();
			geo_location src = gg.getNode(get_curr_edge().getSrc()).getLocation();
			double de = src.distance(dest);
			double dist = pos.distance(dest);
			if(this.get_curr_fruit().get_edge()==this.get_curr_edge())
			{
				 dist = curr_fruit.getLocation().distance(this.pos);
			}
			double norm = dist/de;
			double dt = w*norm / this.getSpeed(); 
			ddt = (long)(1000.0*dt);
		}
		this.set_sg_dt(ddt);
	}
	
	 public edge_data get_curr_edge()
	{
		return this.curr_edge;
	}
	
	 /**
	  * Get the time to get to the destination
	  * @return long the time to get to the destination
	  */
	public long get_sg_dt() {
		return sg_dt;
	}
	
	/**
	 * Set the time to get to the destination
	 * @param _sg_dt the wanted time
	 */
	public void set_sg_dt(long _sg_dt) 
	{
		this.sg_dt = _sg_dt;
	}

	
}