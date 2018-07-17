package collector.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sun.xml.bind.v2.runtime.Location;

@Entity
@Table(name = "SearchSubNode")
public class SearchSubNodeEntity {
    
	public SearchSubNodeEntity (String label, String description, LocationEntity location) {
		this.location = location;
		this.description = description;
		this.searchNodeLabel = label;
	}
	 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id;
	
	@Column(name = "SearchNodeLabel")
	private String searchNodeLabel;
	
	@Column(name = "Description")
	private String description;
	
	@Column(name = "SearchMainId")
	private int searchMainId;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "Location")
	private LocationEntity location;
	
	
	
	public LocationEntity getLocationId() {
		return location;
	}

	public void setLocationId(LocationEntity location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSearchNodeLabel() {
		return searchNodeLabel;
	}

	public void setSearchNodeLabel(String searchNodeLabel) {
		this.searchNodeLabel = searchNodeLabel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSearchMainId() {
		return searchMainId;
	}

	public void setSearchMainId(int searchMainId) {
		this.searchMainId = searchMainId;
	}

	
}
