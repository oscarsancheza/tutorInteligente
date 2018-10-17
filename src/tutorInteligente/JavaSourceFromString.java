package tutorInteligente;

import com.sun.istack.internal.NotNull;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class JavaSourceFromString extends SimpleJavaFileObject {

  private final String code;

  public JavaSourceFromString(@NotNull String name, String code) {
    super(URI.create("string:///"
        + name.replace('.', '/')
        + Kind.SOURCE.extension), Kind.SOURCE);
    this.code = code;
  }

  @SuppressWarnings("RefusedBequest")
  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return code;
  }
}
