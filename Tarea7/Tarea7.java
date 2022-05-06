import java.io.*;
import java.net.*;
import java.util.Date;
import java.math.BigInteger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Scanner;
import java.sql.Timestamp;
import java.lang.reflect.Type;
import java.util.Base64;
import com.google.gson.*;

class Tarea7 {
    public static class Usuario {
        String nombre, email, apellidoP, apellidoM;
        String fechaN;
        String telefono;
        String genero;
        byte[] foto = null;

        Usuario(String email, String nombre, String apellidoP, String apellidoM,
                String fechaN, String telefono, String genero) {
            this.nombre = nombre;
            this.email = email;
            this.apellidoP = apellidoP;
            this.apellidoM = apellidoM;
            this.fechaN = fechaN;
            this.telefono = telefono;
            this.genero = genero;

        }

        public String toString() {
            return "Nombre: " + nombre + "\nemail: " + email + "\napellido paterno: " + apellidoP
                    + "\napellido materno: " + apellidoM + "\nfecha nacimiento: " + fechaN.toString()
                    + "\ntelefono: " + String.valueOf(telefono) + "\ngenero: " + genero;

        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setApellidoPaterno(String apellidoP) {
            this.apellidoP = apellidoP;
        }

        public void setApellidoMaterno(String apellidoM) {
            this.apellidoM = apellidoM;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setFechaDeNacimiento(String fechaN) {
            this.fechaN = fechaN;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public void setGenero(String genero) {
            this.genero = genero;
        }
    }

    public static class SerialGSON implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
        }

        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {

            String s = json.getAsString().replaceAll("\\ ", "+");
            return Base64.getDecoder().decode(s);
        }
    }

