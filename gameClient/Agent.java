package gameClient;

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
	private CL_Pokemon curr_fruit;
	//private long sg_dt;
	private double value;
	
	
	public Agent(directed_weighted_graph g, int start_node) 
	{
		gg = g;
		this.value = 0; // val == money
		this.curr_node = gg.getNode(start_node);
		pos = curr_node.getLocation();
		id = -1;
		this.speed = 0;
	}
	
	
	
	public void update(String json) //ID?
	{
		try
		{
			JSONObject jsonObj = new JSONObject(json);
			JSONObject agentObj = jsonObj.getJSONObject("Agent");
			int id1 = agentObj.getInt("id");
			if(id1 == this.id || id1 == -1)
			{
				if(this.id == -1)
				{
					this.id = id1;
				}
				//value == money
				this.value = agentObj.getDouble("value");
				//source
				this.curr_node = gg.getNode(agentObj.getInt("src"));
				//destination
				int dest = agentObj.getInt("dest");
				this.setNextNode(dest);
				//position
				String p = agentObj.getString("pos");
				Point3D pp = new Point3D(p);
				GeoLocation tmp = new GeoLocation(pp.x(), pp.y(), pp.z());
				pos = tmp;
				//speed
				this.speed = agentObj.getDouble("speed");	
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	public String toJSON() {
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
	
	
	public void setSpeed(int i) {
		this.speed = i;
	}


	public void setMoney(int i) {
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
	
	public int getNextNode() {
		int ans = -2; //??
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
	
}
