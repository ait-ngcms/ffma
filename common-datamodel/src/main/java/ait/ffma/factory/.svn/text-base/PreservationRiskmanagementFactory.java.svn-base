package ait.ffma.factory;

import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.AgentComplexType;
import ait.ffma.domain.preservation.AgentIdentifierComplexType;
import ait.ffma.domain.preservation.EventComplexType;
import ait.ffma.domain.preservation.EventIdentifierComplexType;
import ait.ffma.domain.preservation.EventOutcomeDetailComplexType;
import ait.ffma.domain.preservation.EventOutcomeInformationComplexType;
import ait.ffma.domain.preservation.LinkingAgentIdentifierComplexType;
import ait.ffma.domain.preservation.riskmanagement.AitFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.DipSoftwareId;
import ait.ffma.domain.preservation.riskmanagement.DipVendorId;
import ait.ffma.domain.preservation.riskmanagement.FileFormatStatistics;
import ait.ffma.domain.preservation.riskmanagement.FormatRiskAnalysisReport;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.OverallRiskScoreReport;
import ait.ffma.domain.preservation.riskmanagement.PreservationDimension;
import ait.ffma.domain.preservation.riskmanagement.PreservationDimensions;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.domain.preservation.riskmanagement.RiskScoreReport;

public class PreservationRiskmanagementFactory implements ComponentFactory{

	
	/*
	 * (non-Javadoc)
	 * @see ait.ffma.factory.ComponentFactory#createDomainObject(java.lang.String)
	 */
	public FfmaDomainObject createDomainObject(String domainObjectName) {
		if(EventComplexType.class.getSimpleName().equals(domainObjectName))
			return new EventComplexType();
		else if(EventIdentifierComplexType.class.getSimpleName().equals(domainObjectName))
			return new EventIdentifierComplexType();
		else if(AgentComplexType.class.getSimpleName().equals(domainObjectName))
			return new AgentComplexType();
		else if(AgentIdentifierComplexType.class.getSimpleName().equals(domainObjectName))
			return new AgentIdentifierComplexType();
		else if(LinkingAgentIdentifierComplexType.class.getSimpleName().equals(domainObjectName))
			return new LinkingAgentIdentifierComplexType();
		else if(EventOutcomeInformationComplexType.class.getSimpleName().equals(domainObjectName))
			return new EventOutcomeInformationComplexType();
		else if(EventOutcomeDetailComplexType.class.getSimpleName().equals(domainObjectName))
			return new EventOutcomeDetailComplexType();
		else if(PronomFileFormat.class.getSimpleName().equals(domainObjectName))
			return new PronomFileFormat();
		else if(FreebaseFileFormat.class.getSimpleName().equals(domainObjectName))
			return new FreebaseFileFormat();
		else if(DBPediaFileFormat.class.getSimpleName().equals(domainObjectName))
			return new DBPediaFileFormat();
		else if(AitFileFormat.class.getSimpleName().equals(domainObjectName))
			return new AitFileFormat();
		else if(LODFormat.class.getSimpleName().equals(domainObjectName))
			return new LODFormat();
		else if(LODSoftware.class.getSimpleName().equals(domainObjectName))
			return new LODSoftware();
		else if(LODVendor.class.getSimpleName().equals(domainObjectName))
			return new LODVendor();
		else if(FileFormatStatistics.class.getSimpleName().equals(domainObjectName))
			return new FileFormatStatistics();
		else if(DipFormatId.class.getSimpleName().equals(domainObjectName))
			return new DipFormatId();
		else if(DipSoftwareId.class.getSimpleName().equals(domainObjectName))
			return new DipSoftwareId();
		else if(DipVendorId.class.getSimpleName().equals(domainObjectName))
			return new DipVendorId();
		else if(PreservationDimension.class.getSimpleName().equals(domainObjectName))
			return new PreservationDimension();
		else if(PreservationDimensions.class.getSimpleName().equals(domainObjectName))
			return new PreservationDimensions();
		else if(RiskScoreReport.class.getSimpleName().equals(domainObjectName))
			return new RiskScoreReport();
		else if(OverallRiskScoreReport.class.getSimpleName().equals(domainObjectName))
			return new OverallRiskScoreReport();
		else if(FormatRiskAnalysisReport.class.getSimpleName().equals(domainObjectName))
			return new FormatRiskAnalysisReport();
		
		return null;
	}

}
