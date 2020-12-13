/**
 * This class represents a 3D point in space.
 */
package gameClient.util;

import api.geo_location;

import java.io.Serializable;

public class Point3D implements geo_location, Serializable{
	private static final long serialVersionUID = 1L;
	/**
     * Simple set of constants - should be defined in a different class (say class Constants).*/
    public static final double EPS1 = 0.001, EPS2 = Math.pow(EPS1,2), EPS=EPS2;
    /**
     * This field represents the origin point:[0,0,0]
     */
    public static final Point3D ORIGIN = new Point3D(0,0,0);
    
    private double x,y,z;
    
    public Point3D(double x, double y, double z) {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Point3D(Point3D p)
    {
       this(p.x(), p.y(), p.z());
    }
    
    public Point3D(double x, double y)
    {
    	this(x,y,0);
    }
    
    public Point3D(String s) 
    { try 
    {
            String[] a = s.split(",");
            x = Double.parseDouble(a[0]);
            y = Double.parseDouble(a[1]);
            z = Double.parseDouble(a[2]);
        }
        catch(IllegalArgumentException e) {
            System.err.println("ERR: got wrong format string for POint3D init, got:"+s+"  should be of format: x,y,x");
            throw(e);
        }
    }
    @Override
    public double x() {return x;}
    @Override
    public double y() {return y;}
    @Override
    public double z() {return z;}


    public String toString() 
    { 
    	return x+","+y+","+z; 
    }
    
    @Override
    public double distance(geo_location p2) {
        double dx = this.x() - p2.x();
        double dy = this.y() - p2.y();
        double dz = this.z() - p2.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }

    public boolean equals(Object p) 
    {
        if(p==null || !(p instanceof geo_location)) 
        {
        	return false;
        }
        Point3D p2 = (Point3D)p;
        return ( (x==p2.x) && (y==p2.y) && (z==p2.z) );
    }
    
    public boolean close2equals(geo_location p2) 
    {
        return ( this.distance(p2) < EPS ); 
       }
    
    public boolean equalsXY (Point3D p)
    {
    	return p.x == x && p.y == y;
    }

     public String toString(boolean all) {
        if(all) return "[" + x + "," +y+","+z+"]";
        else return "[" + (int)x + "," + (int)y+","+(int)z+"]";
    }
}

