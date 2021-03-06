package scenes;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Const.GlobalConts;
import controller.Controller;
import handlers.ButtonHandlers;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import utils.ElementConstructors;

public class SearchScreenMain extends Scene {
    
	private Controller controller;
	
	public SearchScreenMain(Parent root, double width, double height, Paint fill, Controller controller) {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
	}
    
	
	private void createLayout () {
		BorderPane border = new BorderPane();
		System.out.println("HEIGGHT"+ controller.topInfoBar.infoBar.getHeight());
	    border.prefHeightProperty().bind(controller.primaryStage.heightProperty().subtract(65));
	    border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
	    
	    
	    HBox hBox = new HBox();
	    hBox.setSpacing(8);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.getChildren().addAll(createButtons ());
	    StackPane  mainPane = new StackPane(hBox);
	    mainPane.setPadding(new Insets(24));
	    //mainPane.setPrefSize( GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT);
	   
        
        ToolBar toolbar = createtoolBar();
        toolbar.setPrefHeight(50);
        
        border.setTop(toolbar);
        border.setCenter(mainPane);
        Group root = (Group)this.getRoot();
        root.getChildren().add(border);
	}
	
	
	private ArrayList  createButtons () {
		
		ArrayList buttons = new ArrayList ();	
		Button  button1 = ElementConstructors.createBigButtonWithText("New Search");
		Button  button2 = ElementConstructors.createBigButtonWithText("Continue Existing Search");
		Button  button3 = ElementConstructors.createBigButtonWithText("Manage Existing Searches");
		
		button1.setOnAction(ButtonHandlers.SearchNew(controller));
		
		button3.setOnAction(ButtonHandlers.AMnageExistingSearches(controller));
			
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		
		return buttons;
	}
	
private ToolBar  createtoolBar () {
		
		ToolBar toolbar = null;
		try {
			Button back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
			back.setOnAction(ButtonHandlers.Home(controller));
			
			toolbar = new ToolBar(back);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return toolbar;
	}
}
