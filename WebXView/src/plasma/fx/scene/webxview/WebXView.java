package plasma.fx.scene.webxview;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.ext.swing.Resources;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.resources.ResourceManager;

import plasma.fx.scene.ClutterActor;
import plasma.fx.scene.ClutterColor;
import plasma.fx.scene.ClutterStage;
import plasma.fx.scene.layout.ClutterBinLayout;
import plasma.fx.scene.webxview.webx.GVTTreeBuilderListener;
import plasma.fx.scene.webxview.webx.LinkActivationListener;
import plasma.fx.scene.webxview.webx.WebXDocumentLoaderListener;
import plasma.fx.scene.webxview.webx.WebXLoadEventDispatcherListener;
import plasma.jna.clutter.ClutterLibrary;

import com.sun.jna.Pointer;

/**
 * The class represents a WebX viewer.
 */
public class WebXView extends ClutterActor
    implements ActionMap,
    WebXDocumentLoaderListener,
    GVTTreeBuilderListener,
    WebXLoadEventDispatcherListener,
    LinkActivationListener,
    UpdateManagerListener {

  private static String EOL;
  static {
    try {
      EOL = System.getProperty("line.separator", "\n");
    } catch (SecurityException e) {
      EOL = "\n";
    }
  }

  /**
   * The gui resources file name
   */
  public static final String RESOURCES =
      "plasma.fx.scene.webxview.resources.GUI";

  // The action names.
  public static final String ABOUT_ACTION = "AboutAction";
  public static final String OPEN_ACTION = "OpenAction";
  public static final String OPEN_LOCATION_ACTION = "OpenLocationAction";
  public static final String NEW_WINDOW_ACTION = "NewWindowAction";
  public static final String RELOAD_ACTION = "ReloadAction";
  public static final String SAVE_AS_ACTION = "SaveAsAction";
  public static final String BACK_ACTION = "BackAction";
  public static final String FORWARD_ACTION = "ForwardAction";
  public static final String FULL_SCREEN_ACTION = "FullScreenAction";
  public static final String PRINT_ACTION = "PrintAction";
  public static final String EXPORT_AS_JPG_ACTION = "ExportAsJPGAction";
  public static final String EXPORT_AS_PNG_ACTION = "ExportAsPNGAction";
  public static final String EXPORT_AS_TIFF_ACTION = "ExportAsTIFFAction";
  public static final String PREFERENCES_ACTION = "PreferencesAction";
  public static final String CLOSE_ACTION = "CloseAction";
  public static final String VIEW_SOURCE_ACTION = "ViewSourceAction";
  public static final String EXIT_ACTION = "ExitAction";
  public static final String RESET_TRANSFORM_ACTION = "ResetTransformAction";
  public static final String ZOOM_IN_ACTION = "ZoomInAction";
  public static final String ZOOM_OUT_ACTION = "ZoomOutAction";
  public static final String PREVIOUS_TRANSFORM_ACTION = "PreviousTransformAction";
  public static final String NEXT_TRANSFORM_ACTION = "NextTransformAction";
  public static final String USE_STYLESHEET_ACTION = "UseStylesheetAction";
  public static final String PLAY_ACTION = "PlayAction";
  public static final String PAUSE_ACTION = "PauseAction";
  public static final String STOP_ACTION = "StopAction";
  public static final String MONITOR_ACTION = "MonitorAction";
  public static final String DOM_VIEWER_ACTION = "DOMViewerAction";
  public static final String SET_TRANSFORM_ACTION = "SetTransformAction";
  public static final String FIND_DIALOG_ACTION = "FindDialogAction";
  public static final String THUMBNAIL_DIALOG_ACTION = "ThumbnailDialogAction";
  public static final String FLUSH_ACTION = "FlushAction";
  public static final String TOGGLE_DEBUGGER_ACTION = "ToggleDebuggerAction";

  /**
   * The cursor indicating that an operation is pending.
   */
  // TODO WAIT_CURSOR

  /**
   * The default cursor.
   */
  // TODO DEFAULT_CURSOR

  /**
   * Name for the os-name property.
   */
  public static final String PROPERTY_OS_NAME = 
      Resources.getString("WebXView.property.os.name");

  /**
   * Name for the os.name default
   */
  public static final String PROPERTY_OS_NAME_DEFAULT = 
      Resources.getString("WebXView.property.os.name.default");

  /**
   * Name for the os.name property prefix we are looking for in OpenAction to
   * work around JFileChooser bug
   */
  public static final String PROPERTY_OS_WINDOWS_PREFIX = 
      Resources.getString("WebXView.property.os.windows.prefix");
  
  /**
   * Resource string name for the Open dialog.
   */
  protected static final String OPEN_TITLE = "Open.title";
  
  /**
   * The input handlers
   */
  protected static Vector handlers;
  
  /**
   * The default input handler
   */
  protected static WebXViewInputHandler defaultHandler = new WebXInputHandler();
  
  /**
   * The resource bundle
   */
  protected static ResourceBundle bundle;
  
  /**
   * The resource manager
   */
  protected static ResourceManager resources;
  static {
      bundle = ResourceBundle.getBundle(RESOURCES, Locale.getDefault());
      resources = new ResourceManager(bundle);
  }
  
  

  public WebXView() {
    super();
  }

  public static void main(String[] argv) {
    // if (argv.length == 0) {
    // System.err.println("wrong usage");
    // System.exit(1);
    // }

    ClutterLibrary.INSTANCE.clutter_init(0, Pointer.NULL);

    try {
      ClutterStage stage = new ClutterStage();
      stage.connect(new QuitSignalHandler());
      stage.setSize(1024, 768);
      stage.setTitle("PlasmaFx Browser");
      stage.setLayoutManager(new ClutterBinLayout());
      // stage.setAlignX(ClutterActorAlign.CLUTTER_ACTOR_ALIGN_FILL);
      // stage.setAlignY(ClutterActorAlign.CLUTTER_ACTOR_ALIGN_FILL);
      stage.setResizable(true);
      stage.show();

      WebXView webview = new WebXView();
      webview.setBackgroundColor(ClutterColor.SKY_BLUE_LIGHT);
      webview.setExpandX(true);
      webview.setExpandY(true);
      stage.addChild(webview);

      ClutterLibrary.INSTANCE.clutter_main();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
