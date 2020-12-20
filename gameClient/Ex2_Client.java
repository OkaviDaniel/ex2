package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import api.node_data;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private int numOfAgents;
	private static SimpleGui _gui;
	private static long id=0;
	private static int scenario=0;
	private static boolean cmdT = false;
	private static boolean Sfull = false;
	private static boolean itsOkayToMove = false;
	private static Agent ras  = new Agent();
	
	
	private static HashMap<Integer, Pokemon> currFruit;			// <agent id, pokemon>
	private static HashMap<Integer, List<node_data>> currPath;    //<agent src node, shortest path>
	private static HashMap<Integer, Integer> currPredator;		//<Pokemon id, Agent id>
	private static HashMap<Integer, Integer> currPokIsTaken;	//<pokemon id, boolean>
	
	
	
	public static void main(String[] a)
	{
	
		if(a.length==2)
		{
			if(a[0].length() == 9 && a[1].length()>0)
			{
				id= Long.parseLong(a[0]);
				scenario = Integer.parseInt(a[1]);
				cmdT = true;
				Thread client = new Thread(new Ex2_Client());
				client.start();	
			}
		}
		if(cmdT == false)
		{
			_gui = new SimpleGui();
			_gui.setVisible(true);
			while(_gui.isVisible())
			{	
				_gui.repaint();
				Sfull = _gui.isSucceed();
			}	
		}
		System.out.println();
		if(Sfull==true)
		{
			id = _gui.getId();
			scenario = _gui.getScenario();
			Thread client = new Thread(new Ex2_Client());
			client.start();		
		}
	}
	
	@Override
	public void run() {
		int scenario_num = 23;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	game.login(id);
		String g = game.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo();
		directed_weighted_graph gg = ga.fromJsonToGraph(g);
		//String pks = game.getPokemons();
		init(game);
//		String agentsJson = game.getAgents();
//		List<Agent> agents = Arena.getAgents(agentsJson, gg);

		game.startGame();
		_win.setTitle("Ex2 - OOP: "+game.toString());
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
	
	private static void update(game_service game, directed_weighted_graph gg) 
	{
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(gg);		
		List<Agent> agents = _ar.getAgents();
		List<Pokemon> pokemons = _ar.getPokemons();		
		double maxVal=0;
		double minW = Double.MAX_VALUE;
		
		for(Agent a: agents)
		{
			//a.get_curr_fruit()==null
			System.out.println(currFruit.get(a.getID()));
			if(currFruit.get(a.getID())==null)
			{
				//System.out.println(a.getCurrentPath());
				ga.Dijkstra(gg, gg.getNode(a.getSrcNode()));
				for(Pokemon p : pokemons)
				{
					Arena.updateEdge(p,gg);	
					//p.getPredator()==null
					//The pokemon is free of agent
					if(currPredator.get(p.getId())==null)
					{					
						// p.val > max val 
						if(p.getValue()>maxVal)
						{
							// if the efficient path from the agent to the pokemon < minW
							if(ga.oneCallShrtPathD(a.getSrcNode(), p.get_edge().getSrc()) < minW)
							{
								//a.get_curr_fruit() != null
								if(currFruit.get(a.getID())!=null)
								{
									//a.get_curr_fruit().setPredator(null);	
									//a.get_curr_fruit().setTaken(false);
									currPredator.put(currFruit.get(a.getID()).get_edge().getSrc(), null);
									currPokIsTaken.put(currFruit.get(a.getID()).get_edge().getSrc(), 0);
									
									
								}
								maxVal = p.getValue();
								minW = ga.oneCallShrtPathD(a.getSrcNode(), p.get_edge().getSrc());							
								//a.set_curr_fruit(p);
								//p.setPredator(a);
								//p.setTaken(true);							
								currFruit.put(a.getID(), p);
								currPredator.put(p.getId(), a.getID());
								currPokIsTaken.put(p.getId(),1);
								//a.setCurrentPath(ga.shortCurrPath(a.getSrcNode(), p.get_edge().getSrc()));
								currPath.put(a.getID(), ga.shortCurrPath(a.getSrcNode(), p.get_edge().getSrc()));
								
							}	
						}
					}
				}
			}
			maxVal=0;
			minW = Double.MAX_VALUE;
			
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
			if(dest==-1) {
				dest = nextNode(gg, ag);
				game.chooseNextEdge(ag.getID(), dest);
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
	private static int nextNode(directed_weighted_graph g, Agent src)
	{
		if(currPath.get(src.getID())!=null)
		{
			if(currPath.get(src.getID()).size()==1)
			{
				int ans = currFruit.get(src.getID()).get_edge().getDest();
				currPath.put(src.getID(), null);
				return ans;
			}else{//there is a path that longer than one
				currPath.get(src.getID()).remove(0);
				return currPath.get(src.getID()).get(0).getKey();
			}
		}else {
			System.out.println(currPath.get(src.getID()));
			return -1;
		}
		
		
		/*
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
		*/
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
			currFruit = new HashMap<Integer, Pokemon>();
			currPath = new HashMap<Integer, List<node_data>>();
			currPredator = new HashMap<Integer, Integer>();
			currPokIsTaken = new HashMap<Integer, Integer>();
			
			jsonObj = new JSONObject(info);
			JSONObject gameServerObj = jsonObj.getJSONObject("GameServer");
			this.numOfAgents = gameServerObj.getInt("agents");
			int counter4agents = 0;
			double maxVal = 0;
			Pokemon j = null;
			ArrayList<Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
			for(int i = 0;i<pokemons.size();i++)
			{
				currPokIsTaken.put(pokemons.get(i).getId(),0);
				Arena.updateEdge(pokemons.get(i),g);
			}			
			for(;counter4agents < numOfAgents; counter4agents++)
			{
				for(int i = 0; i<pokemons.size(); i++)
				{//pokemons.get(i).isTaken()==false
					if(pokemons.get(i).getValue()>maxVal &&  currPokIsTaken.get(pokemons.get(i).getId())==0)
					{
						j=pokemons.get(i);
						System.out.println(j.getId());
						maxVal = pokemons.get(i).getValue();						
					}
				}
				currPokIsTaken.put(j.getId(), 1);
				maxVal = 0;
				game.addAgent(j.get_edge().getSrc());
				_ar.setAgents(Arena.getAgents(game.getAgents(), g));
				List<Agent> tmp1 = _ar.getAgents();
				Agent a = tmp1.get(tmp1.size()-1);
				currFruit.put(a.getID(), j);
				//System.out.println(currFruit.get(a.getID()));
				currPath.put(a.getID(), ga.shortCurrPath(a.getSrcNode(), j.get_edge().getSrc()));
				currPredator.put(j.getId(),a.getID());					
				_ar.setAgents(tmp1);
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}	
	}
	
	
}