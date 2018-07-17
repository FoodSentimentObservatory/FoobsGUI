package collector.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Location")
public class LocationEntity {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO,generator="native")
   @GenericGenerator(name="native",strategy="native")
   private int id;

   @Column(name="locationType")
   private String locationType;

   @Column(name="displayString")
   private String displayString;

   
   
   
   @OneToOne(fetch=FetchType.LAZY,mappedBy="locationId",cascade=CascadeType.ALL)
   private GeoPointEntity geoPoint;
   
   @OneToOne(fetch=FetchType.LAZY,mappedBy="locationId",cascade=CascadeType.ALL)
   private GeoRadiusEntity geoRadius;
   
   @OneToOne(fetch=FetchType.LAZY,mappedBy="locationId",cascade=CascadeType.ALL)
   private PostEntity post;
   

   /**
    * @return the id
    */
   public int getId() {
      return id;
   }
   
   public void setId(int id) {
	       this.id = id ;
	   }

   /**
    * @return the locationType
    */
   public String getLocationType() {
      return locationType;
   }

   /**
    * @param locationType the locationType to set
    */
   public void setLocationType(String locationType) {
      this.locationType = locationType;
   }

   /**
    * @return the displayString
    */
   public String getDisplayString() {
      return displayString;
   }

   /**
    * @param displayString the displayString to set
    */
   public void setDisplayString(String displayString) {
      this.displayString = displayString;
   }

   
   /**
    * @return the geoPoint
    */
   public GeoRadiusEntity getRadius() {
      return geoRadius;
   }

   /**
    * @param geoPoint the geoPoint to set
    */
   public void setGeoRadius(GeoRadiusEntity geoRadius) {
      this.geoRadius = geoRadius;
   }

   /**
    * @return the geoPoint
    */
   public GeoPointEntity getGeoPoint() {
      return geoPoint;
   }

   /**
    * @param geoPoint the geoPoint to set
    */
   public void setGeoPoint(GeoPointEntity geoPoint) {
      this.geoPoint = geoPoint;
   }

public PostEntity getPost() {
	return post;
}

public void setPost(PostEntity post) {
	this.post = post;
}

}
