package plasma.fx.scene.webxview;

import org.apache.batik.bridge.BridgeException;
import org.w3c.dom.Document;

import plasma.webx.bridge.BridgeContext;
import plasma.webx.util.WebXConstants;

/**
 * This class is responsible for creating a PlasmaFx scene graph using an WebX
 * DOM tree.
 */
public class WebXSceneGraphBuilder
    implements WebXConstants {

  /**
   * Constructs a new builder.
   */
  public WebXSceneGraphBuilder() {
  }

  /**
   * Build using the specified bridge context and WebX document.
   * 
   * @param ctx
   *          the bridge context
   * @param document
   *          the WebX document to build from
   * @exception BridgeException
   *              if an error occurred while constructing the scene graph.
   */
  public void build(BridgeContext ctx, Document document) {

  }

}
