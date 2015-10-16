package plasma.webx.dom.impl;

import org.apache.batik.anim.dom.AbstractElement;
import org.apache.batik.anim.dom.TraitInformation;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSNavigableNode;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.svg.ExtendedTraitAccess;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.parser.UnitProcessor;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGTypes;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import plasma.webx.dom.WebXContext;
import plasma.webx.dom.WebXElement;
import plasma.webx.dom.WebXException;
import plasma.webx.dom.WebXRootElement;
import plasma.webx.util.WebXConstants;
import plasma.webx.util.WebXTypes;

/**
 * This class implements the {@link plasma.webx.dom.WebXElement} interface.
 */
public abstract class WebXElementImpl
    extends AbstractElement
    implements WebXElement, ExtendedTraitAccess, WebXConstants {
  private static final long serialVersionUID = -2750463102307907745L;
  
  /**
   * Table mapping XML attribute names to TraitInformation objects.
   */
  protected static DoublyIndexedTable xmlTraitInformation;
  static {
      DoublyIndexedTable t = new DoublyIndexedTable();
      t.put(null, WEBX_ID_ATTRIBUTE,
              new TraitInformation(false, WebXTypes.TYPE_CDATA));
      t.put(XML_NAMESPACE_URI, XML_BASE_ATTRIBUTE,
              new TraitInformation(false, SVGTypes.TYPE_URI));
      t.put(XML_NAMESPACE_URI, XML_SPACE_ATTRIBUTE,
              new TraitInformation(false, SVGTypes.TYPE_IDENT));
      t.put(XML_NAMESPACE_URI, XML_ID_ATTRIBUTE,
              new TraitInformation(false, SVGTypes.TYPE_CDATA));
      t.put(XML_NAMESPACE_URI, XML_LANG_ATTRIBUTE,
              new TraitInformation(false, SVGTypes.TYPE_LANG));
      xmlTraitInformation = t;
  }

  /**
   * Is this element immutable?
   */
  protected transient boolean readonly;

  /**
   * The element prefix.
   */
  protected String prefix;

  /**
   * The WebX context to get WebX specific informations.
   */
  protected transient WebXContext webxContext;

  /**
   * The context used to resolve the units.
   */
  protected UnitProcessor.Context unitContext;

  /**
   * Creates a new Element object.
   */
  protected WebXElementImpl() {
  }

  /**
   * Creates a new Element object.
   * 
   * @param prefix
   *          The namespace prefix.
   * @param owner
   *          The owner document.
   */
  protected WebXElementImpl(String prefix, WebXDocumentImpl owner) {
    super(prefix, owner);
    // initializeLiveAttributes();
  }

  /**
   * Initializes all live attributes for this element.
   */
  protected void initializeAllLiveAttributes() {
    // initializeLiveAttributes();
  }

  /**
   * <b>DOM</b>: Implements {@link SVGElement#getId()}.
   */
  @Override
  public String getId() {
    return getAttributeNS(null, WEBX_ID_ATTRIBUTE);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGElement#setId(String)}.
   */
  @Override
  public void setId(String id) {
    setAttributeNS(null, WEBX_ID_ATTRIBUTE, id);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGElement#getXMLbase()}.
   */
  @Override
  public String getXMLbase() {
    return getAttributeNS(XML_NAMESPACE_URI, XML_BASE_ATTRIBUTE);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGElement#setXMLbase(String)}.
   */
  @Override
  public void setXMLbase(String xmlbase) throws DOMException {
    setAttributeNS(XML_NAMESPACE_URI, XML_BASE_QNAME, xmlbase);
  }

  /**
   * <b>DOM</b>: Implements {@link SVGElement#getOwnerSVGElement()}.
   */
  public WebXRootElement getOwnerWebXElement() {
      for (Element e = CSSEngine.getParentCSSStylableElement(this);
           e != null;
           e = CSSEngine.getParentCSSStylableElement(e)) {
          if (e instanceof WebXRootElement) {
              return (WebXRootElement)e;
          }
      }
      return null;
  }

  /**
   * <b>DOM</b>: Implements {@link SVGElement#getViewportElement()}.
   */
  @Override
  public WebXElement getViewportElement() {
      for (Element e = CSSEngine.getParentCSSStylableElement(this);
           e != null;
           e = CSSEngine.getParentCSSStylableElement(e)) {
//XXX          if (e instanceof WebXFitToViewBox) {
//              return (WebXElement)e;
//          }
      }
      return null;
  }

  /**
   * <b>DOM</b>: Implements {@link Node#getNodeName()}.
   */
  @Override
  public String getNodeName() {
    if (prefix == null || prefix.equals("")) {
      return getLocalName();
    }

    return prefix + ':' + getLocalName();
  }

  /**
   * <b>DOM</b>: Implements {@link Node#getNamespaceURI()}.
   */
  @Override
  public String getNamespaceURI() {
    return WebXDOMImplementation.WEBX_NAMESPACE_URI;
  }

  /**
   * <b>DOM</b>: Implements {@link Node#setPrefix(String)}.
   */
  @Override
  public void setPrefix(String prefix) throws DOMException {
    if (isReadonly()) {
      throw createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
          "readonly.node",
          new Object[] { new Integer(getNodeType()),
              getNodeName() });
    }
    if (prefix != null &&
        !prefix.equals("") &&
        !DOMUtilities.isValidName(prefix)) {
      throw createDOMException(DOMException.INVALID_CHARACTER_ERR,
          "prefix",
          new Object[] { new Integer(getNodeType()),
              getNodeName(),
              prefix });
    }
    this.prefix = prefix;
  }

  /**
   * Returns the xml:base attribute value of the given element,
   * resolving any dependency on parent bases if needed.
   * Follows shadow trees when moving to parent nodes.
   */
  @Override
  protected String getCascadedXMLBase(Node node) {
      String base = null;
      Node n = node.getParentNode();
      while (n != null) {
          if (n.getNodeType() == Node.ELEMENT_NODE) {
              base = getCascadedXMLBase(n);
              break;
          }
          if (n instanceof CSSNavigableNode) {
              n = ((CSSNavigableNode) n).getCSSParentNode();
          } else {
              n = n.getParentNode();
          }
      }
      if (base == null) {
          AbstractDocument doc;
          if (node.getNodeType() == Node.DOCUMENT_NODE) {
              doc = (AbstractDocument) node;
          } else {
              doc = (AbstractDocument) node.getOwnerDocument();
          }
          base = doc.getDocumentURI();
      }
      while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
          node = node.getParentNode();
      }
      if (node == null) {
          return base;
      }
      Element e = (Element) node;
      Attr attr = e.getAttributeNodeNS(XML_NAMESPACE_URI, XML_BASE_ATTRIBUTE);
      if (attr != null) {
          if (base == null) {
              base = attr.getNodeValue();
          } else {
              base = new ParsedURL(base, attr.getNodeValue()).toString();
          }
      }
      return base;
  }

  // WebXContext ///////////////////////////////////////////////////////////
  
  /**
   * Sets the WebX context to use to get WebX specific informations.
   *
   * @param ctx the SVG context
   */
  public void setWebXContext(WebXContext ctx) {
      webxContext = ctx;
  }
  
  /**
   * Returns the WebX context used to get WebX specific informations.
   */
  public WebXContext getWebXContext() {
      return webxContext;
  }
  
  // ExtendedNode //////////////////////////////////////////////////////////

  /**
   * Creates an SVGException with the appropriate error message.
   */
  public WebXException createWebXException(short type,
                                           String key,
                                           Object [] args) {
      try {
          return new WebXException(type, getCurrentDocument().formatMessage(key, args));
      } catch (Exception e) {
          return new WebXException(type, key);
      }
  }
  
  /**
   * Tests whether this node is readonly.
   */
  @Override
  public boolean isReadonly() {
      return readonly;
  }
  
  /**
   * Sets this node readonly attribute.
   */
  @Override
  public void setReadonly(boolean v) {
      readonly = v;
  }
  
  /**
   * Returns the table of TraitInformation objects for this element.
   */
  protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
  }
  
  // ExtendedTraitAccess ///////////////////////////////////////////////////
  
  /**
   * Returns whether the given CSS property is available on this element.
   */
  @Override
  public boolean hasProperty(String pn) {
      AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
      CSSEngine eng = doc.getCSSEngine();
      return eng.getPropertyIndex(pn) != -1
          || eng.getShorthandIndex(pn) != -1;
  }

  /**
   * Returns whether the given trait is available on this element.
   */
  @Override
  public boolean hasTrait(String ns, String ln) {
      // XXX no traits yet
      return false;
  }

  /**
   * Returns whether the given CSS property is animatable.
   */
  @Override
  public boolean isPropertyAnimatable(String pn) {
      AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
      CSSEngine eng = doc.getCSSEngine();
      int idx = eng.getPropertyIndex(pn);
      if (idx != -1) {
          ValueManager[] vms = eng.getValueManagers();
          return vms[idx].isAnimatableProperty();
      }
      idx = eng.getShorthandIndex(pn);
      if (idx != -1) {
          ShorthandManager[] sms = eng.getShorthandManagers();
          return sms[idx].isAnimatableProperty();
      }
      return false;
  }

  /**
   * Returns whether the given XML attribute is animatable.
   */
  @Override
  public final boolean isAttributeAnimatable(String ns, String ln) {
      DoublyIndexedTable t = getTraitInformationTable();
      TraitInformation ti = (TraitInformation) t.get(ns, ln);
      if (ti != null) {
          return ti.isAnimatable();
      }
      return false;
  }

  /**
   * Returns whether the given CSS property is additive.
   */
  @Override
  public boolean isPropertyAdditive(String pn) {
      AbstractStylableDocument doc = (AbstractStylableDocument) ownerDocument;
      CSSEngine eng = doc.getCSSEngine();
      int idx = eng.getPropertyIndex(pn);
      if (idx != -1) {
          ValueManager[] vms = eng.getValueManagers();
          return vms[idx].isAdditiveProperty();
      }
      idx = eng.getShorthandIndex(pn);
      if (idx != -1) {
          ShorthandManager[] sms = eng.getShorthandManagers();
          return sms[idx].isAdditiveProperty();
      }
      return false;
  }

  /**
   * Returns whether the given XML attribute is additive.
   */
  @Override
  public boolean isAttributeAdditive(String ns, String ln) {
      return true;
  }

  /**
   * Returns whether the given trait is animatable.
   */
  @Override
  public boolean isTraitAnimatable(String ns, String tn) {
      // XXX no traits yet
      return false;
  }

  /**
   * Returns whether the given trait is additive.
   */
  @Override
  public boolean isTraitAdditive(String ns, String tn) {
      // XXX no traits yet
      return false;
  }

  /**
   * Returns the type of the given property.
   */
  @Override
  public int getPropertyType(String pn) {
      AbstractStylableDocument doc =
          (AbstractStylableDocument) ownerDocument;
      CSSEngine eng = doc.getCSSEngine();
      int idx = eng.getPropertyIndex(pn);
      if (idx != -1) {
          ValueManager[] vms = eng.getValueManagers();
          return vms[idx].getPropertyType();
      }
      return SVGTypes.TYPE_UNKNOWN;
  }

  /**
   * Returns the type of the given attribute.
   */
  @Override
  public final int getAttributeType(String ns, String ln) {
      DoublyIndexedTable t = getTraitInformationTable();
      TraitInformation ti = (TraitInformation) t.get(ns, ln);
      if (ti != null) {
          return ti.getType();
      }
      return SVGTypes.TYPE_UNKNOWN;
  }
  
  // Importation/Cloning ///////////////////////////////////////////

  /**
   * Exports this node to the given document.
   */
  @Override
  protected Node export(Node n, AbstractDocument d) {
      super.export(n, d);
      WebXElementImpl e = (WebXElementImpl)n;
      e.prefix = prefix;
      e.initializeAllLiveAttributes();
      return n;
  }

  /**
   * Deeply exports this node to the given document.
   */
  @Override
  protected Node deepExport(Node n, AbstractDocument d) {
      super.deepExport(n, d);
      WebXElementImpl e = (WebXElementImpl)n;
      e.prefix = prefix;
      e.initializeAllLiveAttributes();
      return n;
  }

  /**
   * Copy the fields of the current node into the given node.
   * @param n a node of the type of this.
   */
  @Override
  protected Node copyInto(Node n) {
      super.copyInto(n);
      WebXElementImpl e = (WebXElementImpl)n;
      e.prefix = prefix;
      e.initializeAllLiveAttributes();
      return n;
  }

  /**
   * Deeply copy the fields of the current node into the given node.
   * @param n a node of the type of this.
   */
  @Override
  protected Node deepCopyInto(Node n) {
      super.deepCopyInto(n);
      WebXElementImpl e = (WebXElementImpl)n;
      e.prefix = prefix;
      e.initializeAllLiveAttributes();
      return n;
  }
  
  /**
   * To resolve the units.
   */
  protected class UnitContext implements UnitProcessor.Context {
    
    /**
     * Returns the element.
     */
    @Override
    public Element getElement() {
        return WebXElementImpl.this;
    }
    
    /**
     * Returns the size of a px CSS unit in millimeters.
     */
    @Override
    public float getPixelUnitToMillimeter() {
        return getWebXContext().getPixelUnitToMillimeter();
    }
    
    /**
     * Returns the size of a px CSS unit in millimeters.
     * This will be removed after next release.
     * @see #getPixelUnitToMillimeter()
     */
    @Override
    public float getPixelToMM() {
        return getPixelUnitToMillimeter();
    }
    
    /**
     * Returns the font-size value.
     */
    @Override
    public float getFontSize() {
        return getWebXContext().getFontSize();
    }
    
    /**
     * Returns the x-height value.
     */
    @Override
    public float getXHeight() {
        return 0.5f;
    }
    
    /**
     * Returns the viewport width used to compute units.
     */
    @Override
    public float getViewportWidth() {
        return getWebXContext().getViewportWidth();
    }
    
    /**
     * Returns the viewport height used to compute units.
     */
    @Override
    public float getViewportHeight() {
        return getWebXContext().getViewportHeight();
    }
  }
  
}
