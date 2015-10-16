package plasma.fx.scene.webxview.webx;

import java.util.EventObject;

import org.apache.batik.gvt.GraphicsNode;

/**
 * This class represents an event which indicate an event originated from WebXLoadEventDispatcher instance.
 */
public class WebXLoadEventDispatcherEvent extends EventObject {
  private static final long serialVersionUID = 1875260731819139391L;
  
  /**
   * The GVT root.
   */
  protected GraphicsNode gvtRoot;
  
  /**
   * Creates a new SVGLoadEventDispatcherEvent.
   * @param source the object that originated the event, ie. the
   *               SVGLoadEventDispatcher.
   * @param root   the GVT root.
   */
  public WebXLoadEventDispatcherEvent(Object source, GraphicsNode root) {
      super(source);
      gvtRoot = root;
  }
  
  /**
   * Returns the GVT tree root, or null if the gvt construction
   * was not completed or just started.
   */
  public GraphicsNode getGVTRoot() {
      return gvtRoot;
  }
}
