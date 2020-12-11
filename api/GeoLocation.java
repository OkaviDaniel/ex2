package api;


public class GeoLocation implements geo_location
{
	private double x;
	private double y;
	private double z;
	
	public String toString()
	{
		String ans = "";
		ans = ans + x + ",";
		ans = ans + y + ",";
		ans = ans + z;
		return ans;
	}
	
	public GeoLocation()
	{
		this.x=0;
		this.y=0;
		this.z=0;
	}
    public GeoLocation(double x, double y, double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
    
    public GeoLocation(GeoLocation a)
	{
		this.x=a.x;
		this.y=a.y;
		this.z=a.z;
	}
	@Override
	public double x() {
		return this.x;
	}
	
	@Override
	public double y() {
		return this.y;
	}

	@Override
	public double z() {
		return this.z;
	}
	
	@Override
	public double distance(geo_location g)
	{
		double a = this.x - g.x();
		double b = this.y - g.y();
		double c = this.z - g.z();
		a=a*a;
		b=b*b;
		c=c*c;		
		return Math.sqrt(a+b+c);
	}		
}