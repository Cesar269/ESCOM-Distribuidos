package T2_anilloConSSL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {


    public static void servidor(short nodo, short token) throws IOException {

        ServerSocket servidor = new ServerSocket(50000 + nodo);
        System.out.println("Esperando conexion en el nodo: " + nodo);
        Socket conexion = servidor.accept();

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());

        servidor.close();
        salida.writeShort(token);
        salida.close();
        conexion.close();
    }

    public static short cliente(short nodo) {
        if( nodo == 0 ){
            nodo = 5;
        }else{
            nodo = (short) (nodo -1);
        }
        try {
            Socket conexion;
            for (;;) {
                try {
                    System.out.println("Intento de conexion a: " + (50000 + nodo));
                    conexion = new Socket("localhost", 50000 + nodo );
                    break;
                } catch (Exception ex) {
                    Thread.sleep(1000);
                }
            }
            System.out.println("Conectado al Nodo: " + nodo);
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
