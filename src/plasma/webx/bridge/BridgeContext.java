package plasma.webx.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.anim.dom.SVGOMDocument;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.ErrorConstants;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentViewport;
import org.apache.batik.bridge.BridgeContext.CSSEngineUserAgentWrapper;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterPool;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import plasma.fx.scene.ClutterActor;
import plasma.fx.scene.webxview.WebXSceneGraphBuilder;
import plasma.webx.dom.WebXContext;
import plasma.webx.dom.WebXDocument;
import plasma.webx.dom.WebXElement;
import plasma.webx.dom.impl.WebXDOMImplementation;
import plasma.webx.dom.impl.WebXDocumentImpl;

/**
 * This class represents a context used by the various bridges and the builder.
 * A bridge context is associated to a particular document and cannot be reused.
 * <p>
 * The context encapsulates the dynamic bindings between DOM elements and the
 * PlasmaFx Scene Graph nodes and the different objects required by the scene
 * graph builder to interpret a WebX DOM tree such as the current WebXView or
 * the user agent.
 */
public class BridgeContext implements ErrorConstants, WebXContext {

  /**
   * The document.
   */
  protected Document document;

  /**
   * The scene graph builder that might be used to create a scene graph subtree.
   */
  protected WebXSceneGraphBuilder builder;
  
  /**
   * The interpreter cache per document.
   * key is the language -
   * value is a Interpreter
   */
  protected Map interpreterMap = new HashMap(7);

  /**
   * The user agent.
   */
  protected UserAgent userAgent;

  /**
   * Binding Map.
   */
  protected Map<WebXElement, ClutterActor> elementNodeMap;

  /**
   * Reverse binding Map.
   * 
   * @see #elementNodeMap;
   */
  protected Map<ClutterActor, WebXElement> nodeElementMap;

  /**
   * Bridge Map: keys are namespace URI - values are HashMap (with keys are
   * local name and values are a Bridge instance).
   */
  protected Map namespaceURIMap;
  
  /**
   * Default bridge.
   * <p>
   * When a bridge is requested for an element type that does not have a brdige, and
   * there is no other bridge for elements in the same namespace, the default bridge
   * is returned. This is used for custom elements, which all use the same bridge type.
   */
  protected Bridge defaultBridge;
  
  /**
   * Default bridge reserved namespaces set.
   * Default bridges will not be created for elements that have a
   * namespace URI present in this set.
   */
  protected Set reservedNamespaceSet;
  
  /**
   * Element Data Map:
   * This is a general location for elements to 'cache'
   * data.  Such as the graphics tree for a pattern or
   * the Gradient arrays.
   *
   * This is a weak hash map and the data is referenced
   * by SoftReference so both must be referenced elsewhere
   * to stay live.
   */
  protected Map elementDataMap;
  
  /**
   * The interpreter pool used to handle scripts.
   */
  protected InterpreterPool interpreterPool;
  
  /**
   * The document loader used to load/create Document.
   */
  protected DocumentLoader documentLoader;
  
  /**
   * Indicates that no DOM listeners should be registered.  In this
   * case the generated scene graph should be totally independent of
   * the DOM tree (in practice text holds references to the source
   * text elements for font resolution).
   */
  public static final int STATIC      = 0;
  
  /**
   * Indicates that DOM listeners should be registered to support,
   * 'interactivity' this includes anchors and cursors, but does not
   * include support for DOM modifications.
   */
  public static final int INTERACTIVE = 1;
  
  /**
   * Indicates that all DOM listeners should be registered. This supports
   * 'interactivity' (anchors and cursors), as well as DOM modifications
   * listeners to update the scene graph rendering tree.
   */
  public static final int DYNAMIC     = 2;
  
  /**
   * Whether the bridge should support dynamic, or interactive features.
   */
  protected int dynamicStatus = STATIC;
  
  /**
   * The update manager.
   */
  protected UpdateManager updateManager;
  
  /**
   * The XBL manager.
   */
  protected XBLManager xblManager;
  
