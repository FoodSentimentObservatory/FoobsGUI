package scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import Const.GlobalConts;
import controller.Controller;
import db.DbDataManager;
import handlers.ButtonHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.ElementConstructors;

public class DbConnMain extends Scene {
    
	private Controller controller;
	private ComboBox comboBox;
	
	public DbConnMain(Parent root, double width, double height, Paint fill, Controller controller) {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
	}
	
	private void createLayout () {
		BorderPane border = new BorderPane();
	    border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
	    border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
	    
		HBox hBox = new HBox();
	    hBox.setSpacing(8);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.getChildren().addAll(createElements ());
	    StackPane  mainPane = new StackPane(hBox);
	    mainPane.setPrefSize( GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT);
	    
	    mainPane.setPadding(new Insets(24));
	    border.setCenter(mainPane);
	    
	    //palceholder for database info
	    
	   
	    border.setTop(controller.topInfoBar.infoBar);
	    
	    
	    Group root = (Group)this.getRoot();
        root.getChildren().add(border);
	}

	private ArrayList createElements() {
		ArrayList elements = new ArrayList ();
		DbDataManager manager = new DbDataManager (null);
		ObservableList<String> options = FXCollections.observableArrayList(
	    		"No databases available"			       
	    );
		try {
			//manager.getDatabaseNames().forEach((name)-> elements.add(new Label (name.toString())));
		
			 options = 
				    FXCollections.observableArrayList(
				    		manager.getDatabaseNames()			       
				    );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			comboBox = new ComboBox(options);
			
			Button  connect = ElementConstructors.createSmallButtonWithText("connect");
			
			
			elements.add(new Label ("Available databases"));
			
			
			elements.add(comboBox);
			elements.add(connect);
			
			System.out.println("Selected database: "+ comboBox.getSelectionModel().getSelectedItem());
			//to do change event handler
			connect.setOnAction(ButtonHandlers.ConnectDatabase(controller,comboBox));
			
			
		
		return elements;
	}
}
