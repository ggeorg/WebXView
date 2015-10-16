package plasma.webx.bridge;

import plasma.webx.dom.WebXDocument;

/**
 * This class is used to resolve the URI that can be found in a WebX document.
 */
public class URIResolver {

  /**
   * The reference document.
   */
  protected WebXDocument document;
  
  /**
   * The document URI.
   */
  protected String documentURI;
  
  /**
   * The document loader.
   */
  protected DocumentLoader documentLoader;
}
