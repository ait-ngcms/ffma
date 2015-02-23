package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;



public final class RiskConstants {

	private RiskConstants() {
	} 

	/**
	 * String constants for the calculation model
	 */
	public static final String PROPERTIES_XML = "/ffma-preservation-riskmanagement-properties.xml";
	public static final String DEFAULT_PROPERTY_SET_XML = "/default-ffma-preservation-riskmanagement-propertyset.xml";
	public static final String DEFAULT_CLASSIFICATION_XML = "/default-ffma-preservation-riskmanagement-classification.xml";
	
	/**
	 * Property IDs
	 */
	public static final String SOFTWARE_VENDOR_PROPERTY_ID = "SOFTWARE_VENDOR_PROPERTY_ID";
	public static final String GRAPHICS_FILE_FORMATS_PROPERTY_ID = "GRAPHICS_FILE_FORMATS_PROPERTY_ID";
	public static final String VENDOR_PROPERTY_ID = "VENDOR_PROPERTY_ID"; 
	public static final String VENDOR_DBPEDIA2_PROPERTY_ID = "VENDOR_DBPEDIA2_PROPERTY_ID"; 
	public static final String FILE_SIZE_PROPERTY_ID = "FILE_SIZE_PROPERTY_ID"; 
	public static final String LAST_MODIFICATION_DATE_PROPERTY_ID = "LAST_MODIFICATION_DATE_PROPERTY_ID"; 
	public static final String PRONOM_UID_PROPERTY_ID = "PRONOM_UID_PROPERTY_ID";
	public static final String STANDARD_PROPERTY_ID = "STANDARD_PROPERTY_ID";
	public static final String LICENSE_PROPERTY_ID = "LICENSE_PROPERTY_ID";
	public static final String NAME_PROPERTY_ID = "NAME_PROPERTY_ID";
	public static final String RELEASE_DATE_PROPERTY_ID = "RELEASE_DATE_PROPERTY_ID";
	public static final String VERSION_PROPERTY_ID = "VERSION_PROPERTY_ID";
	public static final String FORMATS_PROPERTY_ID = "FORMATS_PROPERTY_ID";
	public static final String FORMATS_EXTENSIONS_PROPERTY_ID = "FORMATS_EXTENSIONS_PROPERTY_ID";
	public static final String SOFTWARE_PROPERTY_ID = "SOFTWARE_PROPERTY_ID";
	public static final String MIME_TYPE_PROPERTY_ID = "MIME_TYPE_PROPERTY_ID";
	public static final String COMPRESSION_TYPE_PROPERTY_ID = "COMPRESSION_TYPE_PROPERTY_ID";
	public static final String IMAGE_WIDTH_PROPERTY_ID = "IMAGE_WIDTH_PROPERTY_ID";
	public static final String IMAGE_HIGHT_PROPERTY_ID = "IMAGE_HIGHT_PROPERTY_ID";
	public static final String COLOR_PROPERTY_ID = "COLOR_PROPERTY_ID";
	public static final String BYTE_ORDER_PROPERTY_ID = "BYTE_ORDER_PROPERTY_ID";
	public static final String ORIENTATION_PROPERTY_ID = "ORIENTATION_PROPERTY_ID";
	public static final String VENDOR_DBPEDIA_YAGO_PROPERTY_ID = "VENDOR_DBPEDIA_YAGO_PROPERTY_ID";
	public static final String SOFTWARE_PLATFORM_PROPERTY_ID = "SOFTWARE_PLATFORM_PROPERTY_ID";
	public static final String SOFTWARE_LICENSE_PROPERTY_ID = "SOFTWARE_LICENSE_PROPERTY_ID";
	public static final String SOFTWARE_DEVELOPER_PROPERTY_ID = "SOFTWARE_DEVELOPER_PROPERTY_ID";
	public static final String PROGRAMMING_LANGUAGE_PROPERTY_ID = "PROGRAMMING_LANGUAGE_PROPERTY_ID";
	public static final String REVENUE_PROPERTY_ID = "REVENUE_PROPERTY_ID";
	public static final String GENRE_PROPERTY_ID = "GENRE_PROPERTY_ID";
	public static final String HOMEPAGE_PROPERTY_ID = "HOMEPAGE_PROPERTY_ID";
	public static final String WORKING_GROUPS_PROPERTY_ID = "WORKING_GROUPS_PROPERTY_ID";
	public static final String FB_SOFTWARE_PROPERTY_ID = "FB_SOFTWARE_PROPERTY_ID";
	public static final String FB_FILE_FORMAT_PROPERTY_ID = "FB_FILE_FORMAT_PROPERTY_ID"; 
	public static final String FB_SOFTWARE_OS_PLATFORM_PROPERTY_ID = "FB_SOFTWARE_OS_PLATFORM_PROPERTY_ID"; 
//	public static final String IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID = "IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID"; 
//	public static final String IS_OPEN_FILE_FORMAT_PROPERTY_ID = "IS_OPEN_FILE_FORMAT_PROPERTY_ID"; 
//	public static final String IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID = "IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID"; 
	public static final String WHICH_VERSION_OFFICIALLY_SUPPORTED_BY_VENDOR_PROPERTY_ID = "WHICH_VERSION_OFFICIALLY_SUPPORTED_BY_VENDOR_PROPERTY_ID"; 
//	public static final String WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID = "WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID"; 
//	public static final String IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID = "IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID"; 
//	public static final String HAS_CREATOR_INFORMATION_PROPERTY_ID = "HAS_CREATOR_INFORMATION_PROPERTY_ID"; 
//	public static final String HAS_PUBLISHER_INFORMATION_PROPERTY_ID = "HAS_PUBLISHER_INFORMATION_PROPERTY_ID"; 
//	public static final String HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID = "HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID"; 
//	public static final String IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID = "IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID"; 
//	public static final String HAS_CREATION_DATE_INFORMATION_PROPERTY_ID = "HAS_CREATION_DATE_INFORMATION_PROPERTY_ID"; 
	public static final String HAS_OBJECT_PREVIEW_PROPERTY_ID = "HAS_OBJECT_PREVIEW_PROPERTY_ID"; 
	public static final String BROKEN_OBJECTS_SCORE_PROPERTY_ID = "BROKEN_OBJECTS_SCORE_PROPERTY_ID"; 
	public static final String PROVENANCE_SCORE_PROPERTY_ID = "PROVENANCE_SCORE_PROPERTY_ID"; 
	public static final String CONTEXT_SCORE_PROPERTY_ID = "CONTEXT_SCORE_PROPERTY_ID"; 
	public static final String ACCESSIBILITY_SCORE_PROPERTY_ID = "ACCESSIBILITY_SCORE_PROPERTY_ID"; 
	
