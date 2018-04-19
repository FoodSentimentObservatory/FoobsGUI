package utils;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class MapUtils {

	public static  Geometry getPointBuffer(Point point, double distanceMeters) throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException, TransformException{
    
        CoordinateReferenceSystem pointCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem radiusCRS = CRS.decode("EPSG:32630");
        

        MathTransform transformToUtm = CRS.findMathTransform(pointCRS, radiusCRS);
        
        Geometry targetGeometry = JTS.transform( point, transformToUtm);
        
        Geometry buffer = targetGeometry.buffer(distanceMeters);
        buffer.setSRID(32630);
        
        MathTransform transformToGeo = CRS.findMathTransform(radiusCRS, pointCRS);

        Geometry bufferGeo = JTS.transform( buffer, transformToGeo);
        bufferGeo.setSRID(4326);
        
        
        return bufferGeo;
        
    }
	
}
