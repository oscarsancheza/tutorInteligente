package tutorInteligente;

import com.google.gson.Gson;
import org.jfree.util.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tutorInteligente.models.Problema;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

  List<Problema> problemas;

  public DataSource() {
    this.problemas = getData();
  }

  private List<Problema> getData() {

    JSONParser parser = new JSONParser();
    Problema problema;
    List<Problema> problemas = new ArrayList<>();
    try {
      JSONArray data = (JSONArray) parser.parse(new FileReader("datos.json"));

      for (Object item : data) {
        JSONObject jsonObject = (JSONObject) item;

        problema = new Gson().fromJson(jsonObject.toJSONString(), Problema.class);
        problemas.add(problema);
      }
    } catch (Exception e) {
      Log.error(e.getMessage());
    }

    return problemas;
  }

  public List<Problema> getProblemasPorNivel(String nivel) {
    List<Problema> problemasNivelBasico = new ArrayList<>();
    if (this.problemas != null && !this.problemas.isEmpty()) {
      for (Problema problema : problemas) {
        if (problema.getDificultad().equals(nivel) && !problema.estaAprobado()) {
          problemasNivelBasico.add(problema);
        }
      }
    }

    return problemasNivelBasico;
  }

  public void problemaVistoPorNivel(String nivel) {
    if (this.problemas != null && !this.problemas.isEmpty()) {
      for (Problema item : problemas) {
        if (item.getDificultad().equals(nivel) && !item.estaAprobado()) {
          item.setEstaAprobado(true);
          break;
        }
      }
    }
  }

  public Problema findOneByNivel(String nivel) {
    Problema problema = null;
    if (this.problemas != null && !this.problemas.isEmpty()) {
      for (Problema item : problemas) {
        if (item.getDificultad().equals(nivel) && !item.estaAprobado()) {
          problema = item;
          break;
        }
      }
    }
    return problema;
  }
}
