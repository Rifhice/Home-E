package user.ui.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import user.ClientFX;
import user.models.Ambience;
import user.models.Behaviour;
import user.ui.componentJavaFX.BehaviourCell;
import user.ui.componentJavaFX.MyButtonFX;
import user.ui.componentJavaFX.MyButtonImage;
import user.ui.componentJavaFX.MyLabel;
import user.ui.componentJavaFX.MyPane;
import user.ui.componentJavaFX.MyRectangle;
import user.ui.componentJavaFX.MyScrollPane;
import user.ui.componentJavaFX.MyTextFieldFX;

public class AmbiencesContent extends Content {

	private static AmbiencesContent content = null;

	private MyRectangle ambiencesBounds = new MyRectangle(0.6f, 0F, 0.4f, 1f);
	private MyRectangle behavioursNotChosenBounds = new MyRectangle(0f, 0.15F, 0.3f, 0.85f);
	private MyRectangle behavioursChosenBounds = new MyRectangle(0.3f, 0.15F, 0.3f, 0.85f);

	private MyRectangle behavioursNotChosenBoundsModif = new MyRectangle(0f, 0.15F, 0.3f, 0.75f);
	private MyRectangle behavioursChosenBoundsModif = new MyRectangle(0.3f, 0.15F, 0.3f, 0.75f);


	
	/*
	 * The display to create a new ambience
	 */
	private MyRectangle topAmbienceBounds = new MyRectangle(0f, 0F, 0.6f, 0.15f);
	private MyRectangle newAmbienceLabelBounds = new MyRectangle(0.17f, 0.35f, 0.3f, 0.3f);
	private MyRectangle newAmbienceTextFieldBounds = new MyRectangle(0.55f, 0.4f, 0.3f, 0.2f);
	private MyRectangle newAmbienceButtonBounds = new MyRectangle(0.9f, 0.35f, 0.05f, 0.05f);
	
	/*
	 * The display to modify an ambience
	 */
	private MyRectangle modifyAmbienceBounds = new MyRectangle(0f, 0.90f, 0.6f, 0.1f);
	private MyRectangle nameAmbienceBounds = new MyRectangle(0.2f, 0.35f, 0.3f, 0.3f);
	private MyRectangle validateAmbienceModificationBounds = new MyRectangle(0.75f, 0.25f, 0.05f, 0.05f);
	private MyRectangle cancelAmbienceModificationBounds = new MyRectangle(0.95f, 0f, 0.05f, 0.05f);
	private MyLabel modifyAmbienceLabel;
	private MyButtonImage validateAmbienceModification;
	private MyButtonImage cancelAmbienceModification;

	private MyPane newAmbiencePane;
	private MyPane modifyAmbiencePane;
	private MyScrollPane ambiencesScrollPane;
	private MyScrollPane behavioursNotChosenScrollPane;
	private MyScrollPane behavioursChosenScrollPane;
	
	private List<BehaviourCell> behaviourCells = new ArrayList<BehaviourCell>();

	private GridPane notChosenBehavioursList = new GridPane();
	private GridPane chosenBehavioursList = new GridPane();
	private GridPane ambiencesList = new GridPane();
	
	private List<Behaviour> notSelectedBehaviours;
	private List<Behaviour> selectedBehaviours;
	private List<Ambience> ambiences;
	
	private int NB_OF_BEHAVIOURS_DISPLAYED = 10;
	private int NB_OF_AMBIENCES_DISPLAYED = 8;
	
	private int currentAmbienceSelected = -1;

	private static Image checkImage = new Image("file:asset/images/check.png");
	private static Image removeImage = new Image("file:asset/images/remove.png");
	
