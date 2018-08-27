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
import collector.entity.GeoRadiusEntity;
import collector.entity.LocationEntity;
import collector.entity.PlatformEntity;
import collector.entity.SearchDetailsEntity;
import collector.entity.SearchLeafNodeEntity;
import collector.entity.SearchMainEntity;
import collector.entity.SearchSubNodeEntity;
import collector.main.SearchManager;
import controller.Controller;
import db.DbConn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scenes.AnalysisWebView;
import status.NewSearchObject;
import twitter4j.GeoLocation;
import utils.ElementConstructors;
import utils.ElementManipulators;
import utils.PasswordHandlers;
import utils.TwitterKeywordSplit;
import utils.TwitterKeywordSplitNew;


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
	
	public static EventHandler<ActionEvent> SearchNewGeolocation (Controller controller, TextField searchName,TextField searchdescription ) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	NewSearchObject obj = new NewSearchObject();
				obj.setMainSearch(new SearchMainEntity (searchName.getText(),searchdescription.getText() ));
				controller.setNEwSearchObject (obj);
		    	controller.setSearchNewGeolocation (controller.primaryStage,true);
		    }
		};
		return eventHandler;
	}
	
	//use only for going back as cache not cleared
	public static EventHandler<ActionEvent> SearchNewGeolocation (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	
		    	controller.setSearchNewGeolocation (controller.primaryStage,false);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> SearchNewKeywords (Controller controller, ArrayList<SearchSubNodeEntity> subSearches) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.newSearchObject.setSubSearches(subSearches);
		    	System.out.println(controller.newSearchObject.getMainSearch().getMainSearchLabel());
		    	System.out.println(controller.newSearchObject.getMainSearch().getDescription());
		    	
		    	for (int i =0;i<subSearches.size();i++) {
		    		System.out.println(subSearches.get(i).getDescription());
		    		System.out.println(subSearches.get(i).getSearchNodeLabel());
		    	}
		    	
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
	
	public static EventHandler<ActionEvent> ClearKeywords (TabPane tabPane, HashMap<String, ArrayList<String>> keywordsList) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	TilePane pane = (TilePane) ((ScrollPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();
		    	System.out.println("Clear Called");
		    	pane.getChildren().clear();
		    	keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()).clear();
		    	
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> AddKeywordManual (TabPane tabPane, HashMap<String, ArrayList<String>> keywordsList, TextField manualText) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	String text = manualText.getText().trim();
		    	TilePane pane = (TilePane) ((ScrollPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();
		    	//repetition with code in OpenKeywordsFromFile - TO DO TIDY UP
		    	if (!text.equals("") &&!keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()).contains(text)) {
		    	keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()).add(text);
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
					remove.setOnAction(RemoveKeyword(pane,keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()),hbox,manualText.getText()));
	    	        grid.add(remove, 1, 0, 1, 1);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    	        
    	        
//TO DO fix the alignment of the remove button
    	       // hbox.getChildren().add(keyword);
    	       // hbox.getChildren().add(remove);
    	        hbox.getChildren().add(grid);
    	        
    	        pane.getChildren().add(hbox);
		    	}
		    	
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> OpenKeywordsFromFile (Controller controller, TabPane tabPane, HashMap<String, ArrayList<String>> keywordsList) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	TilePane pane = (TilePane) ((ScrollPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();
		    	FileChooser fileChooser = new FileChooser();
		    	File file = fileChooser.showOpenDialog(controller.primaryStage);
                if (file != null) {
                	
                	try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                	    StringBuilder sb = new StringBuilder();
                	    String line = br.readLine();

                	    while (line != null ) {  
                	    	line = line.trim();
                	    	//temporaly switch off support for dublicate checks
                	        //if (!line.equals("") &&!keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()).contains(line)) {
                	        	if (!line.equals("") ) {
                	        keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()).add(line);
                	        
                	        HBox hbox = new HBox ();
                	        hbox.setSpacing(8);
                	        GridPane grid = new GridPane ();
  	       
                	        hbox.setPadding(new Insets(4));
                	        Text keyword = new Text (line);
                	        grid.add(keyword, 0, 0, 1, 1);
                	       
                	        //GridPane.setHalignment(keyword, HPos.LEFT);
                	        
                	        Button remove = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"delete_small.png"))), "");
                	        remove.setOnAction(RemoveKeyword(pane,keywordsList.get(tabPane.getSelectionModel().getSelectedItem().getText()),hbox,line));
                	        grid.add(remove, 1, 0, 1, 1);
                	        
          //TO DO fix the alignment of the remove button
                	       // hbox.getChildren().add(keyword);
                	       // hbox.getChildren().add(remove);
                	        hbox.getChildren().add(grid);
                	        
                	        pane.getChildren().add(hbox);
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
	
	



	public static EventHandler<ActionEvent> AddRadius(TextField subField, TextField descField,TextField latField, TextField lonField, TextField radField,
			 FlowPane listOfSearchRadiuses, ArrayList listComponents, WebEngine webEngine, Controller controller, ArrayList<SearchSubNodeEntity> subSearches) {
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
		    	System.out.println("submit called");
		    	UUID id = UUID.randomUUID();
		    	
		    	//TO do check if input not empty
		    	
		    	LocationEntity loc = new LocationEntity ();
		    	//JUST now there is only one option 
		    	loc.setLocationType("GeoRadius");
		    	loc.setDisplayString("fjfh");
		    	//To do set georadius
		    	 GeoLocation geoLocation = new   GeoLocation(Double.parseDouble(latField.getText()), Double.parseDouble(lonField.getText()));
		    	 GeoRadiusEntity radius = new GeoRadiusEntity (geoLocation,Integer.parseInt(radField.getText()));
		    	 radius.setLocationId(loc);
		    	 loc.setGeoRadius (radius);
		    	 
		    	SearchSubNodeEntity subSearch = new  SearchSubNodeEntity (subField.getText(),descField.getText(), loc);
		    	
		    	
		    	subSearches.add(subSearch);
		    	
		    	String parameters = "{\"lat\":"+ latField.getText()+",\"lon\": "+lonField.getText()+",\"rad\": "+radField.getText()+",\"id\": \""+id+"\"}";
		    	System.out.println(parameters);
		    	webEngine.executeScript("document.addMarker('"+parameters+"')");
		    	VBox wrapper = new VBox (); 
		    	Text t = new Text();
		    	t.setText("Lat:"+latField.getText()+"\nLon:"+lonField.getText()+ "\nRad:"+radField.getText());
		    	//listComponents.add(new Label ("Lat:"+latField.getText()+" Lon:"+lonField.getText()+ " Rad:"+radField.getText()));
		    	
		    	Button remove = new Button ("remove");
		    	remove.setOnAction(RemoveRadiusFromList(wrapper,listOfSearchRadiuses,listComponents,id,webEngine,subSearches,subSearch));
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
	
	public static EventHandler<ActionEvent> RemoveRadiusFromList(VBox wrapper, FlowPane listOfSearchRadiuses, ArrayList listComponents,UUID radiusId,WebEngine webEngine, ArrayList<SearchSubNodeEntity> subSearches, SearchSubNodeEntity subSearch) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
		    	System.out.println("remove called");
		    	System.out.println("removing marker: \""+ radiusId+"\"");
		    	
		    	subSearches.remove(subSearch);
		    	
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


	public static EventHandler<ActionEvent> ContinueSearch(Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.continueSearch();
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> startSearch(Controller controller, HashMap<String, ArrayList<String>> keywordsList, TextField pattern, CheckBox allCombinations) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	
		    	System.out.println("Printing keywords: ");
		    	
		    	keywordsList.forEach((k,v) -> {
		    		System.out.println ("tab : "+k);
		    		v.forEach((item) -> {
		    			System.out.println (item);
		    		});
		    		
		    	});
		    	
		    	ArrayList <String> keywrodsSplit = TwitterKeywordSplitNew.createKeywordStrings(keywordsList, pattern.getText(), allCombinations);
		    	
		    	keywrodsSplit.forEach((v)->{
		    		System.out.println(v);
		    	}); 
		    	
		    	
		    	
		    	//ArrayList <String> keywrodssplit = TwitterKeywordSplit.createKeywordAndPhrasesStrings(keywordsList);
		    	System.out.println (keywrodsSplit);
		    	
		    	ArrayList <SearchSubNodeEntity> subsearches  = controller.newSearchObject.getSubSearches();
		    	HashMap <String, ArrayList <SearchLeafNodeEntity>> leafSearches = new HashMap  <String,  ArrayList <SearchLeafNodeEntity>>();
		    	for (int i =0 ; i<subsearches.size();i++) {
		    		 ArrayList <SearchLeafNodeEntity> list = new  ArrayList <SearchLeafNodeEntity>();
		    		SearchSubNodeEntity subsearch = subsearches.get(i);
		    		for (int j =0 ; j<keywrodsSplit.size();j++) {
		    			UUID id = UUID.randomUUID();
		    			SearchLeafNodeEntity leaf = new SearchLeafNodeEntity (id.toString(),keywrodsSplit.get(j));
		    			list.add(leaf);
		    		}
		    		leafSearches.put(subsearch.getSearchNodeLabel(),list);
		    	}
		    	
		    	controller.newSearchObject.setLeafSearches(leafSearches);
		    	DAO.saveNewSearchObject(controller);
		    	SearchManager.continueSearch( controller, controller.scenes.continuedSearchProgress.getSearchInfoBarVBox());
		    }
		};
		return eventHandler;
	}


	public static EventHandler<ActionEvent> SubmitFormInAnalysisWindow(Controller controller, WebEngine webEngine, AnalysisWebView analysisWebView) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		       
		       
		       webEngine.executeScript("next()");
		       webEngine.getLoadWorker().stateProperty().addListener(
		    	        new ChangeListener<State>() {
		    	            public void changed(ObservableValue ov, State oldState, State newState) {
		    	                if (newState == State.SUCCEEDED) {
		    	                	if (webEngine.getLocation().equals("http://localhost:8080/connectToScript")) {
		    	     		    	   //change screens and present choice here 
		    	     		    	   //controller.setFrequencyAnalysisOption (webEngine);
		    	     		    	   analysisWebView.setFrequencyAnalysisToolBar();
		    	     		    	   System.out.println("Trying to change toolbar");
		    	     		       }  
		    	                }
		    	            }
		    	        });
		       
		       
		    }
		 		};
		 		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> SubmitFrequencyAnalysisOption(Controller controller, WebEngine webEngine, AnalysisWebView analysisWebView, String option) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		      
		       webEngine.executeScript("next('"+option+"')");
		       //controller.setAnalysis(controller.primaryStage);
		       webEngine.getLoadWorker().stateProperty().addListener(
		    	        new ChangeListener<State>() {
		    	            public void changed(ObservableValue ov, State oldState, State newState) {
		    	                if (newState == State.SUCCEEDED) {
		    	                	
		    	     		    	   //change screens and present choice here 
		    	     		    	   //controller.setFrequencyAnalysisOption (webEngine);
		    	     		    	   analysisWebView.setGenerateEvidenceToolBar();
		    	     		    	   System.out.println("Trying to change toolbar");
		    	     		       
		    	                }
		    	            }
		    	        });
		       
		    }
		 		};
		 		return eventHandler;
	}


	public static EventHandler<ActionEvent> SubmitGenerateEvidence(Controller controller, WebEngine webEngine,  AnalysisWebView analysisWebView
			) {
		
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		      
		       webEngine.executeScript("submitGenerateEvidenceForm()");
		       //controller.setAnalysis(controller.primaryStage);
		      
		    }
		 		};
		 		return eventHandler;
	}


	public static EventHandler<ActionEvent> Login(Text actiontarget, TextField userTextField, PasswordField pwBox, Controller controller) {
		 
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		   
		    	System.out.print("Hey"+userTextField.getText());
		    	System.out.print("Hey"+pwBox.getText());
		    	 boolean loggedIn = false ;
		    	if (!userTextField.getText().equals("")&&!pwBox.getText().equals("") ) {
		    			 loggedIn = PasswordHandlers.checkPasswordInDatabase(userTextField.getText(),pwBox.getText(), controller );
		    	}
		    
		     if (loggedIn) {
		    	controller.setHome();
		     }
		     else {
		    	 actiontarget.setFill(Paint.valueOf("Red"));
		         actiontarget.setText("Invalid credentials");
		     }
		       //controller.setAnalysis(controller.primaryStage);
		      
		    }
		 		};
		 		return eventHandler;
		
		
		
	}
	
}
