package scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Const.GlobalConts;
import collector.entity.SearchDetailsEntity;
import controller.Controller;
import db.DbDataManager;
import db.SearchInfo;
import handlers.ButtonHandlers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import utils.ElementConstructors;

public class ManageSearches extends BorderPane{

	 private Controller controller;
	 private ListView<String> m_listView;
	 private ArrayList <SearchInfo> searches;
	 private ObservableList<String> list;
	 private VBox listViewPanel;
	 private ArrayList <SearchInfo> lastSearches = new ArrayList();
	 public ManageSearches ( Controller controller) {
			
			this.controller = controller;
			createLayout ();
		}
	 
	 private void createLayout () {
			//BorderPane border = new BorderPane();
			System.out.println("HEIGGHT"+ controller.topInfoBar.infoBar.getHeight());
		    this.prefHeightProperty().bind(controller.primaryStage.heightProperty().subtract(65));
		   // border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
		    this.prefWidthProperty().bind(controller.primaryStage.widthProperty());
		    ToolBar toolbar = createtoolBar();
	        toolbar.setPrefHeight(50);
	        
	        this.setTop(toolbar);
		    
	       
	        listViewPanel = new VBox();
			listViewPanel.setSpacing(10);
			
			// the text to be displayed when clicking on a new item in the list.
			
			// create a list of items.
			list = FXCollections.observableArrayList();
			m_listView = new ListView<String>(list);
			m_listView.prefWidth(150);
			m_listView.setMaxWidth(150);
			m_listView.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<String>() {

						public void changed(
								ObservableValue<? extends String> observable,
								String oldValue, String newValue) {
							
							System.out.println(newValue);
							if (newValue != null) {
							listViewPanel.getChildren().remove(0, listViewPanel.getChildren().size());
							// change the label text value to the newly selected
							// item.
							//label.setText("You Selected " + newValue);
							
							DbDataManager dbManager = new DbDataManager (controller);
							
							try {
								int allTweetCount = dbManager.getNumberOfTweetsIncludingReTweets();
								int withoutRetweetCount = dbManager.getNumberOfTweetsWithoutReTweets();
								
								Text tweetCount = new Text("All Tweets: " + allTweetCount ) ;
								tweetCount.wrappingWidthProperty().bind(listViewPanel.widthProperty());
								
								Text noRetweetCount = new Text("Without Retweets: " + withoutRetweetCount + "\n ") ;
								noRetweetCount.wrappingWidthProperty().bind(listViewPanel.widthProperty());
								listViewPanel.getChildren().addAll(tweetCount,noRetweetCount);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							//EXTRACT TO METHOD OR CLASS LATER
							//check how many subsearches are there in the search
							HashSet <String> subSearches = new HashSet <String> ();
							HashSet <String> startDates = new HashSet <String> ();
							HashSet <String> endDates = new HashSet <String> ();
							HashMap <String,Object[]> latLong = new HashMap <String,Object[]>();
							HashSet <String> keywords = new HashSet <String> ();
							
							
							for (int i =0; i < searches.size(); i++) {
								String searchLabel = ((SearchInfo) searches.get(i)).getMainLabel();
								if (searchLabel.equals(newValue)) {		
									
									SearchInfo obj =  searches.get(i);
									String subsearchLabel = (obj.getSubLabel());
									if (!subSearches.contains(subsearchLabel)) {
										int locId = obj.getLocationId();
										try {
											Object [] objArr = new Object [2];
											float [] latLon = dbManager.getLocationLatLong(locId);
											objArr[0] = latLon;
											objArr[1] = obj.getRadius();
											System.out.println("Saving "+latLon[1]);
											
											System.out.println(searches.get(i).getLon());
											//searches.set(i, obj);
											
											latLong.put(subsearchLabel, objArr);
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									subSearches.add(subsearchLabel);
									
									startDates.add(obj.getStarted());
									endDates.add(obj.getEnded());
								}
							}
							
							List sortedList = new ArrayList(startDates);
							Collections.sort(sortedList);
							
							String latestSearchDate = (String) sortedList.get(sortedList.size()-1);
							
							//HORRIBLE REPETATIVE NEEDs TO CHANGE
							for (int i =0; i < searches.size(); i++) {
								 
								SearchInfo obj =  searches.get(i);
								if (obj.getMainLabel().equals(newValue)&&obj.getStarted().equals(latestSearchDate)) {	
								   keywords.add(obj.getKeywords());
								   
								   try {
									float [] latLon = dbManager.getLocationLatLong(obj.getLocationId());
									searches.get(i).setLat(latLon[0]);
									searches.get(i).setLon(latLon[1]);
								   
								   } catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								    }
								   lastSearches.add(obj);
								}
							}
							
						
							Text txt = new Text("This search was executed " + startDates.size() + " time(s) on the following dates:\n "+sortedList.toString()) ;
							txt.wrappingWidthProperty().bind(listViewPanel.widthProperty());
							listViewPanel.getChildren().add(txt);
							
							Text subsearchesText = new Text ("This search contains " +subSearches.size() + " subsearches: "+ subSearches.toString());
							listViewPanel.getChildren().add(subsearchesText);
							
							final StringBuffer kewordsText = new StringBuffer();
							//add new characetrs 
							keywords.forEach((v)->{kewordsText.append(v+"\n\n");});
							Text subsearchesKeywordsText = new Text ("Each of the subsearches is further divided into " +keywords.size() + " smaller searches with the following keywords:\n "+ kewordsText.toString());
							subsearchesKeywordsText.wrappingWidthProperty().bind(listViewPanel.widthProperty());
							listViewPanel.getChildren().add(subsearchesKeywordsText);
							
							WebView browser = new WebView();
					        WebEngine webEngine = browser.getEngine();
					        webEngine.getLoadWorker().stateProperty().addListener(
					                new ChangeListener<State>() {
					                  @Override public void changed(ObservableValue ov, State oldState, State newState) {

					                      if (newState == Worker.State.SUCCEEDED) {
					                        
					                        System.out.println("called");
					                        latLong.forEach((k,v)->{
												String parameters = "{\"lat\":"+ ((float []) v[0])[0]+",\"lon\": "+((float []) v[0])[1]+",\"rad\":"+(float) v[1] *1000+",\"id\": \"0\"}";
										    	System.out.println(parameters);
										    	webEngine.executeScript("document.addMarker('"+parameters+"')");
											});
					                    }
					                      
					                    }
					                });
					         byte[] encoded;
							try {
								encoded = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+File.separator+"html"+File.separator+"map.html"));
								String content =  new String(encoded, Charset.defaultCharset());
							        // System.out.println(content);
							        
							    webEngine.loadContent(content);
							    listViewPanel.getChildren().add(browser);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							System.out.println(latLong);
					        
							
						}
						}
					});
	        
			ScrollPane scroll = new ScrollPane (listViewPanel);
		    this.setCenter(scroll);
		    
		    this.setLeft(m_listView);
		    
		    //palceholder for database info
		    
		    //controller.topInfoBar.setWarningMessage("Warning: not connected to any database");
		//    border.setTop(controller.topInfoBar.infoBar);
		    
		}
	 
	 public void loadSearchDetails () {
		 try {
			searches = (new DbDataManager (controller).getSearchDetails());
			
			list.clear();
			final Text label = new Text("Nothing Selected.");
			listViewPanel.getChildren().remove(0, listViewPanel.getChildren().size());
			listViewPanel.getChildren().add(label);
			
			for (int i =0; i < searches.size(); i++) {
				String searchLabel = ((SearchInfo) searches.get(i)).getMainLabel();
				if (!list.contains(searchLabel))
				list.add(searchLabel); 
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 private ToolBar  createtoolBar () {
			
			ToolBar toolbar = null;
			try {
				Button back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
				back.setOnAction(ButtonHandlers.SearchMain(controller));
				
				Button continueSearch = ElementConstructors.createSmallButtonWithText("Continue this search");
				continueSearch.setAlignment(Pos.BASELINE_RIGHT);
				continueSearch.setOnAction(ButtonHandlers.ContinueSearch(controller,lastSearches));
				
				toolbar = new ToolBar(back,continueSearch);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return toolbar;
		}
		
	
}
