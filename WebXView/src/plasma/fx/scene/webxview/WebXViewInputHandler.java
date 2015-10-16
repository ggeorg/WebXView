package plasma.fx.scene.webxview;

import java.io.File;

import org.apache.batik.util.ParsedURL;

/**
 * This is the interface expected from classes which can handle specific types
 * of input for the WebXView. The simplest implementation will simply handle
 * WebX documents. Other, more sophisticated implementations will handle other
 * types of documents and convert them into WebX before displaying them in
 * WebXView.
 */
public interface WebXViewInputHandler {

  /**
   * Returns the list of mime types handled by this handler.
   */
  String[] getHandledMimeTypes();

  /**
   * Returns the list of file extensions handled by this handler
   */
  String[] getHandledExtensions();

  /**
   * Returns a description for this handler
   */
  String getDescription();

  /**
   * Returns true if the input file can be handled by the handler
   */
  boolean accept(File f);

  /**
   * Returns true if the input URI can be handled by the handler
   * 
   * @param purl
   *          URL describing the candidate input
   */
  boolean accept(ParsedURL purl);

  /**
   * Handles the given input for the given WebXView
   */
  void handle(ParsedURL purl, WebXView webxview) throws Exception;

}
