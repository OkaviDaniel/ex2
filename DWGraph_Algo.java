package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import api.DWGraph_DS.NodeData;


public class DWGraph_Algo implements dw_graph_algorithms{
	private directed_weighted_graph g;
	
	//Default constructor
	public DWGraph_Algo()
	{
		this.g = new DWGraph_DS();
	}	
	
	@Override
	public void init(directed_weighted_graph g) {
		this.g = (DWGraph_DS)g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return g;
	}

	@Override
	public directed_weighted_graph copy() {
		return null;
	}

	@Override
	public boolean isConnected()
	{
		
		if(g.getV().size()==0 || g.getV().size()==1)
		{
			return true;
		}
		else
		{
			int count = 0;
			for(node_data a: g.getV())
			{
				count++;
				System.out.println(count);
				bfs(a);
				if(visitedAll() == false)
				{
					restoreNodes();
					return false;
				}
				restoreNodes();
			}
			return true;
		}
	}


	@Override
	public double shortestPathDist(int src, int dest) {
		return 0;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		return null;
	}

	@Override
	public boolean save(String file) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(g);
		//System.out.println(json);
		
		//Write json to file
		try {
			PrintWriter aw = new PrintWriter(new File(file+".json"));
			aw.write(json);
			aw.close();
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public boolean load(String file) {
		return false;
	}
	
	
	
	
	///////////////////**Private methods**///////////////////////////
	private void bfs(node_data src)
	{	
		int count=0;
		LinkedList<node_data> q = new LinkedList<node_data>();
		src.setInfo("Visited");
		q.add(src);
		while(!q.isEmpty())
		{			
			count++;
			node_data temp = q.removeFirst();
			//System.out.println(temp);			
			for(node_data t : ((DWGraph_DS)g).getNi(temp.getKey()))
			{
				NodeData inner = (NodeData)t;
				if(!t.getInfo().equals("Visited"))
				{
					inner.setInfo("Visited");
					q.add(inner);
				}
			}			
		}
		System.out.println("The number of iteration of this is: " + count);
	}
	
	private boolean visitedAll() {
		for(node_data a: g.getV())
		{
			if(!a.getInfo().equals("Visited"))
			{
				return false;
			}
		}
		return true;
	}
	

	private void restoreNodes()
	{
		for(node_data a: g.getV())
		{			
			a.setInfo("");
			a.setTag(0);
			a.setWeight(0);
			a.setLocation(new GeoLocation());
		}
	}
}
