package datos;

/**
 * Clase que representa el usuario logeado//firebase auth
 */
public class UserData {
    String nombre;
    String apellidos;
    String departamento;
    String telefono;
    String email;

    public UserData(String nombre, String apellidos, String departamento, String telefono, String email) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
        this.telefono = telefono;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

