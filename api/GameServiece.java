package api;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gameClient.Agent;
import gameClient.Pokemon;

public class GameServiece implements game_service
 {
	private static final long serialVersionUID = 1L;

	private DWGraph_DS g;
	private ArrayList<Pokemon> pokemons;
	private ArrayList<Agent> agents;
	
	
	public GameServiece()
	{
		pokemons = new ArrayList<Pokemon>();
		agents   = new ArrayList<Agent>();
	}
	
	@Override
	public String getGraph() 
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(g);
		return json;
	}

	@Override
	public String getPokemons() 
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(pokemons);
		return json;
	}

	@Override
	public String getAgents()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(agents);
		return json;
	}

	@Override
	public boolean addAgent(int start_node) 
	{
		//addAgent to the Arena/Graph
		return false;
	}

	@Override
	public long startGame() 
	{
		//Starting the game
		return 0;
	}

	@Override
	public boolean isRunning()
	{
		//Thread isAlive()?
		return false;
	}

	@Override
	public long stopGame() 
	{
		// Stop the Thread or something
		return 0;
	}

	@Override
	public long chooseNextEdge(int id, int next_node) 
	{
		//Must think of a good algorithm for moving
		// id=agent to the next node
		return 0;
	}

	@Override
	public long timeToEnd() 
	{
		// Show on the screen the time until the game is ending
		return 0;
	}

	@Override
	public String move() 
	{
		//Move all the agents on the graph one move 
		return null;
	}

	@Override
	public boolean login(long id) 
	{
		//Send the id to the server.
		return false;
	}

}
