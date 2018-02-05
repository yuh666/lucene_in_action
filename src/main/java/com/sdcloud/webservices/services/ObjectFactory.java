
package com.sdcloud.webservices.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sdcloud.webservices.services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoServices_QNAME = new QName("http://services.webservices.sdcloud.com/", "doServices");
    private final static QName _DoServicesResponse_QNAME = new QName("http://services.webservices.sdcloud.com/", "doServicesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sdcloud.webservices.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DoServices }
     * 
     */
    public DoServices createDoServices() {
        return new DoServices();
    }

    /**
     * Create an instance of {@link DoServicesResponse }
     * 
     */
    public DoServicesResponse createDoServicesResponse() {
        return new DoServicesResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoServices }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.webservices.sdcloud.com/", name = "doServices")
    public JAXBElement<DoServices> createDoServices(DoServices value) {
        return new JAXBElement<DoServices>(_DoServices_QNAME, DoServices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoServicesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.webservices.sdcloud.com/", name = "doServicesResponse")
    public JAXBElement<DoServicesResponse> createDoServicesResponse(DoServicesResponse value) {
        return new JAXBElement<DoServicesResponse>(_DoServicesResponse_QNAME, DoServicesResponse.class, null, value);
    }

}
