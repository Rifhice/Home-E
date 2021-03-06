package sensor;


import java.util.Observable;

import org.json.JSONObject;


/**
 * An environment variable is an interesting value  for the server, related to the environment. 
 * It allows the server to send the right action to the actuators.
 * @author Clm-Roig
 */
public class EnvironmentVariable extends Observable{
    
    // ==================== //
    // ==== ATTRIBUTES ==== //
    // ==================== //
    private int id;
	private String name;
	private String unit;
	private String description;
	
	// Attributes from other tables
	private Sensor sensor;
	private Value value;

    // ====================== //
    // ==== CONSTRUCTORS ==== //
    // ====================== //
    public EnvironmentVariable() {}
    
	public EnvironmentVariable(String name, String description, String unit) {
		this.name = name;
		this.unit = unit;
		this.description = description;
	}
	
	public EnvironmentVariable(int id, String name, String description, String unit) {
	    this.id = id;
        this.name = name;
        this.unit = unit;
        this.description = description;
    }
	
	public EnvironmentVariable(int id, String name, String description, String unit, Sensor sensor) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.sensor = sensor;
    }
	
	public EnvironmentVariable(int id, String name, String description, String unit, Value value) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.value = value;
    }
	
	public EnvironmentVariable(String name, String description, String unit, Value value) {
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.value = value;
    }
	
	public EnvironmentVariable(int id, String name, String description, String unit, Sensor sensor, Value value) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.sensor = sensor;
        this.value = value;
    }

    // ================= //
    // ==== METHODS ==== //
    // ================= //	
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    
    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
    
    public void changeValue(String val) {
    	if(value instanceof ContinuousValue) {
    		((ContinuousValue) value).setCurrentValue(Double.parseDouble(val));
    		Sensor.updateVariable(this);
    	}
    	else {
    		((DiscreteValue) value).setCurrentValue(val);
    		Sensor.updateVariable(this);
    	}
    }
    
    // =================================================
    public String toString() {
        return id + ": " + name + description;
    }
    
    public JSONObject toJson() {
    	JSONObject result = new JSONObject();
    	result.put("id", id);
    	result.put("name", name);
    	result.put("unit", unit);
    	result.put("description", description);
    	if(value != null) {
    		result.put("value", value.toJson());
    	}
    	return result;
    }

}
