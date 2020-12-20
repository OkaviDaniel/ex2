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
		for(int i =0;i<11;i++)
		{
			g.addNode(new NodeData(i));
		}

		g.connect(0, 1, 1);
		g.connect(0,10, 12);
		g.connect(1, 0, 13);
		g.connect(1, 2, 14);
		g.connect(2, 1, 15);
		g.connect(2, 3, 16);
		g.connect(3, 2, 17);
		g.connect(3, 4, 18);
		g.connect(4, 3, 19);
		g.connect(4, 5, 14);
		g.connect(5, 4, 15);
		g.connect(5, 6, 11);
		g.connect(6, 5, 51);
		g.connect(6, 7, 14);
		g.connect(7, 6, 16);
		g.connect(7, 8, 12);
		g.connect(8, 7, 112);
		g.connect(8, 9, 51);
		g.connect(9, 8, 14);
		g.connect(9,10, 15);
		g.connect(10,9, 18);
		g.connect(10,0, 17);

		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
//		System.out.println(ga.getComp());
//		System.out.println(g.getE(4));
		System.out.println(ga.shortestPathDist(4, 10));
		//	System.out.println(ga.isConnected());
		//		ga.save("hello.json");
		//		ga.load("hello.json");
		//		System.out.println(ga.connectedOrNot());
		//	System.out.println(ga.shortestPathDist(0, 5));///******When trying to fine the shortest path from an existing node to non-existing node
		//	System.out.println(ga.shortestPath(0, 5));///******When trying to fine the shortest path from an existing node to non-existing node
		//System.out.println(g.getV().size());
		//	System.out.println(ga.oneCallShrtPathD(0, 5));
		//System.out.println(ga.shortestPath(0, 2));
		//	System.out.println(ga.oneCallShrtPathD(0, 2));
		//System.out.println(ga.shortestPath(4, 10));





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