	/**
	 * Format risk constants
	 */
	public static final String SOFTWARE_COUNT_SCORE_PROPERTY_ID = "SOFTWARE_COUNT_SCORE_PROPERTY_ID"; // w 1.0
	public static final String VENDORS_COUNT_SCORE_PROPERTY_ID = "VENDORS_COUNT_SCORE_PROPERTY_ID"; // w 1.0
	public static final String VERSIONS_COUNT_SCORE_PROPERTY_ID = "VERSIONS_COUNT_SCORE_PROPERTY_ID"; // w 1.0
	public static final String HAS_DESCRIPTION_SCORE_PROPERTY_ID = "HAS_DESCRIPTION_SCORE_PROPERTY_ID"; // w 1.0
	public static final String HAS_CREATION_DATE_INFORMATION_PROPERTY_ID = "HAS_CREATION_DATE_INFORMATION_PROPERTY_ID"; // w 1.0
	public static final String HAS_CREATOR_INFORMATION_PROPERTY_ID = "HAS_CREATOR_INFORMATION_PROPERTY_ID"; // w 0.1
	public static final String HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID = "HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID"; // w 0.3
	public static final String HAS_OBJECT_PREVIEW_SCORE_PROPERTY_ID = "HAS_OBJECT_PREVIEW_SCORE_PROPERTY_ID";
	public static final String HAS_PUBLISHER_INFORMATION_PROPERTY_ID = "HAS_PUBLISHER_INFORMATION_PROPERTY_ID"; // w 0.1
	public static final String IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID = "IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID"; // w 0.2
	public static final String IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID = "IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID"; // w 1.0
	public static final String WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID = "WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID"; // w 0.3
	public static final String IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID = 
		"IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID"; // w 0.3
	public static final String IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID = "IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID"; // w 0.5
	public static final String WELL_DOCUMENTED_SCORE_PROPERTY_ID = "WELL_DOCUMENTED_SCORE_PROPERTY_ID";
	public static final String LAST_UPDATE_PROPERTY_ID = "LAST_UPDATE_PROPERTY_ID"; // w 1.0
	public static final String HAS_HOMEPAGE_SCORE_PROPERTY_ID = "HAS_HOMEPAGE_SCORE_PROPERTY_ID";
	public static final String HAS_GENRE_SCORE_PROPERTY_ID = "HAS_GENRE_SCORE_PROPERTY_ID";
	public static final String HAS_MIME_TYPE_PROPERTY_ID = "HAS_MIME_TYPE_PROPERTY_ID";
	public static final String EXISTENCE_PERIOD_PROPERTY_ID = "EXISTENCE_PERIOD_PROPERTY_ID";
	public static final String COMPLEXITY_PROPERTY_ID = "COMPLEXITY_PROPERTY_ID";
//	public static final String PSEUDOSTREAMING_PROPERTY_ID = "PSEUDOSTREAMING_PROPERTY_ID";
//	public static final String WEB_2_COMPATIBLE_PROPERTY_ID = "WEB_2_COMPATIBLE_PROPERTY_ID";
	public static final String IS_DISSEMINATED_PROPERTY_ID = "IS_DISSEMINATED_PROPERTY_ID";
	public static final String IS_OPEN_FILE_FORMAT_PROPERTY_ID = "IS_OPEN_FILE_FORMAT_PROPERTY_ID"; 
	public static final String IS_STANDARDISED_PROPERTY_ID = "IS_STANDARDISED_PROPERTY_ID";
	public static final String IS_ENCRYPTED_PROPERTY_ID = "IS_ENCRYPTED_PROPERTY_ID";
	public static final String IS_OUTDATED_PROPERTY_ID = "IS_OUTDATED_PROPERTY_ID";

