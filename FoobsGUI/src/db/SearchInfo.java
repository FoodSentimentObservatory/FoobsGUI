package db;

public  class SearchInfo {
	int id ;
	
	float lat =0;
	float lon =0;
	String started = "";
    String ended = "";
    float radius ;
    int locationId ;
	public String note = "";
    String keywords = "";
    
    //thi smight change when DB schema corrected
    String mainLabel = "";
    String subLabel = "";
	
	public SearchInfo(int id, String started, String ended, float f, int locationId, String note,
			String keywords) {
		//super();
		this.id = id;
		//!!!change as this is quite bad and should use proper dates
		this.started = started.split("\\.")[0];
		//TO DO if search terminated forcefully then this can be null
		//this.ended = ended.split("\\.")[0];
		this.ended = ended;
		this.radius = f;
		this.locationId = locationId;
		this.note = note;
		this.keywords = keywords;
		
		//thi smight change when DB schema corrected
		String [] parts = note.split("-");
		this.mainLabel = parts[parts.length-1];
		this.subLabel = parts[parts.length-2];
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStarted() {
		return started;
	}

	public void setStarted(String started) {
		this.started = started;
	}

	public String getEnded() {
		return ended;
	}

	public void setEnded(String ended) {
		this.ended = ended;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getMainLabel() {
		return mainLabel;
	}

	public void setMainLabel(String mainLabel) {
		this.mainLabel = mainLabel;
	}

	public String getSubLabel() {
		return subLabel;
	}

	public void setSubLabel(String subLabel) {
		this.subLabel = subLabel;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}
}
