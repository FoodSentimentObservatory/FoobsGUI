package status;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InfoBarHBox {
	
	public Text txt = new Text ("Not connected to any database");
	public ToolBar infoBar = new ToolBar(txt);
	
	
	public InfoBarHBox () { 
	    txt.setFill(Color.RED);
	}

	public void setPositiveMessage (String message) {
		 System.out.println("Setting positive text");
		 txt.setText(message);
		 txt.setFill(Color.GREEN);
		// infoBar.getChildren().remove(0, infoBar.getChildren().size());
		// infoBar.getChildren().add(txt);
		 
		 
	}
	
	public void setWarningMessage (String message) {
		 txt.setText(message);
		 txt.setFill(Color.RED);
		// infoBar.getChildren().remove(0);
		// infoBar.getChildren().add(txt);
	}
}
