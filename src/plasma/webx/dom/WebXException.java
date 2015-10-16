package plasma.webx.dom;

public class WebXException extends RuntimeException {
  private static final long serialVersionUID = -3586124912278511986L;
  public WebXException(short code, String message) {
    super(message);
    this.code = code;
  }
  public short code;
  // ExceptionCode
  public static final short WEBX_WRONG_TYPE_ERR           = 0;
  public static final short WEBX_INVALID_VALUE_ERR        = 1;
  public static final short WEBX_MATRIX_NOT_INVERTABLE    = 2;
}