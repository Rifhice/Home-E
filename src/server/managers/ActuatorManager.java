package server.managers;


import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import server.factories.AbstractDAOFactory;
import server.models.Actuator;
import server.models.Argument;
import server.models.Command;
import server.models.ContinuousArgument;
import server.models.ContinuousEnvironmentVariable;
import server.models.DiscreteArgument;
import server.models.DiscreteEnvironmentVariable;
import server.models.EnvironmentVariable;
import server.models.Sensor;
import ocsf.server.ConnectionToClient;

public class ActuatorManager extends Manager {
	
	private ArrayList<Actuator> actuators;
	
	private static ActuatorManager manager = null;
	
	private ActuatorManager() {
		actuators = new ArrayList<Actuator>();
	}
	
	public static ActuatorManager getManager() {
		if(manager == null) 
			manager = new ActuatorManager();
		return manager;
	}
	
	public void registerActuatorToTheSystem(JSONObject json) {
		// TODO
	}
	
	public ArrayList<Actuator> getActuators(){
		return actuators;
	}
	
	public Actuator getActuatorById(String id) {
		for (int i = 0; i < actuators.size(); i++) {
			if(Integer.toString(actuators.get(i).getId()).equals(id)) {
				return actuators.get(i);
			}
		}
		return null;
	}

	public static Actuator getActuatorFromJson(JSONObject jsonToParse) {
		try {
			String name = jsonToParse.getString("name");
			String description = jsonToParse.getString("description");
			ArrayList<Command> commands = new ArrayList<Command>();
			JSONArray arr = jsonToParse.getJSONArray("commands");
			
			for (int i = 0; i < arr.length(); i++){
				
				JSONObject object = arr.getJSONObject(i);
				String nameCommand = object.getString("name");
				String descriptionCommand = object.getString("description");
				String keyCommand = object.getString("key");
				
				JSONArray arrArg = object.getJSONArray("args");
				ArrayList<Argument> arguments = new ArrayList<Argument>();
				for (int j = 0; j < arrArg.length(); j++){
					
					JSONObject current = arrArg.getJSONObject(j);
					System.out.println(current.toString());
					if(current.getString("type").equals("continuous")) {
						arguments.add(new ContinuousArgument(current.getString("name"), current.getString("description"), current.getDouble("valuemin"), current.getDouble("valuemax"), current.getDouble("precision")));
					}
					else if(current.getString("type").equals("discrete")){
						ArrayList<String> values = new ArrayList<String>();
						JSONArray valuesArray = current.getJSONArray("values");
						for (int z = 0; z < valuesArray.length(); z++) {
							values.add(valuesArray.getString(z));
						}
						arguments.add(new DiscreteArgument(current.getString("name"), current.getString("description"), values));
					}
				}
				commands.add(new Command(nameCommand, descriptionCommand, keyCommand, arguments));
			}
			return new Actuator(name, description, commands);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void registerActuatorToTheSystem(JSONObject json,ConnectionToClient client) {
		Actuator actuator = getActuatorFromJson(json);
		Actuator create = AbstractDAOFactory.getFactory(AbstractDAOFactory.SQLITE_DAO_FACTORY).getActuatorDAO().create(actuator);
		JSONObject result = new JSONObject();
		if(create != null) {
			actuators.add(actuator);
			json.put("result", "success");
			json.put("id", create.getId());
		}
		else {
			json.put("result", "failure");
		}
		try {
			client.sendToClient(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(actuator + "\nAdded to the system !");
	}
	
	@Override
	public void handleMessage(JSONObject json, ConnectionToClient client) {
		String verb = json.getString("verb");
		switch (verb) {
		case "post":
			registerActuatorToTheSystem(json,client);
			break;
		default:
			break;
		}
	}
}
