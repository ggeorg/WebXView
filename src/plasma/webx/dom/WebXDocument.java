package plasma.webx.dom;

import org.w3c.dom.Document;
import org.w3c.dom.events.DocumentEvent;

public interface WebXDocument extends Document, DocumentEvent {
  String getTitle();
  String getReferrer();
  String getDomain();
  String getURL();
  WebXRootElement getRootElement();
}
