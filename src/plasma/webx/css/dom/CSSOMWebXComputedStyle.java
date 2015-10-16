package plasma.webx.css.dom;

import org.apache.batik.css.dom.CSSOMComputedStyle;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.w3c.dom.css.CSSValue;

/**
 * This class represents the computed style of an WebX element.
 */
public class CSSOMWebXComputedStyle extends CSSOMComputedStyle {

  /**
   * Creates a new computed style.
   */
  public CSSOMWebXComputedStyle(CSSEngine e,
                               CSSStylableElement elt,
                               String pseudoElt) {
      super(e, elt, pseudoElt);
  }

  /**
   * Creates a CSSValue to manage the value at the given index.
   */
  protected CSSValue createCSSValue(int idx) {
      return super.createCSSValue(idx);
  }

}
