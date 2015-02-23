package ait.ffma.service.preservation.riskmanagement.api.measurement;

public class DbscanObject {
	
	public enum pointType {
		NOISY,
		CLUSTER
	}
	
	private String sourceId;
	private double score;
	private String availability;
	private String extension;
	private String mimetype;
	private String formatname;
	private String software;
	
	private int clusterNumber;
	private boolean isVisited;
	private int neighborPointsCount;
	private pointType type;
//	private String parent;
	private String neighborId;
	
	public String getNeighborId() {
		return neighborId;
	}
	public void setNeighborId(String neighborId) {
		this.neighborId = neighborId;
	}
//	public String getParent() {
//		return parent;
//	}
//	public void setParent(String parent) {
//		this.parent = parent;
//	}
	public pointType getType() {
		return type;
	}
	public void setType(pointType type) {
		this.type = type;
	}
	public int getNeighborPointsCount() {
		return neighborPointsCount;
	}
	public void setNeighborPointsCount(int neighborPointsCount) {
		this.neighborPointsCount = neighborPointsCount;
	}
	public int getClusterNumber() {
		return clusterNumber;
	}
	public void setClusterNumber(int clusterNumber) {
		this.clusterNumber = clusterNumber;
	}
	public boolean isVisited() {
		return isVisited;
	}
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getFormatname() {
		return formatname;
	}
	public void setFormatname(String formatname) {
		this.formatname = formatname;
	}
	public String getSoftware() {
		return software;
	}
	public void setSoftware(String software) {
		this.software = software;
	}

}
