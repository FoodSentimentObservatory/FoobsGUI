package handlers;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import collector.db.DAO;
import collector.db.HibernateUtil;
import collector.entity.PlatformEntity;
import collector.entity.SearchDetailsEntity;
import controller.Controller;
import db.DbConn;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.ElementConstructors;
import utils.ElementManipulators;


public class ButtonHandlers {
    
	
	public static EventHandler<ActionEvent> SearchMain (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchHome ();
		    }
		};
		return eventHandler;
	}
	
	
	public static EventHandler<ActionEvent> SearchNew (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchNewSearchName (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> SearchNewGeolocation (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchNewGeolocation (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> SearchNewKeywords (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchNewKeywords (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> RemoveKeyword (TilePane keywords, ArrayList keywordsList, HBox hbox, String line) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	keywords.getChildren().remove(hbox);
		    	keywordsList.remove(line);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> ClearKeywords (TilePane keywords, ArrayList keywordsList) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.out.println("Clear Called");
		    	keywords.getChildren().clear();
		    	keywordsList.clear();
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> AddKeywordManual (TilePane keywords, ArrayList keywordsList, TextField manualText) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	String text = manualText.getText().trim();
		    	//repetition with code in OpenKeywordsFromFile - TO DO TIDY UP
		    	if (!text.equals("") &&!keywordsList.contains(text)) {
		    	keywordsList.add(text);
		    	HBox hbox = new HBox ();
    	        hbox.setSpacing(8);
    	        GridPane grid = new GridPane ();
 
    	        hbox.setPadding(new Insets(4));
    	        Text keyword = new Text (text);
    	        grid.add(keyword, 0, 0, 1, 1);
    	       
    	        //GridPane.setHalignment(keyword, HPos.LEFT);
    	        
    	        Button remove;
				try {
					remove = ElementConstructors.createSmallButtonWithImage(new  Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"delete_small.png"))), "");
					remove.setOnAction(RemoveKeyword(keywords,keywordsList,hbox,manualText.getText()));
	    	        grid.add(remove, 1, 0, 1, 1);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    	        
    	        
//TO DO fix the alignment of the remove button
    	       // hbox.getChildren().add(keyword);
    	       // hbox.getChildren().add(remove);
    	        hbox.getChildren().add(grid);
    	        
    	        keywords.getChildren().add(hbox);
		    	}
		    	
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> OpenKeywordsFromFile (Controller controller, TilePane keywords, ArrayList keywordsList) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	FileChooser fileChooser = new FileChooser();
		    	File file = fileChooser.showOpenDialog(controller.primaryStage);
                if (file != null) {
                	
                	try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                	    StringBuilder sb = new StringBuilder();
                	    String line = br.readLine();

                	    while (line != null ) {  
                	    	line = line.trim();
                	        if (!line.equals("") &&!keywordsList.contains(line)) {
                	        keywordsList.add(line);
                	        
                	        HBox hbox = new HBox ();
                	        hbox.setSpacing(8);
                	        GridPane grid = new GridPane ();
  	       
                	        hbox.setPadding(new Insets(4));
                	        Text keyword = new Text (line);
                	        grid.add(keyword, 0, 0, 1, 1);
                	       
                	        //GridPane.setHalignment(keyword, HPos.LEFT);
                	        
                	        Button remove = ElementConstructors.createSmallButtonWithImage(new Image(getClass().getResourceAsStream("/images/delete_small.png")), "");
                	        remove.setOnAction(RemoveKeyword(keywords,keywordsList,hbox,line));
                	        grid.add(remove, 1, 0, 1, 1);
                	        
          //TO DO fix the alignment of the remove button
                	       // hbox.getChildren().add(keyword);
                	       // hbox.getChildren().add(remove);
                	        hbox.getChildren().add(grid);
                	        
                	        keywords.getChildren().add(hbox);
                	        }
                	        line = br.readLine();
                	    }
                	    //String everything = sb.toString();
                	} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
		    }
		};
		return eventHandler;
	}
	
	



	public static EventHandler<ActionEvent> AddRadius(TextField latField, TextField lonField, TextField radField,
			FlowPane listOfSearchRadiuses, ArrayList listComponents, WebEngine webEngine, Controller controller) {
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
		    	System.out.println("submit called");
		    	UUID id = UUID.randomUUID();
		    	String parameters = "{\"lat\":"+ latField.getText()+",\"lon\": "+lonField.getText()+",\"rad\": "+radField.getText()+",\"id\": \""+id+"\"}";
		    	System.out.println(parameters);
		    	webEngine.executeScript("document.addMarker('"+parameters+"')");
		    	VBox wrapper = new VBox (); 
		    	Text t = new Text();
		    	t.setText("Lat:"+latField.getText()+"\nLon:"+lonField.getText()+ "\nRad:"+radField.getText());
		    	//listComponents.add(new Label ("Lat:"+latField.getText()+" Lon:"+lonField.getText()+ " Rad:"+radField.getText()));
		    	
		    	Button remove = new Button ("remove");
		    	remove.setOnAction(RemoveRadiusFromList(wrapper,listOfSearchRadiuses,listComponents,id,webEngine));
		    	wrapper.getChildren().add(t);
		    	wrapper.getChildren().add(remove);
		    	//listComponents.add(t);
		    	//listComponents.add(remove);
		    	listComponents.add(wrapper);
		    	listOfSearchRadiuses.getChildren().remove(0,listOfSearchRadiuses.getChildren().size());
				listOfSearchRadiuses.getChildren().addAll(listComponents);
				
			  }
		};
		return eventHandler;
}
	
	public static EventHandler<ActionEvent> RemoveRadiusFromList(VBox wrapper, FlowPane listOfSearchRadiuses, ArrayList listComponents,UUID radiusId,WebEngine webEngine) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
		    	System.out.println("remove called");
		    	System.out.println("removing marker: \""+ radiusId+"\"");
		    	
		    	String parameters = "{\"id\":\""+radiusId+"\"}";
		    	webEngine.executeScript("document.removeMarker('"+parameters+"')");
		    	listComponents.remove(wrapper);
		    	listOfSearchRadiuses.getChildren().remove(0,listOfSearchRadiuses.getChildren().size());
				//listOfSearchRadiuses.getChildren().addAll(listComponents);
		    	System.out.println(listComponents.size());
				ElementManipulators.addItemsToList(listOfSearchRadiuses,listComponents);
			  }
		};
		return eventHandler;
}


	public static EventHandler<ActionEvent> Home(Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setHome();
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> Analysis(Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setAnalysis(controller.primaryStage);
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> DatabaseConfig(Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setDatabaseConfig(controller.primaryStage);
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> ConnectDatabase(Controller controller, ComboBox comboBox) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.out.println("Selected database "+comboBox.getSelectionModel().getSelectedItem());
		    	if (comboBox.getSelectionModel().getSelectedItem() != null) {
		    	controller.con = DbConn.getDatabaseConnection (comboBox.getSelectionModel().getSelectedItem().toString());
		    	controller.topInfoBar.setPositiveMessage("Connected to: " +comboBox.getSelectionModel().getSelectedItem().toString());
		    	controller.setDbName(comboBox.getSelectionModel().getSelectedItem().toString());
		    	controller.setSessionFactory(HibernateUtil.getSessionFactory(controller));
		    	
		    	//System.out.println(DAO.getPlatfromBasedOnName("Twitter", controller));
		    	Session session = controller.getSessionFactory().openSession();
		    	session.doWork(new Work() {
		    	    @Override
		    	    public void execute(Connection connection) throws SQLException {
		    	        System.out.println("Connection ready");
		    	        //System.out.println(((PlatformEntity)DAO.getPlatfromBasedOnName("Scotsman", controller)).getForumName());
		    	      /*  ArrayList <SearchDetailsEntity> list =  DAO.getAllSearches(controller);
		    	        for (int i =0 ; i <list.size(); i ++) {
		    	        	System.out.println(list.get(i).getStartOfSearch());
		    	        }*/
		    	    }
		    	});
		    	controller.setHome();
		    	}
		    	else {
		    		final Stage dialog = new Stage();
	                dialog.initModality(Modality.APPLICATION_MODAL);
	                dialog.initOwner(controller.primaryStage);
	                VBox dialogVbox = new VBox(20);
	                dialogVbox.getChildren().add(new Text("Please select a database from the drop down menu"));
	                Scene dialogScene = new Scene(dialogVbox, 300, 200);
	                dialog.setScene(dialogScene);
	                dialog.show();
		    	}
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> AMnageExistingSearches(Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setExistingSearches();
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> ContinueSearch(Controller controller, ArrayList lastSearches) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.continueSearch(lastSearches);
		    }
		};
		return eventHandler;
	}
	
}
