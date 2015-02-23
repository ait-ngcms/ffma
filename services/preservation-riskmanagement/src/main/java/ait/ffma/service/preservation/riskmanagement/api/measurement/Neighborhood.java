package ait.ffma.service.preservation.riskmanagement.api.measurement;

import java.util.ArrayList;
import java.util.List;

public class Neighborhood {

	private List<Neighbor> neighborhoodList = new ArrayList<Neighbor>();

	public List<Neighbor> getNeighborhoodList() {
		return neighborhoodList;
	}

	public void setNeighborhoodList(List<Neighbor> neighborhoodList) {
		this.neighborhoodList = neighborhoodList;
	}
}
