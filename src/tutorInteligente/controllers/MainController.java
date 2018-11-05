package tutorInteligente.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import tutorInteligente.DataSource;
import tutorInteligente.NivelFuzzyLogic;
import tutorInteligente.Utils.Chronometer;
import tutorInteligente.Utils.DateTimeUtil;
import tutorInteligente.Utils.JavaSourceFromString;
import tutorInteligente.Utils.NumbersUtil;
import tutorInteligente.models.Ayuda;
import tutorInteligente.models.Problema;

import javax.tools.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

  @FXML private Label dateLbl;
  @FXML private Button compilarBtn;
  @FXML private TextArea mainCodeTxt;
  @FXML private TextArea resCompilacionTxt;
  @FXML private Label chronometerLbl;
  @FXML private Button ayudaBtn;
  @FXML private Label problemaLbl;

  private Chronometer chronometer;
  private DataSource dataSource;
  private Problema problema;
  private List<Ayuda> ayudas;
  private NivelFuzzyLogic nivelFuzzyLogic;

  @FXML
  public void initialize() {
    this.compilarBtn.setOnAction(compilar());
    this.ayudaBtn.setOnAction(ayuda());

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    dateTimeUtil.initClock(dateLbl);

    this.chronometer = new Chronometer(chronometerLbl);
    this.chronometer.play();

    dataSource = new DataSource();

    this.problema = dataSource.findOneByNivel(Problema.DIFICULTAD_BASICA);

    if (problema != null) {
      problemaLbl.setText(this.problema.getDescripcion());
      ayudas = problema.getAyuda();
    } else {
      mostrarMensaje("error", "Ha ocurrido un error al obtener el ejercicio. vuelva a intentar");
    }

    nivelFuzzyLogic = new NivelFuzzyLogic(true);
  }

  private void sumarAyuda() {
    if (problema != null) {
      problema.setTotalAyudas(problema.getTotalAyudas() + 1);
    }
  }

  private void sumarErrores() {
    if (problema != null) {
      problema.setTotalErrores(problema.getTotalErrores() + 1);
    }
  }

  private String getAyuda() {
    String ayuda = "";
    if (this.ayudas != null && !this.ayudas.isEmpty()) {
      for (Ayuda item : this.ayudas) {
        if (!item.getDescripcion().isEmpty() && !item.isVista()) {
          ayuda = item.getDescripcion();
          item.setVista(true);
          break;
        }
      }
    }

    return ayuda;
  }

  private EventHandler<ActionEvent> ayuda() {
    return event -> {
      String ayuda = getAyuda();
      if (!ayuda.isEmpty()) {
        this.mostrarMensaje("Ayuda", ayuda);
        sumarAyuda();
      } else {
        mostrarMensaje("Advertencia", "No existen mas ayudas para este ejercicio.");
      }
    };
  }

  private EventHandler<ActionEvent> compilar() {
    return event -> this.compileJavaCode(this.mainCodeTxt.getText());
  }

  private void compileJavaCode(String javaCode) {
    this.chronometer.pause();

    boolean success;
    StringBuilder mensaje = new StringBuilder("El código compilo correctamente.");

    if (javaCode == null || javaCode.isEmpty()) {
      mensaje = new StringBuilder("Error al compilar.");
      success = false;
    } else {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

      StringWriter writer = new StringWriter();
      PrintWriter out = new PrintWriter(writer);
      out.println(javaCode);
      out.close();

      JavaFileObject file = new JavaSourceFromString("Main", writer.toString());
      final StringWriter sw = new StringWriter();
      JavaCompiler.CompilationTask task =
          compiler.getTask(sw, null, diagnostics, null, null, Arrays.asList(file));

      success = task.call();

      if (!success) {
        mensaje = new StringBuilder();
        sumarErrores();
      }

      for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
        mensaje.append(diagnostic.getKind());
        mensaje.append(" ");
        mensaje.append(diagnostic.getCode());
        mensaje.append(", linea:");
        mensaje.append(diagnostic.getLineNumber());
        mensaje.append(", ");
        mensaje.append(diagnostic.getMessage(null));
        mensaje.append("\n");
      }
    }

    if (success) {
      if (resultados()) {
        this.chronometer.reset();
        this.dataSource.problemaVistoPorNivel(problema.getDificultad());
        mostrarMensaje("Advertencia", "El resultado es correcto, pasara al siguiente ejercicio.");
        mostrarSiguienteEjercicio(
            this.chronometer.getMinutes(),
            this.problema.getTotalAyudas(),
            this.problema.getTotalErrores());
      } else {
        this.chronometer.play();
        mensaje.append("\nEl resultado no fue el esperado, vuelva a intentar.");
        resCompilacionTxt.setText(mensaje.toString());
      }

    } else {
      this.chronometer.play();
      resCompilacionTxt.setText(mensaje.toString());
    }
  }

  private boolean resultados() {
    boolean esCorrecto = false;

    try {
      List<String> resultados = new ArrayList<>();
      Runtime.getRuntime().exec("javac Main.class");
      Process p = Runtime.getRuntime().exec("java Main");

      InputStream inputStream = p.getInputStream();

      String line;
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      while ((line = bufferedReader.readLine()) != null) {
        resultados.add(line);
      }

      if (!resultados.isEmpty()) {
        if (NumbersUtil.esNumeroEntero(resultados.get(0))) {
          if (problema.getResultado().equals(Double.valueOf(resultados.get(0)))) {
            esCorrecto = true;
          }
        } else if (problema.getResultado().equals(resultados.get(0))) {
          esCorrecto = true;
        }
      }

    } catch (Exception e) {
      System.out.println(e.toString());
    }

    return esCorrecto;
  }

  private void mostrarSiguienteEjercicio(int tiempo, int ayuda, int errores) {
    try {
      String nivel = this.nivelFuzzyLogic.getNivel(tiempo, ayuda, errores);

      Problema problema = this.dataSource.findOneByNivel(nivel);

      if (problema != null) {
        this.problema = problema;
        this.problemaLbl.setText(this.problema.getDescripcion());
        ayudas = this.problema.getAyuda();

        mainCodeTxt.setText(
            "public class Main {\n"
                + "\n"
                + "    public static void main(String[] args) {\n"
                + "\n"
                + "    }\n"
                + "}\n");
        resCompilacionTxt.setText("");
      } else {
        mostrarMensaje("error", "Has finalizado con todos los ejercicios.");
      }
    } catch (Exception e) {
      mostrarMensaje("Error", e.getMessage());
    }

    this.chronometer.play();
  }

  private void mostrarMensaje(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.show();
  }
}
