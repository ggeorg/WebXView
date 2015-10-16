package plasma.webx.dom.impl;

import org.apache.batik.util.DoublyIndexedTable;

import plasma.webx.dom.WebXURIReference;

/**
 * This class implements {@link WebXURIReference}.
 */
public abstract class WebXURIReferenceElement extends WebXElementImpl
    implements WebXURIReference {
  private static final long serialVersionUID = -4461037739353498373L;

  /**
   * Table mapping XML attribute names to TraitInformation objects.
   */
  protected static DoublyIndexedTable xmlTraitInformation;
  static {
    DoublyIndexedTable t = 
        new DoublyIndexedTable(WebXElementImpl.xmlTraitInformation);
//    t.put(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE,
//        new TraitInformation(true, WebXTypes.TYPE_URI));
    xmlTraitInformation = t;
  }

  /**
   * The 'xlink:href' attribute value.
   */
  protected String href;

  /**
   * Creates a new SVGOMURIReferenceElement object.
   */
  protected WebXURIReferenceElement() {
  }

  /**
   * Creates a new WebXURIReferenceElement object.
   * 
   * @param prefix
   *          The namespace prefix.
   * @param owner
   *          The owner document.
   */
  protected WebXURIReferenceElement(String prefix, WebXDocumentImpl owner) {
    super(prefix, owner);
    initializeLiveAttributes();
  }

  /**
   * Initializes all live attributes for this element.
   */
  @Override
  protected void initializeAllLiveAttributes() {
    super.initializeAllLiveAttributes();
    initializeLiveAttributes();
  }

  /**
   * Initializes the live attribute values of this element.
   */
  private void initializeLiveAttributes() {
    //href = createLiveAnimatedString(XLINK_NAMESPACE_URI, XLINK_HREF_ATTRIBUTE);
  }

  /**
   * <b>DOM</b>: Implements {@link WebXURIReference#getHref()}.
   */
  @Override
  public String getHref() {
    return href;
  }

  /**
   * Returns the table of TraitInformation objects for this element.
   */
  @Override
  protected DoublyIndexedTable getTraitInformationTable() {
    return xmlTraitInformation;
  }
}
