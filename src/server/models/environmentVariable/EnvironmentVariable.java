package server.models.environmentVariable;


import java.util.Observable;

import server.models.Sensor;

/**
 * An environment variable is an interesting value  for the server, related to the environment. 
 * It allows the server to send the right action to the actuators.
 * @author Clm-Roig
 */
public abstract class EnvironmentVariable extends Observable{
    
    // ==================== //
    // ==== ATTRIBUTES ==== //
    // ==================== //
    private int id;
	private String name;
	private String unit;
	private String description;
	
	// Attributes from other tables
	private Sensor sensor;

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
    
    // =================================================
    
    public String toString() {
        return id + ": " + name;
    }

    public abstract boolean isEqual(Object value);
	public abstract boolean isNotEqual(Object value);
	public abstract boolean isSuperior(Object value);
	public abstract boolean isInferior(Object value);
	public abstract boolean isSuperiorOrEqual(Object value);
	public abstract boolean isInferiorOrEqual(Object value);
}