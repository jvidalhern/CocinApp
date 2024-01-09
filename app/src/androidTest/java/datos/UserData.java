package datos;

/**
 * Clase que representa el usuario logeado//firebase auth
 */
public class UserData {
    String nombreApel;
    String departamento;
    String telefono;
    String email;

    public UserData(String nombreApel, String departamento, String telefono, String email) {
        this.nombreApel = nombreApel;
        this.departamento = departamento;
        this.telefono = telefono;
        this.email = email;
    }

    public String getNombreApel() {
        return nombreApel;
    }

    public void setNombreApel(String nombreApel) {
        this.nombreApel = nombreApel;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
