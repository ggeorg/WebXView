package plasma.webx.dom.impl;

import org.w3c.dom.Node;

import plasma.webx.dom.WebXDivElement;

public class WebXPElementImpl  extends WebXStylableElement
    implements WebXDivElement {
  private static final long serialVersionUID = 8921545054363013703L;
  
  /**
   * Creates a new WebXCardElementImpl object.
   */
  protected WebXPElementImpl() {
  }
  
  /**
   * Creates a new WebXCardElement object.
   * @param prefix The namespace prefix.
   * @param owner The owner document.
   */
  public WebXPElementImpl(String prefix, WebXDocumentImpl owner) {
      super(prefix, owner);
      //initializeLiveAttributes();
  }

  /**
   * <b>DOM</b>: Implements {@link Node#getLocalName()}.
   */
  public String getLocalName() {
    return WEBX_P_TAG;
  }
  
  /**
   * Returns a new uninitialized instance of this object's class.
   */
  @Override
  protected Node newNode() {
    return new WebXPElementImpl();
  }

}
