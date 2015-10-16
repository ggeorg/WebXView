package plasma.webx.dom.impl;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.StyleSheet;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.StyleSheetFactory;
import org.apache.batik.dom.StyleSheetProcessingInstruction;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * This class provides an implementation of the 'xml-stylesheet' processing
 * instructions.
 */
public class WebXStyleSheetProcessingInstruction extends StyleSheetProcessingInstruction
    implements ProcessingInstruction {
  private static final long serialVersionUID = -6308483266821949232L;

  /**
   * The style-sheet.
   */
  protected StyleSheet styleSheet;
  
  /**
   * Creates a new ProcessingInstruction object.
   */
  protected WebXStyleSheetProcessingInstruction() {
  }
  
  /**
   * Creates a new ProcessingInstruction object.
   */
  public WebXStyleSheetProcessingInstruction(String            data,
                                             AbstractDocument  owner,
                                             StyleSheetFactory f) {
      super(data, owner, f);
  }

  /**
   * Returns the URI of the referenced stylesheet.
   */
  public String getStyleSheetURI() {
      WebXDocumentImpl webxDoc = (WebXDocumentImpl) getOwnerDocument();
      ParsedURL url = webxDoc.getParsedURL();
      String href = (String)getPseudoAttributes().get("href");
      if (url != null) {
          return new ParsedURL(url, href).toString();
      }
      return href;
  }
  
  /**
   * Returns the associated style-sheet.
   */
  public StyleSheet getCSSStyleSheet() {
      if (styleSheet == null) {
          HashTable attrs = getPseudoAttributes();
          String type = (String)attrs.get("type");

          if ("text/css".equals(type)) {
              String title     = (String)attrs.get("title");
              String media     = (String)attrs.get("media");
              String href      = (String)attrs.get("href");
              String alternate = (String)attrs.get("alternate");
              WebXDocumentImpl doc = (WebXDocumentImpl)getOwnerDocument();
              ParsedURL durl = doc.getParsedURL();
              ParsedURL burl = new ParsedURL(durl, href);
              CSSEngine e = doc.getCSSEngine();
              
              styleSheet = e.parseStyleSheet(burl, media);
              styleSheet.setAlternate("yes".equals(alternate));
              styleSheet.setTitle(title);
          }
      }
      return styleSheet;
  }
  
  /**
   * <b>DOM</b>: Implements {@link
   * org.w3c.dom.ProcessingInstruction#setData(String)}.
   */
  public void setData(String data) throws DOMException {
      super.setData(data);
      styleSheet = null;
  }
  
  /**
   * Returns a new uninitialized instance of this object's class.
   */
  protected Node newNode() {
      return new WebXStyleSheetProcessingInstruction();
  }
}
