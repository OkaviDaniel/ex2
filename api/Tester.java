package api;



public class Tester {

	public static void main(String[] args) {
		
		//Serializable
		DWGraph_DS g = new DWGraph_DS();
		for(int i = 0; i<10;i++)
		{
			NodeData a = new NodeData(i);
			a.setInfo("This vertex key is equals to: " + i);
			a.setLocation(new GeoLocation(3+i,5+i*2,Math.pow(2, i)));
			g.addNode(a);
		}
	
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		
		g.connect(0, 1, 1);
		g.connect(0, 5, 6);
		g.connect(1, 4, 1);
		g.connect(4, 2, 10);
		g.connect(2, 3, 10);
		g.connect(3, 4, 10);
		g.connect(4, 5, 1);
		g.connect(5, 6, 10);
		g.connect(6, 7, 10);
		g.connect(7, 8, 10);
		g.connect(8, 9, 10);
		g.connect(9, 0, 10);
		
		ga.save("hello.json");
		ga.load("hello.json");
		System.out.println(ga.connectedOrNot());
		System.out.println(ga.shortestPathDist(0, 5));
		System.out.println(ga.shortestPath(0, 5));

	}

}