    public static void registra_usuario() {
        Usuario nuevoUsuario = new Usuario("", "", "", "", "", "", "");
        Scanner lectorUser = new Scanner(System.in);
        System.out.println("Ingrese los valores que se indiquen para el usuario");

        System.out.println("Correo:\n");
        String linea = lectorUser.nextLine();
        nuevoUsuario.setEmail(linea);

        System.out.println("N0mbre:\n");
        linea = lectorUser.nextLine();
        nuevoUsuario.setNombre(linea);

        System.out.println("Apellido paterno:\n");
        linea = lectorUser.nextLine();
        nuevoUsuario.setApellidoPaterno(linea);

        System.out.println("Apellido materno:\n");
        linea = lectorUser.nextLine();
        nuevoUsuario.setApellidoMaterno(linea);

        System.out.println("Telefono:\n");
        linea = lectorUser.nextLine();
        nuevoUsuario.setTelefono(linea);

        System.out.println("Genero m o f: \n");
        linea = lectorUser.nextLine();
        nuevoUsuario.setGenero(linea);

        System.out.println("Fecha de nacimiento (yyyy-MM-dd HH:mm:ss.SS)\n");
        linea = lectorUser.nextLine();
        nuevoUsuario.setFechaDeNacimiento(linea);

        // alta de usuario con Gson y HTTPCONECT
        Gson gson = new Gson();
        String usuarioJson = gson.toJson(nuevoUsuario);
        try {
            URL url = new URL("http://65.52.230.227:8080/Servicio/rest/ws/alta_usuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String parametros = "usuario=" + URLEncoder.encode(usuarioJson, "UTF-8");
            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            if (conexion.getResponseCode() == 200) {
                System.out.println("OK");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
                String respuesta;
                while ((respuesta = br.readLine()) != null)
                    System.out.println(respuesta);
            }
            conexion.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifica_usuario(Usuario usuario) {
        Scanner lectorUser = new Scanner(System.in);

        System.out.println("NOmbre:\n");
        String linea = lectorUser.nextLine();
        usuario.setNombre(linea);

        System.out.println("Apellido paterno:\n");
        linea = lectorUser.nextLine();
        usuario.setApellidoPaterno(linea);

        System.out.println("Apellido materno:\n");
        linea = lectorUser.nextLine();
        usuario.setApellidoMaterno(linea);

        System.out.println("Telefono:\n");
        linea = lectorUser.nextLine();
        usuario.setTelefono(linea);

        System.out.println("Genero M o F:\n");
        linea = lectorUser.nextLine();
        usuario.setGenero(linea);

        System.out.println("Fecha de nacimiento (yyyy-MM-dd HH:mm:ss.SS)\n");
        linea = lectorUser.nextLine();
        usuario.setFechaDeNacimiento(linea);

        Gson gson = new Gson();
        String usuarioJson = gson.toJson(usuario);

        // modificar usuario

        try {
            URL url = new URL("http://65.52.230.227:8080/Servicio/rest/ws/modifica_usuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String parametros = "usuario=" + URLEncoder.encode(usuarioJson, "UTF-8");
            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            if (conexion.getResponseCode() == 200) {
                System.out.println("OK");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
                String respuesta;
                while ((respuesta = br.readLine()) != null)
                    System.out.println(respuesta);
            }
            conexion.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void consulta_usuario() {
        try {

            URL url = new URL("http://65.52.230.227:8080/Servicio/rest/ws/consulta_usuario");

            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            conexion.setDoOutput(true);
z
            conexion.setRequestMethod("POST");

            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Scanner leer = new Scanner(System.in);
            System.out.println("Introduce el correo electronico para hacer la consulta: ");
            String emalToRead = leer.nextLine();
            String parametros = "email=" + URLEncoder.encode(emalToRead, "UTF-8");
            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            Usuario usuario = null;
            if (conexion.getResponseCode() == 200) {

                BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
                String respuesta;

                while ((respuesta = br.readLine()) != null) {

                    Gson gson = new GsonBuilder().registerTypeAdapter(byte[].class, new SerialGSON())
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
                    usuario = (Usuario) gson.fromJson(respuesta, Usuario.class);
                }
                System.out.println(usuario.toString());

                Scanner lecturaModificacion = new Scanner(System.in);
                String modificaPregunta;
                System.out.println("Desea modificar el usuario (s/n) ?: ");
                modificaPregunta = lecturaModificacion.nextLine();
                if (modificaPregunta.equals("s")) {
                    conexion.disconnect();
                    modifica_usuario(usuario);
                } else if (modificaPregunta.equals("n"))
                    System.out.println("");
                else
                    System.err.println("respuesta no valida");

            } else {
                // hubo error
                BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
                String respuesta;
                while ((respuesta = br.readLine()) != null)
                    System.out.println(respuesta);
            }

            conexion.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void elimina_usuario() {
        try {
            URL url = new URL("http://65.52.230.227:8080/Servicio/rest/ws/borra_usuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            conexion.setDoOutput(true);

            conexion.setRequestMethod("POST");

            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Scanner leer = new Scanner(System.in);
            System.out.println("Introduce el correo electronico que se va a eliminar: ");
            String emalToRead = leer.nextLine();
            String parametros = "email=" + URLEncoder.encode(emalToRead, "UTF-8");
            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            if (conexion.getResponseCode() == 200)
                System.out.println("OK");
            else {
                BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
                String respuesta;
                while ((respuesta = br.readLine()) != null)
                    System.out.println(respuesta);
            }
            conexion.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        char opc = 'X';
        Scanner leer = new Scanner(System.in);
        while (opc != 'd') {
            System.out.print("Selecciona una opcion:  \n");
            System.out.println("a. Alta usuario\n");
            System.out.println("b. Consulta usuario\n");
            System.out.println("c. Borra usuario\n");
            System.out.println("d.Salir\n");
            System.out.println("Opcion:\n");
            opc = leer.next().charAt(0);
            switch (opc) {
                case 'a':
                    registra_usuario();
                    break;
                case 'b':
                    consulta_usuario();
                    break;
                case 'c':
                    elimina_usuario();
                    break;
                case 'd':
                    System.exit(0);
                    break;
                default:
                    System.out.println("Elige dentro de las opciones permitidas");
            }
        }
        leer.close();
    }

}