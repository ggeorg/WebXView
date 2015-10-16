package plasma.webx.dom.impl;

import org.apache.batik.anim.dom.AttributeInitializer;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStyleSheetNode;
import org.apache.batik.css.engine.StyleSheet;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.XMLConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.stylesheets.LinkStyle;

import plasma.webx.dom.WebXStyleElement;
import plasma.webx.util.WebXConstants;

/**
 * This class implements {@link WebXStyleElement}.
 */
public class WebXStyleElementImpl extends WebXElementImpl
    implements CSSStyleSheetNode, WebXStyleElement, LinkStyle {
  private static final long serialVersionUID = -6556723942264397762L;

  /**
   * The attribute initializer.
   */
  protected static final AttributeInitializer attributeInitializer;

  static {
    attributeInitializer = new AttributeInitializer(1);
    attributeInitializer.addAttribute(XMLConstants.XML_NAMESPACE_URI, "xml", "space", "preserve");
  }

  /**
   * The style sheet.
   */
  protected transient org.w3c.dom.stylesheets.StyleSheet sheet;

  /**
   * The DOM CSS style-sheet.
   */
  protected transient StyleSheet styleSheet;

  /**
   * The listener used to track the content changes.
   */
  protected transient EventListener domCharacterDataModifiedListener =
      new DOMCharacterDataModifiedListener();

  /**
   * Creates a new WebXStyleElementImpl object.
   */
  protected WebXStyleElementImpl() {
  }

  /**
   * Creates a new SVGOMStyleElement object.
   * 
   * @param prefix
   *          The namespace prefix.
   * @param owner
   *          The owner document.
   */
  public WebXStyleElementImpl(String prefix, WebXDocumentImpl owner) {
    super(prefix, owner);
  }

  /**
   * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
   */
  @Override
  public String getLocalName() {
    return SVG_STYLE_TAG;
  }

  /**
   * Returns the associated style-sheet.
   */
  @Override
  public StyleSheet getCSSStyleSheet() {
    if (styleSheet == null) {
      if (getType().equals(WebXConstants.CSS_MIME_TYPE)) {
        WebXDocumentImpl doc = (WebXDocumentImpl) getOwnerDocument();
        CSSEngine e = doc.getCSSEngine();
        String text = "";
        Node n = getFirstChild();
        if (n != null) {
          StringBuffer sb = new StringBuffer();
          while (n != null) {
            if (n.getNodeType() == Node.CDATA_SECTION_NODE
                || n.getNodeType() == Node.TEXT_NODE)
              sb.append(n.getNodeValue());
            n = n.getNextSibling();
          }
          text = sb.toString();
        }
        ParsedURL burl = null;
        String bu = getBaseURI();
        if (bu != null) {
          burl = new ParsedURL(bu);
        }
        String media = getAttributeNS(null, WEBX_MEDIA_ATTRIBUTE);
        styleSheet = e.parseStyleSheet(text, burl, media);
        addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI,
            "DOMCharacterDataModified",
            domCharacterDataModifiedListener,
            false,
            null);
      }
    }
    return styleSheet;
  }

  /**
   * <b>DOM</b>: Implements {@link org.w3c.dom.stylesheets.LinkStyle#getSheet()}
   * .
   */
  @Override
  public org.w3c.dom.stylesheets.StyleSheet getSheet() {
    throw new UnsupportedOperationException("LinkStyle.getSheet() is not implemented"); // XXX
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#getXMLspace()}.
   */
  @Override
  public String getXMLSpace() {
    return XMLSupport.getXMLSpace(this);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#setXMLspace(String)}.
   */
  @Override
  public void setXMLSpace(String space)
      throws DOMException {
    setAttributeNS(XML_NAMESPACE_URI, XML_SPACE_QNAME, space);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#getType()}.
   */
  @Override
  public String getType() {
    if (hasAttributeNS(null, WEBX_TYPE_ATTRIBUTE))
      return getAttributeNS(null, WEBX_TYPE_ATTRIBUTE);
    else
      return WebXConstants.CSS_MIME_TYPE;
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#setType(String)}.
   */
  @Override
  public void setType(String type)
      throws DOMException {
    setAttributeNS(null, WEBX_TYPE_ATTRIBUTE, type);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#getMedia()}.
   */
  @Override
  public String getMedia() {
    return getAttribute(WEBX_MEDIA_ATTRIBUTE);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#setMedia(String)}.
   */
  @Override
  public void setMedia(String media)
      throws DOMException {
    setAttribute(WEBX_MEDIA_ATTRIBUTE, media);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#getTitle()}.
   */
  @Override
  public String getTitle() {
    return getAttribute(WEBX_TITLE_ATTRIBUTE);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGStyleElement#setTitle(String)}.
   */
  @Override
  public void setTitle(String title)
      throws DOMException {
    setAttribute(WEBX_TITLE_ATTRIBUTE, title);
  }

  /**
   * Returns the AttributeInitializer for this element type.
   * 
   * @return null if this element has no attribute with a default value.
   */
  @Override
  protected AttributeInitializer getAttributeInitializer() {
    return attributeInitializer;
  }

  /**
   * Returns a new uninitialized instance of this object's class.
   */
  @Override
  protected Node newNode() {
    return new WebXStyleElementImpl();
  }

  /**
   * The DOMCharacterDataModified listener.
   */
  protected class DOMCharacterDataModifiedListener
      implements EventListener {
    @Override
    public void handleEvent(Event evt) {
      styleSheet = null;
    }
  }

}
