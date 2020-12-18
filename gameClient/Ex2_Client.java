package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
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
	private int numOfAgents;
	
	
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	
	@Override
	public void run() {
		int scenario_num = 11;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		String g = game.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo();
		directed_weighted_graph gg = ga.fromJsonToGraph(g);
		String pks = game.getPokemons();
		init(game);
		String agentsJson = game.getAgents();
		List<Agent> agents = Arena.getAgents(agentsJson, gg);
		for(int i=0; i<numOfAgents;i++)
		{
			
		}				
	
		game.startGame();
		/*
		 * for(int i=0; i<numOfAgents;i++)
		 *{
		 *
		 *}		
		 */
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;
		
		while(game.isRunning()) {
			update(game,gg);
			moveAgants(game, gg);
			try {			
				if(ind%1==0) 
				{
					_win.repaint();		
				}
				Thread.sleep(dt);
			//	_win.update(_ar);
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
	
	private long st_dt(game_service game, directed_weighted_graph gg) {
		long ans = -1;
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(gg);
		String agentsJson = game.getAgents();
		List<Agent> agents = Arena.getAgents(agentsJson, gg);
		for(Agent a: agents)
		{
			if(a.getSrcNode()==a.get_curr_fruit().get_edge().getSrc())
			{
				a.setCurrentPath(null);
				game.chooseNextEdge(a.getSrcNode(),a.get_curr_fruit().get_edge().getDest());
				a.set_curr_fruit(null);
				a.set_SDT(1514);
				ans = a.get_sg_dt();
				return ans;
			}
			else
			{		
				int tmp = nextNodeImproved(a);
				game.chooseNextEdge(a.getSrcNode(), tmp);
			}
		}
	
		return ans;
	}
	
	private void update(game_service game, directed_weighted_graph gg) 
	{
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(gg);
		String agentsJson = game.getAgents();
		List<Agent> agents = Arena.getAgents(agentsJson, gg);
		String pok = game.getPokemons();
		List<Pokemon> pokemons = Arena.json2Pokemons(pok);
		double maxVal=0;
		double minW = Double.MAX_VALUE;
		
		
		for(Agent a: agents)
		{
			if(a.get_curr_fruit()==null)
			{
				ga.Dijkstra(gg, gg.getNode(a.getSrcNode()));
				for(Pokemon p : pokemons)
				{
					Arena.updateEdge(p,gg);	
					//The pokemon is free of agent
					if(p.getPredator()==null)
					{					
						// p.val > max val 
						if(p.getValue()>maxVal)
						{
							if(ga.oneCallShrtPathD(a.getSrcNode(), p.get_edge().getSrc()) < minW)
							{
								if(a.get_curr_fruit() != null)
								{
									a.get_curr_fruit().setPredator(null);								
								}
								maxVal = p.getValue();
								minW = ga.oneCallShrtPathD(a.getSrcNode(), p.get_edge().getSrc());
								a.set_curr_fruit(p);
								p.setPredator(a);
								//System.out.println("Src: "+a.getSrcNode()+", Dest: "+p.get_edge().getSrc());
								//a.setCurrentPath(ga.shortCurrPath(a.getSrcNode(),p.get_edge().getSrc()));
								//System.out.println(ga.shortCurrPath(a.getSrcNode(),p.get_edge().getSrc()));
							}	
						}
					}
				}
			}
		}
	}

	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {		
		//st_dt
		
		
		String lg = game.move();
		List<Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) {
			Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1)
			{
				dest = nextNode(gg, src);
				synchronized (ag) 
				{				
					game.chooseNextEdge(ag.getID(), dest);
					
				}			 
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
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	private static int nextNodeImproved(Agent a) {
		a.getCurrentPath().remove(0);
		return a.getCurrentPath().get(0).getKey();
	}
	
	private void init(game_service game) {
		String jsong = game.getGraph();
		String fs = game.getPokemons();
		DWGraph_Algo ga = new DWGraph_Algo();	
		directed_weighted_graph g = ga.fromJsonToGraph(jsong);
		ga.init(g);
		_ar = new Arena();
		_ar.setGraph(g);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);
		
	
		_win.show();
		String info = game.toString();
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(info);
			JSONObject gameServerObj = jsonObj.getJSONObject("GameServer");
			this.numOfAgents = gameServerObj.getInt("agents");
			int counter4agents = 0;
			// To take care about the value
			ArrayList<Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
			for(int i = 0;i<pokemons.size();i++) 
			{
				Arena.updateEdge(pokemons.get(i),g);
				game.addAgent(pokemons.get(i).get_edge().getSrc());
				_ar.setAgents(Arena.getAgents(game.getAgents(), g));
				List<Agent> tmp1 = _ar.getAgents();
				Agent a = tmp1.get(tmp1.size()-1);
				a.set_curr_fruit(pokemons.get(i));
				a.setCurrentPath(ga.shortCurrPath(a.getSrcNode(), pokemons.get(i).get_edge().getSrc()));
				//System.out.println(a.getCurrentPath());
				pokemons.get(i).setPredator(a);			
				counter4agents++;
				if(counter4agents==numOfAgents)	break;		
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}	
	}
}