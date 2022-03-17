package alphaMulicastChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class multicastChat {
    static void envia_mensaje_multicast(byte[] buffer,String ip,int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer,buffer.length, InetAddress.getByName(ip),puerto));
        socket.close();
    }
    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
    static class Worker extends Thread {
        public void run () {
            try {
                InetAddress ip = InetAddress.getByName("230.0.0.0");
                MulticastSocket socket = new MulticastSocket(50000);
                socket.joinGroup(ip);
                while (true) {
                    byte[] mensaje = recibe_mensaje_multicast(socket, 100);
                    System.out.println(new String(mensaje, "IBM850"));

                    // socket.leaveGroup(ip); // socket.close();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length<1) {
            System.err.println("Error, debe pasar el nombre");
            System.exit(1);
        }
        new Worker().start();
        String nombre = args[0];
        BufferedReader aux = new BufferedReader(new InputStreamReader(System.in));
        String mensaje = "";
        while (true) {
            System.out.println("Ingrese el mensaje a enviar");
            mensaje = nombre+":"+aux.readLine();
            byte[] buffer = mensaje.getBytes();
            envia_mensaje_multicast(buffer, "230.0.0.0", 50000);
        }
    }

}