	private AmbiencesContent() {
		newAmbiencePane = new MyPane(topAmbienceBounds.computeBounds(width, height));
		modifyAmbiencePane = new MyPane(modifyAmbienceBounds.computeBounds(width, height));
		modifyAmbiencePane.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

		modifyAmbienceLabel = new MyLabel("", nameAmbienceBounds.computeBounds(modifyAmbiencePane.getPrefWidth(), modifyAmbiencePane.getPrefHeight()), 1f);
		validateAmbienceModification = new MyButtonImage(checkImage, validateAmbienceModificationBounds.computeBounds(modifyAmbiencePane.getPrefWidth(), modifyAmbiencePane.getPrefHeight()), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		cancelAmbienceModification = new MyButtonImage(removeImage, cancelAmbienceModificationBounds.computeBounds(modifyAmbiencePane.getPrefWidth(), modifyAmbiencePane.getPrefHeight()), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				behavioursNotChosenScrollPane.changeBounds(behavioursNotChosenBounds.computeBounds(width, height));
				behavioursChosenScrollPane.changeBounds(behavioursChosenBounds.computeBounds(width, height));
				getChildren().remove(modifyAmbiencePane);
			}
			
		});
		modifyAmbiencePane.getChildren().add(modifyAmbienceLabel);
		modifyAmbiencePane.getChildren().add(validateAmbienceModification);
		modifyAmbiencePane.getChildren().add(cancelAmbienceModification);
		
		MyLabel newAmbienceLabel = new MyLabel("New ambience: ", newAmbienceLabelBounds.computeBounds(newAmbiencePane.getPrefWidth(), newAmbiencePane.getPrefHeight()), 1f);
		MyTextFieldFX newAmbienceName = new MyTextFieldFX("Name", newAmbienceTextFieldBounds.computeBounds(newAmbiencePane.getPrefWidth(), newAmbiencePane.getPrefHeight()));
		
