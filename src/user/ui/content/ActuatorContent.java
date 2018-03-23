package user.ui.content;

import javafx.scene.control.Label;
import user.ui.componentJavaFX.MyPane;
import user.ui.componentJavaFX.MyRectangle;

public class ActuatorContent extends Content {

	private static ActuatorContent content = null;
	
	private MyRectangle actuatorsListBounds = new MyRectangle(0f, 0f, 0.2f, 1.0f);
	private MyRectangle currentActuatorBounds = new MyRectangle(0.2f, 0f, 0.4f, 1.0f);
    private MyRectangle latestActionsBounds = new MyRectangle(0.6f, 0f, 0.4f, 1.0f);


	private ActuatorContent() {
	    MyPane actuatorsListPane = new MyPane(actuatorsListBounds.computeBounds(width, height));
	    actuatorsListPane.setStyle("-fx-background-color: rgb(255,0,0)");
	    MyPane currentActuatorPane = new MyPane(currentActuatorBounds.computeBounds(width, height));
	    currentActuatorPane.setStyle("-fx-background-color: rgb(0,0,255)");
	    MyPane latestActionsPane = new MyPane(latestActionsBounds.computeBounds(width, height));
	    latestActionsPane.setStyle("-fx-background-color: rgb(0,255,0)");

	    this.getChildren().add(actuatorsListPane);
	    this.getChildren().add(currentActuatorPane);
	    this.getChildren().add(latestActionsPane);
	    actuatorsListPane.getChildren().add(new Label("Actuators List"));
	    currentActuatorPane.getChildren().add(new Label("Actuator selected"));
	    latestActionsPane.getChildren().add(new Label("Latest Actions"));
	}

	public static Content getInstance() {
		// TODO Auto-generated method stub
		if(content == null) {
			content = new ActuatorContent();
		}
		return content;
	}
	
	@Override
	public void handleMessage(Object message) {
		
	}
}
