Generate from xml-schema Ffma-preservation-riskmanagement-riskanalysis.xsd:

A. Generate RiskAnalysis.xml:
File -> New -> Others -> Xml -> XmlFile -> <Filename> -> Create from Xml Schema -> <Ffma-preservation-riskmanagement-riskanalysis.xsd>

B. Generate the java risk properties classes from Ffma-preservation-riskmanagement-riskanalysis.xsd:
1. Commandline:
> cd <Ffma_workspace>
> cd riskanalysis\resources
> %JAVA_HOME%\bin\xjc -p ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel -d .\temp Ffma-preservation-riskmanagement-riskanalysis.xsd
2. Import generated files into eclipse project
3. Generate LOD classes: <Ffma_workspace>\Ffma\services\preservation-riskmanagement\src\main\resources>
xjc -p ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel -d .\temp Ffma-preservation-lod.xsd
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Access to dbpedia:
Test SPARQL queries: http://DBpedia.org/snorql
Info about formats: http://dbpedia.org/page/Category:Computer_file_formats

