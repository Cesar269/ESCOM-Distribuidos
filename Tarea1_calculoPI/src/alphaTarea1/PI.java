package alphaTarea1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class PI {
    static double PI;
    static final Object obj = new Object();

    static class Worker extends Thread{
    int nodo;
        Worker(int nodo){
            this.nodo = nodo;
        }

        @Override
        public void run() {
            try {
                Socket conexion = null;
                int puerto = 50000+nodo, cont = 0;

                    // Intento de reconexión
                    for (;;){
                        try {
                            conexion = new Socket("localhost", puerto);
                            break;
                        }catch(Exception ex){Thread.sleep(100);}
                    }
                    System.out.println("Conectado al Nodo: "+ nodo +" en el puerto: "+ puerto);

                    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                    double suma = entrada.readDouble();
                    System.out.println("Valor de PI en el cliente: "+puerto+" suma: "+suma);
                    synchronized(obj){
                        PI += suma;// debemos ocupar synchronized
                    }

                    entrada.close();
                    conexion.close();

                }catch(Exception ex){ ex.printStackTrace();}
        }

    }
 //https://www.java67.com/2020/05/how-to-deal-with-javanetsocketexception-connection-reset-client-server-error.html

    static void servidor(int nodo) throws IOException {
        int puerto = 50000+nodo; ServerSocket servidor = new ServerSocket(puerto);

        System.out.println("Esperando conexion en el nodo: "+ nodo +" en el puerto: "+ puerto);

        Socket conexion = servidor.accept();

        System.out.println("Conexion exitosa en el puerto: "+ puerto);

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

        double suma=0.0;

        for ( int i=0; i<1000000; i++ ) {
            suma += 4.0/(8*i+2*(nodo-2)+3);
        }
        suma = (nodo%2 == 0) ? (-suma) : (suma);

        System.out.println("Valor de PI: "+PI+"En el servior con el puerto"+puerto);
        salida.writeDouble(suma);

        entrada.close();
        salida.close();
        conexion.close();
        servidor.close();

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int nodo = Integer.valueOf(args[0]);

        if ( args.length == 1 ) {

            if(nodo == 0 ){
                Worker[] cliente = new Worker[4];
                for (int i = 0; i <4 ; i++){
                    cliente[i] = new Worker(i+1);
                    cliente[i].start();
                }

                for (int j = 0; j <4 ; j++) {
                    cliente[j].join();
                }
                System.out.println("Valor de pi: "+PI);

            }else if (nodo > 0 && nodo <=4 ){
                servidor(nodo);
            }
        }else{
            System.exit(0);
        }
    }
}
