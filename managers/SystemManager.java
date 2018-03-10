package managers;


import java.io.IOException;

import org.json.JSONObject;

import factories.AbstractDAOFactory;
import models.User;

import server.EchoServer;

public class SystemManager extends Manager{
	private final int USER_SERVER_PORT = 6543;
	private final int SENSOR_SERVER_PORT = 5432;
	private final int ACTUATOR_SERVER_PORT = 4321;
	
	private static SystemManager manager = null;
	
	private AmbienceManager ambienceManager;
	private BehaviourManager behaviourManager;
	private UserManager userManager;
	private SensorManager sensorManager;
	private ActuatorManager actuatorManager;
	
	private EchoServer userServer;
	private EchoServer sensorServer;
	private EchoServer actuatorServer;
	
	public static final int db = AbstractDAOFactory.SQLITE_DAO_FACTORY;
	
	private SystemManager() {
		ambienceManager = AmbienceManager.getManager();
		behaviourManager = BehaviourManager.getManager();
		sensorManager = SensorManager.getManager();
		actuatorManager = ActuatorManager.getManager();
		userManager = UserManager.getManager();
		
		userServer = new EchoServer(USER_SERVER_PORT,this);
		try {
			userServer.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//sensorServer = new EchoServer(SENSOR_SERVER_PORT,sensorManager);
		//actuatorServer = new EchoServer(ACTUATOR_SERVER_PORT,actuatorManager);
	}
	
	public static SystemManager getManager() {
		if(manager == null) 
			manager = new SystemManager();
		return manager;
	}
	
	public void handleMessage(JSONObject json) {
		System.out.println(json);
		String recipient = json.getString("recipient");
		switch (recipient) {
		case "ambience":
			ambienceMessage(json);
			break;
		case "behaviour":
			behaviourMessage(json);
			break;
		case "sensor":
			sensorMessage(json);
			break;
		case "actuator":
			actuatorMessage(json);
			break;
		case "user":
			userMessage(json);
			break;
		default:
			break;
		}
	}
	
	private void userMessage(JSONObject json) {
		String action = json.getString("action");
		switch(action) {
			case "login":
				String pseudo = json.getString("pseudo");
				String password = json.getString("password");
				User user = userManager.login(pseudo, password);
				break;
			}
	}
	
	private void ambienceMessage(JSONObject json) {
		String action = json.getString("action");
		switch (action) {
		case "create":
			ambienceManager.createAmbience(json);
			break;
		default:
			break;
		}
	}
	
	private void behaviourMessage(JSONObject json) {
		String action = json.getString("action");
		switch (action) {
		case "create":
			behaviourManager.createBehaviour(json);
			break;
		default:
			break;
		}
	}
	
	private void sensorMessage(JSONObject json) {
		sensorManager.handleMessage(json);
	}
	
	private void actuatorMessage(JSONObject json) {
		actuatorManager.handleMessage(json);
	}
	
	public static void main(String[] args) {
		SystemManager systemManager = new SystemManager();
	}
}

/*
ContinuousEnvironmentVariable time = new ContinuousEnvironmentVariable("Le temps", "C'est le super temps", "minutes", 0, 60, 1, 20);
DiscreteEnvironmentVariable temperature = new DiscreteEnvironmentVariable("La temperature", "Savoir si il fait chaud ou pas", "", new ArrayList<String>(Arrays.asList("Froid","Normal","Chaud")), "Chaud");

Block b1 = new Block(time,30,"==");
Block b2 = new Block(temperature,"Froid","==");

ArrayList<Evaluable> evaluable = new ArrayList<Evaluable>();		
ArrayList<String> operators = new ArrayList<String>();

evaluable.add(b1);
operators.add("&&");
evaluable.add(b2);

Expression expression = new Expression(evaluable,operators);
System.out.println(expression + " => " + expression.evaluate());


ArrayList<Evaluable> evaluable2 = new ArrayList<Evaluable>();		
ArrayList<String> operators2 = new ArrayList<String>();

Block b3 = new Block(time,55,"==");

evaluable2.add(expression);
operators2.add("||");
evaluable2.add(b3);

Expression expression2 = new Expression(evaluable2,operators2);
System.out.println(expression2 + " => " + expression2.evaluate());

Behaviour behaviour = new Behaviour(expression2, null);

while(true) {
    Scanner in = new Scanner(System.in);
    System.out.print("New value for the time (current : " + time		.getCurrentValue() + "): ");
    double timeValue = in.nextDouble();
    time.setCurrentValue(timeValue);
    System.out.print("New value for the temperature (current : " + temperature.getCurrentValue() + ") : ");
    String tempValue = in.next();
    temperature.setCurrentValue(tempValue);
}

JSONObject json = new JSONObject("{\r\n" + 
		"  recipient: 'externalActor',\r\n" + 
		"  action: 'register',\r\n" + 
		"  type: 'sensor',\r\n" + 
		"  name: 'Des bails',\r\n" + 
		"  description: 'Ce capteur signale des trucs.',\r\n" + 
		"  variables: [{\r\n" + 
		"    type: 'continuous',\r\n" + 
		"    name: \"Le temps\",\r\n" + 
		"    description: \"C'est le super temps\",\r\n" + 
		"    unity: \"minutes\",\r\n" + 
		"    valuemin: 0,\r\n" + 
		"    valuemax: 60,\r\n" + 
		"    precision: 1,\r\n" + 
		"    currentvalue: 20\r\n" + 
		"  }, {\r\n" + 
		"    type: 'discrete',\r\n" + 
		"    name: \"La temperature\",\r\n" + 
		"    description: \"Savoir si il fait chaud ou pas\",\r\n" + 
		"    unity: \"�\",\r\n" + 
		"    values: [\"Froid\", \"Normal\", \"Chaud\"],\r\n" + 
		"    currentvalue: \"Froid\"\r\n" + 
		"  }]\r\n" + 
		"}\r\n" + 
		"");
SystemManager manager = SystemManager.getManager();
manager.handleMessage(json);
json = new JSONObject("{\r\n" + 
		"  recipient: \"behaviour\",\r\n" + 
		"  action: \"create\",\r\n" + 
		"  expression: {\r\n" + 
		"    type: 'expression',\r\n" + 
		"    evaluable: [{\r\n" + 
		"      type: 'expression',\r\n" + 
		"      evaluable: [{\r\n" + 
		"        type: 'block',\r\n" + 
		"        variable: '0',\r\n" + 
		"        value: 30,\r\n" + 
		"        valueType: 'Double',\r\n" + 
		"        operators: '=='\r\n" + 
		"      }, {\r\n" + 
		"        type: 'block',\r\n" + 
		"        variable: '0',\r\n" + 
		"        value: 'Froid',\r\n" + 
		"        valueType: 'String',\r\n" + 
		"        operators: '=='\r\n" + 
		"      }],\r\n" + 
		"      operators: ['&&']\r\n" + 
		"    }, {\r\n" + 
		"      type: 'block',\r\n" + 
		"      variable: '1',\r\n" + 
		"      value: 55,\r\n" + 
		"      valueType: 'Double',\r\n" + 
		"      operators: '=='\r\n" + 
		"    }],\r\n" + 
		"    operators: [\"||\"]\r\n" + 
		"  },\r\n" + 
		"  command: \"lol\"\r\n" + 
		"}\r\n" + 
		"");
manager.handleMessage(json);


json = new JSONObject("{\r\n" + 
		"  recipient: 'externalActor',\r\n" + 
		"  type: \"sensor\",\r\n" + 
		"  id: \"0\",\r\n" + 
		"  action: \"setName\",\r\n" + 
		"  name: \"Ca marche\"\r\n" + 
		"}");
manager.handleMessage(json);
*/
