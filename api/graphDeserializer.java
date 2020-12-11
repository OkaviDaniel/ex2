package api;

import java.lang.reflect.Type;
import com.google.gson.JsonArray;
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
		DWGraph_DS g = new DWGraph_DS();
		
		//This transfer the json string/file to a json object
		JsonObject jsonObject = json.getAsJsonObject();
		
		//This json array include all the nodes on the graph
		JsonArray arrNodesObject = jsonObject.get("Nodes").getAsJsonArray();
		for(int i =0; i<arrNodesObject.size();i++)
		{
			//Get the node json object
			JsonElement t1 = arrNodesObject.get(i).getAsJsonObject();
			//Get the node id and create a new node
			int k = t1.getAsJsonObject().get("id").getAsInt();
			NodeData tmp = new NodeData(k);
			
			//Get the geo location
			String geoTmp = t1.getAsJsonObject().get("pos").getAsString();
			String[] geoTmp2 = geoTmp.split(",");
			double x = Double.parseDouble(geoTmp2[0]);
			double y = Double.parseDouble(geoTmp2[1]);
			double z = Double.parseDouble(geoTmp2[2]);
			tmp.setLocation(new GeoLocation(x, y, z));
			g.addNode(tmp);	
		}
		
		JsonArray arrEdgesObject2 = jsonObject.get("Edges").getAsJsonArray();
		for(int i=0; i<arrEdgesObject2.size();i++)
		{		
			JsonElement t2 = arrEdgesObject2.get(i).getAsJsonObject();			
			int src = t2.getAsJsonObject().get("src").getAsInt();
			int dest = t2.getAsJsonObject().get("dest").getAsInt();
			double weight = t2.getAsJsonObject().get("w").getAsDouble();
			g.connect(src, dest, weight);
		}
	
		return g;
	}

	
}
