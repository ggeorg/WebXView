package plasma.webx.dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.batik.dom.util.DocumentFactory;

/**
 * This interface represents an object which can build a WebXDocument.
 */
public interface WebXDocumentFactory extends DocumentFactory {

  /**
   * Creates a WebX Document instance.
   * 
   * @param uri
   *          The document URI.
   * @throws IOException
   *           IOException if an error occurred while reading the document.
   */
  WebXDocument createWebXDocument(String uri) 
      throws IOException;

  /**
   * Creates a WebX Document instance.
   * 
   * @param uri
   *          The document URI.
   * @param is
   *          The document input stream.
   * @exception IOException
   *              if an error occurred while reading the document.
   */
  WebXDocument createWebXDocument(String uri, InputStream is)
      throws IOException;
  
  /**
   * Creates a WebX Document instance.
   * @param uri The document URI.
   * @param r The document reader.
   * @exception IOException if an error occurred while reading the document.
   */
  WebXDocument createWebXDocument(String uri, Reader r) 
      throws IOException;
}
