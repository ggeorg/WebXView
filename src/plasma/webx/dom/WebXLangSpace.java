package plasma.webx.dom;

import org.w3c.dom.DOMException;

public interface WebXLangSpace {
  String getXMLlang();
  void setXMLlang(String xmllang)
      throws DOMException;

  String getXMLspace();
  void setXMLspace(String xmlspace)
      throws DOMException;
}