	/**
	 * Property Set IDs
	 */
	public static final String VENDOR_PROPERTY_SET_ID = "VENDOR_PROPERTY_SET_ID";
	public static final String RENDERING_SOFTWARE_PROPERTY_SET_ID = "RENDERING_SOFTWARE_PROPERTY_SET_ID";
	public static final String AGGREGATED_PROPERTIES_PROPERTY_SET_ID = "AGGREGATED_PROPERTIES_PROPERTY_SET_ID";
	public static final String FORMAT_PROPERTY_SET_ID = "FORMAT_PROPERTY_SET_ID";
	public static final String PRESERVATION_DIMENSION_PROPERTY_SET_ID = "PRESERVATION_DIMENSION_PROPERTY_SET_ID";

	/**
	 * Definitions for data repositories
	 */
	public static final String DBPEDIA_PREFIXES = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX foaf: <http://xmlns.com/foaf/0.1/> PREFIX dc: <http://purl.org/dc/elements/1.1/> PREFIX : <http://dbpedia.org/resource/> PREFIX prop: <http://dbpedia.org/property/> PREFIX dbpedia: <http://dbpedia.org/> PREFIX skos: <http://www.w3.org/2004/02/skos/core#> PREFIX dbo: <http://dbpedia.org/ontology/>";
	public static final String DBPEDIA_DEFAULT_URI = "?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=";
	public static final String PRONOM_DEFAULT_URI = "http://www.nationalarchives.gov.uk/pronom/";
	public static final String FREEBASE_DEFAULT_URI = "http://api.freebase.com/api/service/mqlread?query=";

