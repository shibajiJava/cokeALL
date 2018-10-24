package com.ibm.app.services.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {
	/**
     * Converts a XML text representation into its {@link Document} equivalent.
     */
    public static Document getDocument(final String xml)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder documentbuilder = factory.newDocumentBuilder();
        return documentbuilder.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Converts a XML {@link Document} into its textual representation equivalent.
     * 
     * @param doc
     * @return
     * @throws TransformerException
     */
    public static StringWriter write(final Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer;
    }

    /**
     * Removes UTF-8 BOM trailer, if existing.
     * 
     * @param xml XML document as string
     * @return XML document without trailing BOM
     */
    public static String removeBOM(final String xml) {
        return xml != null ? xml.trim().replaceFirst("^([\\W]+)<", "<") : null;
    }

    /**
     * Unmarshalls an XML document element to an OpenSaml object.
     * 
     * @param e the DOM Element
     * @throws UnmarshallingException thrown if an error occurs unmarshalling the DOM element into the XMLObject
     */
    public static <T extends XMLObject> T unmarschall(final Element e) throws UnmarshallingException {
        // Get apropriate unmarshaller
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(e);

        // Unmarshall using the document root element, an EncryptedData in this case
        @SuppressWarnings("unchecked")
        T xmlObject = (T) unmarshaller.unmarshall(e);
        return xmlObject;
    }

    /**
     * Marshalls an OpenSAML object.
     * 
     * @param xmlObject the XMLObject to retrieve the marshaller for
     * @throws MarshallingException thrown if there is a problem marshalling the given object
     */
    public static <T extends Element> T marschall(final XMLObject xmlObject) throws MarshallingException {
        MarshallerFactory fac = Configuration.getMarshallerFactory();
        Marshaller marshaller = fac.getMarshaller(xmlObject);
        @SuppressWarnings("unchecked")
        T e = (T) marshaller.marshall(xmlObject);
        return e;
    }

    /**
     * Null-safe check if the specified node list is empty.
     * 
     * @param list the node list to check, may be null
     * @return <code>true</code> if empty or null
     */
    public static boolean isEmpty(final NodeList list) {
        return list == null || list.getLength() == 0;
    }

    private XmlUtils() {
        // It's an utility class.
    }
}
