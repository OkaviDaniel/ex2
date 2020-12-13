package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static long id;
	private static int scenario;
	
	public static void main(String[] a) 
	{
		Thread client = new Thread(new Ex2_Client());
		client.start();
		//id = Long.parseLong(a[0]);
		//scenario = Integer.parseInt(a[1]);
	}
	
	@Override
	public void run()
	{
		int scenario_num = 4;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		
		// getting the graph
		String g = game.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo();
		directed_weighted_graph gg = ga.fromJsonToGraph(g);

		String pks = game.getPokemons();
		init(game);
		
		game.startGame();
		_win.setTitle("Ex2 - OOP: "+game.toString());
		
		int ind=0;
		long dt=100;
		//every 0.1 seconds
		while(game.isRunning())
		{
			moveAgants(game, gg);
			try 
			{
				if(ind%1==0) 
				{
					_win.repaint();
				}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}
	
	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) 
	{
		
		String lg = game.move();
		System.out.println("-------->"+lg);
		List<Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		
		String fs =  game.getPokemons();
		List<Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) 
		{
			Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) 
			{
				dest = nextNode(gg, src); 				//
				game.chooseNextEdge(ag.getID(), dest);  //
				System.out.println(game.getAgents());
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src)
	{
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) 
		{
			itr.next();i++;
		}
		ans = itr.next().getDest();
		return ans;
	}
	
	private void init(game_service game) 
	{
		String g = game.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo();
		String fs = game.getPokemons();
		directed_weighted_graph gg = ga.fromJsonToGraph(g);
		//gg.init(g);
		
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);			
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			
			//components
		//	ga.tarjan(gg.getV().iterator().next());
		//	ArrayList<ArrayList<Integer>> comps = ga.getComp();
		//	int[] compCounter = new int[comps.size()];
			ArrayList<Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());			
			
			//int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			for(int a = 0;a<cl_fs.size();a++)
			{
				Arena.updateEdge(cl_fs.get(a),gg);
				
			}
			
//			int max= 0;
//			for (int i = 0; i < cl_fs.size(); i++) 
//			{
//				for (int j = 0; j < compCounter.length; j++) 
//				{
//					if(comps.get(j).contains(cl_fs.get(i).get_edge().getSrc()))
//					{
//						compCounter[j]++;
//						if(compCounter[j]>max)	max = j;
//						break;
//					}
//				}			
//			}
//			if(rs==1)
//			{
//				List<Agent> ag = Arena.getAgents(game.getAgents(), gg);
//				ag.iterator().next().setComp(comps.get(max));
//				for (int i = 0; i < cl_fs.size(); i++) 
//				{
//						if(comps.get(max).contains(cl_fs.get(i).get_edge().getSrc()))
//						{
//							game.addAgent(cl_fs.get(i).get_edge().getSrc());
//						}		
//				}
//				
//			}
//			
//			else
//			{
//				for(int a = 0;a<rs;a++) 
//				{
//					int ind = a%cl_fs.size();
//					Pokemon c = cl_fs.get(ind);
//					int nn = c.get_edge().getDest();
//					if(c.getType()<0 ) 
//					{
//						nn = c.get_edge().getSrc();
//					}
//					game.addAgent(nn);
//				}
//			}
//			
			
			
			  for(int a = 0;a<rs;a++) 
				{
					int ind = a%cl_fs.size();
					Pokemon c = cl_fs.get(ind);
					int nn = c.get_edge().getDest();
					if(c.getType()<0 ) 
					{
						nn = c.get_edge().getSrc();
					}
					game.addAgent(nn);
				}
			 
		}
		
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	private static void moveAgants2(game_service game, directed_weighted_graph gg)
	{
		String lg = game.move();
		
		//Getting the new list of agents from the new "game"/"move" on the graph
		List<Agent> log = Arena.getAgents(lg, gg);
		// and apply it on the arena
		_ar.setAgents(log);
		
		
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		
		String fs =  game.getPokemons();
		List<Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		
		DWGraph_Algo tmp = new DWGraph_Algo();
		directed_weighted_graph etasd = _ar.getGraph();
		tmp.init(etasd);
		
		for(int i=0;i<log.size();i++) 
		{
			Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) 
			{
				//If there is already a short path to one of the pokemons (for the current agent), then:
				if(ag.getComp().size()>0)
				{
					for (int j = 0; j < ag.getComp().size(); j++) {
						if(ag.getComp().get(j)==src)
						{
							dest = ag.getComp().get(j+1);
						}		
					}
				}
			//	List<List<Integer>> tmp2 = tmp.getComp();
			//	int agentcomp =0;				
				dest = nextNode2(gg, src); 				//
				game.chooseNextEdge(ag.getID(), dest);  //
				System.out.println(game.getAgents());
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	
	
	private static int nextNode2(directed_weighted_graph g, int src)
	{
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) 
		{
			itr.next();i++;
		}
		ans = itr.next().getDest();
		return ans;
	}
}
