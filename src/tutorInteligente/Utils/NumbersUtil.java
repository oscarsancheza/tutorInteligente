package tutorInteligente.Utils;

public class NumbersUtil {

  public static boolean esNumeroEntero(String numero) {
    try {
      Double.valueOf(numero);
    } catch (NumberFormatException excepcion) {
      return false;
    }
    return true;
  }
}
