//package pruebas;


public class Prueba extends Thread {
    static long n;
    static Object obj = new Object();

    public void run() {
        for (int i = 0; i < 100000; i++)
            synchronized (obj) {
                n++;
            }
    }

    public static void main(String[] args) throws Exception {
        Prueba t1 = new Prueba();
        Prueba t2 = new Prueba();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(n);
    }
}