	/**
	 * DBPedia column names
	 */
	public static final String EXTENSION = "extension";
	public static final String SOFTWARE = "software";
	public static final String PRODUCT = "product";
	public static final String VENDOR = "company";
	public static final String NUMBER_EMPLOYEES = "numEmployees";
	public static final String SUBJECT = "subject";
	public static final String EMPLOYEES = "employees";
	public static final String ABSTRACT = "abstract";
	public static final String LABEL = "label";
	public static final String TYPE_CODE= "typeCode";
	public static final String COLUMN_NAME = "name";
	public static final String MIME = "mime";
	public static final String DEVELOPER = "developer";
	public static final String OWNER = "owner";
	public static final String COLUMN_RELEASED = "released";
	public static final String STANDARD = "standard";
	public static final String LATEST_RELEASE_VERSION = "latestReleaseVersion";
	public static final String REVENUE = "revenue";
	public static final String HOMEPAGE = "homepage";
	public static final String LOCATION = "location";
	public static final String KEY_PEOPLE = "keyPeople";
	public static final String PLATFORM = "platform";
	public static final String PROGRAMMING_LANGUAGE = "programmingLanguage";
	public static final String LICENSE = "license";
	public static final String GENRE = "genre";
	public static final String DBPEDIA_DESCRIPTION = "description";
	public static final String DBPEDIA_ID = "id";
	
	/**
	 * PRONOM column names
	 */
	public static final String NAME = "Name";
	public static final String VERSION = "Version";
	public static final String ORIENTATION = "Orientation";
	public static final String BYTE_ORDER = "Byte order";
	public static final String RELEASED = "Released";
	public static final String DEVELOPED_BY = "Developed by";
	public static final String DESCRIPTION = "Description";
	public static final String IDENTIFIERS = "Identifiers";
	public static final String OTHER_NAMES = "Other names";
	public static final String FORMAT_RISK = "Format Risk";
	public static final String SUPPORTED_UNTIL = "Supported until";
	
	/**
	 * PRONOM parser definitions
	 */
	public static final String COLUMN_NAME_BEGIN = "<th class=\"data\" scope=\"row\" valign=\"top\">";
	public static final String COLUMN_NAME_END = "</th>";
	public static final String VALUE_NAME_BEGIN = "<td class=\"data\">";
	public static final String VALUE_NAME_END = "</td>";
	public static final String LINK = "<a href";
	public static final String LINK_BEGIN = "/>"; 
	public static final String LINK_END = "</a>";
	public static final String FIELD_PREFIX = ">";
	public static final String FIELD_SUFFIX = "<";
	public static final String UNKNOWN = "Unknown";
		
	/**
	 * Freebase column names
	 */
	public static final String FB_NAME = "name";
	public static final String FB_RESULT = "result";
	public static final String FB_EXTENSION = "extension";
	public static final String FB_DEVELOPER = "developer";
	public static final String FB_WRITTEN_BY = "written_by";
	public static final String FB_READ_BY = "read_by";
	public static final String FB_MIME_TYPE = "mime_type";
	public static final String FB_OS_PLATFORM = "compatible_oses";
	public static final String FB_PROGRAMMING_LANGUAGE = "languages_used";
	
	/**
	 * JSON definitions
	 */
	public static final String EMPTY_ARRAY = "[]";