		MyButtonImage newAmbienceButton = new MyButtonImage(checkImage, newAmbienceButtonBounds.computeBounds(newAmbiencePane.getPrefWidth(), newAmbiencePane.getPrefHeight()), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				String name = newAmbienceName.getText();
				if(!name.equals("")) {
					JSONObject json = new JSONObject();
	                json.put("recipient", "ambience");
	                json.put("action", "create");
	                json.put("name", name);
	                for (int i = 0; i < selectedBehaviours.size(); i++) {
		                json.append("behaviours", selectedBehaviours.get(i).getId());
	                }
	                try {
	                    ClientFX.client.sendToServer(json.toString());
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }	
				}
				 
			}
			
		});
		
		ambiencesScrollPane = new MyScrollPane(ambiencesBounds.computeBounds(width, height));
		behavioursNotChosenScrollPane = new MyScrollPane(behavioursNotChosenBounds.computeBounds(width, height));
		behavioursChosenScrollPane = new MyScrollPane(behavioursChosenBounds.computeBounds(width, height));
		
		this.getChildren().add(newAmbiencePane);
		newAmbiencePane.getChildren().add(newAmbienceLabel);
		newAmbiencePane.getChildren().add(newAmbienceName);
		newAmbiencePane.getChildren().add(newAmbienceButton);
		this.getChildren().add(ambiencesScrollPane);
		this.getChildren().add(behavioursNotChosenScrollPane);
		this.getChildren().add(behavioursChosenScrollPane);
		
		notSelectedBehaviours = new ArrayList<Behaviour>();
		selectedBehaviours = new ArrayList<Behaviour>();
		
		notChosenBehavioursList.setPrefWidth(behavioursNotChosenScrollPane.getPrefWidth());
		notChosenBehavioursList.setPrefHeight(behavioursNotChosenScrollPane.getPrefHeight());
		behavioursNotChosenScrollPane.setContent(notChosenBehavioursList);
		
		chosenBehavioursList.setPrefWidth(behavioursChosenScrollPane.getPrefWidth());
		chosenBehavioursList.setPrefHeight(behavioursChosenScrollPane.getPrefHeight());
		behavioursChosenScrollPane.setContent(chosenBehavioursList);
		
		ambiencesList.setPrefWidth(ambiencesScrollPane.getPrefWidth());
		ambiencesList.setPrefHeight(ambiencesScrollPane.getPrefHeight());
		ambiencesScrollPane.setContent(ambiencesList);
		
		this.updateAmbiences();
		this.updateBehaviours();
	}

	public static Content getInstance() {
		// TODO Auto-generated method stub
		if(content == null) {
			content = new AmbiencesContent();
		}
		return content;
	}
	
	public void updateBehaviours() {
		JSONObject getActuator = new JSONObject();
		getActuator.put("recipient", "behaviour");
		getActuator.put("action", "getAll");
		
		try {
			ClientFX.client.sendToServer(getActuator.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAmbiences() {
		JSONObject getActuator = new JSONObject();
		getActuator.put("recipient", "ambience");
		getActuator.put("action", "getAll");
		
		try {
			ClientFX.client.sendToServer(getActuator.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleMessage(Object message) {
		if(message instanceof String) {
			try {
				System.out.println(message.toString());
				JSONObject json = new JSONObject((String)message);
				String recipient = json.getString("recipient");
				String action;
				switch(recipient) {
					case "behaviour":
						action = json.getString("action");
						switch (action) {
							case "getAll":
								this.notSelectedBehaviours = new ArrayList<Behaviour>();
								JSONArray arrArg = json.getJSONArray("behaviours");
								for (int j = 0; j < arrArg.length(); j++){
									JSONObject current = arrArg.getJSONObject(j);
									Behaviour behaviour = new Behaviour(current.getInt("id"), current.getString("name"));
									this.notSelectedBehaviours.add(behaviour);
									this.behaviourCells.add(new BehaviourCell(behaviour ,notChosenBehavioursList.getPrefWidth(),notChosenBehavioursList.getPrefHeight() / NB_OF_BEHAVIOURS_DISPLAYED, 
		        						new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent event) {
												int pressedButton = Integer.parseInt(((MyButtonImage)event.getSource()).getId());
												changeBehaviourState(pressedButton);
											}
										},
		        						new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent event) {
												int pressedButton = Integer.parseInt(((MyButtonImage)event.getSource()).getId());
											}
										}, 
		        						false
									));
								}
								updateBehavioursUI();
								break;
							}
						break;
					case "ambience":
						action = json.getString("action");
						switch (action) {
							case "getAll":
								this.ambiences = new ArrayList<Ambience>();
								JSONArray arrArg = json.getJSONArray("ambiences");
								for (int j = 0; j < arrArg.length(); j++){
									JSONObject current = arrArg.getJSONObject(j);
									List<Integer> behav = new ArrayList<Integer>();
									JSONArray arrBehav = current.getJSONArray("behaviours");
									System.out.println(arrBehav);
									for(int k = 0; k < arrBehav.length(); k++) {
										behav.add(arrBehav.getJSONObject(k).getInt("id"));
									}
									this.ambiences.add(new Ambience(current.getInt("id"), current.getString("name"), behav));
								}
								updateAmbienceUI();
								break;
							case "create":
								try {
									List<Integer> behav = new ArrayList<Integer>();
									JSONObject ambience = json.getJSONObject("ambience");
									JSONArray arrBehav = ambience.getJSONArray("behaviours");
									for(int k = 0; k < arrBehav.length(); k++) {
										behav.add(arrBehav.getJSONObject(k).getInt("id"));
									}
									this.ambiences.add(new Ambience(ambience.getInt("id"), ambience.getString("name"), behav));
								} catch (Exception e) {
									e.printStackTrace();
								}
								updateAmbienceUI();
								break;
						}
				}
			} catch(Exception e) {
				
			}
		}
	}

	private BehaviourCell getBehaviourCell(int id) {
		int i = 0;
		BehaviourCell cell = null;
		while(i < behaviourCells.size() && cell == null) {
			if(behaviourCells.get(i).getCellId() == id) {
				cell = behaviourCells.get(i);
			}
			i++;
		}
		return cell;
	}
	
	private Ambience getAmbienceSelected() {
		Ambience ambience = null;
		int i = 0;
		if (currentAmbienceSelected == -1) {
			return null;
		}
		while(i < ambiences.size() && ambience == null) {
			if(ambiences.get(i).getId() == currentAmbienceSelected) {
				ambience = ambiences.get(i);
			}
			i++;
		}
		return ambience;
	}


	private void changeBehaviourState(int pressedButton) {
		BehaviourCell behaviourCell = this.getBehaviourCell(pressedButton);
		behaviourCell.changeState();
		this.updateCellState(behaviourCell);
	}
	
	private void updateCellState(BehaviourCell behaviourCell) {
		if(behaviourCell.getState() == true) {
			selectedBehaviours.add(behaviourCell.getBehaviour());
			notSelectedBehaviours.remove(behaviourCell.getBehaviour());

			 Platform.runLater(new Runnable() {
			        @Override public void run() {
			        	System.out.println("Set selected: " + behaviourCell.getCellId());
			        	BehaviourCell bc = behaviourCell;
			notChosenBehavioursList.getChildren().remove(bc);
			chosenBehavioursList.getChildren().add(bc);

				    }
				});
		} else {
			notSelectedBehaviours.add(behaviourCell.getBehaviour());
			 selectedBehaviours.remove(behaviourCell.getBehaviour());

			 Platform.runLater(new Runnable() {
			        @Override public void run() {
			        	BehaviourCell bc = behaviourCell;
			chosenBehavioursList.getChildren().remove(bc);
			notChosenBehavioursList.getChildren().add(bc);
				    }
				});
		}
		
	}
	
	private void updateBehavioursUI() {
		if(this.notSelectedBehaviours.size() > 0) {
			
            Platform.runLater(new Runnable() {
                @Override public void run() {
                	notChosenBehavioursList.getChildren().clear();
        			for (int i = 0; i < notSelectedBehaviours.size(); i++) {
        				BehaviourCell behaviour = getBehaviourCell(notSelectedBehaviours.get(i).getId());
        				notChosenBehavioursList.add(behaviour,0,i);
        			}
                }
            });
		}
		if(this.selectedBehaviours.size() > 0) {
			Platform.runLater(new Runnable() {
                @Override public void run() {
                	chosenBehavioursList.getChildren().clear();
        			for (int i = 0; i < selectedBehaviours.size(); i++) {
        				BehaviourCell behaviour = getBehaviourCell(selectedBehaviours.get(i).getId());
        				chosenBehavioursList.add(behaviour,0,i);
        			}
                }
            });
		}
	}
	
	private void updateAmbienceUI() {
		if(this.ambiences.size() > 0) {
             Platform.runLater(new Runnable() {
                 @Override public void run() {
                	ambiencesList.getChildren().clear();
         			for (int i = 0; i < ambiences.size(); i++) {
         				ambiencesList.add(new MyButtonFX(ambiences.get(i).getName(), ambiences.get(i).getId(), ambiencesList.getPrefWidth(),ambiencesList.getPrefHeight() / NB_OF_AMBIENCES_DISPLAYED, 
         						new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										currentAmbienceSelected = Integer.parseInt(((MyButtonFX)event.getSource()).getId());
										modificateAmbience();
									}
								}
         				),0,i);
         			}
                 }
             });
		}
	}

	private void modificateAmbience() {
		Ambience ambience = this.getAmbienceSelected();
		if(ambience == null) {
			return;
		}
		System.out.println("Reinit");
		reinitBehaviours();
		for (Integer id : ambience.getBehaviours()) {
			changeBehaviourState(id);
		}
		behavioursNotChosenScrollPane.changeBounds(behavioursNotChosenBoundsModif.computeBounds(width, height));
		behavioursChosenScrollPane.changeBounds(behavioursChosenBoundsModif.computeBounds(width, height));
		modifyAmbienceLabel.setText(ambience.getName());
		if(!getChildren().contains(modifyAmbiencePane)){
			getChildren().add(modifyAmbiencePane);
		}
	}

	private void reinitBehaviours() {
		List<Behaviour> behavioursSelected = new ArrayList<Behaviour>();
		for (int i = 0; i < selectedBehaviours.size(); i++) {
			behavioursSelected.add(selectedBehaviours.get(i));
		}
		for (int i = 0; i < behavioursSelected.size(); i++) {
			changeBehaviourState(behavioursSelected.get(i).getId());
		}
	}
}
