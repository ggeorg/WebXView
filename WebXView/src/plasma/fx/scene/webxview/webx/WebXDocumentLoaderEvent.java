package plasma.fx.scene.webxview.webx;

import java.util.EventObject;

import plasma.webx.dom.WebXDocument;

/**
 * This class represents an event which indicate an event originated from a
 * WebXDocumentLoader instance.
 */
public class WebXDocumentLoaderEvent extends EventObject {
  private static final long serialVersionUID = -7225203590511867685L;

  /**
   * The associated SVG document.
   */
  protected WebXDocument webxDocument;

  /**
   * Creates a new SVGDocumentLoaderEvent.
   * 
   * @param source
   *          the object that originated the event, ie. the SVGDocumentLoader.
   * @param doc
   *          The associated document.
   */
  public WebXDocumentLoaderEvent(Object source, WebXDocument doc) {
    super(source);
    webxDocument = doc;
  }

  /**
   * Returns the associated SVG document, or null if the loading was just
   * started or an error occured.
   */
  public WebXDocument getWebXDocument() {
    return webxDocument;
  }
}
