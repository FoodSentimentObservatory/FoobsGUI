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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.ElementConstructors;

public class HomeScreen extends Scene {
    
	private Controller controller;
	
	public HomeScreen(Parent root, double width, double height, Paint fill, Controller controller) {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
	}
    
	
	private void createLayout () {
		BorderPane border = new BorderPane();
		System.out.println("HEIGGHT"+ controller.topInfoBar.infoBar.getHeight());
	    border.prefHeightProperty().bind(controller.primaryStage.heightProperty().subtract(100));
	   // border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
	    border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
	    
		HBox hBox = new HBox();
	    hBox.setSpacing(8);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.getChildren().addAll(createButtons ());
	    StackPane  mainPane = new StackPane(hBox);
	    mainPane.setPrefSize( GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT);
	    
	    mainPane.setPadding(new Insets(24));
	    border.setCenter(mainPane);
	    
	    //palceholder for database info
	    
	    //controller.topInfoBar.setWarningMessage("Warning: not connected to any database");
	//    border.setTop(controller.topInfoBar.infoBar);
	    
	    
	    Group root = (Group)this.getRoot();
        root.getChildren().add(border);
	}
	
	
	private ArrayList  createButtons () {
		
		ArrayList buttons = new ArrayList ();	
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		System.out.println(System.getProperty("user.dir")+File.separator+"src"+File.separator+"images"+File.separator+"search.png");
		
		try {
		Button	button1 = ElementConstructors.createBigButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"search.png"))));
		
		Button  button2 = ElementConstructors.createBigButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"analyse.png"))));
		
		
		Button  button3 = ElementConstructors.createBigButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"settings.png"))));
		
		button1.setOnAction(ButtonHandlers.SearchMain(controller));
		button2.setOnAction(ButtonHandlers.Analysis(controller));
		button3.setOnAction(ButtonHandlers.DatabaseConfig(controller));
			
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buttons;
	}
	

}
