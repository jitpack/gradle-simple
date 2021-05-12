
import io.grpc.CallOptions;

public class Hello {

  public static String GREETING = "Hello world!";

  public static void main(String []args) {

      CallOptions bla = CallOptions.DEFAULT;
      System.out.println(bla);
  }
}
