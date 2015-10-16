package plasma.webx.dom.impl;

import java.net.URL;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.dom.events.DocumentEventSupport;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.ParsedURL;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.stylesheets.StyleSheet;

import plasma.webx.css.dom.CSSOMWebXViewCSS;
import plasma.webx.css.engine.WebXCSSEngine;
import plasma.webx.util.WebXConstants;

/**
 * This class implements the {@link DOMImplementation} interface.
 * It provides support the WebX 1.0 documents.
 */
public class WebXDOMImplementation extends ExtensibleDOMImplementation {
  private static final long serialVersionUID = 8820204767902461928L;

  /**
   * The WebX namespace uri.
   */
  public static final String WEBX_NAMESPACE_URI =
      WebXConstants.WEBX_NAMESPACE_URI;
  
  /**
   * The error messages bundle class name.
   */
  protected static final String RESOURCES =
      "plasma.webx.dom.resources.Messages";
  
  protected HashTable factories;
  
  /**
   * Returns the default instance of this class.
   */
  public static DOMImplementation getDOMImplementation() {
      return DOM_IMPLEMENTATION;
  }
  
  /**
   * Creates a new SVGDOMImplementation object.
   */
  public WebXDOMImplementation() {
      factories = webx10Factories;
      registerFeature("CSS",            "2.0");
      registerFeature("StyleSheets",    "2.0");
      registerFeature("WebX",            new String[] {"1.0"});
      registerFeature("WebXEvents",      new String[] {"1.0"});
  }
  
  @Override
  protected void initLocalizable() {
    localizableSupport = new LocalizableSupport
        (RESOURCES, getClass().getClassLoader());
  }

  @Override
  public CSSEngine createCSSEngine(AbstractStylableDocument doc,
                                   CSSContext               ctx,
                                   ExtendedParser           ep,
                                   ValueManager     []      vms,
                                   ShorthandManager []      sms) {

      ParsedURL durl = ((WebXDocumentImpl)doc).getParsedURL();
      CSSEngine result = new WebXCSSEngine(doc, durl, ep, vms, sms, ctx);

      URL url = getClass().getResource("resources/UserAgentStyleSheet.css");
      if (url != null) {
          ParsedURL purl = new ParsedURL(url);
          InputSource is = new InputSource(purl.toString());
          result.setUserAgentStyleSheet
              (result.parseStyleSheet(is, purl, "all"));
      }

      return result;
  }

  /**
   * Creates a ViewCSS.
   */
  @Override
  public ViewCSS createViewCSS(AbstractStylableDocument doc) {
      return new CSSOMWebXViewCSS(doc.getCSSEngine());
  }
  
  /**
   * <b>DOM</b>: Implements {@link
   * DOMImplementation#createDocument(String,String,DocumentType)}.
   */
  @Override
  public Document createDocument(String namespaceURI,
                                 String qualifiedName,
                                 DocumentType doctype)
      throws DOMException {
      Document result = new WebXDocumentImpl(doctype, this);
      // BUG 32108: return empty document if qualifiedName is null.
      if (qualifiedName != null)
          result.appendChild(result.createElementNS(namespaceURI,
                                                    qualifiedName));
      return result;
  }

  // DOMImplementationCSS /////////////////////////////////////////////////

  /**
   * <b>DOM</b>: Implements {@link
   * org.w3c.dom.css.DOMImplementationCSS#createCSSStyleSheet(String,String)}.
   */
  @Override
  public CSSStyleSheet createCSSStyleSheet(String title, String media) {
      throw new UnsupportedOperationException
          ("DOMImplementationCSS.createCSSStyleSheet is not implemented"); // XXX
  }

  // CSSStyleDeclarationFactory ///////////////////////////////////////////

  /**
   * Creates a style declaration.
   * @return a CSSOMStyleDeclaration instance.
   */
  public CSSStyleDeclaration createCSSStyleDeclaration() {
      throw new UnsupportedOperationException
          ("CSSStyleDeclarationFactory.createCSSStyleDeclaration is not implemented"); // XXX
  }

  // StyleSheetFactory /////////////////////////////////////////////

