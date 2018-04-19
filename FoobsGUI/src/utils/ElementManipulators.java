package utils;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class ElementManipulators {

	
	public static void addItemsToList(FlowPane flow,ArrayList listComponents) {
	    
	    if (listComponents.size() ==0) {
	    	flow.getChildren().addAll(new Label ("No items"));
	    }
	    else {
	    flow.getChildren().addAll(listComponents);
	    }
	   
	}
	
}
