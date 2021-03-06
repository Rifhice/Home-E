package server.managers;


import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import server.dao.abstractDAO.CommandDAO;
import server.factories.AbstractDAOFactory;
import server.models.Command;
import server.models.ComplexAction;
import ocsf.server.ConnectionToClient;

public class CommandManager extends Manager{

    private CommandDAO commandDAO =  AbstractDAOFactory.getFactory(0).getCommandDAO();
	private static CommandManager manager = null;
	// ====================== //
    // ==== CONSTRUCTORS ==== //
    // ====================== // 
    /**
     *  Singleton pattern
     */
	private CommandManager() {
		
	}
	
	public static CommandManager getManager() {
		if(manager == null) 
			manager = new CommandManager();
		return manager;
	}
	
	// ================= //
    // ==== METHODS ==== //
    // ================= // 
	public void createComplexCommand(JSONObject json,ConnectionToClient client) {
		
	}
	
	public void deleteComplexCommand(JSONObject json,ConnectionToClient client) {

	}
	
	public void updateComplexCommand(JSONObject json,ConnectionToClient client) {
		
	}

	public void getAllAtomicCommand(JSONObject json,ConnectionToClient client) {
		ArrayList<Command> command = commandDAO.getAll();
		JSONObject result = new JSONObject();
		result.put("recipient", "command");
		result.put("action", "getAllAtomic");
		for (int i = 0; i < command.size(); i++) {
			result.append("command", command.get(i).toJson());
		}
		try {
			client.sendToClient(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getAllComplexAction(JSONObject json,ConnectionToClient client) {
		ArrayList<ComplexAction> complexValue = AbstractDAOFactory.getFactory(SystemManager.db).getComplexActionDAO().getAll();
		JSONObject result = new JSONObject();
		result.put("recipient", "command");
		result.put("action", "getAllComplex");
		for (int i = 0; i < complexValue.size(); i++) {
			result.append("complexAction", complexValue.get(i).toJson());
		}
		try {
			client.sendToClient(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getAllCommand(JSONObject json,ConnectionToClient client) {
		ArrayList<Command> command = null;
		ArrayList<ComplexAction> complexValue = null;
		try {
			command = commandDAO.getAll();
			complexValue = AbstractDAOFactory.getFactory(SystemManager.db).getComplexActionDAO().getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		result.put("recipient", "command");
		result.put("action", "getAll");
		for (int i = 0; i < complexValue.size(); i++) {
			result.append("complexAction", complexValue.get(i).toJson());
		}
		for (int j = 0; j < command.size(); j++) {
			try {
				System.out.println(command.get(j).toJson());				
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.append("commands", command.get(j).toJson());
		}
		try {
			client.sendToClient(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Possible values for key "action":
     * <ul>
     * <li>getAll</li>
     * <li>getAllComplex</li>
     * <li>getAllAtomic</li>
     * <li>create</li>
     * <li>delete</li>
     * <li>update</li>
     * </ul>
     */
	@Override
	public void handleMessage(JSONObject json, ConnectionToClient client) {
		String action = json.getString("action");
        switch(action) {
	        case "getAll":
	        	getAllCommand(json,client);
	            break;
	        case "getAllComplex":
	        	getAllComplexAction(json,client);
	            break;
	        case "getAllAtomic":
	        	getAllAtomicCommand(json,client);
	            break;
	        case "create":
	        	createComplexCommand(json,client);
	            break;
	        case "delete":
	        	deleteComplexCommand(json,client);
	            break;
	        case "update":
	        	updateComplexCommand(json,client);
	            break;
        }
	}
}
