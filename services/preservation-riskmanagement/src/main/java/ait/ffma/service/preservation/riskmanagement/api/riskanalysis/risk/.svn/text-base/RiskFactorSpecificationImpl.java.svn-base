package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.io.InputStream;

public class RiskFactorSpecificationImpl implements RiskFactorSpecification{

	public InputStream getRiskFactorSpecification() {
		InputStream in = RiskFactorSpecificationImpl.class.getResourceAsStream
        		(RiskUtils.getGeneralProperty("Riskanalysis.XML"));
		return in;
	}

	public void setRiskFactorSpecification(InputStream spec) {
		RiskUtils.setRiskAnalysis(spec);		
	}
}
