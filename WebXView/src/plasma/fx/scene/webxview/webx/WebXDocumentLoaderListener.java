package plasma.fx.scene.webxview.webx;


/**
 * This interface represents a listener to the WebXDocumentLoaderEvent events.
 */
public interface WebXDocumentLoaderListener {

  /**
   * Called when the loading of a document was started.
   */
  void documentLoadingStarted(WebXDocumentLoaderEvent e);

  /**
   * Called when the loading of a document was completed.
   */
  void documentLoadingCompleted(WebXDocumentLoaderEvent e);

  /**
   * Called when the loading of a document was cancelled.
   */
  void documentLoadingCancelled(WebXDocumentLoaderEvent e);

  /**
   * Called when the loading of a document has failed.
   */
  void documentLoadingFailed(WebXDocumentLoaderEvent e);
  
}
