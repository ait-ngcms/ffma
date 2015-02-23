package ait.ffma.service.preservation.common.api.measurement;

import java.util.Calendar;
import java.util.Vector;

/**
 * @author Andrew Lindley (AIT) - andrew.lindley@ait.ac.at
 * @since 13.12.2010 
 *********************************************************
 * Copyright (c) 2010, 2011 The Ffma4Europeana Project Partners.
 *
 * All rights reserved. This program and the accompanying 
 * materials are made available under the terms of the 
 * European Union Public Licence (EUPL), version 1.1 which 
 * accompanies this distribution, and is available at 
 * http://ec.europa.eu/idabc/eupl.html
 *
 ***********************************************************
 * 
 */

public interface MeasurementEvent {

	/** The experiment stage */
    public static enum EXP_STAGE {
        /** MeasurementTarget being measured is an input to an experiment. */
        EXP_INPUT,
        /** MeasurementTarget being measured is during the execution of a workflow. */
        EXP_PROCESS,
        /** MeasurementTarget being measured is an output to an experiment */
        EXP_OUTPUT,
        /** MeasurementTarget is the overall experiment execution */
        EXP_OVERALL,
        /** MeasurementTarget is being measured outside of an experimental context. */
        NO_EXP,
    }
    
	/**
	 * @return
	 */
	public long getId();

	/**
	 * @param m2
	 */
	public void addMeasurement(Measurement m);

	/**
	 * @return
	 */
	public Vector<Measurement> getMeasurements();

	/**
	 * @return
	 */
	public int getMeasurmentsSize();

	/**
	 * @return
	 */
	public String getWorkflowStage();

	/**
	 * @return the experimentStage
	 */
	public EXP_STAGE getExperimentStage();

	/**
	 * @param experimentStage the experimentStage to set
	 */
	public void setExperimentStage(EXP_STAGE experimentStage);

	/**
	 * @return the stage
	 */
	public String getStage();

	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage);

	/**
	 * @return the agentType
	 */
	public MeasurementAgent getAgent();

	/**
	 * @param agentType the agentType to set
	 */
	public void setAgent(MeasurementAgent agent);

	/**
	 * @return the date
	 */
	public Calendar getDate();

	/**
	 * @return The date as a briefly formatted string. Done here because f:convertDateTime does not seem to be working.
	 */
	public String getShortDate();

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date);

	/**
	 * NOTE that this may not work if the event has been updated and persisted during the same POST.
	 */
	public void deleteMeasurementEvent();

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MeasurementEvent o);

}