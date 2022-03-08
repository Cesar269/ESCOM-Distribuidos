import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {

    public static void main(String[] args) throws InterruptedException, IOException {
        int nodo = Integer.valueOf(args[0]);
        int token = 0;
        if (args.length == 1) {

            if (nodo == 0) {
                while(token <= 500){
                    servidor(nodo, token);
                    token = cliente(nodo);
                    token = token +1; 
                }

            } else if (nodo <= 5 && nodo >= 1) {
                while(true){
                    token = cliente(nodo);
                    servidor(nodo, token + 1 );
                }
            }
            
        } else {
            System.exit(0);
        }

    }

    public static void servidor(int nodo, int token) throws IOException {

        ServerSocket servidor = new ServerSocket(50000 + nodo);
        System.out.println("Esperando conexion en el nodo: " + nodo);
        Socket conexion = servidor.accept();

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    
        servidor.close();
        salida.writeInt(token);
        salida.close();
        conexion.close();
    }

    public static int cliente(int nodo) throws IOException {
        if( nodo == 0 ){
            nodo = 5;
        }else{
            nodo = nodo -1; 
        }
        try {
            Socket conexion = null;
            for (;;) {
                try {
                    System.out.println("Intento de conexión a: " + (50000 + nodo));
                    conexion = new Socket("localhost", 50000 + nodo );
                    break;
                } catch (Exception ex) {
                    Thread.sleep(1000);
                }
            }
            System.out.println("Conectado al Nodo: " + nodo);
            DataInputStream DIS = new DataInputStream(conexion.getInputStream());
            int token = DIS.readInt();
            System.out.println("Token:" + token);

            DIS.close();
            conexion.close();
            return token;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 9999; 
        }
    }
}
