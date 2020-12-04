package api;

public class NodeData implements node_data{
	private int key;
	private GeoLocation geoL;
	private double weight;
	private String info;
	private int tag;
	
	public NodeData()
	{
		 this.key=0;
		 geoL = new GeoLocation();
		 this.weight = 0;
		 this.info = "";
		 this.tag = 0;
	}
	
	public String toString()
	{
		return "" + this.key;
	}
	 public NodeData(node_data other)
	
	 { 
	        this.key = other.getKey();
	        this.setLocation(other.getLocation());
	        this.setWeight(other.getWeight());
	        this.setInfo(other.getInfo());
	        this.setTag(other.getTag());
	 }
	
	 public NodeData(int key)
	 {
		 this.key=key;
		 geoL = new GeoLocation();
		 this.weight = 0;
		 this.info = "";
		 this.tag = 0;
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