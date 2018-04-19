package scenes;

import java.util.ArrayList;

import controller.Controller;
import handlers.ButtonHandlers;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import utils.ElementConstructors;

public class NewSearchScreenKeywords extends Scene{
    
	private Controller controller;
	private TilePane keywords;
	private ArrayList keywordsList = new ArrayList ();
	
	public NewSearchScreenKeywords(Parent root, double width, double height, Paint fill, Controller controller) throws Exception {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
		
	}
	
    private void createLayout () {
		//MultiColumnList list = new MultiColumnList ();
    	
    	BorderPane border = new BorderPane();
    	
    	keywords = new TilePane();
    	keywords.setPrefColumns(10);
    	keywords.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        //System.out.println(controller.getScene());
        border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
        border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        ToolBar toolbar = createtoolBar();
        toolbar.setPrefHeight(50);
        
        border.setTop(toolbar);
		
	    keywords.setAlignment(Pos.TOP_LEFT);
	    keywords.getChildren().addAll(createElements ());
	    
	    
	    ScrollPane scroll = new ScrollPane(keywords);
	     
	    scroll.prefHeightProperty().bind(controller.primaryStage.heightProperty());
	    scroll.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        border.setCenter(scroll);
	    Group root = (Group)this.getRoot();
        root.getChildren().add(border);
	}
    
   private ArrayList createElements() {
		ArrayList keywords = new ArrayList();
		//Text defaultText = new Text ("Add some keywords");
		//keywords.add(defaultText);
		return keywords;
	}

private ToolBar  createtoolBar () {
		
		Button  back = ElementConstructors.createSmallButtonWithImage(new Image(getClass().getResourceAsStream("/images/left.png")),"Back");
		back.setOnAction(ButtonHandlers.SearchNewGeolocation(controller));
		
		Button  clear = ElementConstructors.createSmallButtonWithText("Clear");
		clear.setOnAction(ButtonHandlers.ClearKeywords(keywords, keywordsList));
		Button  fromFile = ElementConstructors.createSmallButtonWithText("Add From File");
		fromFile.setOnAction(ButtonHandlers.OpenKeywordsFromFile(controller,keywords,keywordsList));
		
		Label label = new Label ("Enter Search term:");
		TextField manualText = new TextField ();
		Button  manual = ElementConstructors.createSmallButtonWithText("Add Search Term");
		manual.setOnAction(ButtonHandlers.AddKeywordManual(keywords, keywordsList,manualText));
		
		Button  start = ElementConstructors.createSmallButtonWithText("Start Search!");
		
		
		
		ToolBar toolbar = new ToolBar(back, clear, fromFile, label, manualText, manual,start);
		return toolbar;
	}
   
}
