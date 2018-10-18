package tutorInteligente.models;

public class Problema<T> {
  private int id;
  private String descripcion;
  private T resultado;
  private Ayuda ayuda;
  private int dificultad;
  private int totalAyudas;
  private int totalErrores;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public Ayuda getAyuda() {
    return ayuda;
  }

  public void setAyuda(Ayuda ayuda) {
    this.ayuda = ayuda;
  }

  public int getDificultad() {
    return dificultad;
  }

  public void setDificultad(int dificultad) {
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
