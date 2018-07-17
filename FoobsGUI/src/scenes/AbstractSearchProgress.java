package scenes;

import java.util.ArrayList;

import controller.Controller;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import status.SearchInfoBarVBox;

public abstract class AbstractSearchProgress extends BorderPane {

	protected ArrayList searches = new ArrayList ();
	protected Controller controller;
	protected SearchInfoBarVBox searchViewPanel;
	
	public AbstractSearchProgress(Controller controller) {	
		this.controller = controller;
		createLayout ();
	}
    
	
	private void createLayout ()  {
		
	    this.prefHeightProperty().bind(controller.primaryStage.heightProperty().subtract(65));
	    this.prefWidthProperty().bind(controller.primaryStage.widthProperty());
	    ToolBar toolbar = createtoolBar();
       
        this.setTop(toolbar);
        searchViewPanel = new SearchInfoBarVBox();
        searchViewPanel.setSpacing(10);
        ScrollPane scroll = new ScrollPane (searchViewPanel);
	    this.setCenter(scroll);
	}
 
	public SearchInfoBarVBox getSearchInfoBarVBox() {
		return searchViewPanel;
	}

	abstract ToolBar createtoolBar();
	
	
}
