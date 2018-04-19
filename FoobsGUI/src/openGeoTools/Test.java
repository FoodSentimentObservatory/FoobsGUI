package openGeoTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.AbstractZoomTool;
import org.geotools.swing.tool.PanTool;
import org.geotools.swing.tool.ZoomInTool;
import org.geotools.swing.tool.ZoomOutTool;

/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 * <p>
 * This is the GeoTools Quickstart application used in documentationa and tutorials. *
 */
public class Test extends JMapPane implements ActionListener {

	
	JPanel buttons = new JPanel();
	protected JButton zoomIn, zoomOut, move;
	
    public Test () throws Exception {
    	System.out.println("hello");
        // display a data store file chooser dialog for shapefiles
        File file = new File ("src/images/ne_50m_admin_0_countries.shp");
        System.out.println("hello");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("Quickstart");
        
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        System.out.println("hello");
        // Now display the map
       //JMapFrame.showMap(map);
        
        this.setRenderer( new StreamingRenderer() );
        this.setMapContent( map );
        
        
        
        zoomIn = new JButton("Zoom in");
        buttons.add(zoomIn);
        zoomOut = new JButton("Zoom out");
        buttons.add(zoomOut);
        move = new JButton("Move");
        buttons.add(move);
        
        zoomIn.addActionListener(this);
        zoomOut.addActionListener(this);
        move.addActionListener(this);

        zoomIn.setActionCommand("zoomIn");
        zoomOut.setActionCommand("zoomOut");
        move.setActionCommand("move");
        
        
        this.add(buttons);
        
        //select defaul button to selected and assign zoomin tool
        ZoomInTool abs2 = new ZoomInTool(); 
        this.setCursorTool(abs2); 
        abs2.setZoom(AbstractZoomTool.DEFAULT_ZOOM_FACTOR);
        zoomIn.setSelected(true);
        
        this.addMouseListener(new MapMouseAdapter() {

            @Override
            public void onMouseClicked(MapMouseEvent ev) {
                // print the screen and world position of the mouse
                System.out.println("mouse click at");
                System.out.printf("  screen: x=%d y=%d \n", ev.getX(), ev.getY());

                DirectPosition2D pos = ev.getWorldPos();
                System.out.printf("  world: x=%.2f y=%.2f \n", pos.x, pos.y);
                
                
            }

            @Override
            public void onMouseEntered(MapMouseEvent ev) {
                System.out.println("mouse entered map pane");
            }

            @Override
            public void onMouseExited(MapMouseEvent ev) {
                System.out.println("mouse left map pane");
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("zoomOut".equals(e.getActionCommand())) {
			ZoomOutTool abs = new ZoomOutTool(); 
	        this.setCursorTool(abs); 
	        abs.setZoom(AbstractZoomTool.DEFAULT_ZOOM_FACTOR);
	        handleSelectedButtons (zoomOut);
        } else if ("zoomIn".equals(e.getActionCommand())) {
        	ZoomInTool abs2 = new ZoomInTool(); 
            this.setCursorTool(abs2); 
            abs2.setZoom(AbstractZoomTool.DEFAULT_ZOOM_FACTOR);
            handleSelectedButtons (zoomIn);
        }
		
        else if  ("move".equals(e.getActionCommand())) {
        	PanTool abs2 = new PanTool(); 
            this.setCursorTool(abs2); 
            handleSelectedButtons (move);
        }
		
	}
	
	private void handleSelectedButtons (JButton selected) {
		
		selected.setSelected(true);
		
		for (int i = 0; i <buttons.getComponentCount();i++) {
			if (selected!=buttons.getComponent(i)  ) {
				((JButton) buttons.getComponent(i)).setSelected(false);
			}
			
		}
		
	}
	
}
    
    
    
