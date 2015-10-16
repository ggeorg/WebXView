package plasma.webx.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.batik.dom.util.DocumentDescriptor;
import org.apache.batik.util.CleanerThread;
import org.w3c.dom.Document;

import plasma.webx.dom.WebXDocument;
import plasma.webx.dom.WebXDocumentFactory;
import plasma.webx.dom.impl.SAXWebXDocumentFactory;

/**
 * This class is responsible on loading an WebX document and maintain a cache.
 */
public class DocumentLoader {

  /**
   * The document factory used to create the document according a DOM
   * implementation.
   */
  protected WebXDocumentFactory documentFactory;

  /**
   * The map that contains the Document index by the URI.
   */
  protected HashMap<String, DocumentState> cacheMap = new HashMap<String, DocumentState>();

  /**
   * The user agent.
   */
  protected UserAgent userAgent;

  /**
   * Constructs a new {@code DocumentLoader}.
   */
  protected DocumentLoader() {
  }

  /**
   * Constructs a new {@code DocumentLoader}.
   * 
   * @param userAgent
   *          the user agent to use.
   */
  public DocumentLoader(UserAgent userAgent) {
    this.userAgent = userAgent;
    documentFactory = new SAXWebXDocumentFactory(userAgent.getXMLParserClassName(), true);
    documentFactory.setValidating(userAgent.isXMLParserValidating());
  }

  public Document checkCache(String uri) {
    int n = uri.lastIndexOf('/');
    if (n == -1)
      n = 0;
    n = uri.indexOf('#', n);
    if (n != -1) {
      uri = uri.substring(0, n);
    }
    DocumentState state;
    synchronized (cacheMap) {
      state = cacheMap.get(uri);
    }
    if (state != null)
      return state.getDocument();
    return null;
  }

  /**
   * Returns a document from the specified uri.
   * 
   * @param uri
   *          the uri of the document
   * @throws IOException
   *           if an I/O error occurred while loading the dcoument
   */
  public Document loadDocument(String uri) throws IOException {
    Document ret = checkCache(uri);
    if (ret != null)
      return ret;

    WebXDocument document = documentFactory.createWebXDocument(uri);

    DocumentDescriptor desc = documentFactory.getDocumentDescriptor();
    DocumentState state = new DocumentState(uri, document, desc);
    synchronized (cacheMap) {
      cacheMap.put(uri, state);
    }
    
    return state.getDocument();
  }
  
  /**
   * Returns a document from the specified uri.
   * @param uri The uri of the document
   * @param is The document input stream.
   * @throws IOException
   *           if an I/O error occurred while loading the dcoument
   */
  public Document loadDocument(String uri, InputStream is) throws IOException {
    Document ret = checkCache(uri);
    if (ret != null)
        return ret;
    
    WebXDocument document = documentFactory.createWebXDocument(uri, is);
    
    DocumentDescriptor desc = documentFactory.getDocumentDescriptor();
    DocumentState state = new DocumentState(uri, document, desc);
    synchronized (cacheMap) {
        cacheMap.put(uri, state);
    }

    return state.getDocument();
  }
  
  /**
   * Returns the userAgent used by this DocumentLoader
   */
  public UserAgent getUserAgent(){
      return userAgent;
  }

  /**
   * A simple class that contains a Document and its number of nodes.
   */
  private class DocumentState extends CleanerThread.SoftReferenceCleared {
    private final String uri;
    private final DocumentDescriptor desc;

    public DocumentState(String uri, Document document, DocumentDescriptor desc) {
      super(document);
      this.uri = uri;
      this.desc = desc;
    }

    public void cleared() {
      synchronized (cacheMap) {
        cacheMap.remove(uri);
      }
    }
    
    public String getURI() {
      return uri;
    }
    
    public Document getDocument() {
      return (Document)get();
    }
    
    public DocumentDescriptor getDocumentDescriptor() {
      return desc;
    }
  }
}
