package api;
/**
 * This class represents the set of operations applicable on a
 * node in a weighted graph.
 * @author boaz.benmoshe
 *
 */
public class NodeData implements node_data{
	private int key;
	private GeoLocation geoL;
	private double weight;
	private String info;
	private int tag;

	/**
	 * empty constructor
	 */
	public NodeData()
	{
		 this.key=0;
		 geoL = new GeoLocation();
		 this.weight = 0;
		 this.info = "";
		 this.tag = 0;
	}

	 /**
	 *@return String displays the key
	 */
	 public String toString()
	{
		return "" + this.key;
	}

	/**
	 *copy constructor
	 * @param other the node we copy
	 */
	 public NodeData(node_data other)
	 { 
	        this.key = other.getKey();
	        this.setLocation(other.getLocation());
	        this.setWeight(other.getWeight());
	        this.setInfo(other.getInfo());
	        this.setTag(other.getTag());
	 }

	/**
	 * constructor
	 * @param key the key of the new node
	 */
	 public NodeData(int key)
	 {
		 this.key=key;
		 geoL = new GeoLocation();
		 this.weight = 0;
		 this.info = "";
		 this.tag = 0;
	 }

	/**
	 * displays the node in Json format
	 * @return string node in json format
	 */
	public String toJson()
	{
		String ans = 
				"{"
				+ "\"pos\":"+"\""+this.geoL.toString()+"\""+","
				+ "\"id\":"+this.key
				+ "}";		
		return ans;
	}

	/**
	 *@return the key of node
	 */
	@Override
	public int getKey() {
		return this.key;
	}
	/**
	 *@return the location of node
	 */
	@Override
	public geo_location getLocation() {
		return geoL;
	}

	/**
	 * set the location of the node
	 *@param p the new location of the node
	 */
	@Override
	public void setLocation(geo_location p) {
		this.geoL = (GeoLocation)p;
	}
	/**
	 *@return the weight of the node
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}
	/**
	 * set the weight of the node
	 *@param w the new weight of the node
	 */
	@Override
	public void setWeight(double w) {
		this.weight=w;
		
	}
	/**
	 *@return string the info of the node
	 */
	@Override
	public String getInfo() {
		
		return this.info;
	}
	/**
	 * set the info of the node
	 *@param s the new info of the node
	 */
	@Override
	public void setInfo(String s) {
		this.info=s;
		
	}
	/**
	 *@return the tag of the node
	 */
	@Override
	public int getTag() {
		
		return this.tag;
	}

	/**
	 * set the tag of the node
	 *@param t the new tag of the node
	 */
	@Override
	public void setTag(int t) {
		this.tag=t;
		
	}
	
}