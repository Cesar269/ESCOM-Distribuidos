package Cliente;
public class Usuario {

    public String email,telefono,genero,fecha_nacimiento;
    public String nombre, apellido_paterno, apellido_materno;
    public byte[] foto = null;

    public Usuario(String email, String nombre, String APaterno, String AMaterno,
                   String FNacimiento, String Telefono, String Genero){
        this.email  = email;
        this.nombre = nombre;
        this.apellido_paterno = APaterno;
        this.apellido_materno = AMaterno;
        this.fecha_nacimiento = FNacimiento;
        this.telefono = Telefono;
        this.genero   = Genero;
    }
    @Override
    public String toString() {
        return  "\nEmail:  " + email  +
                "\nNombre: " + nombre +
                "\nApellido paterno: " + apellido_paterno +
                "\nApellido_materno: " + apellido_materno +
                "\nFecha nacimiento: " + fecha_nacimiento +
                "\nTelefono: " + telefono +
                "\nGenero  : " + genero;
    }
}