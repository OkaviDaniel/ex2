package api;

import java.util.ArrayList;
import java.util.List;

public class Tester {

	public static void main(String[] args) {
		
		//Serializable
		DWGraph_DS g = new DWGraph_DS();
//		for(int i = 0; i<10;i++)
//		{
//			NodeData a = new NodeData(i);
//			a.setInfo("This vertex key is equals to: " + i);
//			a.setLocation(new GeoLocation(3+i,5+i*2,Math.pow(2, i)));
//			g.addNode(a);
//		}
		for(int i =0;i<10;i++)
		{
			g.addNode(new NodeData(i));
		}
	
		g.connect(0, 1, 1);
		g.connect(1, 4, 1);
		g.connect(4, 3, 1);
		g.connect(4, 2, 3);
		g.connect(3, 2, 1);
		
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
	//	System.out.println(ga.isConnected());
//		ga.save("hello.json");
//		ga.load("hello.json");
//		System.out.println(ga.connectedOrNot());
	//	System.out.println(ga.shortestPathDist(0, 5));///******When trying to fine the shortest path from an existing node to non-existing node
	//	System.out.println(ga.shortestPath(0, 5));///******When trying to fine the shortest path from an existing node to non-existing node
		System.out.println(g.getV().size());
	//	System.out.println(ga.oneCallShrtPathD(0, 5));
		System.out.println(ga.shortestPath(0, 2));
	//	System.out.println(ga.oneCallShrtPathD(0, 2));
		System.out.println(ga.shortCurrPath(0, 2));
		

		
		
		
//		for(int i =0 ; i<10; i++)
//		{
//			g.addNode(new NodeData(i));			
//		}
//		for (int i = 0; i < 10; i++) {
//			g.connect(i, i+1, 1);
//		}
//		g.connect(3, 0, 1);
//		
		//g.connect(0, 999999, 1);
		
		
		//ga.save("hello.json");
		//ga.load("hello.json");
		
		
		/*
		 * List<Integer> asad = new ArrayList<Integer>(); asad.add(2); asad.add(4);
		 * asad.add(5); asad.add(26); System.out.println(asad);
		 * System.out.println(asad.get(2)); asad.remove(2); System.out.println(asad);
		 */
		
	}

}
