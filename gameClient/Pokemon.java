package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import api.EdgeData;
import api.edge_data;
import gameClient.util.Point3D;

public class Pokemon 
{

	private static int counter = 0;
	private edge_data edge;
	private double value;
	private int type;
	private Point3D pos;
	private double min_dist;
	private int min_ro;
	private int id;
	
	
	public Pokemon(Point3D pos, int t, double v, edge_data e)
	{
		this.pos = pos;
		this.type = t;
		this.value = v;
		edge = e;
		min_dist = -1;
		min_ro = -1;
		this.setId(counter++);
	}

	/**
	 * Init a pokemon from a Json String
	 * @param json A string with json format
	 * @return Pokemon 
	 */
	public static Pokemon init_from_json(String json)
	{
		try
		{
			JSONObject p = new JSONObject(json);
			JSONObject u = p.getJSONObject("Pokemon");
			int val1 = u.getInt("value");
			int type1  = u.getInt("type");
			String cord = u.getString("pos");
			String[] arr = cord.split(",");
			double x1 = Double.parseDouble(arr[0]);
			double y1 = Double.parseDouble(arr[1]);
			double z1 = Double.parseDouble(arr[2]);
			Point3D pos1 = new Point3D(x1, y1, z1);
			JSONObject r = u.getJSONObject("edge");
			int src1 = r.getInt("src");
			int dest1 = r.getInt("dest");
			int tag1 = r.getInt("tag");
			double weight1 = r.getDouble("weight");
			String info1 = r.getString("info");
			EdgeData edge1 = new EdgeData(src1, dest1, tag1, weight1, info1);
			return new Pokemon(pos1, type1, val1, /*speed1,*/ edge1);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}	
		return null;
	}
	
	/** return a string with a data of the pokemon
	 * @return String
	 */
	public String toString()
	{
		return "F:{v="+ value + ", t=" + type + "}";
	}
	
	/**
	 * Return the edge of the pokemon
	 * @return edge_data edge
	 */
	public edge_data get_edge()
	{
		return edge;
	}
	
	public void set_edge(edge_data _edge) 
	{
		this.edge = _edge;
	}
	
	/**
	 * return the geo location of the node
	 * @return Point3D location
	 */
	public Point3D getLocation()
	{
		return pos;
	}
	
	/**
	 * returns the type of the pokemon
	 * @return int type
	 */
	public int getType() 
	{
		return type;
	}
	
	/**
	 * return the value of the pokemon
	 * @return double
	 */
	public double getValue() 
	{
		return value;
	}
	
	/**
	 * return the min distance of the pokemon
	 * @return double
	 */
	public double getMin_dist() 
	{
		return min_dist;
	}
	
	public void setMin_dist(double mid_dist)
	{
		this.min_dist = mid_dist;
	}
	
	
	public int getMin_ro() 
	{
		return min_ro;
	}
	
	public void setMin_ro(int min_ro) 
	{
		this.min_ro = min_ro;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
