package server.managers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import server.dao.abstractDAO.DAOException;
import server.dao.abstractDAO.RoleDAO;
import server.dao.abstractDAO.UserDAO;
import server.factories.AbstractDAOFactory;
import javafx.util.Pair;
import server.models.Role;
import server.models.User;
import server.models.Right;
import ocsf.server.ConnectionToClient;
import server.tools.Security;


public class UserManager extends Manager{
    // ==================== //
    // ==== ATTRIBUTES ==== //
    // ==================== //
    private ArrayList<User> users;
    private UserDAO userDAO = AbstractDAOFactory.getFactory(SystemManager.db).getUserDAO();
    private RoleDAO roleDAO = AbstractDAOFactory.getFactory(SystemManager.db).getRoleDAO();
    private static UserManager manager = null;

    // ====================== //
    // ==== CONSTRUCTORS ==== //
    // ====================== //
    /**
     *  Singleton pattern
     */
    private UserManager() {
        users = new ArrayList<User>();
    }

    public static UserManager getManager() {
        if(manager == null) 
            manager = new UserManager();
        return manager;
    }

    // ================= //
    // ==== METHODS ==== //
    // ================= // 
    /**
     * Return a pair with the user and a code defining success or failure : 
     * 0 = success, 
     * -1 = wrong password, 
     * -2 = wrong pseudo
     * @param pseudo
     * @param password
     * @return Pair<User,Integer>
     */
    public Pair<User,Integer> login(String pseudo, String password) {
        User user = null;
        try {
            user = userDAO.getByPseudo(pseudo);
        } catch(DAOException exception) {
            System.out.println("Error login.");
        }
        if(user != null) {
            String hashPassword = password;
            String storedPassword = user.getPassword();
            if(hashPassword.equals(storedPassword)) {
                users.add(user);
                return new Pair<User,Integer>(user,0);
            }
            else {
                return new Pair<User,Integer>(null,-1);
            }
        }
        return new Pair<User,Integer>(null,-2);
    }
    
    public User loginAsGuest(String pseudo) {
    	if(pseudo.equals("")) {
    	    // TODO : give a random pseudo
    		pseudo = "Guest";
    	}
    	Role role = roleDAO.getByName("guest");
    	User user = new User(pseudo, role);
    	users.add(user);
    	return user;
    }
    
    public void getAllFamilyMembers(JSONObject json, ConnectionToClient client) {
    	ArrayList<User> users = null;

		try {
			users = userDAO.getAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		result.put("recipient", "user");
		result.put("action", "getAll");
		for (int i = 0; i < users.size(); i++) {
			User currentUser = users.get(i);
			JSONObject user = new JSONObject();
			if(currentUser.getRole().getId() != 1) {
				user.put("id", currentUser.getId());
				user.put("pseudo", currentUser.getPseudo());
				result.append("users", user);
			}
		}
		try {
			client.sendToClient(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void createFamilyMember(JSONObject json, ConnectionToClient client) {
    	
    	Role role= roleDAO.getById(2);
    	User user = new User( json.getString("pseudo"), json.getString("password"), role);
    	User newUser = null;
    	try {
			newUser = userDAO.create(user);
		} catch(Exception e) {
			e.printStackTrace();
		}
    	JSONObject result = new JSONObject();
    	result.put("recipient", "user");
		result.put("action", "createFamilyMember");
		result.put("user", newUser);
		result.put("id", newUser.getId());
		try {
			client.sendToClient(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Possible values for key "action":
     * <ul>
     * <li>login</li>
     * <li>loginAsGuest</li>
     * <li>getAll</li>
     * </ul>
     */
    @Override
    public void handleMessage(JSONObject json, ConnectionToClient client) {
        String action = json.getString("action");
        String pseudo;
        String password;
        switch(action) {
	        case "login":
	            pseudo = json.getString("pseudo");
	            password = json.getString("password");
	            Pair<User,Integer> user = login(pseudo, password);
	            if(user.getKey() != null) {
	                JSONObject token = new JSONObject();
	                token.put("id", user.getKey().getId());
	                token.put("type", user.getKey().getRole().getId());
	                
	                JSONObject result = new JSONObject();
	                result.put("result","success");
	                result.put("token", Security.encrypt(token.toString()));
	                try {
	                    client.sendToClient(result.toString());
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            else {
	                JSONObject result = new JSONObject();
	                if(user.getValue() == -1) {
	                    result.put("result","wrong_password");
	                }
	                else {
	                    result.put("result","wrong_pseudo");
	                }
	
	                try {
	                    client.sendToClient(result.toString());
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            break;
	        case "loginAsGuest":
	        	User tmp = null;
	        	try {
		        	tmp = loginAsGuest(json.getString("pseudo"));
				} catch (Exception e) {
					tmp = loginAsGuest("");
				}
	        	System.out.println(tmp);
                JSONObject token = new JSONObject();
                token.put("type", tmp.getRole().getId());
                token.put("pseudo", tmp.getPseudo());
                
                JSONObject result = new JSONObject();
                result.put("result","success");
                result.put("token", Security.encrypt(token.toString()));
                try {
                    client.sendToClient(result.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
	        	break;
	        case "getAll":  
	        	getAllFamilyMembers(json,client);
	        	break;
	        	
	        case "createFamilyMember":
	        	createFamilyMember(json, client);
	        	break;
        }
    }
}
