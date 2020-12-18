package api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sun.source.tree.AssertTree;

class DWGraph_AlgoTest {

	@Test
	public void connectivity()
	{
		DWGraph_DS g = new DWGraph_DS();
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		int n = 1000000;
		for (int i = 0; i < n; i++) 
		{
			g.addNode(new NodeData(i));
		}
		for (int i = 0; i <n; i++) {
			g.connect(i,i+1,1);
		}
		g.connect(n-1, 0, 1);		
		assertTrue(ga.isConnected());
	}
	
	@Test
	public void shortestp()
	{
		DWGraph_DS g = new DWGraph_DS();
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		
		for (int i = 0; i < 3; i++) {
			g.addNode(new NodeData(i));
		}
		g.connect(0, 1, 4);
		g.connect(1, 2, 24);
		g.connect(0, 2, 4);
		assertEquals(4, ga.shortestPathDist(0, 2));
	}
	@Test
	public void getCompCheck()
	{
		DWGraph_DS g = new DWGraph_DS();
		for (int i = 0; i < 5; i++) {
			g.addNode(new NodeData(i));
		}		
		g.connect(0,1, 1);
		g.connect(1,0, 1);
		g.connect(0,4, 1);
		g.connect(4,0, 1);
		g.connect(3,4, 1);
		g.connect(4,3, 1);
		g.connect(3,2, 1);
		g.connect(1,2, 1);
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		assertEquals(2, ga.getComp().size());
		
		/*	The graph should looks like this:
		 * 				0 <-------->1
		 * 				^			|
		 * 				|			|
		 * 				|			v
		 * 				|			2
		 * 				|			^
		 * 				v			|
		 * 				4<--------->3
		 * 
		 */
	}
	@Test
	public void shortestP()
	{
		
		DWGraph_DS g = new DWGraph_DS();
		for (int i = 0; i < 5; i++) {
			g.addNode(new NodeData(i));
		}		
		g.connect(0,1, 5);
		g.connect(1,0, 5);
		g.connect(0,4, 2);
		g.connect(4,0, 2);
		g.connect(3,4, 2);
		g.connect(4,3, 2);
		g.connect(3,2, 1);
		g.connect(1,2, 1);
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		assertEquals(5, ga.shortestPathDist(0, 2));
		
		/*	The graph should looks like this:
		 * 					  5
		 * 				0 <-------->1
		 * 				^			|
		 * 				|			|1
		 * 				|			v
		 * 			   2|			2
		 * 				|			^
		 * 				v			|1
		 * 				4<--------->3
		 * 					  2
		 */
	}	
	
	@Test
	public void fromJsontoGraphCheck()
	{
		String a = "{\"Edges\":[],\"Nodes\":[{\"pos\":\"35,32,0.0\",\"id\":0}]}";
		DWGraph_Algo ga = new DWGraph_Algo();
		directed_weighted_graph g =	ga.fromJsonToGraph(a);
		assertEquals(1, g.getV().size());
	}
}
