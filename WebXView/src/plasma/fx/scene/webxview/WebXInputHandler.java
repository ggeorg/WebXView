package plasma.fx.scene.webxview;

import java.io.File;

import org.apache.batik.apps.svgbrowser.JSVGViewerFrame;
import org.apache.batik.util.ParsedURL;

/**
 * This implementation of the {@code WebXViewInputHandler} class simply displays
 * an WebX file into the {@link WebXView}.
 */
public class WebXInputHandler implements WebXViewInputHandler {
  public static final String[] WEBX_MIME_TYPES = 
    { "text/xml" }; // TODO we need our own MIME type
  
  public static final String[] WEBX_FILE_EXTENSIONS =
    { ".xml" };
  
  /**
   * Returns the list of MIME types handled by this handler.
   */
  public String[] getHandledMimeTypes() {
      return WEBX_MIME_TYPES;
  }
  
  /**
   * Returns the list of file extensions handled by this handler
   */
  public String[] getHandledExtensions() {
      return WEBX_FILE_EXTENSIONS;
  }
  
  /**
   * Returns a description for this handler.
   */
  public String getDescription() {
      return "";
  }
  
  /**
   * Handles the given input for the given WebXView.
   */
  public void handle(ParsedURL purl, WebXView webxview) {
      webxview.getWebXCanvas().loadWebXDocument(purl.toString());
  }
  
  /**
   * Returns true if the input file can be handled.
   */
  public boolean accept(File f) {
      return f != null && f.isFile() && accept(f.getPath());
  }
  
  /**
   * Returns true if the input URI can be handled by the handler
   */
  public boolean accept(ParsedURL purl) {
      // <!> Note: this should be improved to rely on Mime Type 
      //     when the http protocol is used. This will use the 
      //     ParsedURL.getContentType method.
      if (purl == null) {
          return false;
      }

      String path = purl.getPath();
      if (path == null) return false;

      return accept(path);
  }
  
  /**
   * Returns true if the resource at the given path can be handled
   */
  public boolean accept(String path) {
      if (path == null) return false;
      for (int i=0; i<WEBX_FILE_EXTENSIONS.length; i++) {
          if (path.endsWith(WEBX_FILE_EXTENSIONS[i])) {
              return true;
          }
      }

      return false;
  }
}
