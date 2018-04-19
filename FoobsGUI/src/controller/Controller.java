package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
     
	 public SceneBuilder scenes;
	 public Stage primaryStage;
	 
	 public Controller (Stage primaryStage) {
		 this.primaryStage = primaryStage;
	 }
	 
	 public void initializeScenes () throws Exception {
		 this.scenes = new SceneBuilder(this);
	 }
	 
	 public void setHome (Stage primaryStage) {
		 
		 this.primaryStage.setScene(scenes.homeScene);
		 this.primaryStage.show();
	 }

	public void setSearchHome(Stage primaryStage) {
		this.primaryStage.setScene(scenes.searchHome);
		this.primaryStage.show();
		
	}

	public void setSearchNewSearchName(Stage primaryStage) {
		this.primaryStage.setScene(scenes.searchNewSearchName);
		this.primaryStage.show();
		
	}
	
	public void setSearchNewGeolocation(Stage primaryStage) {
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
	 
}
