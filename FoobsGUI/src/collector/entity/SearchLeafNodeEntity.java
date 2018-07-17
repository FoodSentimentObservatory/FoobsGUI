package collector.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SearchLeafNode")
public class SearchLeafNodeEntity {

	public SearchLeafNodeEntity (String label, String keywords) {
		this.leafSearchLabel = label;
		this.keywords = keywords;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id;
	
	@Column(name = "LeafSearchLabel")
	private String leafSearchLabel;
	
	@Column(name = "Keywords")
	private String keywords;

	@Column(name = "SearchSubNodeId")
	private int searchSubNodeId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLeafSearchLabel() {
		return leafSearchLabel;
	}

	public void setLeafSearchLabel(String leafSearchLabel) {
		this.leafSearchLabel = leafSearchLabel;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getSearchSubNodeId() {
		return searchSubNodeId;
	}

	public void setSearchSubNodeId(int searchSubNodeId) {
		this.searchSubNodeId = searchSubNodeId;
	}
	
	
}
