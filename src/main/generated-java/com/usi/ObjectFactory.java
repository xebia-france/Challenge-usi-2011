//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.27 at 03:13:16 PM CEST 
//


package com.usi;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.usi package. 
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

    private final static QName _Gamesession_QNAME = new QName("http://www.usi.com", "gamesession");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.usi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Sessiontype }
     * 
     */
    public Sessiontype createSessiontype() {
        return new Sessiontype();
    }

    /**
     * Create an instance of {@link Parametertype }
     * 
     */
    public Parametertype createParametertype() {
        return new Parametertype();
    }

    /**
     * Create an instance of {@link Questiontype }
     * 
     */
    public Questiontype createQuestiontype() {
        return new Questiontype();
    }

    /**
     * Create an instance of {@link Question }
     * 
     */
    public Question createQuestion() {
        return new Question();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sessiontype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.usi.com", name = "gamesession")
    public JAXBElement<Sessiontype> createGamesession(Sessiontype value) {
        return new JAXBElement<Sessiontype>(_Gamesession_QNAME, Sessiontype.class, null, value);
    }

}
