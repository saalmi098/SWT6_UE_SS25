package swt6.spring.basics.ioc.util;

@Log(Log.Type.STANDARD)
public class ConsoleLogger implements Logger {

  private String prefix = "Log";

  public void setPrefix(String prefix) { // ermoeglicht auch das Setzen ueber XML-Config-File
    this.prefix = prefix;
  }

  public void log(String msg) {
    System.out.format("%s: %s%n", prefix, msg);
  }
}
