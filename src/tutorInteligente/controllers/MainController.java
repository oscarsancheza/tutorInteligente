package tutorInteligente.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import tutorInteligente.Utils.JavaSourceFromString;
import tutorInteligente.Utils.Chronometer;
import tutorInteligente.Utils.DateTimeUtil;

import javax.tools.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

public class MainController {

  @FXML private Label dateLbl;
  @FXML private Button compilarBtn;
  @FXML private TextArea mainCodeTxt;
  @FXML private TextArea ResCompilacionTxt;
  @FXML private Label chronometerLbl;
  @FXML private Button ayudaBtn;

  private Chronometer chronometer;

  @FXML
  public void initialize() {
    this.compilarBtn.setOnAction(compilar());
    this.ayudaBtn.setOnAction(ayuda());

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    dateTimeUtil.initClock(dateLbl);

    this.chronometer = new Chronometer(chronometerLbl);
    this.chronometer.play();
  }

  private EventHandler<ActionEvent> ayuda() {
    return event -> this.mostrarMensaje("Ayuda", "Este es el mensaje de ayuda.");
  }

  private EventHandler<ActionEvent> compilar() {
    return event -> this.compileJavaCode(this.mainCodeTxt.getText());
  }

  private void compileJavaCode(String javaCode) {
    this.chronometer.pause();

    boolean success;
    StringBuilder error = new StringBuilder("El código compilo correctamente.");

    if (javaCode == null || javaCode.isEmpty()) {
      error = new StringBuilder("Error al compilar.");
      success = false;
    } else {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

      StringWriter writer = new StringWriter();
      PrintWriter out = new PrintWriter(writer);
      out.println(javaCode);
      out.close();

      JavaFileObject file = new JavaSourceFromString("Main", writer.toString());

      Iterable<? extends JavaFileObject> compilationUnits = Collections.singletonList(file);
      JavaCompiler.CompilationTask task =
          compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

      success = task.call();
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
    }

    if (success) {
      this.chronometer.reset();
    } else {
      this.chronometer.play();
    }

    ResCompilacionTxt.setText(error.toString());
  }

  private void mostrarMensaje(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.show();
  }
}
