package application;
	
import java.sql.SQLException;

import Const.GlobalConts;
import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			
			
			HBox laodingBox = new HBox ();
			laodingBox.getChildren().add(new Text("loading"));
			Scene loading = new Scene(laodingBox);
			laodingBox.setPrefSize(GlobalConts.MAIN_WINDOW_WIDTH, GlobalConts.MAIN_WINDOW_HEIGHT);     
			primaryStage.setScene(loading);
			primaryStage.show();
			Controller controller = new Controller (primaryStage) ;
			
			controller.initializeScenes();
			BorderPane border = new BorderPane();
		    border.prefHeightProperty().bind(primaryStage.heightProperty());
		    border.prefWidthProperty().bind(primaryStage.widthProperty());
			border.setBottom(controller.topInfoBar.infoBar);
			
			Scene rootScene = new Scene (border);
			primaryStage.setScene(rootScene);
			
			
			//controller.setHome();
			controller.setLogin();
			primaryStage.show();
			primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

		        @Override
		        public void handle(WindowEvent event) {
		            Platform.runLater(new Runnable() {

		                @Override
		                public void run() {
		                    System.out.println("Application Closed by click to Close Button(X)");
		                    
		                    if (controller.con!= null) {
		                    	System.out.println("Colosing Db connection");
		                    	try {
									controller.con.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                    	controller.getSessionFactory().close();
		                    }
		                    System.exit(0);
		                }
		            });
		        }
		    });
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
