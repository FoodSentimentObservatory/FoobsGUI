package handlers;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;

import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.awt.PointShapeFactory.Circle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import controller.Controller;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import openGeoTools.Test;
import utils.ElementConstructors;
import utils.ElementManipulators;
import utils.MapUtils;

public class ButtonHandlers {
    
	
	public static EventHandler<ActionEvent> SearchMain (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchHome (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	
	public static EventHandler<ActionEvent> SearchNew (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchNewSearchName (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> SearchNewGeolocation (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchNewGeolocation (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> SearchNewKeywords (Controller controller) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	controller.setSearchNewKeywords (controller.primaryStage);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> RemoveKeyword (TilePane keywords, ArrayList keywordsList, HBox hbox, String line) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	keywords.getChildren().remove(hbox);
		    	keywordsList.remove(line);
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> ClearKeywords (TilePane keywords, ArrayList keywordsList) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.out.println("Clear Called");
		    	keywords.getChildren().clear();
		    	keywordsList.clear();
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> AddKeywordManual (TilePane keywords, ArrayList keywordsList, TextField manualText) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	String text = manualText.getText().trim();
		    	//repetition with code in OpenKeywordsFromFile - TO DO TIDY UP
		    	if (!text.equals("") &&!keywordsList.contains(text)) {
		    	keywordsList.add(text);
		    	HBox hbox = new HBox ();
    	        hbox.setSpacing(8);
    	        GridPane grid = new GridPane ();
 
    	        hbox.setPadding(new Insets(4));
    	        Text keyword = new Text (text);
    	        grid.add(keyword, 0, 0, 1, 1);
    	       
    	        //GridPane.setHalignment(keyword, HPos.LEFT);
    	        
    	        Button remove = ElementConstructors.createSmallButtonWithImage(new Image(getClass().getResourceAsStream("/images/delete_small.png")), "");
    	        remove.setOnAction(RemoveKeyword(keywords,keywordsList,hbox,manualText.getText()));
    	        grid.add(remove, 1, 0, 1, 1);
    	        
//TO DO fix the alignment of the remove button
    	       // hbox.getChildren().add(keyword);
    	       // hbox.getChildren().add(remove);
    	        hbox.getChildren().add(grid);
    	        
    	        keywords.getChildren().add(hbox);
		    	}
		    	
		    }
		};
		return eventHandler;
	}
	
	public static EventHandler<ActionEvent> OpenKeywordsFromFile (Controller controller, TilePane keywords, ArrayList keywordsList) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	FileChooser fileChooser = new FileChooser();
		    	File file = fileChooser.showOpenDialog(controller.primaryStage);
                if (file != null) {
                	
                	try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                	    StringBuilder sb = new StringBuilder();
                	    String line = br.readLine();

                	    while (line != null ) {  
                	    	line = line.trim();
                	        if (!line.equals("") &&!keywordsList.contains(line)) {
                	        keywordsList.add(line);
                	        
                	        HBox hbox = new HBox ();
                	        hbox.setSpacing(8);
                	        GridPane grid = new GridPane ();
  	       
                	        hbox.setPadding(new Insets(4));
                	        Text keyword = new Text (line);
                	        grid.add(keyword, 0, 0, 1, 1);
                	       
                	        //GridPane.setHalignment(keyword, HPos.LEFT);
                	        
                	        Button remove = ElementConstructors.createSmallButtonWithImage(new Image(getClass().getResourceAsStream("/images/delete_small.png")), "");
                	        remove.setOnAction(RemoveKeyword(keywords,keywordsList,hbox,line));
                	        grid.add(remove, 1, 0, 1, 1);
                	        
          //TO DO fix the alignment of the remove button
                	       // hbox.getChildren().add(keyword);
                	       // hbox.getChildren().add(remove);
                	        hbox.getChildren().add(grid);
                	        
                	        keywords.getChildren().add(hbox);
                	        }
                	        line = br.readLine();
                	    }
                	    //String everything = sb.toString();
                	} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
		    }
		};
		return eventHandler;
	}
	
	

/*
	public static EventHandler<ActionEvent> AddRadius(TextField latField, TextField lonField, TextField radField,
			FlowPane listOfSearchRadiuses, ArrayList listComponents, Test map, Controller controller, SwingNode swingNode) {
	
	
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
		@Override public void handle(ActionEvent e) {
	    	System.out.println("submit called");
	    	
	    	
	    	GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
	    	Point p = gf.createPoint(new Coordinate(-2.0943,57.1497));
	    	
	    	
			SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
			b.setName("Flag");
			
			//b.add( "classification", Integer.class );
			
			b.crs( DefaultGeographicCRS.WGS84 );
			//b.crs(map.getMapContent().getCoordinateReferenceSystem());
			b.add( "location", Point.class );
			
			final SimpleFeatureType TYPE = b.buildFeatureType();
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
			featureBuilder.add(p);
			SimpleFeature feature = featureBuilder.buildFeature(null);
			
            
			//List<SimpleFeature> list = new ArrayList<SimpleFeature> ();
			//list.add(feature);
			DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal", TYPE);
			featureCollection.add(feature);
			System.out.println("Features: "+featureCollection.size());
			//ListFeatureCollection col = new ListFeatureCollection(TYPE, featureCollection);
       
			//Color color = Color.RED;
			//Style style = SLD.createPointStyle("Star", Color.BLUE, Color.BLUE, 103.9f, 105);
			 Style style = SLD.createPointStyle("Circle", Color.BLUE, Color.BLUE, 0.3f, 10);
			//Style style = SLD.createPointStyle("hello", color, color, 0.5f, 10f);
			FeatureLayer layer = new FeatureLayer(featureCollection, style);
			layer.setVisible(true);
			layer.setTitle("Searches");
			map.getMapContent().layers().add(layer);
			
			//polygon?
			
			GeodeticCalculator calc = new  GeodeticCalculator(DefaultGeographicCRS.WGS84);
			  calc.setStartingGeographicPoint(p.getX(), p.getY());
			  calc.setDirection(0.0, 10000);
			  Point2D p2 = calc.getDestinationGeographicPoint();
			  calc.setDirection(90.0, 10000);
			  Point2D p3 = calc.getDestinationGeographicPoint();

			  double dy = p2.getY() - p.getY();
			  double dx = p3.getX() - p.getX();
			  double distance = (dy + dx) / 20.0;
			  Polygon p1 = (Polygon) p.buffer(distance);
			  
			    b = new SimpleFeatureTypeBuilder();
				b.setName("Flag");
				
				//b.add( "classification", Integer.class );
				
				b.crs( DefaultGeographicCRS.WGS84 );
				//b.crs(map.getMapContent().getCoordinateReferenceSystem());
				b.add( "location", Polygon.class );
				
				final SimpleFeatureType TYPE2 = b.buildFeatureType();
				 featureBuilder = new SimpleFeatureBuilder(TYPE2);
				featureBuilder.add(p1);
				 feature = featureBuilder.buildFeature(null);
				  featureCollection = new DefaultFeatureCollection("internal", TYPE2);
					featureCollection.add(feature);
			
					  style = SLD.createPolygonStyle(Color.RED, Color.RED, 0.5f);
						
						 layer = new FeatureLayer(featureCollection, style);
						layer.setVisible(true);
						layer.setTitle("hd");
						map.getMapContent().layers().add(layer);
			
			
			// end 
			
			System.out.println("Layers: "+map.getMapContent().layers().size());
			swingNode.getContent().repaint();
			System.out.println(map.getMapContent().layers().toArray().toString());
			
			listComponents.add(new Label ("Lat:"+latField.getText()+" Lon:"+lonField.getText()+ " Rad:"+radField.getText()));
			listOfSearchRadiuses.getChildren().remove(0,listOfSearchRadiuses.getChildren().size());
			listOfSearchRadiuses.getChildren().addAll(listComponents);
			
	    }
	};
	return eventHandler;
}
*/

