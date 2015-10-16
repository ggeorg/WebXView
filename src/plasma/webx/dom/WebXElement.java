package plasma.webx.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * The interface is modeled after DOM1 Spec for HTML from W3C. The DTD used in
 * this DOM model is from ...
 * <p>
 * All WebX Elements are derived from this class that contains two core
 * attributes defined in the DTD.
 */
public interface WebXElement extends Element {

  /**
   * The element's identifier which is unique in a single stage.
   */
  public void setId(String newValue)
      throws DOMException;

  public String getId();

  public String getXMLbase();

  public void setXMLbase(String xmlbase)
      throws DOMException;

  public WebXRootElement getOwnerWebXElement();

  public WebXElement getViewportElement();

  /**
   * The 'class' attribute of a element that affiliates an elements with one or
   * more elements.
   */
  // public void setClassName(String newValue);
  //
  // public String getClassName();

}
