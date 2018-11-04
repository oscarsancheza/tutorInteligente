package tutorInteligente;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import tutorInteligente.models.Problema;

public class NivelFuzzyLogic {

  String fileName = "nivel.fcl";
  FIS file;
  boolean mostrarGraficas;

  public NivelFuzzyLogic(boolean mostrarGraficas) {
    this.file = FIS.load(fileName, true);
    this.mostrarGraficas = mostrarGraficas;
  }

  public String getNivel(int tiempo, int ayuda, int errores) {
    String nivel = "";
    if (this.file == null) {
      throw new RuntimeException("Ha ocurrido un error al cargar el archivo de fuzzy Logic.");
    }

    if (mostrarGraficas) {
      JFuzzyChart.get().chart(file);
    }

    file.setVariable("tiempo", tiempo);
    file.setVariable("ayuda", ayuda);
    file.setVariable("errores", errores);

    file.evaluate();

    Variable datos = file.getVariable("nivel");
    double valor = datos.getValue();

    if (valor <= 10) {
      nivel = Problema.DIFICULTAD_AVANZADA;
    } else if (valor <= 20) {
      nivel = Problema.DIFICULTAD_INTERMEDIA;
    } else {
      nivel = Problema.DIFICULTAD_BASICA;
    }

    if (mostrarGraficas) {
      JFuzzyChart.get().chart(datos, datos.getDefuzzifier(), true);
    }

    return nivel;
  }
}
