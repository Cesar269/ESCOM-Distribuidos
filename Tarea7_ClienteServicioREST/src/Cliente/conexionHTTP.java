package Cliente;

import java.io.*;
import java.net.*;
import com.google.gson.*;
import java.lang.reflect.Type;

public class conexionHTTP {
   /* Recursos:
    * https://zetcode.com/java/gson/
    * Clase-04/04/2022 url: https://m4gm.com/moodle/course/view.php?id=11 */

    public static <T> String serializeJson(T object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static <T> T deserializeJson(String json, Type t) {
        Gson gson = new Gson();
        return gson.fromJson(json, t);
    }

    public static String opciones(String operacion, String parametro, String json, String ip) throws IOException, RuntimeException {
        // http://ip-de-la-máquina-virtual:8080/Servicio/rest/ws/consulta_usuario
        URL url = new URL("http://" + ip + ":8080/Servicio/rest/ws/" + operacion +"_usuario");
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();

        httpconn.setDoOutput(true); //true si se va a enviar un "body", en este caso el "body" son los parámetros
        httpconn.setRequestMethod("POST");// en este caso utilizamos el método POST de HTTP
        httpconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// indica que la petición estará codificada como URL
        // el método web "consulta_usuario" recibe como parámetro el email de un usuario, en este caso el email es a@c
        String parametros = parametro + "=" + URLEncoder.encode(json, "UTF-8");

        OutputStream os = httpconn.getOutputStream();

        os.write(parametros.getBytes());
        os.flush();
        // se debe verificar si hubo error
        if (httpconn.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new RuntimeException("Codigo de error HTTP: " + httpconn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
        String respuesta, aux = "";

        while ((respuesta = br.readLine()) != null)
            aux += respuesta;

        httpconn.disconnect();

        return aux;
    }
}
