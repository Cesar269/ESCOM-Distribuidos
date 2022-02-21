package alphaTarea1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class PI {
    static double PI;
    static final Object obj = new Object();
    //Clase Worker que nos permitirá crear hilos que actúen como clientes
    static class Worker extends Thread{
    int nodo;
        Worker(int nodo){
            this.nodo = nodo;
        }

        @Override
        public void run() {
            try {
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

                    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                    double suma = entrada.readDouble();
                    System.out.println("Valor de PI en el cliente: "+ puerto +" suma: "+ suma);
                    //Sincronizando el valor de PI
                    synchronized(obj){
                        PI += suma;
                    }
                    //Cerramos las conexiones y DIS
                    entrada.close();
                    conexion.close();

                }catch(Exception ex){ ex.printStackTrace();}
        }

    }
    //Función que crea un servidor en el nodo indicado
    static void servidor(int nodo) throws IOException {
        int puerto = 50000+nodo;
        ServerSocket servidor = new ServerSocket(puerto);

        System.out.println("Esperando conexion en el nodo: "+ nodo +" en el puerto: "+ puerto);

        Socket conexion = servidor.accept();

        System.out.println("Conexion exitosa en el puerto: "+ puerto);

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());

        double suma=0.0;

        for (int i=0; i<1000000; i++ ) {
            suma += 4.0/(8*i+2*(nodo-2)+3); //Σ(4.0/(8∗i+2∗(nodo−2)+3))
        }
    /* Si "nodo" es impar: Σ(4.0/(8∗i+2∗(nodo−2)+3))
       Si "nodo" es par : −Σ(4.0/(8∗i+2∗(nodo−2)+3)) */
        suma = (nodo%2 == 0) ? (-suma) : (suma);

        salida.writeDouble(suma);//Escribimos el Double de la suma
        //Cerramos las conexiones y el DOS
        salida.close();
        conexion.close();
        servidor.close();
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        int nodo = Integer.parseInt(args[0]);//Obtenemos el argumento en la ejecución del código
        //Verifiquemos que exista algún argumento
        if ( args.length == 1 ) {
            //Al ser 0 ejecutamos el cliente
            if(nodo == 0 ){
                Worker[] cliente = new Worker[4];//se instancia a worker
                //Inicializamos a worker para que se conecte al nodo indicado
                for (int i = 0; i <4 ; i++){
                    cliente[i] = new Worker(i+1);
                    cliente[i].start();
                }
                //Permitimos que los hilos se reincorporen ocupando join()
                for (int j = 0; j <4 ; j++) {
                    cliente[j].join();
                }

                System.out.println("Valor de PI: "+ PI);//Se despliega el valor de PI

            }else if (nodo > 0 && nodo <=4 ){
                servidor(nodo);//Creamos un servidor del nodo indicado
            }
        }else{System.exit(0);}//En el caso de no existir argumento, salimos del programa.
    }
}
