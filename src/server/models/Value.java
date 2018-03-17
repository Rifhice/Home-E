package server.models;


/**
 * A value is an information the actuator needs to perform an action or modify its state
 * It allows the server to send the right action to the actuators.
 * @author Jade
 */
public abstract class Value {
	    
	// ==================== //
	// ==== ATTRIBUTES ==== //
	// ==================== //
	private int id;
	private String name;   

	// ====================== //
	// ==== CONSTRUCTORS ==== //
	// ====================== //
	public Value() {}
	    
	public Value(String name) {
		this.name = name;
	}
	
	public Value(int id, String name) {
	    this.id = id;
        this.name = name;
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
    
    // ===========================================
	
		

}
