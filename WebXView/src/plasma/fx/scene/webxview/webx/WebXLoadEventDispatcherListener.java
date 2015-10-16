package plasma.fx.scene.webxview.webx;

/**
 * This interface represents a listener to the WebXLoadEventDispatcherEvent events.
 */
public interface WebXLoadEventDispatcherListener {

  /**
   * Called when a onload event dispatch started.
   */
  void webxLoadEventDispatchStarted(WebXLoadEventDispatcherEvent e);

  /**
   * Called when a onload event dispatch was completed.
   */
  void webxLoadEventDispatchCompleted(WebXLoadEventDispatcherEvent e);

  /**
   * Called when a onload event dispatch was cancelled.
   */
  void webxLoadEventDispatchCancelled(WebXLoadEventDispatcherEvent e);

  /**
   * Called when a onload event dispatch failed.
   */
  void webxLoadEventDispatchFailed(WebXLoadEventDispatcherEvent e);
  
}
