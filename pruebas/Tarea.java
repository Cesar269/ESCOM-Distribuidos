//package pruebas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Tarea {
    static double PI;
    static Object obj = new Object();

    static class Worker extends Thread {
        int nodo;

        Worker(int nodo) {
            this.nodo = nodo;
        }

        @Override
        public void run() {

        }

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int nodo = Integer.valueOf(args[0]);

        if (args.length == 1) {

            if (nodo != 0) {

            } else if (nodo == 0) {
                try{cliente();}catch(Exception e){
                    e.getStackTrace();
                }
            }
        } else {
            System.exit(0);
        }
    }

    public static void cliente() throws Exception{
        Socket conexion = null;
        int i = 0;
        for (;;)
            try {
                if(i > 4) i = 0;
                conexion = new Socket("localhost", 50000+i);
                System.out.println("intento de conexion a "+ (50000+i));
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
            System.out.println("ya conecto");
    }

}
