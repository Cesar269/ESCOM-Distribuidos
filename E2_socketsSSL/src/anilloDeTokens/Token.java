//package anilloDeTokens;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {
    //variables de ayuda
    static DataInputStream entrada;
    static DataOutputStream salida;
    static boolean inicio = true;
    static String ip = "localhost";
    static short nodo,token;


    static class Worker extends Thread {

        public void run () {
            //Algoritmo 1
            try {
                //instancia de la clase SSLServerFactory
                SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                //creacion del socket
                ServerSocket servidor = socket_factory.createServerSocket(50000 + nodo );

                Socket conexion = servidor.accept();
                //obtencion de la entrada
                entrada = new DataInputStream(conexion.getInputStream());

                // servidor.close(); conexion.close(); entrada.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //obtencion del argumento del nodo a ejecutar
        nodo  = Short.parseShort(args[0]);

        if ( args.length < 0 ) {
            System.err.println("Se debe pasar como parametros el numero del nodo");
            System.exit(1);
        }

        //System.setProperty("javax.net.ssl.trustStore","keystore_cliete.jks");
        //System.setProperty("javax.net.ssl.trustStorePassword","123456");
        //instancia de la clase SSLSocketFactory
        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();

        //Algoritmo 2
        Worker w = new Worker();
        //inicializacion del hilo servidor
        w.start();
        Socket conexion;

        //reintento de conexion por parte del cliente
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
        //hasta este punto se ha podido realizar la conexion de cliente-servidor

        //bloque de inicializacion e incremento del token
        while (true) {
            if ( nodo  == 0 ) {
                //si es el inicio entonces el token se incrementa a 1
                if ( inicio ) {
                    inicio = false;
                    token = 1;
                } else {
                    //si no es el inicio entonces lee el token y lo incrementa
                    token = entrada.readShort();
                    token++;
                    //despliegue de los valores
                    System.out.println("Nodo: "  + nodo);
                    System.out.println("Token: " + token);
                }
            } else {
                //incremento y despligue
                token = entrada.readShort();
                token++;
                System.out.println("Nodo: " + nodo);
                System.out.println("Token: " + token);
            }
            if ( nodo == 0 && token>=500 ) {
                //se detiene si el valor es mayor o igual a cero
                break;
            }
            //envio del token
            salida.writeShort(token);
        }

        conexion.close();
        salida.close();
    }
}
