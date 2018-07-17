package collector.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SearchMain")
public class SearchMainEntity {
	
	public SearchMainEntity (String label, String description) {
		this.mainSearchLabel = label;
		this.description = description;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id;
	
	@Column(name = "MainSearchLabel")
	private String mainSearchLabel;
	
	@Column(name = "Description")
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMainSearchLabel() {
		return mainSearchLabel;
	}

	public void setMainSearchLabel(String mainSearchLabel) {
		this.mainSearchLabel = mainSearchLabel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
