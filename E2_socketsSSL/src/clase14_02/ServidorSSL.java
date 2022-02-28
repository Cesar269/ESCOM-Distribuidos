package clase14_02;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSSL {
    public static void main(String[] args) throws IOException {

        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(50000);
        System.out.println("Esperando la conexion de un cliente SSL, en el puerto: "+50000);
        Socket conexion = socket_servidor.accept();
        System.out.println("!Conexion de un cliente SSL exitosa! , en el puerto: "+50000);

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

        double x = entrada.readDouble();
        System.out.println(x);

        conexion.close();
    }
}
