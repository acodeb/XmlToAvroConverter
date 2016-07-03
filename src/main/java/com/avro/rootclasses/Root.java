
package com.avro.rootclasses;

import com.avro.main.CustomXmlAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="foo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "foo",
    "bar",
    "unmappedVariables"
})
@XmlRootElement(name = "root")
public class Root {

    @XmlElement(name = "foo", required = true)
    private String foo;
    @XmlElement(name = "bar", required = true)
    private String bar;

    /*
    * Use this variable and annotations if you are expecting unknown elements in your xml's which were are not present in xsd.
    * All the unknown elements will be added to a List in the format (Element Name:Element Value)
    */
    @XmlAnyElement(lax = true)
    @XmlJavaTypeAdapter(CustomXmlAdapter.class)
    private List<String> unmappedVariables;

    public List<String> getUnmappedVariables() {
        return unmappedVariables;
    }

    /**
     * Gets the value of the foo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoo() {
        return foo;
    }

    /**
     * Sets the value of the foo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoo(String value) {
        this.foo = value;
    }

    /**
     * Gets the value of the bar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBar() {
        return bar;
    }

    /**
     * Sets the value of the bar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBar(String value) {
        this.bar = value;
    }

}