  /**
   * Creates a stylesheet from the data of an xml-stylesheet
   * processing instruction or return null.
   */
  @Override
  public StyleSheet createStyleSheet(Node n, HashTable attrs) {
      throw new UnsupportedOperationException
          ("StyleSheetFactory.createStyleSheet is not implemented"); // XXX
  }

  /**
   * Returns the user-agent stylesheet.
   */
  public CSSStyleSheet getUserAgentStyleSheet() {
      throw new UnsupportedOperationException
          ("StyleSheetFactory.getUserAgentStyleSheet is not implemented"); // XXX
  }
  
  /**
   * Implements the behavior of Document.createElementNS() for this
   * DOM implementation.
   */
  @Override
  public Element createElementNS(AbstractDocument document,
                                 String           namespaceURI,
                                 String           qualifiedName) {
      if (WebXConstants.WEBX_NAMESPACE_URI.equals(namespaceURI)) {
          String name = DOMUtilities.getLocalName(qualifiedName);
          ElementFactory ef = (ElementFactory)factories.get(name);
          if (ef != null)
              return ef.create(DOMUtilities.getPrefix(qualifiedName),
                               document);
          throw document.createDOMException
              (DOMException.NOT_FOUND_ERR, "invalid.element",
               new Object[] { namespaceURI, qualifiedName });
      }

      return super.createElementNS(document, namespaceURI, qualifiedName);
  }
  
  /**
   * Creates an DocumentEventSupport object suitable for use with
   * this implementation.
   */
  @Override
  public DocumentEventSupport createDocumentEventSupport() {
      DocumentEventSupport result =  new DocumentEventSupport();
//      result.registerEventFactory("WebXEvents",
//                                  new DocumentEventSupport.EventFactory() {
//                                          public Event createEvent() {
//                                              return new WebXEvent();
//                                          }
//                                      });
//      result.registerEventFactory("TimeEvent",
//                                  new DocumentEventSupport.EventFactory() {
//                                          public Event createEvent() {
//                                              return new DOMTimeEvent();
//                                          }
//                                      });
      return result;
  }
  
  // The element factories /////////////////////////////////////////////////
  
  /**
   * The WebX element factories.
   */
  protected static final HashTable webx10Factories = new HashTable();
  
  static {
    webx10Factories.put(WebXConstants.WEBX_DIV_TAG, 
        new WebXDivElementFactory());
    webx10Factories.put(WebXConstants.WEBX_P_TAG, 
        new WebXPElementFactory());
    webx10Factories.put(WebXConstants.WEBX_SCRIPT_TAG, 
        new WebXScriptElementFactory());
    webx10Factories.put(WebXConstants.WEBX_STYLE_TAG, 
        new WebXStyleElementFactory());
    webx10Factories.put(WebXConstants.WEBX_ROOT_TAG, 
        new WebXRootElementFactory());
  }
  
  /**
   * The default instance of this class.
   */
  protected static final DOMImplementation DOM_IMPLEMENTATION =
      new WebXDOMImplementation();
  
  /**
   * To create a 'webx' element.
   */
  protected static class WebXRootElementFactory implements ElementFactory {
    @Override
    public Element create(String prefix, Document doc) {
      return new WebXRootElementImpl(prefix, (WebXDocumentImpl)doc);
    }
  }
  
  /**
   * To create a 'div' element.
   */
  protected static class WebXDivElementFactory implements ElementFactory {
    @Override
    public Element create(String prefix, Document doc) {
      return new WebXDivElementImpl(prefix, (WebXDocumentImpl)doc);
    }
  }
  
  /**
   * To create a 'p' element.
   */
  protected static class WebXPElementFactory implements ElementFactory {
    @Override
    public Element create(String prefix, Document doc) {
      return new WebXPElementImpl(prefix, (WebXDocumentImpl)doc);
    }
  }
  
  /**
   * To create a 'script' element.
   */
  protected static class WebXScriptElementFactory implements ElementFactory {
      public Element create(String prefix, Document doc) {
          return new WebXScriptElementImpl(prefix, (WebXDocumentImpl)doc);
      }
  }
  
  /**
   * To create a 'style' element.
   */
  protected static class WebXStyleElementFactory implements ElementFactory {
      public Element create(String prefix, Document doc) {
          return new WebXStyleElementImpl(prefix, (WebXDocumentImpl)doc);
      }
  }
}
