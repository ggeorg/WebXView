package plasma.webx.dom;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * This interface is the placeholder for WebX application informations.
 */
public interface WebXContext {

  // Constants for percentage interpretation.
  int PERCENTAGE_FONT_SIZE = 0;
  int PERCENTAGE_VIEWPORT_WIDTH = 1;
  int PERCENTAGE_VIEWPORT_HEIGHT = 2;
  int PERCENTAGE_VIEWPORT_SIZE = 3;

  /**
   * Returns the size of a px CSS unit in millimeters.
   */
  float getPixelUnitToMillimeter();

  /**
   * Returns the tight bounding box in current user space (i.e., after
   * application of the transform attribute, if any) on the geometry of all
   * contained graphics elements, exclusive of stroke-width and filter effects).
   */
  Rectangle2D getBBox();

  /**
   * Returns the transform from the global transform space to pixels.
   */
  AffineTransform getScreenTransform();

  /**
   * Sets the transform to be used from the global transform space to pixels.
   */
  void setScreenTransform(AffineTransform at);

  /**
   * Returns the transformation matrix from current user units (i.e., after
   * application of the transform attribute, if any) to the viewport coordinate
   * system for the nearestViewportElement.
   */
  AffineTransform getCTM();

  /**
   * Returns the global transformation matrix from the current element to the
   * root.
   */
  AffineTransform getGlobalTransform();

  /**
   * Returns the width of the viewport which directly contains the associated
   * element.
   */
  float getViewportWidth();

  /**
   * Returns the height of the viewport which directly contains the associated
   * element.
   */
  float getViewportHeight();

  /**
   * Returns the font-size on the associated element.
   */
  float getFontSize();

}
