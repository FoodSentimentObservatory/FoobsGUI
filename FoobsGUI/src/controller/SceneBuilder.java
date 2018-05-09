package controller;

import Const.GlobalConts;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import scenes.AnalysisWebView;
import scenes.DbConnMain;
import scenes.HomeScreen;
import scenes.ManageSearches;
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
	public Scene analysis;
	public Scene databaseConfig; 
	public ManageSearches manageSearches; 
	
	public SceneBuilder (Controller controller) throws Exception {
		this.homeScene = new HomeScreen(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchHome = new SearchScreenMain(new Group(),   GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchNewGeolocation = new NewSearchScreenGeolocation(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchNewSearchName = new NewSearchScreenSearchName(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.searchNewKeywords = new NewSearchScreenKeywords(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.analysis = new AnalysisWebView(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		this.databaseConfig = new DbConnMain(new Group(), GlobalConts.MAIN_WINDOW_WIDTH,GlobalConts.MAIN_WINDOW_HEIGHT,  GlobalConts.MAIN_WINDOW_COLOR,controller);
		
		//new way of creating views -> previous ones need to be changed
		this.manageSearches = new ManageSearches (controller);
	}
	
}
