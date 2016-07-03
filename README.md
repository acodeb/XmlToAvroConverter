# XmlToAvroConverter
Xml To Avro Converter using JAXB


This project is to convert XML to avro file and to do that following steps are taken

  1. All the JAXB POJO classes can be creted by the feature provided in Eclipse/Intellij out of XSD.
  
  2. Once the POJO classes are created we can add a newe variable "<b>unmappedVariables</b>" to root and sub-root element POJO classes.
    This variale can help if in the XML any unmapped elements are expected.
    
            @XmlAnyElement(lax = true)
            @XmlJavaTypeAdapter(CustomXmlAdapter.class)
            private List<String> unmappedVariables;
    
    Return type of this variable is "<b>List</b>" so that if there are more that one unmapped variables all those will be added to the <b>List</b>
    
    To achieve this we need two annotations - 
            
            <b>@XmlAnyElement(lax = true)</b> and 
            <b>@XmlJavaTypeAdapter(CustomXmlAdapter.class)</b>
  
    While processing the xml file if any unmapped element is encounter then these annotation will call <b>CustomXmlAdapter</b> adapter 
    which will return the Element Name and the Element value.

  If we don't want to capture the unmapped elements or if we are sure that no unmapped elements will come in the xml, 
  than we can ignore this step.
