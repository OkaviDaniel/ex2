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

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static long id;
	private static int scenario;
	
	
	private HashMap <Integer, ArrayList<Pokemon>> gPfC; // get pokemon from components (each Integer is index of component from the variable comps)
	private boolean[] pokCounterTaken;					// check if we already put an agent on the game
	private List<List<Integer>> comps;
	private HashMap<Integer,List<Integer>> components;
	
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
		int scenario_num = 1;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		
		// getting the graph
		String g = game.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo();
		directed_weighted_graph gg = ga.fromJsonToGraph(g);

		this.components = new HashMap<Integer, List<Integer>>();
		
		//String pks = game.getPokemons();
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
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();		
		List<Agent> agents = Arena.getAgents(lg, gg);
		_ar.setAgents(agents);	
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<Pokemon> pokemons = Arena.json2Pokemons(fs);
		_ar.setPokemons(pokemons);
		for(int i=0;i<agents.size();i++) {
			Agent ag = agents.get(i);
			int id = ag.getID();
			System.out.println("ID-------------->" + id);
			int dest = ag.getNextNode();//if next node == -1
			int src = ag.getSrcNode();// return the source node
			System.out.println(src);
			System.out.println("is: " +ag.getID());
			double v = ag.getValue();
			if(dest==-1) 
			{
				//[0,1,2,3,4,5]
				dest = nextNode(gg, src);
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
	
	///////////////////Init//////////////////
	private void init(game_service game) {
		String jsonG = game.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo();
		directed_weighted_graph g = ga.fromJsonToGraph(jsonG);
		ga.init(g);
		
		String fs = game.getPokemons();
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
			//creating a json object
			jsonObj = new JSONObject(info);
			JSONObject gameServerObj = jsonObj.getJSONObject("GameServer");
			//getting the number of agents of the current game
			int numOfAgents = gameServerObj.getInt("agents");		
			
			gPfC = new HashMap<Integer, ArrayList<Pokemon>>();
			ArrayList<Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<pokemons.size();a++) { Arena.updateEdge(pokemons.get(a),g);} //Updating the arena with the pokemons and the graph	
			this.comps = ga.getComp();			
			// Creating array that count the number of pokemons on each component
			int[] pokCounter = new int[this.comps.size()];
			pokCounterTaken = new boolean[this.comps.size()];
			int max = 0;
			//finding and setting the number of pokemons on each comp
			for (int i = 0; i < pokCounter.length; i++) {
				gPfC.put(i, new ArrayList<Pokemon>());//for each i there will be a new array
				for(int j =0; j<pokemons.size();j++)
				{
					if(this.comps.get(i).contains(pokemons.get(j).get_edge().getSrc())) {						
						pokCounter[i]++;
						this.components.put(pokemons.get(j).get_edge().getSrc(), this.comps.get(i));
						//adding the Pokemon to the right component (in index)
						ArrayList<Pokemon> a = gPfC.get(i);
						a.add(pokemons.get(j));
						if(pokCounter[i]>=pokCounter[max])	max=i;
					}
				}
			}
			System.out.println(gPfC);
			
			int counterForAgents = 0;
			if(this.comps.size()==1){
				if(numOfAgents<=pokemons.size()){
					for(int i =0;i<pokemons.size();i++){				
						game.addAgent(pokemons.get(i).get_edge().getSrc());
						_ar.setAgents(Arena.getAgents(game.getAgents(), g));
						pokemons.get(i).setTaken(true);
						if((counterForAgents++) == numOfAgents) break;		
					}				
				}//The case when there are more agents than pokemons
				else
				{
					for(;counterForAgents<numOfAgents;counterForAgents++){
						if(counterForAgents<=pokemons.size()){
							game.addAgent(pokemons.get(counterForAgents).get_edge().getSrc());
							_ar.setAgents(Arena.getAgents(game.getAgents(), g));	
							pokemons.get(counterForAgents).setTaken(true);
						}else{
							game.addAgent(pokemons.get((counterForAgents%(pokemons.size()))).get_edge().getSrc());//instead of pokemon it sould be pokCounter
							_ar.setAgents(Arena.getAgents(game.getAgents(), g));
							pokemons.get((counterForAgents%(pokemons.size()))).setTaken(true);
						}
					}
				}
			}
			
			else if(this.comps.size() > 1)
			{
				if(numOfAgents == this.comps.size())
				{
					for(counterForAgents = 0; counterForAgents<numOfAgents; counterForAgents++) // <=?
					{
						int mx1 = findMaxIndex2(pokCounter);
						if(mx1!=-1) //That means we have an index that lead us to the max
						{
							// if the component with the max pokemons have one node then:
							if(comps.get(mx1).size()==1)
							{
								pokCounterTaken[mx1]=true;	//Assume we already put an agent in there to not calculate it
								counterForAgents--;
							}else if(comps.get(mx1).size()>1){//In the component there is more than one node
								
								for(int i =0; i < gPfC.get(mx1).size();i++){//for every pokemon in that component
									
									if(gPfC.get(mx1).get(i).isTaken()==false){//if the the pokemon[i] in the component is not taken by someone than
										
										game.addAgent(gPfC.get(mx1).get(i).get_edge().getSrc());
										_ar.setAgents(Arena.getAgents(game.getAgents(), g));
										gPfC.get(mx1).get(i).setTaken(true);
										pokCounter[mx1]--;
									}//else do nothing ****for now****
								}
							}			
						}
						else
						{
							setFree();
							mx1 = findMaxIndex(pokCounter);
							while(counterForAgents<numOfAgents){//
								
								if(pokCounter[mx1] > 0){								
									for (int i = 0; i < comps.get(mx1).size(); i++) {
										if(gPfC.get(mx1).get(i).isTaken()==false)
										{
											game.addAgent(gPfC.get(mx1).get(i).get_edge().getSrc());
											_ar.setAgents(Arena.getAgents(game.getAgents(), g));
											gPfC.get(mx1).get(i).setTaken(true);
											pokCounter[mx1]--;
											counterForAgents++;
										}
									}
								}
							}
						}
						
						
					}
				}else if(numOfAgents < this.comps.size())
				{
					counterForAgents=0;
					while(counterForAgents<numOfAgents)
					{
						int mx1 = findMaxIndex2(pokCounter);
						if(mx1>-1)
						{
							if(comps.get(mx1).size()<=1)
							{
								pokCounterTaken[mx1]=true;								
							}else
							{
								ArrayList<Pokemon> tmp = gPfC.get(mx1);						
								for(int i =0; i<tmp.size();i++)
								{
									if(tmp.get(i).isTaken()==false)
									{
										game.addAgent(tmp.get(i).get_edge().getSrc());
										_ar.setAgents(Arena.getAgents(game.getAgents(), g));
										tmp.get(i).setTaken(true);
										pokCounterTaken[mx1]=true;
										pokCounter[mx1]--;
										counterForAgents++;	
										break;
									}
								}							
							}
						}
						else//mx1==-1 that mean we already put in all the components that bigger than 1 an agent
						{
							setFree();
							mx1 = findMaxIndex2(pokCounter);
							while(counterForAgents<numOfAgents)
							{
								if(pokCounter[mx1] > 0)
								{
									for (int i = 0; i < comps.get(mx1).size(); i++) {
										if(gPfC.get(mx1).get(i).isTaken()==false){
											game.addAgent(gPfC.get(mx1).get(i).get_edge().getSrc());
											_ar.setAgents(Arena.getAgents(game.getAgents(), g));
											gPfC.get(mx1).get(i).setTaken(true);
											pokCounter[mx1]--;
											counterForAgents++;
										}
									}
								}else {//we have already put agents besides all the pokemons on the biggest component, then randomly add agents to the pokemons
									game.addAgent((gPfC.get(mx1).get(counterForAgents%pokCounter[mx1]).get_edge().getSrc()));
									_ar.setAgents(Arena.getAgents(game.getAgents(), g));
									counterForAgents++;						
								}
							}
						}
					}
				}else if(numOfAgents > this.comps.size())
				{
					counterForAgents=0;
					for(int i =0; i<this.comps.size();i++)
					{
						int mx1 = findMaxIndex2(pokCounter);
						if(mx1 > -1)	//That means we didn't put in all the components agents
						{
							if(comps.get(mx1).size()<=1)
							{
								pokCounterTaken[mx1]=true;								
							}else
							{
								for(int j = 0; j<gPfC.get(mx1).size();j++)
								{
									if(counterForAgents<numOfAgents)									
									{
										if(gPfC.get(mx1).get(j).isTaken()==false)
										{
											game.addAgent(gPfC.get(mx1).get(j).get_edge().getSrc());
											_ar.setAgents(Arena.getAgents(game.getAgents(), g));
											gPfC.get(mx1).get(j).setTaken(true);	
											pokCounterTaken[mx1] = true;
											pokCounter[mx1]--;
											counterForAgents++;
											break;
										}
									}	
								}
							}						
						}else {
							
							
						}
					}			
				}
					
				
				
				
				
				
				/*
				
				{
					for(int i=0; i<comps.size() ;i++){
						if(comps.get(i).size()>1){
							if(pokCounterTaken[i]==false){
								game.addAgent(pokemons.get(i).get_edge().getSrc());
								_ar.setAgents(Arena.getAgents(game.getAgents(), g));
								if((counterForAgents++) == numOfAgents) break;
							}							
						}
						if(counterForAgents < numOfAgents)
						{
							
						}
					}
				}else if(numOfAgents > this.comps.size()){
					
				}else if(numOfAgents < this.comps.size()){
					
				}
				
				*/
			}

			
			
			
			/*
			for(int a = 0;a<numOfAgents;a++) {
				int ind = a%pokemons.size();
				Pokemon c = pokemons.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge() .getSrc();}
				game.addAgent(nn);
				_ar.setAgents(Arena.getAgents(game.getAgents(), g));
				System.out.println(_ar.getAgents());
			}
			*/
		}
		catch (JSONException e) {e.printStackTrace();}
	}	
	

	
	private int findMaxIndex2(int[] arr){
		int ans = -1;
		for(int i=1;i<arr.length;i++)
		{
			if(arr[i]>=arr[ans] && pokCounterTaken[i] == false) {
				ans=i;
				}
		}return ans;
	}
	
	
	private int findMaxIndex(int[] arr){
		int ans = -1;
		for(int i=1;i<arr.length;i++)
		{
			if(arr[i]>arr[ans]) {ans=i;}
		}return ans;
	}
	
	
	private void setFree()
	{
		for(int i = 0; i<pokCounterTaken.length;i++)
		{
			pokCounterTaken[i]=false;
		}
	}
	/*
	private int[] removeMaxFromArr(int[] arr, int index) {
		int[] ans = new int[arr.length-1];
		for (int i = 0; i < arr.length; i++) 
		{
			if(i==index) 
			{
				while(i<arr.length-2)
				{
					ans[i]=arr[i+1];}
				}
			else ans[i]=arr[i+1];
			}
		return ans;
	}
	
	*/
	
}

