package PrimerSemana;

import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;

public class Servidor {
    
    public static void main(String args[]){
        try{
            ServerSocket servidor = new ServerSocket(3012);

            Socket conexion = servidor.accept();
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            
            int n = entrada.readInt();
            System.out.println(n);

            double x = entrada.readDouble();
            System.out.println(x);

            byte[] buffer = new byte[4];
            nuevoRead(entrada,buffer,0,4);
            System.out.println(new String(buffer,"UTF-8"));

            salida.write("HOLA".getBytes());

            byte[] a = new byte[5*8];
            nuevoRead(entrada, a, 0, 5*8);
            
            ByteBuffer b = ByteBuffer.wrap(a);

            for(int i = 0; i<5;i++){
                System.out.println(b.getDouble());
            }
            conexion.close();



                
        
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main

    static void nuevoRead(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{
        while(longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
}
