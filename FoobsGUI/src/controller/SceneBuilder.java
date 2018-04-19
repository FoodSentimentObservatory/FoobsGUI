package controller;

import Const.GlobalConts;
import javafx.scene.Group;
import javafx.scene.Scene;
import scenes.HomeScreen;
import scenes.NewSearchScreenGeolocation;
import scenes.NewSearchScreenKeywords;
import scenes.NewSearchScreenSearchName;
import scenes.SearchScreenMain;

public class SceneBuilder {
    
	public Scene homeScene;
	public Scene searchHome;
	public Scene searchNewGeolocation;
	public Scene searchNewSearchName; 
	public Scene searchNewKeywords; 
	
	public SceneBuilder (Controller controller) throws Exception {
		this.homeScene = new HomeScreen(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchHome = new SearchScreenMain(new Group(),   GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchNewGeolocation = new NewSearchScreenGeolocation(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchNewSearchName = new NewSearchScreenSearchName(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchNewKeywords = new NewSearchScreenKeywords(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		
	}
	
}
