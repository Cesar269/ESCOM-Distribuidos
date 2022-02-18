package getThreadsClear;

public class E3_syncThreads extends Thread {
        static long n;
        static Object obj = new Object();

        public void run() {
            for (int i = 0; i < 100000; i++)
                synchronized(obj){
                    n++;
                }
        }

    public static void main(String[] args) throws Exception {
        E3_syncThreads t1 = new E3_syncThreads();
        E3_syncThreads t2 = new E3_syncThreads();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(n);
    }
}
