import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;

public class Cliente {

    public static void main(String args[]) {
        try {
            Socket conexion = new Socket("localhost",3012);
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            salida.writeInt(123);
            salida.writeDouble(1234.1234);
            salida.write("hola".getBytes());

            byte[] buffer = new byte[4];
            entrada.read(buffer,0,4);
            System.out.println(new String(buffer,"UTF-8"));

            byte[] nuevoBuffer = new byte[4];
            nuevoRead(entrada, nuevoBuffer, 0, 4);
            System.out.println(new String(buffer,"UTF-8"));

            ByteBuffer b = ByteBuffer.allocate(5*8);
            b.putDouble(1.1);
            b.putDouble(1.2);
            b.putDouble(1.3);
            b.putDouble(1.4);
            b.putDouble(1.5);

            byte[] a = b.array();

            salida.write(a);

            Thread.sleep(1000);
            conexion.close();
            

          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void nuevoRead(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{
        while(longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }



}