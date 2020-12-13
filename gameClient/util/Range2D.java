package gameClient.util;
import api.geo_location;

/**
 * This class represents a 2D Range, composed from two 1D Ranges.
 */
public class Range2D 
{
	private Range y_range;
	private Range x_range;
	
	public Range2D(Range x, Range y) 
	{
		x_range = new Range(x);
		y_range = new Range(y);
	}
	
	public Range2D(Range2D w) 
	{
		x_range = new Range(w.x_range);
		y_range = new Range(w.y_range);
	} 
	
	public Point3D getPortion(geo_location p) 
	{
		double x = x_range.getPortion(p.x());
		double y = y_range.getPortion(p.y());
		return new Point3D(x,y,0);
	}
	
	public Point3D fromPortion(geo_location p) 
	{
		double x = x_range.fromPortion(p.x());
		double y = y_range.fromPortion(p.y());
		return new Point3D(x,y,0);
	}	
}
