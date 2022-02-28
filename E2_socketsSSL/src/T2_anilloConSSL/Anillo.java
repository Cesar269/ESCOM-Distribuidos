package T2_anilloConSSL;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Anillo {

    static void enviar_token(int nodo, short tokenID) throws InterruptedException, IOException {
        Socket conexion;
        int puerto = 50000+nodo;

        //Intento de reconexión
        for (;;){
            try {
                conexion = new Socket("localhost", puerto);
                break;
            }catch(Exception ex){Thread.sleep(100);}
        }

        //Conexión exitosa
        System.out.println("Conectado al Nodo: "+ nodo +" en el puerto: "+ puerto);

        //Enviar el Token
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        salida.writeShort(tokenID);

        //Desconectarse
        //salida.close();
       //conexion.close();
    }

    //Función que crea un servidor en el nodo indicado
    static void servidor(int nodo) throws IOException, InterruptedException {
        int puerto = 50000+nodo;
        ServerSocket servidor = new ServerSocket(puerto);

        System.out.println("Esperando conexion en el nodo: "+ nodo +" en el puerto: "+ puerto);

        Socket conexion = servidor.accept();

        System.out.println("Conexion exitosa en el puerto: "+ puerto);

        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        short token = entrada.readShort();
        if(nodo == 0 && token >= 500)
            System.exit(0);
        else{
            System.out.println("Nodo: "+ nodo +"Token: "+ token);
        }
        enviar_token((token+1)%4,token);
        token++;
        entrada.close();
        conexion.close();
        servidor.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //Definir las 4 propiedades del keystore
        int nodo = Integer.parseInt(args[0]);
        short token = 1;

        if(nodo == 0)
            enviar_token(1, token);
        else{
            for(;;){
                int puerto = 50000 + nodo;
                ServerSocket servidor = new ServerSocket(puerto);

            }
        }
    }
}
