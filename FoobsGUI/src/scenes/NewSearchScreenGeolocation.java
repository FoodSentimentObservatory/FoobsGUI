package scenes;


import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import Const.GlobalConts;
import collector.entity.SearchSubNodeEntity;
import controller.Controller;
import handlers.ButtonHandlers;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import status.NewSearchObject;
import utils.ElementConstructors;
import utils.ElementManipulators;

public class NewSearchScreenGeolocation extends Scene {

	private FlowPane listOfSearchRadiuses;
	private ArrayList listComponents = new ArrayList ();
	private Controller controller;
	private NewSearchObject newSearchObject ;
	private ArrayList <SearchSubNodeEntity> subSearches = new ArrayList <SearchSubNodeEntity> () ;

//	final SwingNode swingNode = new SwingNode();
	WebEngine webEngine;
	
	public NewSearchScreenGeolocation(Parent root, double width, double height, Paint fill, Controller controller) throws Exception {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
		
	}
    
	public void clearSubsearches () {
		subSearches.clear();
	}
	
	private void createLayout () throws Exception {
		
		//test adding map
       /* 
         map = new Test ();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	map.setPreferredSize(new Dimension (600,600));
                swingNode.setContent(map);
                
            }
        });
		*/
		
		//test web view map 
		 WebView browser = new WebView();
         webEngine = browser.getEngine();
         byte[] encoded = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+File.separator+"html"+File.separator+"map.html"));
         String content =  new String(encoded, Charset.defaultCharset());
        // System.out.println(content);
        
         webEngine.loadContent(content);
       
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
        
        
        VBox vbox = new VBox();
        /*
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefColumns(1);
        tile.setStyle("-fx-background-color: DAE6F3;");
        
        //tile.getChildren().add(swingNode);
        tile.getChildren().add(browser);
        tile.prefHeightProperty().bind(border.heightProperty());
        tile.prefWidthProperty().bind(border.widthProperty());
        */
       // tile.setPrefSize(600, 200);
        border.setCenter(browser);  
        listOfSearchRadiuses = new FlowPane ();
        listOfSearchRadiuses.setPadding(new Insets(15, 12, 15, 12));
        listOfSearchRadiuses.prefWidthProperty().bind(vbox.widthProperty());
        //listOfSearchRadiuses.setMaxWidth(30);
	    /*
        flow.setVgap(4);
	    flow.setHgap(2);
	    flow.setPrefWrapLength(10); // preferred width allows for two columns
	    */
        listOfSearchRadiuses.setStyle("-fx-background-color: DAE6F3;");
        ElementManipulators.addItemsToList(listOfSearchRadiuses,listComponents);
        
	    vbox.getChildren().addAll(addRadiusInput(createRadiusInput ()));	    
	    vbox.getChildren().add( new Separator()); 
	    
	    ScrollPane scroll = new ScrollPane (listOfSearchRadiuses);
	    scroll.prefWidthProperty().bind(vbox.widthProperty());
	    scroll.setPrefHeight(400);
	    
	    
	    vbox.getChildren().addAll(scroll);
	    border.setLeft(vbox);
   
        
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
			
		Label subLabel = new Label("Label:");
		TextField subField = new TextField ();
		inputs.add(subLabel);
		inputs.add(subField);
		
		Label descLabel = new Label("Label:");
		TextField descField = new TextField ();
		inputs.add(descLabel);
		inputs.add(descField);
		
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
		submit.setOnAction(ButtonHandlers.AddRadius(subField,descField,latField,lonField,radField,listOfSearchRadiuses,listComponents,webEngine,controller,subSearches));
		
		inputs.add(submit);
		
		return inputs;
	}
	
	private ToolBar  createtoolBar () {
		
		ToolBar toolbar = null;
		try {
			Button back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
			back.setOnAction(ButtonHandlers.SearchMain(controller));
			Button  next = ElementConstructors.createSmallButtonWithText("Next");
			next.setOnAction(ButtonHandlers.SearchNewKeywords(controller,subSearches));
			toolbar = new ToolBar(back,next);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return toolbar;
	}
	

}
