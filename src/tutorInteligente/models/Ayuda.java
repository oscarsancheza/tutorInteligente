package tutorInteligente.models;

public class Ayuda {
  private String descripcion;
  private boolean vista;

  public Ayuda() {}

  public boolean isVista() {
    return vista;
  }

  public void setVista(boolean vista) {
    this.vista = vista;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
}
