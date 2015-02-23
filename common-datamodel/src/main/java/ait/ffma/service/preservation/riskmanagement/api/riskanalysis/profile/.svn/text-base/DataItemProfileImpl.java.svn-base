package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile;

import java.util.List;
import ait.ffma.service.preservation.common.api.measurement.Measurement;

public class DataItemProfileImpl implements DataItemProfile {

	private List<Measurement> measurements;
	
	public DataItemProfileImpl(List<Measurement> measurements) {
		this.measurements = measurements;
	}
	
	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}


	/**
	 * Returns a list of all Measurements that have been performed on
	 * the DataItem at hand.
	 * @return
	 */
	public List<Measurement> getMeasurements() {
		return measurements;
	}

}