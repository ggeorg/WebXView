package plasma.webx.util;

import org.apache.batik.util.CSSConstants;
import org.apache.batik.util.XMLConstants;

/**
 * Define WebX constants, such as tag names, attribute names and URI
 */
public interface WebXConstants extends CSSConstants, XMLConstants {
  
  //
  // The CSS mime-type string.
  //
  String CSS_MIME_TYPE = "text/css";

  //
  // WEBX general
  //

  String WEBX_PUBLIC_ID =
      "-//WEBXFORUM//DTD WEBX 1.0//EN";
  String WEBX_SYSTEM_ID =
      "http://www.webxforum.org/DTD/webx10.dtd";
  String WEBX_NAMESPACE_URI =
      "http://www.webx.org/2015/webx";
  String WEBX_VERSION =
      "1.0";

  //
  // WebX tags
  //
  
  String WEBX_DIV_TAG = "div";
  String WEBX_P_TAG = "p";
  String WEBX_SCRIPT_TAG = "script";
  String WEBX_STYLE_TAG = "style";
  String WEBX_ROOT_TAG = "webx";

  //
  // WEBX attributes
  //
  
  String WEBX_MEDIA_ATTRIBUTE = "media";
  String WEBX_TITLE_ATTRIBUTE = "title";
  String WEBX_TYPE_ATTRIBUTE = "type";

  //
  // WebX attribute value
  //
  
  String WEBX_CLASS_ATTRIBUTE = "class";
  String WEBX_CONTENT_SCRIPT_TYPE_ATTRIBUTE = "contentScriptType";
  String WEBX_CONTENT_STYLE_TYPE_ATTRIBUTE = "contentStyleType";
  String WEBX_ID_ATTRIBUTE = XMLConstants.XML_ID_ATTRIBUTE;
  String WEBX_SCRIPT_TYPE_ECMASCRIPT = "text/ecmascript";
  String WEBX_VERSION_ATTRIBUTE = "version";
  

  //
  // default values for attributes
  //
  
  //
  // event constants
  //
  
  String WEBX_EVENT_CLICK     = "click";
  String WEBX_EVENT_KEYDOWN   = "keydown";
  String WEBX_EVENT_KEYPRESS  = "keypress";
  String WEBX_EVENT_KEYUP     = "keyup";
  String WEBX_EVENT_MOUSEDOWN = "mousedown";
  String WEBX_EVENT_MOUSEMOVE = "mousemove";
  String WEBX_EVENT_MOUSEOUT  = "mouseout";
  String WEBX_EVENT_MOUSEOVER = "mouseover";
  String WEBX_EVENT_MOUSEUP   = "mouseup";
}