  /**
   * The bridge context for the primary document, if this is a bridge
   * context for a resource document.
   */
  protected BridgeContext primaryContext;
  
  /**
   * Set of WeakReferences to child BridgeContexts.
   */
  protected HashSet childContexts = new HashSet();
  
  /**
   * By default we share a unique instance of InterpreterPool.
   */
  private static InterpreterPool sharedPool = new InterpreterPool();
  
  /**
   * Constructs a new empty bridge context.
   */
  protected BridgeContext() {}
  
  /**
   * Constructs a new bridge context.
   * @param userAgent the user agent
   */
  public BridgeContext(UserAgent userAgent) {
      this(userAgent, sharedPool, new DocumentLoader(userAgent));
  }
  
  /**
   * Constructs a new bridge context.
   * @param userAgent the user agent
   * @param loader document loader
   */
  public BridgeContext(UserAgent userAgent, DocumentLoader loader) {
      this(userAgent, sharedPool, loader);
  }
  
  /**
   * Constructs a new bridge context.
   * @param userAgent the user agent
   * @param interpreterPool the interpreter pool
   * @param documentLoader document loader
   */
  public BridgeContext(UserAgent userAgent,
                       InterpreterPool interpreterPool,
                       DocumentLoader documentLoader) {
      this.userAgent = userAgent;
      //this.viewportMap.put(userAgent, new UserAgentViewport(userAgent));
      this.interpreterPool = interpreterPool;
      this.documentLoader = documentLoader;
  }
  
  /**
   * Calls dispose on this BridgeContext, if it is a child context.
   */
  @Override
  protected void finalize() {
      if (primaryContext != null) {
          dispose();
      }
  }
  
  /**
   * This function creates a new 'sub' BridgeContext to associated
   * with 'newDoc' if one currently doesn't exist, otherwise it
   * returns the BridgeContext currently associated with the
   * document.
   * @param newDoc The document to get/create a BridgeContext for.
   */
  public BridgeContext createSubBridgeContext(WebXDocumentImpl newDoc) {
      BridgeContext subCtx;

      CSSEngine eng = newDoc.getCSSEngine();
      if (eng != null) {
          subCtx = (BridgeContext) newDoc.getCSSEngine().getCSSContext();
          return subCtx;
      }

      subCtx = createBridgeContext(newDoc);
      subCtx.primaryContext = primaryContext != null ? primaryContext : this;
      subCtx.primaryContext.childContexts.add(new WeakReference(subCtx));
      subCtx.dynamicStatus = dynamicStatus;
      subCtx.setBuilder(getBuilder());
      //subCtx.setTextPainter(getTextPainter());
      subCtx.setDocument(newDoc);
      subCtx.initializeDocument(newDoc);
      if (isInteractive())
          subCtx.addUIEventListeners(newDoc);
      return subCtx;
  }
  
  /**
   * This function creates a new BridgeContext, it mostly
   * exists so subclasses can provide an instance of
   * themselves when a sub BridgeContext is needed.
   */
  public BridgeContext createBridgeContext(WebXDocumentImpl doc) {
//      if (doc.isSVG12()) {
//          return new SVG12BridgeContext(getUserAgent(), getDocumentLoader());
//      }
      return new BridgeContext(getUserAgent(), getDocumentLoader());
  }
  
  /**
   * Initializes the given document.
   */
  protected void initializeDocument(Document document) {
      WebXDocumentImpl doc = (WebXDocumentImpl)document;
      CSSEngine eng = doc.getCSSEngine();
      if (eng == null) {
          WebXDOMImplementation impl;
          impl = (WebXDOMImplementation)doc.getImplementation();
          eng = impl.createCSSEngine(doc, this);
          eng.setCSSEngineUserAgent(new CSSEngineUserAgentWrapper(userAgent));
          doc.setCSSEngine(eng);
          eng.setMedia(userAgent.getMedia());
          String uri = userAgent.getUserStyleSheetURI();
          if (uri != null) {
              try {
                  ParsedURL url = new ParsedURL(uri);
                  eng.setUserAgentStyleSheet
                      (eng.parseStyleSheet(url, "all"));
              } catch (Exception e) {
                  userAgent.displayError(e);
              }
          }
          eng.setAlternateStyleSheet(userAgent.getAlternateStyleSheet());
      }
  }
  
