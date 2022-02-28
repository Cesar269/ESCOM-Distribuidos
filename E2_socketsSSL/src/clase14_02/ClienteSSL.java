package clase14_02;

import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteSSL {

    public static void main(String[] args) throws IOException, InterruptedException {

        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        System.out.println("Intentando conexion con un servidor SSL en el puerto: "+50000);
        Socket conexion = cliente.createSocket("localhost",50000);
        System.out.println("Conectado a un servidor SSL en el puerto: "+50000);

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

        salida.writeDouble(123456789.123456789);

        System.out.println("Dato enviado");
        Thread.sleep(1000);
        conexion.close();
    }

}
