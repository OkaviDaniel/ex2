package api;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gameClient.CL_Agent;
import gameClient.CL_Pokemon;

public class GameServiece implements game_service
 {
	private static final long serialVersionUID = 1L;

	private DWGraph_DS g;
	private ArrayList<CL_Pokemon> pokemons;
	private ArrayList<CL_Agent> agents;
	
	
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
		return false;
	}

	@Override
	public long startGame() {
		return 0;
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public long stopGame() {
		return 0;
	}

	@Override
	public long chooseNextEdge(int id, int next_node) {
		return 0;
	}

	@Override
	public long timeToEnd() {
		return 0;
	}

	@Override
	public String move() {
		return null;
	}

	@Override
	public boolean login(long id) {
		return false;
	}

}