	public static EventHandler<ActionEvent> AddRadius(TextField latField, TextField lonField, TextField radField,
			FlowPane listOfSearchRadiuses, ArrayList listComponents, WebEngine webEngine, Controller controller) {
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
		    	System.out.println("submit called");
		    	UUID id = UUID.randomUUID();
		    	String parameters = "{\"lat\":"+ latField.getText()+",\"lon\": "+lonField.getText()+",\"rad\": "+radField.getText()+",\"id\": \""+id+"\"}";
		    	System.out.println(parameters);
		    	webEngine.executeScript("document.addMarker('"+parameters+"')");
		    	VBox wrapper = new VBox (); 
		    	Text t = new Text();
		    	t.setText("Lat:"+latField.getText()+"\nLon:"+lonField.getText()+ "\nRad:"+radField.getText());
		    	//listComponents.add(new Label ("Lat:"+latField.getText()+" Lon:"+lonField.getText()+ " Rad:"+radField.getText()));
		    	
		    	Button remove = new Button ("remove");
		    	remove.setOnAction(RemoveRadiusFromList(wrapper,listOfSearchRadiuses,listComponents,id,webEngine));
		    	wrapper.getChildren().add(t);
		    	wrapper.getChildren().add(remove);
		    	//listComponents.add(t);
		    	//listComponents.add(remove);
		    	listComponents.add(wrapper);
		    	listOfSearchRadiuses.getChildren().remove(0,listOfSearchRadiuses.getChildren().size());
				listOfSearchRadiuses.getChildren().addAll(listComponents);
				
			  }
		};
		return eventHandler;
}
	
	public static EventHandler<ActionEvent> RemoveRadiusFromList(VBox wrapper, FlowPane listOfSearchRadiuses, ArrayList listComponents,UUID radiusId,WebEngine webEngine) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
		    	System.out.println("remove called");
		    	System.out.println("removing marker: \""+ radiusId+"\"");
		    	
		    	String parameters = "{\"id\":\""+radiusId+"\"}";
		    	webEngine.executeScript("document.removeMarker('"+parameters+"')");
		    	listComponents.remove(wrapper);
		    	listOfSearchRadiuses.getChildren().remove(0,listOfSearchRadiuses.getChildren().size());
				//listOfSearchRadiuses.getChildren().addAll(listComponents);
		    	System.out.println(listComponents.size());
				ElementManipulators.addItemsToList(listOfSearchRadiuses,listComponents);
			  }
		};
		return eventHandler;
}
	
}