	/**
	 * Internal mapping registry keys
	 */
	public static final String REG_ADOBE = "adobe";
	public static final String REG_JPG = "jpg";
	public static final String REG_PNG = "png";
	public static final String REG_CSV = "csv";
	
	/**
	 * Help definitions for operations on string
	 */
	public static final String STRING_SEPARATOR = "#";
	public static final String CSV_SEPARATOR = ";";
	public static final String LINE_END = "\n";
	public static final String EMPTY_STRING = " ";
	public static final String GETTER_METHOD = "get";
	
	/**
	 * Definitions for CSV metadata reports
	 */
	public static final int CSV_BROKEN_OBJECTS = 0;
	public static final int CSV_EMPTY_LINKS_OBJECTS = 1;
	public static final int CSV_EMPTY_FIELDS_OBJECTS = 2;
	public static final int CSV_FILLING_FIELD_DISSEMINATION = 3;
	public static final int CSV_BROKEN_LINKS_OBJECTS = 4;
	public static final int CSV_EUROPEANA_YEAR_DISSEMINATION = 5;
	public static final int CSV_MIME_TYPE_DISSEMINATION = 6;
	public static final int CSV_EUROPEANA_LANGUAGE_DISSEMINATION = 7;
	public static final int CSV_DC_CREATOR_DISSEMINATION = 8;
	public static final int CSV_DC_SUBJECT_DISSEMINATION = 9;
	public static final int CSV_DC_DATE_DISSEMINATION = 10;
	public static final int CSV_DC_TYPE_DISSEMINATION = 11;
	public static final int CSV_EUROPEANA_TYPE_DISSEMINATION = 12;
	public static final int CSV_EUROPEANA_COUNTRY_DISSEMINATION = 13;
	public static final int CSV_EUROPEANA_PROVIDER_DISSEMINATION = 14;

	/**
	 * Field definition for collection search from Mongo database
	 */
	public static final String DOMAIN_OBJECT_NAME = "DomainObjectName";
	public static final String COLLECTION_NAME = "CollectionName";
	public static final String EUROPEANA_COLLECTION_NAME = "EuropeanaCollectionName";

	/**
	 * Headers for metadata analysis reports in CSV format
	 */
	public static final String CSV_BROKEN_OBJECTS_HEADER = "europeanaUri;totalObjectsCount;\n";
	public static final String CSV_EMPTY_LINKS_OBJECTS_HEADER = "europeanaUri;totalObjectsCount;\n";
	public static final String CSV_EMPTY_FIELDS_OBJECTS_HEADER = "field;fillingLevel(%);emptyFieldsCount;totalEmptyFieldsCount;totalObjectFieldsCount;totalObjectsCount;\n";
	public static final String CSV_FILLING_FIELD_DISSEMINATION_HEADER = "Filling Range(%);objectsCount;totalObjectsCount;\n";
	public static final String CSV_BROKEN_LINKS_OBJECTS_HEADER = "europeanaUri;link;fehlerCode;AllValues;\n";
	public static final String CSV_EUROPEANA_YEAR_DISSEMINATION_HEADER = " Value;ValueCount;AllValuesCount;\n";
	public static final String CSV_FIELD_DISSEMINATION_HEADER = " Value;ValueCount;%;DistinctValueCount;AllValuesCount;\n";
	
	/**
	 * These are repository connection modes.
	 */
	public enum ConnectorMode {
       Sparql,
       Http;
	}
	
	/**
	 * Risk score range definitions
	 */
	public static final int RISK_SCORE_LOW_RANGE = 25;//50;
	public static final int RISK_SCORE_MIDDLE_RANGE = 35;//80;

	/**
	 * Risk levels
	 */
	public enum RiskLevelEnum {
		Low  { public String getColorCode() { return "#00C000"; } },
		Middle { public String getColorCode() { return "#FFCC00"; } },
		High    { public String getColorCode() { return "#FF0000"; } };
		
		public String getColorCode() { return this.name(); }
	}

