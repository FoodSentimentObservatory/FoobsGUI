package scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import controller.Controller;
import handlers.ButtonHandlers;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import status.NewSearchObject;
import utils.ElementConstructors;

public class NewSearchScreenKeywords extends Scene{
    
	private Controller controller;
	private TilePane keywords;
	private TilePane column2;
	private HashMap <String,ArrayList<String>> keywordsList = new HashMap <String,ArrayList<String>> ();
	private NewSearchObject newSearchObject;
	private ArrayList<String> keywordsString;
	TabPane tabPane;
	
	public NewSearchScreenKeywords(Parent root, double width, double height, Paint fill, Controller controller) throws Exception {
		super(root, width, height, fill);
		this.controller = controller;
		createLayout ();
		
	}
	
    private void createLayout () {
		//MultiColumnList list = new MultiColumnList ();
    	
    	BorderPane border = new BorderPane();
    	
    	
    	 tabPane = new TabPane();
        ArrayList <ScrollPane> tabs = new ArrayList <ScrollPane>();
    	
        //create column 1 - need to refactor this and make more generic
        TilePane col1 = new TilePane();
    	col1.setPrefColumns(2);
    	col1.prefWidthProperty().bind(controller.primaryStage.widthProperty());
    	col1.setAlignment(Pos.TOP_LEFT);
    	col1.getChildren().addAll(createElements ());
    	ScrollPane scrollCol1 = new ScrollPane(col1);
    	tabs.add(scrollCol1);
    	
    	TilePane col2 = new TilePane();
     	col2.setPrefColumns(2);
     	col2.prefWidthProperty().bind(controller.primaryStage.widthProperty());
     	col2.setAlignment(Pos.TOP_LEFT);
     	col2.getChildren().addAll(createElements ());
     	ScrollPane scrollCol2 = new ScrollPane(col2);
    	tabs.add(scrollCol2);
    	
    	
    	
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = new Tab();
            String tabLabel = "Col-" + i;
            tab.setText(tabLabel);
            keywordsList.put(tabLabel, new ArrayList <String>());
            
            tab.setContent(tabs.get(i));
            tabPane.getTabs().add(tab);
        }
    	
    	
    	
    	
    	
    	
        //System.out.println(controller.getScene());
        border.prefHeightProperty().bind(controller.primaryStage.heightProperty());
        border.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        ToolBar toolbar = createtoolBar();
        toolbar.setPrefHeight(50);
        
        border.setTop(toolbar);
		

	    scrollCol1.prefHeightProperty().bind(controller.primaryStage.heightProperty());
	    scrollCol1.prefWidthProperty().bind(controller.primaryStage.widthProperty());
        border.setCenter(tabPane);
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
		
		Button back;
		ToolBar toolbar=null;
		try {
			back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
		
		back.setOnAction(ButtonHandlers.SearchNewGeolocation(controller));
		
		Button  clear = ElementConstructors.createSmallButtonWithText("Clear");
		clear.setOnAction(ButtonHandlers.ClearKeywords(tabPane, keywordsList));
		Button  fromFile = ElementConstructors.createSmallButtonWithText("Add From File");
		fromFile.setOnAction(ButtonHandlers.OpenKeywordsFromFile(controller,tabPane,keywordsList));
		
		Label label = new Label ("Search term:");
		TextField manualText = new TextField ();
		Button  manual = ElementConstructors.createSmallButtonWithText("Add Search Term");
		manual.setOnAction(ButtonHandlers.AddKeywordManual(tabPane, keywordsList,manualText));
		
		Label labelPattern = new Label ("Pattern:");
		TextField patetrn = new TextField ();
		CheckBox allCombinations = new CheckBox("All Combinations");		
		
		Button  start = ElementConstructors.createSmallButtonWithText("Start Search!");
		start.setOnAction(ButtonHandlers.startSearch(controller,keywordsList,patetrn,allCombinations));
		
		
		
		 toolbar = new ToolBar(back, clear, fromFile, label, manualText, manual,labelPattern,patetrn,allCombinations,start);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toolbar;
	}
   
}
