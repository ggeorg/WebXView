package plasma.webx.dom.impl;

import org.apache.batik.anim.dom.AttributeInitializer;
import org.apache.batik.anim.dom.SVGOMAnimatedBoolean;
import org.apache.batik.anim.dom.SVGOMURIReferenceElement;
import org.apache.batik.anim.dom.TraitInformation;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.SVGTypes;
import org.apache.batik.util.XMLConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGScriptElement;

import plasma.webx.dom.WebXDivElement;
import plasma.webx.dom.WebXRootElement;
import plasma.webx.dom.WebXScriptElement;
import plasma.webx.util.WebXTypes;

/**
 * This class implements {@link plasma.webx.dom.WebXScriptElement}.
 */
public class WebXScriptElementImpl  extends WebXURIReferenceElement
    implements WebXScriptElement {
  private static final long serialVersionUID = 8921545054363013703L;
  
  /**
   * Table mapping XML attribute names to TraitInformation objects.
   */
  protected static DoublyIndexedTable xmlTraitInformation;
  static {
      DoublyIndexedTable t =
          new DoublyIndexedTable(WebXURIReferenceElement.xmlTraitInformation);
//      t.put(null, SVG_EXTERNAL_RESOURCES_REQUIRED_ATTRIBUTE,
//              new TraitInformation(true, WebXTypes.TYPE_BOOLEAN));
//       t.put(null, SVG_TYPE_ATTRIBUTE,
//               new TraitInformation(false, SVGTypes.TYPE_CDATA));
      xmlTraitInformation = t;
  }
  
  /**
   * The attribute initializer.
   */
  protected static final AttributeInitializer attributeInitializer;
  static {
      attributeInitializer = new AttributeInitializer(1);
      attributeInitializer.addAttribute(XMLConstants.XMLNS_NAMESPACE_URI,
                                        null, "xmlns:xlink",
                                        XMLConstants.XLINK_NAMESPACE_URI);
      attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI,
                                        "xlink", "type", "simple");
      attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI,
                                        "xlink", "show", "other");
      attributeInitializer.addAttribute(XMLConstants.XLINK_NAMESPACE_URI,
                                        "xlink", "actuate", "onLoad");
  }
  
  /**
   * The 'externalResourcesRequired' attribute value.
   */
  protected SVGAnimatedBoolean externalResourcesRequired;
  
  /**
   * Creates a new WebXCardElementImpl object.
   */
  protected WebXScriptElementImpl() {
  }
  
  /**
   * Creates a new WebXCardElement object.
   * @param prefix The namespace prefix.
   * @param owner The owner document.
   */
  public WebXScriptElementImpl(String prefix, WebXDocumentImpl owner) {
      super(prefix, owner);
      //initializeLiveAttributes();
  }

  /**
   * <b>DOM</b>: Implements {@link Node#getLocalName()}.
   */
  @Override
  public String getLocalName() {
    return WEBX_SCRIPT_TAG;
  }
  
  /**
   * <b>DOM</b>: Implements {@link WebXScriptElement#getType()}.
   */
  @Override
  public String getType() {
      return getAttributeNS(null, WEBX_TYPE_ATTRIBUTE);
  }
  
  /**
   * <b>DOM</b>: Implements {@link WebXScriptElement#setType(String)}.
   */
  @Override
  public void setType(String type) throws DOMException {
      setAttributeNS(null, WEBX_TYPE_ATTRIBUTE, type);
  }
  
//SVGExternalResourcesRequired support /////////////////////////////
  
  /**
   * <b>DOM</b>: Implements {@link
   * org.w3c.dom.svg.SVGExternalResourcesRequired}.
   */
  @Override
  public SVGAnimatedBoolean getExternalResourcesRequired() {
      return externalResourcesRequired;
  }
  
  /**
   * Returns the AttributeInitializer for this element type.
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
    return new WebXScriptElementImpl();
  }

  /**
   * Returns the table of TraitInformation objects for this element.
   */
  protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
  }
}
