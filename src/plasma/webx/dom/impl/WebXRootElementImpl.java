package plasma.webx.dom.impl;

import org.apache.batik.anim.dom.AttributeInitializer;
import org.apache.batik.dom.util.XMLSupport;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGSVGElement;

import plasma.webx.dom.WebXRootElement;

/**
 * This class implements {@link plasma.webx.dom.WebXRootElement}.
 */
public class WebXRootElementImpl extends WebXStylableElement
    implements WebXRootElement {
  private static final long serialVersionUID = -639903799966077208L;

  /**
   * The attribute initializer.
   */
  protected static final AttributeInitializer attributeInitializer;
  static {
    attributeInitializer = new AttributeInitializer(7);
    attributeInitializer.addAttribute(XMLSupport.XMLNS_NAMESPACE_URI,
        null,
        "xmlns",
        WEBX_NAMESPACE_URI);
//    attributeInitializer.addAttribute(XMLSupport.XMLNS_NAMESPACE_URI,
//        "xmlns",
//        "xlink",
//        XLinkSupport.XLINK_NAMESPACE_URI);
//    attributeInitializer.addAttribute(null,
//        null,
//        SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE,
//        "xMidYMid meet");
//    attributeInitializer.addAttribute(null,
//        null,
//        SVG_ZOOM_AND_PAN_ATTRIBUTE,
//        SVG_MAGNIFY_VALUE);
    attributeInitializer.addAttribute(null,
        null,
        WEBX_VERSION_ATTRIBUTE,
        WEBX_VERSION);
    attributeInitializer.addAttribute(null,
        null,
        WEBX_CONTENT_SCRIPT_TYPE_ATTRIBUTE,
        "text/ecmascript");
    attributeInitializer.addAttribute(null,
        null,
        WEBX_CONTENT_STYLE_TYPE_ATTRIBUTE,
        "text/css");
  }
  
  /**
   * Creates a new WebXRootElementImpl object.
   */
  protected WebXRootElementImpl() {
  }
  
  /**
   * Creates a new WebXRootElement object.
   * @param prefix The namespace prefix.
   * @param owner The owner document.
   */
  public WebXRootElementImpl(String prefix, WebXDocumentImpl owner) {
      super(prefix, owner);
      initializeLiveAttributes();
  }
  
  /**
   * Initializes all live attributes for this element.
   */
  protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      initializeLiveAttributes();
  }
  
  /**
   * Initializes the live attribute values of this element.
   */
  private void initializeLiveAttributes() {
      //className = createLiveAnimatedString(null,WEBX_CLASS_ATTRIBUTE);
  }
  
  /**
   * <b>DOM</b>: Implements {@link Node#getLocalName()}.
   */
  public String getLocalName() {
      return WEBX_ROOT_TAG;
  }
  
  /**
   * <b>DOM</b>: Implements {@link SVGSVGElement#getContentScriptType()}.
   */
  public String getContentScriptType() {
      return getAttributeNS(null, WEBX_CONTENT_SCRIPT_TYPE_ATTRIBUTE);
  }
  
  /**
   * <b>DOM</b>: Implements {@link SVGSVGElement#setContentScriptType(String)}.
   */
  public void setContentScriptType(String type) {
      setAttributeNS(null, WEBX_CONTENT_SCRIPT_TYPE_ATTRIBUTE, type);
  }
  
  /**
   * <b>DOM</b>: Implements {@link SVGSVGElement#getContentStyleType()}.
   */
  public String getContentStyleType() {
      return getAttributeNS(null, WEBX_CONTENT_STYLE_TYPE_ATTRIBUTE);
  }
  
  /**
   * <b>DOM</b>: Implements {@link SVGSVGElement#setContentStyleType(String)}.
   */
  public void setContentStyleType(String type) {
      setAttributeNS(null, WEBX_CONTENT_STYLE_TYPE_ATTRIBUTE, type);
  }

  /**
   * Returns a new uninitialized instance of this object's class.
   */
  @Override
  protected Node newNode() {
    return new WebXRootElementImpl();
  }
}
