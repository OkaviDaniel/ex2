package gameClient;

import api.GeoLocation;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame implements MouseListener, MouseWheelListener{
	
	private Arena ar;
	private gameClient.util.Range2Range w2f;
	private static long seconds;
	

	

	MyFrame(String a, long timeToEnd) 
	{
		 super(a);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 seconds = Integer.MAX_VALUE;
		 Thread timer = new Thread(new Runnable() {
			 @Override
			 public void run() {
					try {
						while(seconds >= 0) {
							Thread.sleep(1000);
							seconds-=1000;
						}
						
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
			 }
		 });
		 timer.start();
	}
	
	/**
	 * update the frame with the given arena
	 * @param ar	The given arena
	 */
	public void update(Arena ar) {
		this.ar = ar;
		updateFrame();
	}

	/**
	 * Updates the frame with the arena dimensions.
	 */
	private void updateFrame() {
		
		Range rx = new Range(30,this.getWidth()-50);
		Range ry = new Range(this.getHeight()-50,100);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = ar.getGraph();
		w2f = Arena.w2f(g,frame);
	}
	
	/**
	 * Paint the whole objects on the frame
	 */
	public void paint(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		updateFrame();
		drawPokemons(g);
		drawGraph(g);
		drawAgants(g);
		drawInfo(g);
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD,20));
		drawTimer(g);

	}
	
	/**
	 * Draw the remains time on the frame
	 * @param g Graphics
	 */
	private void drawTimer(Graphics g) {		
			if(seconds != Integer.MAX_VALUE) {
				g.setColor(Color.black);
				g.drawString("Time left: "+(seconds/1000) , this.getWidth()-200,100);
			}                                                 
		}          
	
	/**
	 * Draw the info of the arena on the frame
	 * @param g Graphics
	 */
	private void drawInfo(Graphics g) {
		List<String> str = ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) 
		{
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}

	}
	
	/**
	 * Draw the graph on the frame
	 * @param g Graphics
	 */
	private void drawGraph(Graphics g) {
		directed_weighted_graph gg = ar.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.blue);
			drawNode(n,5,g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while(itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
	}
	
	/**
	 * Draw the pokemons on the frame
	 * @param g Graphics
	 */
	private void drawPokemons(Graphics g) {
		if(ar==null)
		{
			return;
		}
		else
		{
			List<Pokemon> fs = ar.getPokemons();
			if(fs!=null) {
				Iterator<Pokemon> itr = fs.iterator();

				while(itr.hasNext()) {

					Pokemon f = itr.next();
					Point3D c = f.getLocation();
					int r=10;
					g.setColor(Color.green);
					if(f.getType()<0) {g.setColor(Color.ORANGE);}
					if(c!=null) {

						geo_location fp = this.w2f.world2frame(c);
						g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
						g.drawString(""+f.getValue(), (int)fp.x(),(int)fp.y()-3*r);

					}
				}
			}
		}	
	}
	
	/**
	 * Draw the agents on the frame
	 * @param g Graphics
	 */
	private void drawAgants(Graphics g) {
		List<Agent> rs = ar.getAgents();
		g.setColor(Color.red);
		int i=0;
		while(rs!=null && i<rs.size()) {
			geo_location c = rs.get(i).getLocation();
			int r=8;
			if(c!=null) {
				geo_location fp = this.w2f.world2frame(c);
				g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
				g.drawString(""+rs.get(i).getValue(), (int)fp.x(),(int)fp.y()-3*r);
			}
			i++;
		}
	}
	
	
	/**
	 * Draw the node on 
	 * @param n the node we want to draw
	 * @param r	a default number
	 * @param g Graphics
	 */
	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this.w2f.world2frame(pos);
		g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}
	
	
	/**
	 *  Draw the edge on the frame
	 * @param e the edge we want to draw
	 * @param g Graphics
	 */
	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gg = ar.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this.w2f.world2frame(s);
		geo_location d0 = this.w2f.world2frame(d);
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static long getSeconds() {
		return seconds;
	}

	public static void setSeconds(long seconds) {
		MyFrame.seconds = seconds;
	}

}
