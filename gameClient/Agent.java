package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import api.GeoLocation;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;

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
	
	
	private List<Integer> currentComp;
	//public int indexOnTheComp;
	
	public Agent(directed_weighted_graph g, int start_node) 
	{
		gg = g;
		this.value = 0; // val == money
		this.curr_node = gg.getNode(start_node);
		pos = curr_node.getLocation();
		id = -1;
		this.speed = 0;
		currentComp = new ArrayList<Integer>(); //??
	}
	
	// a new constructor with component
	public Agent(directed_weighted_graph g, int start_node, List<Integer> comp) 
	{
		gg = g;
		this.value = 0; // val == money
		this.curr_node = gg.getNode(start_node);
		pos = curr_node.getLocation();
		id = -1;
		this.speed = 0;
		this.currentComp = comp;
	}
	
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
	
	//******************* 			*****************************
	//******************* to change *****************************
	//******************* 			*****************************
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
	
	public List<Integer> getComp()
	{
		return currentComp;
	}
	
	public void setComp(List<Integer> l)
	{
		this.currentComp = l;
	}
	
	public int getSrcNode() 
	{
		return this.curr_node.getKey();
	}
	
	public void setSpeed(int i) {
		this.speed = i;
	}


	public void setMoney(double i) {
		this.value = i;
	}
	
	
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
	
	public boolean isMoving() {
		return this.curr_edge!=null;
	}
	
	
	
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
			if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
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
	
	public long get_sg_dt() {
		return sg_dt;
	}
	
	public void set_sg_dt(long _sg_dt) 
	{
		this.sg_dt = _sg_dt;
	}
	
}