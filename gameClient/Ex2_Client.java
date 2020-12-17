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
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import api.node_data;

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
		int scenario_num = 23;
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
		
		/*
		//  Setting the components for each agent, we can do it on the init()
		String ls = game.getAgents();
		List<Agent> agents12 = Arena.getAgents(ls, gg);
		for(int i = 0; i<agents12.size();i++)
		{
			int src = agents12.get(i).getSrcNode();
			for(List<Integer> comp : comps)
			{
				if(comp.contains(src))
				{
					agents12.get(i).setComp(comp);
				}
			}
		}
		// end of the assigning 
		 * 
		 */
		
		game.startGame();
		_win.setTitle("Ex2 - OOP: "+game.toString());

		int ind=0;
		long dt=100;
		
		while(game.isRunning())
		{
			moveAgants(game, gg);
			try 
			{
				if(ind%1==0) 
				{
					_win.repaint();
					_win.update(_ar);
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
			//System.out.println("ID-------------->" + id);
			
			
			int dest = ag.getNextNode();//if next node == -1
			int src = ag.getSrcNode();// return the source node
			//System.out.println(src);
			//System.out.println("is: " +ag.getID());
			double v = ag.getValue();
			
			directed_weighted_graph g = _ar.getGraph();
			
//			for(edge_data a : g.getE(ag.getSrcNode()))
//			{
//				
//			}
			if(dest==-1) 
			{				
				if(ag.getCurrentPath()==null)
				{
					System.out.println("id: " + id + "empty");
				}
				else if(ag.getCurrentPath().size()>0)//There is a path for this current node to a pokemon
				{
					dest = nextNode(gg, src);
				}
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
			//System.out.println(gPfC);

			int counterForAgents = 0;	
			if(this.comps.size()==1){// If the number of components is equals to 1
				if(numOfAgents<=pokemons.size()){
					// If the number of agents <= number of pokemons
					for(int i =0;i<pokemons.size();i++){				
						game.addAgent(pokemons.get(i).get_edge().getSrc());//add each agent to the game near the pokemon[i]
						_ar.setAgents(Arena.getAgents(game.getAgents(), g));// updating the arena with the changes
						pokemons.get(i).setTaken(true);//The pokemon[i] is taken by agent so set pokmon[i].taken to be true
						//predator
						if((counterForAgents++) == numOfAgents) break;// if the number of the agent counter is equals to the number of agents then break
					}				
				}//else the number of agents > number of pokemons
				else
				{
					for(;counterForAgents<numOfAgents;counterForAgents++){
						//for each agent
						if(counterForAgents<=pokemons.size()){//If the number of the counter of agents < number of pokemons						
							game.addAgent(pokemons.get(counterForAgents).get_edge().getSrc());//add a new agent to the graph near the pokemon[counterForAgents]
							_ar.setAgents(Arena.getAgents(game.getAgents(), g));//	updating the arena with the changes
							pokemons.get(counterForAgents).setTaken(true);//The pokemon[counterForAgents] is taken by agent so set pokmon[counterForAgents].taken to be true
						}else{
							//predator
							game.addAgent(pokemons.get((counterForAgents%(pokemons.size()))).get_edge().getSrc());//same as above, just to not get out of index, we use the modulo operation 
							_ar.setAgents(Arena.getAgents(game.getAgents(), g));// same as above
							//pokemons.get((counterForAgents%(pokemons.size()))).setTaken(true);//same as above
						}
					}
				}
			}

			else if(this.comps.size() > 1){// The case when the number of components > 1 (The graph is not connected)

				if(numOfAgents == this.comps.size())// If the number of agents = number of components
				{
					for(counterForAgents = 0; counterForAgents<numOfAgents; counterForAgents++) // <=?				
					{//for each agent						
						int mx1 = findMaxIndex2(pokCounter); // find the the maximum (index) on pokCounter array - (that array contains the number of pokemons on each component)
						if(mx1!=-1){// if the index that returned is not equals to -1: That means we have an index that lead us to the component that contains the most pokemons that don't have any agent.										
							if(comps.get(mx1).size()==1){// if the component with the max pokemons have one node then:												
								pokCounterTaken[mx1]=true;	//We will pass that component because there is no point to put an agent on that component
								counterForAgents--;//reduce the counter because we didn't put any agent on the arena				
							}else if(comps.get(mx1).size()>1){//If the component have more than one node				
								for(int i =0; i < gPfC.get(mx1).size();i++){//for every pokemon in the component with the biggest num of pokemons, we need to find a "free pokemon"				
									if(gPfC.get(mx1).get(i).isTaken()==false){//if the pokemon[i] in the component is not taken by agent then

										//predator
										pokCounterTaken[mx1]=true;// we took a pokemon from that component
										game.addAgent(gPfC.get(mx1).get(i).get_edge().getSrc());//Add the agent to the game on the source vertex of the pokemon
										_ar.setAgents(Arena.getAgents(game.getAgents(), g));// update the arena						
										gPfC.get(mx1).get(i).setTaken(true);//set the pokemon[i] on the comps[mx1] to be taken
										pokCounter[mx1]--;// decrease the number of pokemons on the pokCounter[mx1]
										break;//and break because we found a pokemon on that component that we can add an agent close to it						
									}
								}
							}			
						}//else mx1 = -1, that means that we already put agents in all the "biggest" components and still we have more agents in our hand
						else
						{
							setFree(); // so change back the pokCounterTaken to normal
							mx1 = findMaxIndex(pokCounter); // find the new maximum of the pokCounter array
							while(counterForAgents<numOfAgents){//While the number of the counter of agents < number of agents

								if(pokCounter[mx1] > 0){//Maybe this if is irrelevant but if the number of pokemons on that component			
									for (int i = 0; i < comps.get(mx1).size(); i++) {// for every pokemon on that component
										if(gPfC.get(mx1).get(i).isTaken()==false){// if the pokemon is not taken add an agent near it
											game.addAgent(gPfC.get(mx1).get(i).get_edge().getSrc());
											_ar.setAgents(Arena.getAgents(game.getAgents(), g));
											gPfC.get(mx1).get(i).setTaken(true);
											pokCounter[mx1]--;
											counterForAgents++;
										}
									}
									while(counterForAgents<numOfAgents)//if we added for each pokemon on that component and agent and still have more agents, then add them to the pokemons randomly
									{
										game.addAgent((gPfC.get(mx1).get(counterForAgents%pokCounter[mx1]).get_edge().getSrc())); //add agent to a "randnom" pokemon
										_ar.setAgents(Arena.getAgents(game.getAgents(), g));
										counterForAgents++;
									}
								}//maybe we can put here the else just like the other
							}
						}			

					}
				}else if(numOfAgents < this.comps.size()){ //if the number of agents < number of the components

					counterForAgents=0;
					while(counterForAgents<numOfAgents){//while the counter of the agents < the number of agents

						int mx1 = findMaxIndex2(pokCounter);// mx1 is equals to the maximum index of the component that contains the most pokemons on the graph
						if(mx1>-1){//if there is a component with no agent
							if(comps.get(mx1).size()<=1){ // if the size of that component is equals to 1 - skip it
								pokCounterTaken[mx1]=true;//That's how we skip it				
							}else{
								//else mx1 is equals to specific index 
								ArrayList<Pokemon> tmp = gPfC.get(mx1);			//get the pokemons from that component			
								for(int i =0; i<tmp.size();i++){				// searching for pokemon that is not taken
									if(tmp.get(i).isTaken()==false){
										game.addAgent(tmp.get(i).get_edge().getSrc());	//add that pokemon to the graph
										_ar.setAgents(Arena.getAgents(game.getAgents(), g));// update the arena
										tmp.get(i).setTaken(true);// set the pokemon as taken
										pokCounterTaken[mx1]=true;//set the component as taken
										pokCounter[mx1]--;//reduce the number of pokemons on that component
										counterForAgents++;	//agents counter + 1
										break;//break to move to another pokemon and component
									}
								}							
							}
						}
						else//mx1==-1 that mean we already put in all the components (that bigger than 1) an agent
						{
							setFree();//change back the pokCounterTaken to normal
							mx1 = findMaxIndex(pokCounter);// find the new maximum on the pokCounter array
							while(counterForAgents<numOfAgents){
								if(pokCounter[mx1] > 0)	// if the number of the pokemons on the current component is greater than zero
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
				}else if(numOfAgents > this.comps.size()){ // if the number of agents > the number of components

					counterForAgents=0;
					for(int i =0; i<this.comps.size();i++) // for every component
					{
						int mx1 = findMaxIndex2(pokCounter);// find the maximum index of the component that have the most pokemons
						if(mx1 > -1)//if there is a component with no agent
						{
							if(comps.get(mx1).size()<=1){// if the size of that component is 1 then skip it
								pokCounterTaken[mx1]=true;								
							}else{
								for(int j = 0; j<gPfC.get(mx1).size();j++){ // find a free pokemon on that component and add an agent to the game/arena
									if(counterForAgents<numOfAgents){//maybe this is irrelevant
										if(gPfC.get(mx1).get(j).isTaken()==false){
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
						}else{//mx1==-1 that mean we already put in all the components (that bigger than 1) an agent
							setFree();//change back the pokCounterTaken to normal
							mx1 = findMaxIndex(pokCounter);// find the new maximum on the pokCounter array
							while(counterForAgents<numOfAgents)//while the counter < num of agents
							{
								if(pokCounter[mx1]>0) // if the number of pokemons on that component is larger than zero
								{
									for(int k = 0; k<gPfC.get(mx1).size();k++) // find a free pokemon and add a new agent
									{
										if(gPfC.get(mx1).get(k).isTaken()==false)
										{											 
											game.addAgent(gPfC.get(mx1).get(k).get_edge().getSrc());
		/******************************/	_ar.setAgents(Arena.getAgents(game.getAgents(), g));
											List<Agent> tmp = Arena.getAgents(game.getAgents(), g);
											Agent a = tmp.get(tmp.size()-1);
		/******************************/	a.set_curr_fruit(gPfC.get(mx1).get(k));
											gPfC.get(mx1).get(k).setTaken(true);									
											pokCounter[mx1]--;
											counterForAgents++;
											break;
										}
									}
								}else {// the number of agents on that component is equals to the number of pokemons on that component
									game.addAgent((gPfC.get(mx1).get(counterForAgents%pokCounter[mx1]).get_edge().getSrc()));
									_ar.setAgents(Arena.getAgents(game.getAgents(), g));
									counterForAgents++;
								}
							}

						}
					}			
				}
			}
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

	
}

