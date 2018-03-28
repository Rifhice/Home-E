package sensor;

import java.util.ArrayList;

import org.json.JSONObject;

public class DiscreteValue extends Variable {

    // ==================== //
    // ==== ATTRIBUTES ==== //
    // ==================== //
    private ArrayList<String> possibleValues;
    private String currentValue;

    // ====================== //
    // ==== CONSTRUCTORS ==== //
    // ====================== //
    public DiscreteValue(String name, String description, String unity, ArrayList<String> values, String currentValue) {
        super(name,description,unity);
        this.possibleValues = values;
        this.currentValue = currentValue;
    }

	public void setValue(Object newValue) {
		if(newValue instanceof String) {
			currentValue = (String) newValue;
		}
		Sensor.updateVariable(this);
	}

	public String getCurrentValue() {
		return currentValue;
	}
	
	@Override
	public JSONObject getJson() {
		JSONObject result = new JSONObject();
		result.put("id", id);
		result.put("type", "discrete");
		result.put("name", name);
		result.put("description", description);
		result.put("unit", unit);
		for (int i = 0; i < possibleValues.size(); i++) {
			result.append("possiblevalues", possibleValues.get(i));
		}
		result.put("currentvalue", currentValue);
		return result;
	}
	
}