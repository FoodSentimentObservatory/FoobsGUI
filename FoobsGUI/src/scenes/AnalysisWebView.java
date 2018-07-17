package scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import controller.Controller;
import handlers.ButtonHandlers;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import utils.ElementConstructors;
import utils.ElementManipulators;

public class AnalysisWebView extends Scene {

	private FlowPane listOfSearchRadiuses;
	private ArrayList listComponents = new ArrayList ();
	private Controller controller;
	
	
	
	
//	final SwingNode swingNode = new SwingNode();
	WebEngine webEngine;
	
	public AnalysisWebView(Parent root, double width, double height, Paint fill, Controller controller) throws Exception {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
		
	}
    
	
	private void createLayout () throws Exception {
		
		
		 WebView browser = new WebView();
         webEngine = browser.getEngine();
         
         webEngine.load("http://localhost:8080");
         //webEngine.loadContent("http://localhost:8080");
       
        Group root = (Group)this.getRoot();
        
        BorderPane border = new BorderPane();
        //System.out.println(controller.getScene());
        border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
        border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        
        //border.setPrefSize(GlobalConts.MAIN_WINDOW_WIDTH, GlobalConts.MAIN_WINDOW_HEIGHT);        
        ToolBar toolbar = createtoolBar();
        toolbar.setPrefHeight(50);
        
        border.setTop(toolbar);
       // border.setLeft(addVBox());
        //addStackPane(hbox);         // Add stack to HBox in top region
        
        
        border.setCenter(browser);  
       
   
        
        root.getChildren().add(border);
        
        
	}
	
	
	
	
	public FlowPane addRadiusInput(ArrayList inputs) {
	    FlowPane flow = new FlowPane();
	    flow.setPadding(new Insets(15, 12, 15, 12));
	    flow.setPrefWidth(30);
	    flow.setMaxWidth(30);
	    flow.setVgap(4);
	    flow.setHgap(2);
	    flow.setPrefWrapLength(10); // preferred width allows for two columns
	    flow.setStyle("-fx-background-color: DAE6F3;");
    
	    flow.getChildren().addAll(inputs);
	    
	    return flow;
	}
	
	
	
	
    private ArrayList  createSearchListInput () {
		
		ArrayList inputs = new ArrayList ();
			
	
		
		Label latLabel = new Label("Lat:");
		TextField latField = new TextField ();
		inputs.add(latLabel);
		inputs.add(latField);
		
		Label lonLabel = new Label("Lon:");
		TextField lonField = new TextField ();
		inputs.add(lonLabel);
		inputs.add(lonField);
		
		Label radiusLabel = new Label("Radius:");
		TextField radField = new TextField ();
		inputs.add(radiusLabel);
		inputs.add(radField);
		
		Button submit = new Button("Submit");
		inputs.add(submit);
		
		return inputs;
	}
	
	
	private ArrayList  createRadiusInput () {
		
		ArrayList inputs = new ArrayList ();
			
	
		
		Label latLabel = new Label("Lat:");
		TextField latField = new TextField ();
		inputs.add(latLabel);
		inputs.add(latField);
		
		Label lonLabel = new Label("Lon:");
		TextField lonField = new TextField ();
		inputs.add(lonLabel);
		inputs.add(lonField);
		
		Label radiusLabel = new Label("Radius:");
		TextField radField = new TextField ();
		inputs.add(radiusLabel);
		inputs.add(radField);
		
		Button submit = new Button("Add Radius");
	//	submit.setOnAction(ButtonHandlers.AddRadius(latField,lonField,radField,listOfSearchRadiuses,listComponents,map,controller,swingNode));
	//	submit.setOnAction(ButtonHandlers.AddRadius(latField,lonField,radField,listOfSearchRadiuses,listComponents,webEngine,controller));
		
		inputs.add(submit);
		
		return inputs;
	}
	
	private ToolBar  createtoolBar () {
		
		ToolBar toolbar = null;
		try {
			Button back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
			back.setOnAction(ButtonHandlers.Home(controller));
			Button  next = ElementConstructors.createSmallButtonWithText("Next");
			//next.setOnAction(ButtonHandlers.SearchNewKeywords(controller));
			toolbar = new ToolBar(back,next);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return toolbar;
	}
	

}
