package alphaAnilloSSL;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {


    public static void servidor(short nodo, short token) throws IOException {

        System.setProperty("javax.net.ssl.keyStore","keystore_servidor.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","1234567");

        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(50000 + nodo);
        System.out.println("Servidor esperando en('localhost'," +(50000 + nodo)+")" );

        Socket conexion = socket_servidor.accept();
        System.out.println("Conexion exitosa en('localhost', " +(50000 + nodo)+")");

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());

        salida.writeShort(token);
        
        salida.close();
        conexion.close();
        socket_servidor.close();
    }

    public static short cliente(short nodo) {
        System.setProperty("javax.net.ssl.trustStore","keystore_cliete.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");

        if( nodo == 0 )
            nodo = 5;
        else
            nodo = (short) (nodo -1);

        try {
            SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket conexion;
            for (;;) {
                try {
                    System.out.println("Cliente intentando conectarse a "+(50000 + nodo));
                    conexion = cliente.createSocket("localhost",50000 + nodo);
                    //conexion = new Socket("localhost", 50000 + nodo );
                    break;
                } catch (Exception ex) {
                    Thread.sleep(1000);
                }
            }
            System.out.println("Cliente conectado al Nodo: " + nodo+" en el puerto "+(50000 + nodo));
            DataInputStream DIS = new DataInputStream(conexion.getInputStream());
            short token = DIS.readShort();
            System.out.println("Token:" + token);

            DIS.close();
            conexion.close();
            return token;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 9999;
        }
    }

    public static void main(String[] args) throws IOException {

        short nodo = Short.parseShort(args[0]), token = 0;

        if (args.length == 1) {
            if (nodo == 0) {
                while(token <= 500){
                    servidor(nodo, token);
                    token =  cliente(nodo);
                    token = (short) (token +1);
                }

            } else if (nodo <= 5 && nodo >= 1) {
                while(true){
                    token = cliente(nodo);
                    servidor(nodo, (short) (token + 1));
                }
            }
        } else {
            System.exit(0);
        }
    }
}
