package scenes;


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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import utils.ElementConstructors;

public class HomeScreen extends Scene {
    
	private Controller controller;
	
	public HomeScreen(Parent root, double width, double height, Paint fill, Controller controller) {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
	}
    
	
	private void createLayout () {
		
		HBox hBox = new HBox();
	    hBox.setSpacing(8);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.getChildren().addAll(createButtons ());
	    StackPane  mainPane = new StackPane(hBox);
	    mainPane.setPrefSize( GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT);
	    
	    mainPane.setPadding(new Insets(24));
	    Group root = (Group)this.getRoot();
        root.getChildren().add(mainPane);
	}
	
	
	private ArrayList  createButtons () {
		
		ArrayList buttons = new ArrayList ();	
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		Button  button1 = ElementConstructors.createBigButtonWithImage(new Image(getClass().getResourceAsStream("/images/search.png")));
		Button  button2 = ElementConstructors.createBigButtonWithImage(new Image(getClass().getResourceAsStream("/images/analyse.png")));
		Button  button3 = ElementConstructors.createBigButtonWithImage(new Image(getClass().getResourceAsStream("/images/settings.png")));
		
		button1.setOnAction(ButtonHandlers.SearchMain(controller));
			
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		
		return buttons;
	}
	

}
