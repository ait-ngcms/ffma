package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class FormatRiskAnalysisReport extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 2648429127507919534L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Extension                   { public Class<?> evalType() { return String.class; } },
		VendorsCount                { public Class<?> evalType() { return Integer.class; } },
		SoftwareCount        		{ public Class<?> evalType() { return Integer.class; } },
		DescriptionCount        	{ public Class<?> evalType() { return Integer.class; } },
		Versions                	{ public Class<?> evalType() { return Integer.class; } },
		HasCreationDate             { public Class<?> evalType() { return Boolean.class; } },
		IsSupportedByVendor      	{ public Class<?> evalType() { return Boolean.class; } },
		IsFrequentlyUsedVersion     { public Class<?> evalType() { return Boolean.class; } },
		IsCompressedFileFormat      { public Class<?> evalType() { return Boolean.class; } },
		HasCreatorInformation       { public Class<?> evalType() { return Boolean.class; } },
		HasPublisherInformation     { public Class<?> evalType() { return Boolean.class; } },
		HasDigitalRightsInformation { public Class<?> evalType() { return Boolean.class; } },
		IsFileMigrationSupported    { public Class<?> evalType() { return Boolean.class; } },
		HasObjectPreview            { public Class<?> evalType() { return Boolean.class; } },
		IsWellDocumented            { public Class<?> evalType() { return Boolean.class; } },
		IsSupportedByWebBrowsers    { public Class<?> evalType() { return Boolean.class; } },
		HasHomepage                 { public Class<?> evalType() { return Boolean.class; } },
		HasGenre                    { public Class<?> evalType() { return Boolean.class; } },
		ExistencePeriod             { public Class<?> evalType() { return Boolean.class; } },
		Complexity                  { public Class<?> evalType() { return Boolean.class; } },
		Dissemination               { public Class<?> evalType() { return Boolean.class; } },
		Standardisation             { public Class<?> evalType() { return Boolean.class; } },
		Outdated                    { public Class<?> evalType() { return Boolean.class; } },
		MimeType                    { public Class<?> evalType() { return Boolean.class; } };

		public String evalName() { return this.name(); }
	}

	private boolean isEmpty = true;
	
	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param ffmaObjectName
	 */
	public FormatRiskAnalysisReport () {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
		put(FieldsEnum.VendorsCount.name(), 0);
		put(FieldsEnum.SoftwareCount.name(), 0);
		put(FieldsEnum.DescriptionCount.name(), 0);
		put(FieldsEnum.Versions.name(), 0);
		put(FieldsEnum.HasCreationDate.name(), false);
		put(FieldsEnum.HasCreatorInformation.name(), false);
		put(FieldsEnum.HasDigitalRightsInformation.name(), false);
		put(FieldsEnum.HasPublisherInformation.name(), false);
		put(FieldsEnum.IsCompressedFileFormat.name(), false);
		put(FieldsEnum.IsFileMigrationSupported.name(), false);
		put(FieldsEnum.IsFrequentlyUsedVersion.name(), false);
		put(FieldsEnum.IsSupportedByVendor.name(), false);
		put(FieldsEnum.IsSupportedByWebBrowsers.name(), false);
		put(FieldsEnum.HasHomepage.name(), false);
		put(FieldsEnum.HasGenre.name(), false);
		put(FieldsEnum.ExistencePeriod.name(), false);
		put(FieldsEnum.Complexity.name(), false);
		put(FieldsEnum.Dissemination.name(), false);
		put(FieldsEnum.Standardisation.name(), false);
		put(FieldsEnum.Outdated.name(), false);
		put(FieldsEnum.MimeType.name(), false);
	}
	
	/**
	 * Constructor by component name
	 * @param ffmaObjectName
	 */
	public FormatRiskAnalysisReport (boolean isEmpty) {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
		if (!isEmpty) {
			put(FieldsEnum.VendorsCount.name(), 0);
			put(FieldsEnum.SoftwareCount.name(), 0);
			put(FieldsEnum.DescriptionCount.name(), 0);
			put(FieldsEnum.Versions.name(), 0);
		}
	}
	
	/**
	 * @return the extension
	 */
	@XmlElement
	public String getExtension() {
		return getString(FieldsEnum.Extension.name());
	}

	public void setExtension(String extension) {
		this.put(FieldsEnum.Extension.name(), extension);
	}

	/**
	 * @return the VendorsCount
	 */
	@XmlElement
	public Integer getVendorsCount() {
		return (Integer) get(FieldsEnum.VendorsCount.name());
	}

	public void setVendorsCount(Integer vendorsCount) {
		this.put(FieldsEnum.VendorsCount.name(), vendorsCount);
		isEmpty = false;
	}

	/**
	 * @return the SoftwareCount
	 */
	@XmlElement
	public Integer getSoftwareCount() {
		return (Integer) get(FieldsEnum.SoftwareCount.name());
	}

	public void setSoftwareCount(Integer softwareCount) {
		this.put(FieldsEnum.SoftwareCount.name(), softwareCount);
		isEmpty = false;
	}

	/**
	 * @return the DescriptionCount
	 */
	@XmlElement
	public Integer getDescriptionCount() {
		return (Integer) get(FieldsEnum.DescriptionCount.name());
	}

	public void setDescriptionCount(Integer descriptionCount) {
		this.put(FieldsEnum.DescriptionCount.name(), descriptionCount);
		isEmpty = false;
	}

	/**
	 * @return the Versions
	 */
	@XmlElement
	public Integer getVersions() {
		return (Integer) get(FieldsEnum.Versions.name());
	}

	public void setVersions(Integer versions) {
		this.put(FieldsEnum.Versions.name(), versions);
		isEmpty = false;
	}

	/**
	 * @return the HasCreationDate
	 */
	@XmlElement
	public Boolean getHasCreationDate() {
		return (Boolean) get(FieldsEnum.HasCreationDate.name());
	}

	public void setHasCreationDate(Boolean hasCreationDate) {
		this.put(FieldsEnum.HasCreationDate.name(), hasCreationDate);
	}

	/**
	 * @return the IsSupportedByVendor
	 */
	@XmlElement
	public Boolean getIsSupportedByVendor() {
		return (Boolean) get(FieldsEnum.IsSupportedByVendor.name());
	}

	public void setIsSupportedByVendor(Boolean isSupportedByVendor) {
		this.put(FieldsEnum.IsSupportedByVendor.name(), isSupportedByVendor);
	}

	/**
	 * @return the IsFrequentlyUsedVersion
	 */
	@XmlElement
	public Boolean getIsFrequentlyUsedVersion() {
		return (Boolean) get(FieldsEnum.IsFrequentlyUsedVersion.name());
	}

	public void setIsFrequentlyUsedVersion(Boolean IsFrequentlyUsedVersion) {
		this.put(FieldsEnum.IsFrequentlyUsedVersion.name(), IsFrequentlyUsedVersion);
	}

	/**
	 * @return the IsCompressedFileFormat
	 */
	@XmlElement
	public Boolean getIsCompressedFileFormat() {
		return (Boolean) get(FieldsEnum.IsCompressedFileFormat.name());
	}

	public void setIsCompressedFileFormat(Boolean isCompressedFileFormat) {
		this.put(FieldsEnum.IsCompressedFileFormat.name(), isCompressedFileFormat);
	}

	/**
	 * @return the HasCreatorInformation
	 */
	@XmlElement
	public Boolean getHasCreatorInformation() {
		return (Boolean) get(FieldsEnum.HasCreatorInformation.name());
	}

	public void setHasCreatorInformation(Boolean hasCreatorInformation) {
		this.put(FieldsEnum.HasCreatorInformation.name(), hasCreatorInformation);
	}

	/**
	 * @return the HasPublisherInformation
	 */
	@XmlElement
	public Boolean getHasPublisherInformation() {
		return (Boolean) get(FieldsEnum.HasPublisherInformation.name());
	}

	public void setHasPublisherInformation(Boolean hasPublisherInformation) {
		this.put(FieldsEnum.HasPublisherInformation.name(), hasPublisherInformation);
	}

	/**
	 * @return the HasDigitalRightsInformation
	 */
	@XmlElement
	public Boolean getHasDigitalRightsInformation() {
		return (Boolean) get(FieldsEnum.HasDigitalRightsInformation.name());
	}

	public void setHasDigitalRightsInformation(Boolean hasDigitalRightsInformation) {
		this.put(FieldsEnum.HasDigitalRightsInformation.name(), hasDigitalRightsInformation);
	}

	/**
	 * @return the IsFileMigrationSupported
	 */
	@XmlElement
	public Boolean getIsFileMigrationSupported() {
		return (Boolean) get(FieldsEnum.IsFileMigrationSupported.name());
	}

	public void setIsFileMigrationSupported(Boolean isFileMigrationSupported) {
		this.put(FieldsEnum.IsFileMigrationSupported.name(), isFileMigrationSupported);
	}

	/**
	 * @return the HasObjectPreview
	 */
	@XmlElement
	public Boolean getHasObjectPreview() {
		return (Boolean) get(FieldsEnum.HasObjectPreview.name());
	}

	public void setHasObjectPreview(Boolean hasObjectPreview) {
		this.put(FieldsEnum.HasObjectPreview.name(), hasObjectPreview);
	}

	/**
	 * @return the IsWellDocumented
	 */
	@XmlElement
	public Boolean getIsWellDocumented() {
		return (Boolean) get(FieldsEnum.IsWellDocumented.name());
	}

	public void setIsWellDocumented(Boolean isWellDocumented) {
		this.put(FieldsEnum.IsWellDocumented.name(), isWellDocumented);
	}

	/**
	 * @return the IsSupportedByWebBrowsers
	 */
	@XmlElement
	public Boolean getIsSupportedByWebBrowsers() {
		return (Boolean) get(FieldsEnum.IsSupportedByWebBrowsers.name());
	}

	public void setIsSupportedByWebBrowsers(Boolean isSupportedByWebBrowsers) {
		this.put(FieldsEnum.IsSupportedByWebBrowsers.name(), isSupportedByWebBrowsers);
	}

	@XmlElement
	public Boolean getHasHomepage() {
		return (Boolean) get(FieldsEnum.HasHomepage.name());
	}

	public void setHasHomepage(Boolean hasHomepage) {
		this.put(FieldsEnum.HasHomepage.name(), hasHomepage);
	}

	@XmlElement
	public Boolean getHasGenre() {
		return (Boolean) get(FieldsEnum.HasGenre.name());
	}

	public void setHasGenre(Boolean hasGenre) {
		this.put(FieldsEnum.HasGenre.name(), hasGenre);
	}

	@XmlElement
	public Boolean getExistencePeriod() {
		return (Boolean) get(FieldsEnum.ExistencePeriod.name());
	}

	public void setExistencePeriod(Boolean existencePeriod) {
		this.put(FieldsEnum.ExistencePeriod.name(), existencePeriod);
	}

	@XmlElement
	public Boolean getComplexity() {
		return (Boolean) get(FieldsEnum.Complexity.name());
	}

	public void setComplexity(Boolean complexity) {
		this.put(FieldsEnum.Complexity.name(), complexity);
	}

	@XmlElement
	public Boolean getDissemination() {
		return (Boolean) get(FieldsEnum.Dissemination.name());
	}

	public void setDissemination(Boolean dissemination) {
		this.put(FieldsEnum.Dissemination.name(), dissemination);
	}

	@XmlElement
	public Boolean getStandardisation() {
		return (Boolean) get(FieldsEnum.Standardisation.name());
	}

	public void setStandardisation(Boolean standardisation) {
		this.put(FieldsEnum.Standardisation.name(), standardisation);
	}

	@XmlElement
	public Boolean getOutdated() {
		return (Boolean) get(FieldsEnum.Outdated.name());
	}

	public void setOutdated(Boolean outdated) {
		this.put(FieldsEnum.Outdated.name(), outdated);
	}

	@XmlElement
	public Boolean getMimeType() {
		return (Boolean) get(FieldsEnum.MimeType.name());
	}

	public void setMimeType(Boolean mimeType) {
		this.put(FieldsEnum.MimeType.name(), mimeType);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

}
