package controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.SessionFactory;

import Const.GlobalConts;
import collector.main.SearchManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import status.InfoBarHBox;
import status.NewSearchObject;

public class Controller {
     
	 public SceneBuilder scenes;
	 public Stage primaryStage;
	 public Connection con = null;
	 public InfoBarHBox topInfoBar = new InfoBarHBox (); 
	 public SessionFactory sessionFactory;
	 public String dbName ="";
	public NewSearchObject newSearchObject;
	 
	 public Controller (Stage primaryStage) {
		 this.primaryStage = primaryStage;
	 }
	 
	 public void initializeScenes () throws Exception {
		 this.scenes = new SceneBuilder(this);
	 }
	 
	 public void setHome () {
		 
		 ((BorderPane)this.primaryStage.getScene().getRoot()).setCenter(scenes.homeScene.getRoot());
		 
		// this.primaryStage.setScene(scenes.homeScene);
		// this.primaryStage.show();
	 }

	public void setSearchHome() {
		
		((BorderPane)this.primaryStage.getScene().getRoot()).setCenter(scenes.searchHome.getRoot());
		//this.primaryStage.setScene(scenes.searchHome);
		//this.primaryStage.show();
		
	}

	public void setSearchNewSearchName(Stage primaryStage) {
		this.primaryStage.setScene(scenes.searchNewSearchName);
		this.primaryStage.show();
		
	}
	
	public void setSearchNewGeolocation(Stage primaryStage, boolean clearCache) {
		if (clearCache) {
		scenes.searchNewGeolocation.clearSubsearches();
		// TO do need to reset inputs too
		}
		
		
		this.primaryStage.setScene(scenes.searchNewGeolocation);
		this.primaryStage.show();
		
	}
	public void setSearchNewKeywords(Stage primaryStage) {
		this.primaryStage.setScene(scenes.searchNewKeywords);
		this.primaryStage.show();
		
	}
	 
	public Scene getScene() {
		System.out.println("Scene"+this.primaryStage);
		System.out.println(this.primaryStage.getScene());
		return this.primaryStage.getScene();
		
	}

	public void setAnalysis(Stage primaryStage2) {
		this.primaryStage.setScene(scenes.analysis);
		this.primaryStage.show();
		
	}

	public void setDatabaseConfig(Stage primaryStage2) {
		
		((BorderPane)this.primaryStage.getScene().getRoot()).setCenter(scenes.databaseConfig.getRoot());
		
		//this.primaryStage.setScene(scenes.databaseConfig);
		//this.primaryStage.show();
		
	}

	public void setExistingSearches() {
		scenes.manageSearches.loadSearchDetails();
		((BorderPane)this.primaryStage.getScene().getRoot()).setCenter(scenes.manageSearches);
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		
		
		
		this.sessionFactory = sessionFactory;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTwitterAppConsumerKey() {
		return GlobalConts.consumerKey;
	}

	public String getTwitterAppConsumerSecret() {
		return GlobalConts.consumerSecret;
	}

	public void continueSearch() {
		
	 
		 ((BorderPane)this.primaryStage.getScene().getRoot()).setCenter(scenes.continuedSearchProgress);
	   
		 Task loadDatesFromDB = new Task<Void>() {
		        @Override
		        protected Void call() throws Exception {
		        	 
		              SearchManager.continueSearch( Controller.this,scenes.continuedSearchProgress.getSearchInfoBarVBox());
			    	
		            return null;
		             
			    	 }            
		    };
		    new Thread(loadDatesFromDB).start();  
		 
		
	}

	public void setNEwSearchObject(NewSearchObject obj) {
		this.newSearchObject = obj;
		
	}
	 
}
