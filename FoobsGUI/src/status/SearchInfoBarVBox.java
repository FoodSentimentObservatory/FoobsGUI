package status;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SearchInfoBarVBox extends VBox{

	public SearchInfoBarVBox () {
		
	}
	
	public void write(String string) {
		Platform.runLater(new Runnable() {
	         @Override public void run() {
		Text txt = new Text (string +"\n");
		SearchInfoBarVBox.this.getChildren().add(txt);
	         }});
	}

}
