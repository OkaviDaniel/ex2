package api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;




class DWGraph_DSTest {
	
	@Test
	public void mcCounterCheck()
	{
		DWGraph_DS g = new DWGraph_DS();
		int n=1000;	
		for (int i = 0; i < n; i++) 
		{
			g.addNode(new NodeData(i));
		}		
		g.connect(3, 1, 3);		
		g.connect(1, 3, 1);
		g.removeNode(3);		
		assertEquals(1005, g.getMC());
	}
	
	@Test
	public void edgesCheck0()
	{
		DWGraph_DS f = new DWGraph_DS();
		int n = 5;
		for (int i = 0; i < n; i++)
		{
			f.addNode(new NodeData(i));
		}
		f.connect(3, 1, 2);
		f.removeEdge(3, 1);
		assertEquals(0, f.edgeSize());
	}
	
	@Test
	public void edgesCheck()
	{
		DWGraph_DS g = new DWGraph_DS();
		int n=1000;
		for (int i = 0; i < n; i++) 
		{
			g.addNode(new NodeData(i));
		}		
		for (int i = 0; i < 25; i++) 
		{
			g.connect(i, i+1,i*13);	
			if(i<5)
				g.removeNode(i);
		}
		assertEquals(20, g.edgeSize());
	}
	
	@Test
	public void hasEdgeOrNot()
	{
		DWGraph_DS g = new DWGraph_DS();
		g.addNode(new NodeData(10));
		g.connect(10, 10, 5);
		assertFalse(g.hasEdge(10, 10));
	}
	
	@Test
	public void mcCounterCheck1()
	{
		DWGraph_DS g = new DWGraph_DS();
		int n=1000000;	
		for (int i = 0; i < n; i++) 
		{
			g.addNode(new NodeData(i));
		}		
		g.connect(3, 1, 3);		
		g.connect(1, 3, 1);
		g.removeNode(3);		
		assertEquals(1000005, g.getMC());
	}
	
	@Test
	public void lidrus()
	{
		DWGraph_DS g = new DWGraph_DS();
		g.addNode(new NodeData(15));
		g.addNode(new NodeData(1));
		g.connect(15, 1, 2);
		g.connect(15, 1, 5);
		assertEquals(5, g.getEdge(15, 1).getWeight());
	}
	
	@Test
	public void removeNodeThatNotExist()
	{
		DWGraph_DS g = new DWGraph_DS();
		node_data a = g.removeNode(2020);
		assertEquals(null, a);
	}
	
}
