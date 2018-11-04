package tutorInteligente.models;

import java.util.List;

public class Problema<T> {

  public static final String DIFICULTAD_BASICA = "basico";
  public static final String DIFICULTAD_INTERMEDIA = "intermedio";
  public static final String DIFICULTAD_AVANZADA = "avanzado";

  private String descripcion;
  private T resultado;
  private List<Ayuda> ayuda;
  private String dificultad;
  private int totalAyudas;
  private int totalErrores;
  private boolean estaAprobado;

  public boolean estaAprobado() {
    return estaAprobado;
  }

  public void setEstaAprobado(boolean estaAprobado) {
    this.estaAprobado = estaAprobado;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public T getResultado() {
    return resultado;
  }

  public void setResultado(T resultado) {
    this.resultado = resultado;
  }

  public List<Ayuda> getAyuda() {
    return ayuda;
  }

  public void setAyuda(List<Ayuda> ayuda) {
    this.ayuda = ayuda;
  }

  public String getDificultad() {
    return dificultad;
  }

  public void setDificultad(String dificultad) {
    this.dificultad = dificultad;
  }

  public int getTotalAyudas() {
    return totalAyudas;
  }

  public void setTotalAyudas(int totalAyudas) {
    this.totalAyudas = totalAyudas;
  }

  public int getTotalErrores() {
    return totalErrores;
  }

  public void setTotalErrores(int totalErrores) {
    this.totalErrores = totalErrores;
  }
}
