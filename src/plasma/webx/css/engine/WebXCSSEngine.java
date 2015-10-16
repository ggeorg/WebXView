package plasma.webx.css.engine;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.engine.value.css2.FontShorthandManager;
import org.apache.batik.css.engine.value.css2.VisibilityManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;

/**
 * This class provides a CSS engine initialized for WebX.
 */
public class WebXCSSEngine extends CSSEngine {

  /**
   * Creates a new WebXCSSEngine.
   * @param doc The associated document.
   * @param uri The document URI.
   * @param p The CSS parser to use.
   * @param ctx The CSS context.
   */
  public WebXCSSEngine(Document doc,
                       ParsedURL uri,
                       ExtendedParser p,
                       CSSContext ctx) {
      super(doc, uri, p,
            WEBX_VALUE_MANAGERS,
            WEBX_SHORTHAND_MANAGERS,
            null,
            null,
            "style",
            null,
            "class",
            true,
            null,
            ctx);
      // WebX defines line-height to be font-size.
      lineHeightIndex = fontSizeIndex;
  }
  
  /**
   * Creates a new WebXCSSEngine.
   * @param doc The associated document.
   * @param uri The document URI.
   * @param p The CSS parser to use.
   * @param vms Extension value managers.
   * @param sms Extension shorthand managers.
   * @param ctx The CSS context.
   */
  public WebXCSSEngine(Document doc,
                       ParsedURL uri,
                       ExtendedParser p,
                       ValueManager[] vms,
                       ShorthandManager[] sms,
                       CSSContext ctx) {
      super(doc, uri, p,
            mergeArrays(WEBX_VALUE_MANAGERS, vms),
            mergeArrays(WEBX_SHORTHAND_MANAGERS, sms),
            null,
            null,
            "style",
            null,
            "class",
            true,
            null,
            ctx);
      // SVG defines line-height to be font-size.
      lineHeightIndex = fontSizeIndex;
  }

  protected WebXCSSEngine(Document doc,
                          ParsedURL uri,
                          ExtendedParser p,
                          ValueManager[] vms,
                          ShorthandManager[] sms,
                          String[] pe,
                          String sns,
                          String sln,
                          String cns,
                          String cln,
                          boolean hints,
                          String hintsNS,
                          CSSContext ctx) {
      super(doc, uri, p,
            mergeArrays(WEBX_VALUE_MANAGERS, vms),
            mergeArrays(WEBX_SHORTHAND_MANAGERS, sms),
            pe, sns, sln, cns, cln, hints, hintsNS, ctx);
      // SVG defines line-height to be font-size.
      lineHeightIndex = fontSizeIndex;
  }


  /**
   * Merges the given arrays.
   */
  protected static ValueManager[] mergeArrays(ValueManager[] a1,
                                              ValueManager[] a2) {
      ValueManager[] result = new ValueManager[a1.length + a2.length];
      System.arraycopy(a1, 0, result, 0, a1.length);
      System.arraycopy(a2, 0, result, a1.length, a2.length);
      return result;
  }

  /**
   * Merges the given arrays.
   */
  protected static ShorthandManager[] mergeArrays(ShorthandManager[] a1,
                                                  ShorthandManager[] a2) {
      ShorthandManager[] result =
          new ShorthandManager[a1.length + a2.length];
      System.arraycopy(a1, 0, result, 0, a1.length);
      System.arraycopy(a2, 0, result, a1.length, a2.length);
      return result;
  }

  /**
   * The value managers for SVG.
   */
  public static final ValueManager[] WEBX_VALUE_MANAGERS = {
    new VisibilityManager()
  };

  /**
   * The shorthand managers for SVG.
   */
  public static final ShorthandManager[] WEBX_SHORTHAND_MANAGERS = {
      new FontShorthandManager()
  };
  
  
}
