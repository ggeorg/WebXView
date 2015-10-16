package plasma.fx.scene.webxview.webx;

import java.util.EventObject;

import plasma.webx.dom.WebXElement;

/**
 * This class represents an event which indicate an event originated
 * from a GVTTreeBuilder instance.
 */
public class LinkActivationEvent extends EventObject {
  private static final long serialVersionUID = 1424692841751976602L;

  /**
   * The URI the link references.
   */
  protected String referencedURI;
  
  /**
   * Creates a new LinkActivationEvent.
   * @param source the object that originated the event, ie. the
   *               GVTTreeBuilder.
   * @param link   the link element.
   * @param uri    the URI of the document loaded.
   */
  public LinkActivationEvent(Object source, WebXElement link, String uri) {
      super(source);
      referencedURI = uri;
  }
  
  /**
   * Returns the referenced URI.
   */
  public String getReferencedURI() {
      return referencedURI;
  }
}
