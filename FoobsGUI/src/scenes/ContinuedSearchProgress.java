package scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import controller.Controller;
import handlers.ButtonHandlers;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import utils.ElementConstructors;

public class ContinuedSearchProgress extends AbstractSearchProgress {

	public ContinuedSearchProgress(Controller controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	ToolBar createtoolBar() {
		ToolBar toolbar = null;
		try {
			Button back = ElementConstructors.createSmallButtonWithImage(new Image(new FileInputStream(new File (System.getProperty("user.dir")+File.separator+"icons"+File.separator+"left.png"))),"Back");
			back.setOnAction(ButtonHandlers.AMnageExistingSearches(controller));
			
			Button continueSearch = ElementConstructors.createSmallButtonWithText("Stop");
			continueSearch.setAlignment(Pos.BASELINE_RIGHT);
			
			
			toolbar = new ToolBar(back,continueSearch);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toolbar;
	}

}
