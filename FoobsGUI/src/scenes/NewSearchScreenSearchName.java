package scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Const.GlobalConts;
import collector.entity.SearchMainEntity;
import controller.Controller;
import handlers.ButtonHandlers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import status.NewSearchObject;
import utils.ElementConstructors;

public class NewSearchScreenSearchName extends Scene{
    
	private Controller controller;
	
	public NewSearchScreenSearchName(Parent root, double width, double height, Paint fill, Controller controller) throws Exception {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
		
	}
	
    private void createLayout () {
		
    	BorderPane border = new BorderPane();
        //System.out.println(controller.getScene());
        border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
        border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        ToolBar toolbar = createtoolBar();
        toolbar.setPrefHeight(50);
        
        border.setTop(toolbar);
		HBox hBox = new HBox();
	    hBox.setSpacing(8);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.getChildren().addAll(createElements ());
	    
	    border.setCenter(hBox);  
	    Group root = (Group)this.getRoot();
        root.getChildren().add(border);
	}

	private ArrayList createElements() {
		ArrayList elements = new ArrayList ();	
		// TODO Auto-generated method stub
		Label label = new Label ("Enter new search name");
		TextField searchName = new TextField ();
		Label labelDescription = new Label ("Enter new search description");
		TextField searchdescription = new TextField ();
		
		Button next = new Button ("Next");
		next.setOnAction(ButtonHandlers.SearchNewGeolocation(controller,searchName, searchdescription));
		elements.add(label);
		elements.add(searchName);
		elements.add(labelDescription);
		elements.add(searchdescription);
		elements.add(next);
		
		return elements;
	}
	
private ToolBar  createtoolBar () {
		
	     ToolBar toolbar =null;
		Button back;
		try {
			back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
			back.setOnAction(ButtonHandlers.SearchMain(controller));
			 toolbar = new ToolBar(back);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toolbar;
	}
	
}
