package anilloDeTokens;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {
    static DataInputStream entrada;
    static DataOutputStream salida;
    static boolean inicio = true;
    static String ip = "localhost";
    static short nodo,token;


    static class Worker extends Thread {

        public void run () {
            //Algoritmo 1
            try {
                SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                ServerSocket servidor = socket_factory.createServerSocket(50000 + nodo );

                Socket conexion = servidor.accept();
                entrada = new DataInputStream(conexion.getInputStream());

                // servidor.close(); conexion.close(); entrada.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        nodo  = Short.parseShort(args[0]);

        if ( args.length < 0 ) {
            System.err.println("Se debe pasar como parametros el numero del nodo");
            System.exit(1);
        }

        //System.setProperty("javax.net.ssl.trustStore","keystore_cliete.jks");
        //System.setProperty("javax.net.ssl.trustStorePassword","123456");

        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();

        //Algoritmo 2
        Worker w = new Worker();
        w.start();
        Socket conexion;

        while (true) {
            try {
                conexion = cliente.createSocket(ip, 50000+(nodo+1)%6);
                break;
            } catch (Exception e) {
                Thread.sleep(500);
            }
        }

        salida = new DataOutputStream(conexion.getOutputStream());
        w.join();

        while (true) {
            if ( nodo  == 0 ) {
                if ( inicio ) {
                    inicio = false;
                    token = 1;
                } else {
                    token = entrada.readShort();
                    token++;
                    System.out.println("Nodo: "  + nodo);
                    System.out.println("Token: " + token);
                }
            } else {
                token = entrada.readShort();
                token++;
                System.out.println("Nodo: " + nodo);
                System.out.println("Token: " + token);
            }
            if ( nodo == 0 && token>=500 ) {
                break;
            }
            salida.writeShort(token);
        }

        conexion.close();
        salida.close();
    }
}
