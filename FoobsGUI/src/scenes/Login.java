package scenes;


import java.util.ArrayList;

import controller.Controller;
import handlers.ButtonHandlers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Login extends BorderPane {

	
	
	private Controller controller;
	
	BorderPane border;
	
	
	

	
	public Login( Controller controller) throws Exception {
		
		this.controller = controller;
		createLayout ();
		
	}
    
	
	private void createLayout () throws Exception {
		
		this.prefHeightProperty().bind(controller.primaryStage.heightProperty());
		   // border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
		this.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        
       
        
  
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        userTextField.setText("milan");

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        pwBox.setText("hello");
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        btn.setOnAction(ButtonHandlers.Login (actiontarget,userTextField, pwBox, controller));
        
        this.setCenter(grid);    
       
        
        
	}
	
	
	
	
	
	
	

}
