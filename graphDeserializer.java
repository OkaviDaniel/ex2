package api;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class graphDeserializer implements JsonDeserializer<DWGraph_DS> {

	@Override
	public DWGraph_DS deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException 
	{		
		//System.out.println("hello?");
		DWGraph_DS g = new DWGraph_DS();
		
		//This transfer the json string/file to a json object
		JsonObject jsonObject = json.getAsJsonObject();
		
		
		//This Json object contains all the nodes on the graph.
		JsonObject nInGJsonObject = jsonObject.get("nodesInGraph").getAsJsonObject();
		for(Entry<String, JsonElement> set : nInGJsonObject.entrySet())
		{
			Integer k = Integer.parseInt(set.getKey());
			NodeData tmp = new NodeData(k);
			
		//	Getting into the data of the node
			JsonElement jsonVauluElement = set.getValue();
			
			
			//Getting a json element of the geoL data
			JsonElement p1 = jsonVauluElement.getAsJsonObject().get("geoL");
			double x1=0,y1=0,z1=0;
			x1 = p1.getAsJsonObject().get("x").getAsDouble();
			y1 = p1.getAsJsonObject().get("y").getAsDouble();
			z1 = p1.getAsJsonObject().get("z").getAsDouble();
			tmp.setLocation(new GeoLocation(x1, y1, z1));
			
		//	Getting the weight data
			tmp.setWeight(jsonVauluElement.getAsJsonObject().get("weight").getAsDouble());
			
			//Getting the info data
			tmp.setInfo(jsonVauluElement.getAsJsonObject().get("info").getAsString());
			
		//	Getting the tag element
			tmp.setTag(jsonVauluElement.getAsJsonObject().get("tag").getAsInt());
			g.addNode(tmp);
		}				
		
		// Now getting the edges.
		nInGJsonObject = jsonObject.get("edges").getAsJsonObject();
		// For every key on the edges HashMap
		for(Entry<String, JsonElement> set : nInGJsonObject.entrySet())
		{
			int topKey = Integer.parseInt(set.getKey());
			JsonObject jsonVauluElement = set.getValue().getAsJsonObject();
			for(Entry<String, JsonElement> set2 : jsonVauluElement.entrySet())
			{
				EdgeData tmp = new EdgeData();
				tmp.setSrc(topKey);
				tmp.setDest(Integer.parseInt(set2.getKey()));
				tmp.setTag(set2.getValue().getAsJsonObject().get("tag").getAsInt());
				tmp.setWeight(set2.getValue().getAsJsonObject().get("weight").getAsDouble());
				tmp.setInfo(set2.getValue().getAsJsonObject().get("info").getAsString());
				g.connect(tmp.getSrc(), tmp.getDest(), tmp.getWeight());
			}					
		}
		
		
		return g;
	}

	
}
