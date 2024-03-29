
package docman.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fileRequest" type="{http://services.docman/}fileRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveRequest", propOrder = {
    "fileRequest"
})
public class SaveRequest {

    protected FileRequest fileRequest;

    /**
     * Gets the value of the fileRequest property.
     * 
     * @return
     *     possible object is
     *     {@link FileRequest }
     *     
     */
    public FileRequest getFileRequest() {
        return fileRequest;
    }

    /**
     * Sets the value of the fileRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileRequest }
     *     
     */
    public void setFileRequest(FileRequest value) {
        this.fileRequest = value;
    }

}
