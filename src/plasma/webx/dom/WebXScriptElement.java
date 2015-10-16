package plasma.webx.dom;

import org.w3c.dom.DOMException;

public interface WebXScriptElement
    extends WebXElement,
    WebXURIReference, WebXExternalResourceRequired {

  public String getType();
  public void setType(String type)
      throws DOMException;

}
