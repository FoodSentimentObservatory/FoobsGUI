package collector.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Search")
public class SearchDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id;

	@Column(name = "StartOfSearch")
	private Date startOfSearch;
	@Column(name = "EndOfSearch")
	private Date endOfSearch;
	@Column(name = "SearchMain")
	private int searchMain;
	@Column(name = "SearchSubNode")
	private int searchSubNode;
	@Column(name = "SearchLeafNode")
	private int searchLeafNode;


	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "searchDetailsId", cascade = CascadeType.ALL)
	//private Set<PostEntity> posts = new HashSet<PostEntity>();

	public int getSearchMain() {
		return searchMain;
	}

	public void setSearchMain(int searchMain) {
		this.searchMain = searchMain;
	}

	public int getSearchSubNode() {
		return searchSubNode;
	}

	public void setSearchSubNode(int searchSubNode) {
		this.searchSubNode = searchSubNode;
	}

	public int getSearchLeafNodeId() {
		return searchLeafNode;
	}

	public void setSearchLeafNodeId(int searchLeafNode) {
		this.searchLeafNode = searchLeafNode;
	}

	public int getId() {
		return id;
	}

	public Date getStartOfSearch() {
		return startOfSearch;
	}

	public void setStartOfSearch(Date startOfSearch) {
		this.startOfSearch = startOfSearch;
	}

	public Date getEndOfSearch() {
		return endOfSearch;
	}

	public void setEndOfSearch(Date endOfSearch) {
		this.endOfSearch = endOfSearch;
	}

	
}