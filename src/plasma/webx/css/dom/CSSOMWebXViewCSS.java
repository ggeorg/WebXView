package plasma.webx.css.dom;

import org.apache.batik.css.dom.CSSOMViewCSS;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

/**
 * This class represents an object which provides the computed styles
 * of the elements of a WebX document.
 */
public class CSSOMWebXViewCSS extends CSSOMViewCSS {

  /**
   * Creates a new ViewCSS.
   */
  public CSSOMWebXViewCSS(CSSEngine engine) {
      super(engine);
  }

  /**
   * <b>DOM</b>: Implements {@link
   * org.w3c.dom.css.ViewCSS#getComputedStyle(Element,String)}.
   */
  public CSSStyleDeclaration getComputedStyle(Element elt,
                                              String pseudoElt) {
      if (elt instanceof CSSStylableElement) {
          return new CSSOMWebXComputedStyle(cssEngine,
                                            (CSSStylableElement)elt,
                                            pseudoElt);
      }
      return null;
  }
  
}