	/**
	 * Parameter "-all" means that analysis should be done for 
	 * all Europeana collections.
	 */
	public static final String ALL_COLLECTIONS = "-all";
	
	/**
	 * Parameter "-collectionId 10" provides one or more Europeana 
	 * collection IDs standing for the collection which should be analyzed.
	 */
	public static final String COLLECTION_ID = "-collectionId";
	
	/**
	 * Parameter "-csv" means that analysis should be done for 
	 * all Europeana collections for particular type and result
	 * returned in CSV format.
	 */
	public static final String CSV = "-csv";
	
	/**
	 * Parameter "-test" enables analysing just for 2 objects of the collection to
	 * make quick testing.
	 */
	public static final String TESTING = "-test";
	
	public static final int DEFAULT_OBJECT_COUNT = 2;
	
	/**
	 * Risk report types
	 */
//	public enum RiskRulesEnum {
//		Extension,
//		HasCreationDate,
//		VendorsCount,
//		SoftwareCount,
//		DescriptionCount,
//		Versions,
//		IsSupportedByVendor,
//		IsOpenFileFormat,
//		IsSupportedByWebBrowsers,
//		IsFrequentlyUsedVersion,
//		IsCompressedFileFormat,
//		HasCreatorInformation,
//		HasPublisherInformation,
//		HasDigitalRightsInformation,
//		IsFileMigrationSupported,
//		HasObjectPreview,
//		IsWellDocumented;
//	}
	
	/**
	 * Metadata report types
	 */
	public enum CsvEnum {
		BrokenObjects,
		EmptyLinksObjects,
		EmptyFieldsObjects,
		FillingFieldDissemination,
		BrokenLinksObjects,
		EuropeanaYearDissemination,
		MimeTypeDissemination,
		EuropeanaLanguageDissemination,
		DcCreatorDissemination,
		DcSubjectDissemination,
		DcDateDissemination,
		DcTypeDissemination,
		EuropeanaTypeDissemination,
		EuropeanaCountryDissemination,
		EuropeanaProviderDissemination;
	}
	
	/**
	 * Provenance fields
	 */
	public enum ProvenanceEnum {
		DcCreator,
		DcPublisher,
		DcContributor,
		DcCoverage,
		DcTermsProvenance,
		EuropeanaCountry,
		EuropeanaDataProvider,
		EuropeanaProvider,
		EuropeanaYear,
		DcTermsSpatial,
		DcTermsTemporal
	}
	
	/**
	 * Context fields
	 */
	public enum ContextEnum {
		DcDate,
		DcRelation,
		DcTermsIsPartOf,
		DcTermsCreated,
		DcTermsIssued,
		DcTermsIsVersionOf,
		DcTermsIsReplacedBy,
		DcTermsReplaces,
		DcTermsRequires,
		DcTermsHasPart,
		DcTermsIsReferencedBy,
		DcTermsReferences,
		DcTermsIsFormatOf,
		DcTermsHasFormat,
		DcTermsConformsTo,
		DcTermsHasVersion,
		DcTermsIsRequiredBy
	}
	
	/**
	 * Accessibility fields
	 */
	public enum AccessibilityEnum {
		DcType,
		DcFormat,
		DcLanguage,
		DcRights,
		EuropeanaObject,
		EuropeanaIsShownBy,
		EuropeanaIsShownAt
	}
		
	/**
	 * Definition for overall risk score report request
	 */
	public static final String RISK_SCORE_REPORT = "RiskScoreReport";
	
	/**
	 * Risk report types
	 */
	public enum RiskReportTypesEnum {
		MetadataAnalysis,
		RiskScore, // preservation dimensions
		OverallRiskScore,
		RiskReport;
	}
	
	/**
	 * Definitions for metadata analysis statistics header
	 */
	public static final String TOTAL_OBJECTS_COUNT = "totalObjectsCount";
	public static final String FIELD = "field";
	public static final String PERCENT = "%";

}
