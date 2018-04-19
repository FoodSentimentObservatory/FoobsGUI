package utils;

import Const.GlobalConts;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ElementConstructors {

	public static Button createBigButtonWithImage (Image searchImage) {
		Button button = new Button ();
		ImageView imgView = new ImageView(searchImage);
		imgView.setFitWidth(GlobalConts.BIG_BUTTON_WIDTH);
		imgView.setFitHeight(GlobalConts.BIG_BUTTON_HEIGHT);
		button.setGraphic(imgView);
		return button;
	}
	
	public static Button createBigButtonWithText (String text) {
		Button button = new Button (text);
		button.setPrefSize(GlobalConts.BIG_BUTTON_WIDTH, GlobalConts.BIG_BUTTON_HEIGHT);
	    //to do controll the dimensions of the buttoin
		return button;
	}
	
	public static Button createSmallButtonWithImage (Image searchImage, String text) {
		Button button = new Button ();
		ImageView imgView = new ImageView(searchImage);
		imgView.setFitWidth(GlobalConts.SMALL_BUTTON_WIDTH);
		imgView.setFitHeight(GlobalConts.SMALL_BUTTON_HEIGHT);
		button.setGraphic(imgView);
		button.setText(text);
		return button;
	}
	
	public static Button createSmallButtonWithText ( String text) {
		Button button = new Button ();
		//button.setPrefSize(GlobalConts.SMALL_BUTTON_WIDTH, GlobalConts.SMALL_BUTTON_HEIGHT);
		button.setText(text);
		return button;
	}
}
