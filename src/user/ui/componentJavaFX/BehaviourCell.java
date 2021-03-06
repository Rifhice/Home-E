package user.ui.componentJavaFX;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import user.models.Behaviour;
import user.tools.GraphicalCharter;

public class BehaviourCell extends MyPane {
	
	private static Image redArrow = new Image("file:asset/images/redArrow.png");
	private static Image greenArrow = new Image("file:asset/images/greenArrow.png");
	
	MyRectangle labelButtonBounds;
	MyRectangle modifyButtonBounds;
	
	MyButtonFX labelButton = null;
	MyButtonImage modifyButton = null;
	
	Behaviour behaviour;
	boolean activated;
	
	Image arrow;
	
	EventHandler<ActionEvent> infoEvent;
	EventHandler<ActionEvent> modifyStateEvent;
	
	public BehaviourCell(Behaviour behaviour,Double width, Double height,EventHandler<ActionEvent> modifyStateEvent, EventHandler<ActionEvent> infoEvent, boolean activated) {
		super(width, height);
		this.behaviour = behaviour;
		this.activated = activated;
		this.infoEvent = infoEvent;
		this.modifyStateEvent = modifyStateEvent;
		updateCell();
	}
	
	public int getCellId() {
		return behaviour.getId();
	}
	
	public boolean getState() {
		return this.activated;
	}
	
	public void changeState() {
		this.activated = !this.activated;
		this.updateCell();
	}
	
	public void updateCell() {
		if(activated) {
			labelButtonBounds = new MyRectangle(0.2f, 0f, 0.76f, 1f);
			modifyButtonBounds = new MyRectangle(0f, 0f, 0.2f, 1f);
			arrow = BehaviourCell.redArrow;
		} else {
			labelButtonBounds = new MyRectangle(0f, 0f, 0.76f, 1f);
			modifyButtonBounds = new MyRectangle(0.76f, 0f, 0.2f, 1f);
			arrow = BehaviourCell.greenArrow;
		}
		if(labelButton != null) {
			this.getChildren().remove(labelButton);
		}
		if(modifyButton != null) {
			this.getChildren().remove(modifyButton);
		}
		labelButton = new MyButtonFX(behaviour.getName(), behaviour.getId(), labelButtonBounds.computeBounds(getPrefWidth(), getPrefHeight()), infoEvent);
		modifyButton = new MyButtonImage(arrow, modifyButtonBounds.computeBounds(getPrefWidth(), getPrefHeight()), modifyStateEvent, behaviour.getId());
		this.getChildren().add(labelButton);
		this.getChildren().add(modifyButton);
		labelButton.setFont(Font.font(GraphicalCharter.FONT,GraphicalCharter.FONT_SIZE));
	}
	
	public Behaviour getBehaviour() {
		return this.behaviour;
	}
}