  /**
   * Returns the CSS engine associated with given element.
   */
  public CSSEngine getCSSEngineForElement(Element e) {
      WebXDocumentImpl doc = (WebXDocumentImpl)e.getOwnerDocument();
      return doc.getCSSEngine();
  }
  
//properties ////////////////////////////////////////////////////////////
  
  /**
   * Returns the document this bridge context is dedicated to.
   */
  public Document getDocument() {
      return document;
  }
  
  /**
   * Sets the document this bridge context is dedicated to, to the
   * specified document.
   * @param document the document
   */
  protected void setDocument(Document document) {
      if (this.document != document){
          //fontFamilyMap = null;
      }
      this.document = document;
      registerSVGBridges();
  }
  
  /**
   * Returns the map of font families
   */
//  public Map getFontFamilyMap(){
//      if (fontFamilyMap == null){
//          fontFamilyMap = new HashMap();
//      }
//      return fontFamilyMap;
//  }
  
  /**
   * Sets the map of font families to the specified value.
   *
   *@param fontFamilyMap the map of font families
   */
//  protected void setFontFamilyMap(Map fontFamilyMap) {
//      this.fontFamilyMap = fontFamilyMap;
//  }
  
  /**
   * Associates a data object with a node so it can be retrieved later.
   * This is primarily used for caching the graphics node generated from
   * a 'pattern' element.  A soft reference to the data object is used.
   */
  public void setElementData(Node n, Object data) {
      if (elementDataMap == null) {
          elementDataMap = new WeakHashMap();
      }
      elementDataMap.put(n, new SoftReference(data));
  }
  
  /**
   * Retrieves a data object associated with the given node.
   */
  public Object getElementData(Node n) {
      if (elementDataMap == null)
          return null;
      Object o = elementDataMap.get(n);
      if (o == null) return null;
      SoftReference sr = (SoftReference)o;
      o = sr.get();
      if (o == null) {
          elementDataMap.remove(n);
      }
      return o;
  }
  
  /**
   * Returns the user agent of this bridge context.
   */
  public UserAgent getUserAgent() {
      return userAgent;
  }
  
  /**
   * Sets the user agent to the specified user agent.
   * @param userAgent the user agent
   */
  protected void setUserAgent(UserAgent userAgent) {
      this.userAgent = userAgent;
  }
  
  /**
   * Returns the scene graph builder that is currently used to build the scene graph.
   */
  public WebXSceneGraphBuilder getBuilder() {
      return builder;
  }
  
  /**
   * Sets the scene graph builder that uses this context.
   */
  protected void setBuilder(WebXSceneGraphBuilder builder) {
      this.builder = builder;
  }
  
  /**
   * Returns the interpreter pool used to handle scripts.
   */
  public InterpreterPool getInterpreterPool() {
      return interpreterPool;
  }
  
  /**
   * Sets the interpreter pool used to handle scripts to the
   * specified interpreter pool.
   * @param interpreterPool the interpreter pool
   */
  protected void setInterpreterPool(InterpreterPool interpreterPool) {
      this.interpreterPool = interpreterPool;
  }
  
  /**
   * Returns a Interpreter for the specified language.
   *
   * @param language the scripting language
   */
  public Interpreter getInterpreter(String language) {
      if (document == null) {
          throw new RuntimeException("Unknown document");
      }
      Interpreter interpreter = (Interpreter)interpreterMap.get(language);
      if (interpreter == null) {
          try {
              interpreter = interpreterPool.createInterpreter(document, 
                                                              language,
                                                              null);
              String[] mimeTypes = interpreter.getMimeTypes();
              for (int i = 0; i < mimeTypes.length; i++) {
                  interpreterMap.put(mimeTypes[i], interpreter);
              }
          } catch (Exception e) {
              if (userAgent != null) {
                  userAgent.displayError(e);
                  return null;
              }
          }
      }

      if (interpreter == null) {
          if (userAgent != null) {
              userAgent.displayError(new Exception("Unknown language: " + language));
          }
      }

      return interpreter;
  }
  
