package plasma.webx.dom;

import org.w3c.dom.DOMException;

public interface WebXStyleElement extends WebXElement {
  public String getXMLSpace();
  public void setXMLSpace(String xmlspace)
    throws DOMException;
  
  public String getType();
  public void setType(String type)
      throws DOMException;
  
  public String getMedia();
  public void setMedia(String media)
      throws DOMException;
  
  public String getTitle();
  public void setTitle(String title)
      throws DOMException;
}
