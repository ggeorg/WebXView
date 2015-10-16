package plasma.webx.dom.impl;

import org.apache.batik.anim.dom.SVGOMDocument;
import org.apache.batik.anim.dom.SVGOMElement;
import org.apache.batik.anim.dom.SVGStylableElement;
import org.apache.batik.anim.dom.TraitInformation;
import org.apache.batik.css.dom.CSSOMStoredStyleDeclaration;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.SVGTypes;
import org.w3c.dom.DOMException;

/**
 * This class provides a common superclass for elements which implement
 * WebXStylable.
 */
public abstract class WebXStylableElement extends WebXElementImpl
    implements CSSStylableElement {
  private static final long serialVersionUID = 4386875710085905207L;
  
  /**
   * Table mapping XML attribute names to TraitInformation objects.
   */
  protected static DoublyIndexedTable xmlTraitInformation;
  static {
      DoublyIndexedTable t =
          new DoublyIndexedTable(WebXElementImpl.xmlTraitInformation);
      t.put(null, WEBX_CLASS_ATTRIBUTE,
              new TraitInformation(true, SVGTypes.TYPE_CDATA));
      xmlTraitInformation = t;
  }
  
  /**
   * The computed style map.
   */
  protected StyleMap computedStyleMap;
  
  /**
   * The override style declaration for this element.
   */
  protected OverrideStyleDeclaration overrideStyleDeclaration;

  public WebXStylableElement() {
  }

  public WebXStylableElement(String prefix, WebXDocumentImpl owner) {
    super(prefix, owner);
  }

  /**
   * This class is a CSSStyleDeclaration for the override style of element.
   */
  protected class OverrideStyleDeclaration extends CSSOMStoredStyleDeclaration {
    
    /**
     * Creates a new OverrideStyleDeclaration.
     */
    protected OverrideStyleDeclaration(CSSEngine eng) {
        super(eng);
        declaration = new org.apache.batik.css.engine.StyleDeclaration();
    }
    
 // ModificationHandler ///////////////////////////////////////////////
    
    /**
     * Called when the value text has changed.
     */
    public void textChanged(String text) throws DOMException {
        ((WebXDocumentImpl) ownerDocument).overrideStyleTextChanged
            (WebXStylableElement.this, text);
    }
  }
}