  /**
   * Returns the document loader used to load external documents.
   */
  public DocumentLoader getDocumentLoader() {
      return documentLoader;
  }

  /**
   * Sets the document loader used to load external documents.
   * @param newDocumentLoader the new document loader
   */
  protected void setDocumentLoader(DocumentLoader newDocumentLoader) {
      this.documentLoader = newDocumentLoader;
  }
  
  /**
   * Returns true if the document is dynamic, false otherwise.
   */
  public boolean isDynamic() {
      return (dynamicStatus == DYNAMIC);
  }
  
  /**
   * Returns true if the document is interactive, false otherwise.
   */
  public boolean isInteractive() {
      return (dynamicStatus != STATIC);
  }
  
  /**
   * Sets the document as a STATIC, INTERACTIVE or DYNAMIC document.
   * Call this method before the build phase
   * (ie. before <code>gvtBuilder.build(...)</code>)
   * otherwise, that will have no effect.
   *
   *@param status the document dynamicStatus
   */
  public void setDynamicState(int status) {
      dynamicStatus = status;
  }
  
  /**
   * Sets the document as DYNAMIC if <code>dynamic</code> is true
   * STATIC otherwise.
   */
  public void setDynamic(boolean dynamic) {
      if (dynamic)
          setDynamicState(DYNAMIC);
      else
          setDynamicState(STATIC);
  }
  
  /**
   * Sets the document as INTERACTIVE if <code>interactive</code> is
   * true STATIC otherwise.
   */
  public void setInteractive(boolean interactive) {
      if (interactive)
          setDynamicState(INTERACTIVE);
      else
          setDynamicState(STATIC);
  }
  
  /**
   * Returns the update manager, if the bridge supports dynamic features.
   */
  public UpdateManager getUpdateManager() {
      return updateManager;
  }

  /**
   * Sets the update manager.
   */
  protected void setUpdateManager(UpdateManager um) {
      updateManager = um;
  }
  
  /**
   * Sets the update manager on the given BridgeContext.
   */
  protected void setUpdateManager(BridgeContext ctx, UpdateManager um) {
      ctx.setUpdateManager(um);
  }
  
  /**
   * Sets the xblManager variable of the given BridgeContext.
   */
  protected void setXBLManager(BridgeContext ctx, XBLManager xm) {
      ctx.xblManager = xm;
  }
  
  /**
   * Returns the primary bridge context.
   */
  public BridgeContext getPrimaryBridgeContext() {
      if (primaryContext != null) {
          return primaryContext;
      }
      return this;
  }
  
  /**
   * Returns an array of the child contexts.
   */
  public BridgeContext[] getChildContexts() {
      BridgeContext[] res = new BridgeContext[childContexts.size()];
      Iterator it = childContexts.iterator();
      for (int i = 0; i < res.length; i++) {
          WeakReference wr = (WeakReference) it.next();
          res[i] = (BridgeContext) wr.get();
      }
      return res;
  }
  
//reference management //////////////////////////////////////////////////
  
  /**
   * Returns a new URIResolver object.
   */
  public URIResolver createURIResolver(WebXDocument doc, DocumentLoader dl) {
      return new URIResolver(doc, dl);
  }

  @Override
  public float getPixelUnitToMillimeter() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Rectangle2D getBBox() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AffineTransform getScreenTransform() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setScreenTransform(AffineTransform at) {
    // TODO Auto-generated method stub

  }

  @Override
  public AffineTransform getCTM() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AffineTransform getGlobalTransform() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public float getViewportWidth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getViewportHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getFontSize() {
    // TODO Auto-generated method stub
    return 0;
  }

}
