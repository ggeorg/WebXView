package plasma.webx.dom.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import plasma.webx.dom.WebXDocument;
import plasma.webx.dom.WebXDocumentFactory;

/**
 * This class contains methods for creating WebXDocument instances from an URI
 * using SAX2.
 */
public class SAXWebXDocumentFactory extends SAXDocumentFactory
    implements WebXDocumentFactory {

  public static final Object LOCK = new Object();

  /**
   * Key used for public identifiers
   */
  public static final String KEY_PUBLIC_IDS = "publicIds";

  /**
   * Key used for public identifiers
   */
  public static final String KEY_SKIPPABLE_PUBLIC_IDS = "skippablePublicIds";

  /**
   * Key used for the skippable DTD substitution
   */
  public static final String KEY_SKIP_DTD = "skipDTD";

  /**
   * Key used for system identifiers
   */
  public static final String KEY_SYSTEM_ID = "systemId.";

  /**
   * The dtd public IDs resource bundle class name.
   */
  protected static final String DTDIDS = "plasma.webx.dom.resources.dtdids";

  /**
   * Constant for HTTP content type header charset field.
   */
  protected static final String HTTP_CHARSET = "charset";

  /**
   * The accepted DTD public IDs.
   */
  protected static String dtdids;

  /**
   * The DTD public IDs we know we can skip.
   */
  protected static String skippable_dtdids;

  /**
   * The DTD content to use when skipping
   */
  protected static String skip_dtd;

  /**
   * The ResourceBunder for the public and system ids
   */
  protected static Properties dtdProps;

  /**
   * Creates a new WebXDocumentFactory object.
   * 
   * @param parser
   *          The SAX2 parser class name.
   */
  public SAXWebXDocumentFactory(String parser) {
    super(WebXDOMImplementation.getDOMImplementation(), parser);
  }

  /**
   * Creates a new WebXDocumentFactory object.
   * 
   * @param parser
   *          The SAX2 parser class name.
   * @param dd
   *          Whether a document descriptor must be generated.
   */
  public SAXWebXDocumentFactory(String parser, boolean dd) {
    super(WebXDOMImplementation.getDOMImplementation(), parser, dd);
  }

  public WebXDocument createWebXDocument(String uri)
      throws IOException {
    return (WebXDocument) createDocument(uri);
  }

  /**
   * Creates a WebX Document instance.
   * 
   * @param uri
   *          The document URI.
   * @param inp
   *          The document input stream.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public WebXDocument createWebXDocument(String uri, InputStream inp)
      throws IOException {
    return (WebXDocument) createDocument(uri, inp);
  }

  /**
   * Creates a WebX Document instance.
   * 
   * @param uri
   *          The document URI.
   * @param r
   *          The document reader.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public WebXDocument createWebXDocument(String uri, Reader r)
      throws IOException {
    return (WebXDocument) createDocument(uri, r);
  }

  /**
   * Creates a WebX Document instance. This method supports gzipped sources.
   * 
   * @param uri
   *          The document URI.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public Document createDocument(String uri)
      throws IOException {
    ParsedURL purl = new ParsedURL(uri);

    // InputStream is = purl.openStream(mimeTypes)
    InputStream is = purl.openStream();
    uri = purl.getPostConnectionURL();

    InputSource isrc = new InputSource(is);

    // looking for charset encoding in the content type
    String contentType = purl.getContentType();
    int cindex = -1;
    if (contentType != null) {
      contentType = contentType.toLowerCase();
      cindex = contentType.indexOf(HTTP_CHARSET);
    }

    String charset = null;
    if (cindex != -1) {
      int i = cindex + HTTP_CHARSET.length();
      int eqIdx = contentType.indexOf('=', i);
      if (eqIdx != -1) {
        eqIdx++;

        // The patch had ',' as the terminator but I suspect
        // that is the delimiter between possible charsets,
        // but if another 'attribute' were in the accept header
        // charset would be terminated by a ';'. So I look
        // for both and take to closer of the two.
        int idx = contentType.indexOf(',', eqIdx);
        int semiIdx = contentType.indexOf(';', eqIdx);
        if ((semiIdx != -1) && ((semiIdx < idx) || (idx == -1)))
          idx = semiIdx;
        if (idx != -1)
          charset = contentType.substring(eqIdx, idx);
        else
          charset = contentType.substring(eqIdx);
        charset = charset.trim();
        isrc.setEncoding(charset);
      }
    }

    isrc.setSystemId(uri);

    WebXDocument doc = (WebXDocument) super.createDocument
        (WebXDOMImplementation.WEBX_NAMESPACE_URI, "webx", uri, isrc);
    // doc.setParsedURL(new ParsedURL(uri));
    // doc.setDocumentInputEncoding(charset);
    doc.setXmlStandalone(isStandalone);
    doc.setXmlVersion(xmlVersion);

    return doc;
  }

  /**
   * Creates a WebX Document instance.
   * 
   * @param uri
   *          The document URI.
   * @param inp
   *          The document input stream.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public Document createDocument(String uri, InputStream inp)
      throws IOException {
    Document doc;
    InputSource is = new InputSource(inp);
    is.setSystemId(uri);

    try {
      doc = super.createDocument
          (WebXDOMImplementation.WEBX_NAMESPACE_URI, "webx", uri, is);
      if (uri != null) {
        // ((WebXDocument)doc).setParsedURL(new ParsedURL(uri));
      }

      AbstractDocument d = (AbstractDocument) doc;
      d.setDocumentURI(uri);
      d.setXmlStandalone(isStandalone);
      d.setXmlVersion(xmlVersion);
    } catch (MalformedURLException e) {
      throw new IOException(e.getMessage());
    }
    return doc;
  }

  /**
   * Creates a WebX Document instance.
   * 
   * @param uri
   *          The document URI.
   * @param r
   *          The document reader.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public Document createDocument(String uri, Reader r)
      throws IOException {
    Document doc;
    InputSource is = new InputSource(r);
    is.setSystemId(uri);

    try {
      doc = super.createDocument
          (WebXDOMImplementation.WEBX_NAMESPACE_URI, "webx", uri, is);
      if (uri != null) {
        // ((WebXDocument)doc).setParsedURL(new ParsedURL(uri));
      }

      AbstractDocument d = (AbstractDocument) doc;
      d.setDocumentURI(uri);
      d.setXmlStandalone(isStandalone);
      d.setXmlVersion(xmlVersion);
    } catch (MalformedURLException e) {
      throw new IOException(e.getMessage());
    }
    return doc;
  }

  /**
   * Creates a Document instance.
   * 
   * @param ns
   *          The namespace URI of the root element of the document.
   * @param root
   *          The name of the root element of the document.
   * @param uri
   *          The document URI.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public Document createDocument(String ns, String root, String uri)
      throws IOException {
    if (!WebXDOMImplementation.WEBX_NAMESPACE_URI.equals(ns) ||
        !"webx".equals(root)) {
      throw new RuntimeException("Bad root element");
    }
    return createDocument(uri);
  }

  /**
   * Creates a Document instance.
   * 
   * @param ns
   *          The namespace URI of the root element of the document.
   * @param root
   *          The name of the root element of the document.
   * @param uri
   *          The document URI.
   * @param is
   *          The document input stream.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public Document createDocument(String ns, String root, String uri,
      InputStream is) throws IOException {
    if (!WebXDOMImplementation.WEBX_NAMESPACE_URI.equals(ns) ||
        !"webx".equals(root)) {
      throw new RuntimeException("Bad root element");
    }
    return createDocument(uri, is);
  }

  /**
   * Creates a Document instance.
   * 
   * @param ns
   *          The namespace URI of the root element of the document.
   * @param root
   *          The name of the root element of the document.
   * @param uri
   *          The document URI.
   * @param r
   *          The document reader.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  public Document createDocument(String ns, String root, String uri,
      Reader r) throws IOException {
    if (!WebXDOMImplementation.WEBX_NAMESPACE_URI.equals(ns) ||
        !"webx".equals(root)) {
      throw new RuntimeException("Bad root element");
    }
    return createDocument(uri, r);
  }

  public DOMImplementation getDOMImplementation(String ver) {
    if (ver == null || ver.length() == 0 || ver.equals("1.0")) {
      return WebXDOMImplementation.getDOMImplementation();
    }
    throw new RuntimeException("Unsupport WebX version '" + ver + "'");
  }
  
  /**
   * <b>SAX</b>: Implements {@link
   * org.xml.sax.ContentHandler#startDocument()}.
   */
  public void startDocument() throws SAXException {
      super.startDocument();
      // Do not assume namespace declarations when no DTD has been specified.
      // namespaces.put("", WebXDOMImplementation.WEBX_NAMESPACE_URI);
      // namespaces.put("xlink", XLinkSupport.XLINK_NAMESPACE_URI);
  }
  
  /**
   * <b>SAX2</b>: Implements {@link
   * org.xml.sax.EntityResolver#resolveEntity(String,String)}.
   */
  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException {
      try {
          synchronized (LOCK) {
              // Bootstrap if needed - move to a static block???
              if (dtdProps == null) {
                  dtdProps = new Properties();
                  try {
                      Class<?> cls = SAXWebXDocumentFactory.class;
                      InputStream is = cls.getResourceAsStream
                          ("resources/dtdids.properties");
                      dtdProps.load(is);
                  } catch (IOException ioe) {
                      throw new SAXException(ioe);
                  }
              }

              if (dtdids == null)
                  dtdids = dtdProps.getProperty(KEY_PUBLIC_IDS);

              if (skippable_dtdids == null)
                  skippable_dtdids =
                      dtdProps.getProperty(KEY_SKIPPABLE_PUBLIC_IDS);

              if (skip_dtd == null)
                  skip_dtd = dtdProps.getProperty(KEY_SKIP_DTD);
          }

          if (publicId == null)
              return null; // Let SAX Parser find it.

          if (!isValidating &&
              (skippable_dtdids.indexOf(publicId) != -1)) {
              // We are not validating and this is a DTD we can
              // safely skip so do it...  Here we provide just enough
              // of the DTD to keep stuff running (set svg and
              // xlink namespaces).
              return new InputSource(new StringReader(skip_dtd));
          }

          if (dtdids.indexOf(publicId) != -1) {
              String localSystemId =
                  dtdProps.getProperty(KEY_SYSTEM_ID +
                                       publicId.replace(' ', '_'));

              if (localSystemId != null && !"".equals(localSystemId)) {
                  return new InputSource
                      (getClass().getResource(localSystemId).toString());
              }
          }
      } catch (MissingResourceException e) {
          throw new SAXException(e);
      }
      // Let the SAX parser find the entity.
      return null;
  }
  
  /-------------------------------------------------------------------------
  
  public static void main(String[] args) throws Exception {
    SAXWebXDocumentFactory factory = new SAXWebXDocumentFactory("org.apache.xerces.parsers.SAXParser");
    WebXDocument doc = factory.createWebXDocument("http://localhost:8080/webx/index.xml");
    
    //
    // print
    //
    
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    //initialize StreamResult with File object to save to file
    StreamResult result = new StreamResult(new StringWriter());
    DOMSource source = new DOMSource(doc);
    transformer.transform(source, result);
    String xmlString = result.getWriter().toString();
    System.out.println(xmlString);
  }
}
