package tutorInteligente;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import javax.tools.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Controller {

  private DateTimeFormatter formatter;

  @FXML private Label dateLbl;
  @FXML private Button compilarBtn;
  @FXML private TextArea mainCodeTxt;
  @FXML private TextArea ResCompilacionTxt;

  @FXML
  public void initialize() {
    compilarBtn.setOnAction(compilar());
    initClock();
  }

  private EventHandler<ActionEvent> compilar() {
    return event -> compileJavaCode(mainCodeTxt.getText());
  }

  private void compileJavaCode(String javaCode) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    StringWriter writer = new StringWriter();
    PrintWriter out = new PrintWriter(writer);
    out.println(javaCode);
    out.close();

    JavaFileObject file = new JavaSourceFromString("Main", writer.toString());

    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
    JavaCompiler.CompilationTask task =
        compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

    boolean success = task.call();
    StringBuilder error =
        new StringBuilder("El codigo compilo correctamente, lo siguiente es ejecutarlo.");

    if (!success) {
      error = new StringBuilder();
    }

    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
      error.append(diagnostic.getKind());
      error.append(" ");
      error.append(diagnostic.getCode());
      error.append(", linea:");
      error.append(diagnostic.getLineNumber());
      error.append(", ");
      error.append(diagnostic.getMessage(null));
      error.append("\n");
    }

    ResCompilacionTxt.setText(error.toString());
  }

  private void initClock() {
    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
                  dateLbl.setText(LocalDateTime.now().format(formatter));
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }
}
