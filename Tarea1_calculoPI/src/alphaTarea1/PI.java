package alphaTarea1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class PI {
    static double PI;
    static Object obj = new Object();
    static class Worker extends Thread{
        int nodo;

        Worker(int nodo){
            this.nodo = nodo;
        }

        @Override
        public void run() {
            try {
                Socket conexion = null;
                    // Intento de reconexión
                    for (;;){
                        try {
                            conexion = new Socket("localhost", 50000+nodo);
                            break;
                        }catch(Exception ex){Thread.sleep(3000);}

                    DataInputStream DIS = new DataInputStream(conexion.getInputStream());
                    double suma = DIS.readDouble();

                    synchronized(obj){
                        PI += suma;// debemos ocupar synchronized
                    }
                    DIS.close();
                    conexion.close();
                }}catch(Exception ex){ ex.printStackTrace();}
        }

    }

    static void servidor(int nodo) throws IOException {
        ServerSocket servidor = new ServerSocket(50000+nodo);
        Socket conexion = servidor.accept();

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

        double suma = entrada.readDouble();
        for ( int i=0; i<1000000; i++ ) {
            suma += 4.0/(8*i+2*(nodo-2)+3);
        }
        suma = (nodo%2 == 0) ? (-suma) : (suma);
        salida.writeDouble(suma);

        entrada.close();
        salida.close();
        conexion.close();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int nodo = Integer.valueOf(args[0]);

        if ( args.length == 1 ) {

            if(nodo == 0 ){
                Worker cliente[] = new Worker[4];
                for (int i = 0; i <=4 ; i++){
                    cliente[i] = new Worker(i);
                    cliente[i].start();
                }

                for (int j = 0; j <=4 ; j++) {
                    cliente[j].join();
                }
                System.out.println("Valor de pi: "+PI);

            }else if (nodo <=4 ){
                switch(nodo){
                    case 1: servidor(nodo);
                        break;
                    case 2: servidor(nodo);
                        break;
                    case 3: servidor(nodo);
                        break;
                    case 4: servidor(nodo);
                        break;
                }
            }
        }else{
            System.exit(0);
        }
    }
}
