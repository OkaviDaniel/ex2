package api;

public class EdgeData implements edge_data{
	private int src;
	private int dest;
	private int tag;
	private double weight;
	private String info;
	
	
	public EdgeData()
	{
		this.src=0;
		this.dest=0;
		this.tag=0;
		this.weight=0;
		this.info="";
	}
	
	public String toString()
	{
		return "Src: +"+src+",Dest: "+dest+",Weight: "+weight;
	}
	public EdgeData(int src, int dest, double weight)
	{
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.tag=0;
		this.info="";
	}
	public EdgeData(int src, int dest, int tag, double weight, String info)
	{
		this.src = src;
		this.dest = dest;
		this.tag = tag;
		this.weight = weight;
		this.info = info;
	}
	
	public String toJson()
	{
		String ans = 
				"{"
				+ "\"src\":" + this.src+","
				+ "\"w\":" + this.weight+","
				+ "\"dest\":" + this.dest
				+ "}";
		return ans;
	}
	public void setSrc(int s)
	{
		this.src = s;
	}
	
	public void setDest(int d)
	{
		this.dest = d;
	}
	
	public void setWeight(double w)
	{
		this.weight = w;
	}
	
	@Override
	public int getSrc() {
		return src;
	}

	@Override
	public int getDest() {
		return dest;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}
	
}
