import java.rmi.Naming;

public class ClienteRMI {
    static final Object obj = new Object();
    static int N = 8;
    static double A[][] = new double[N][N];
    static double B[][] = new double[N][N];
    static double C[][] = new double[N][N];
    static double[][] A1;
    static double[][] A2;
    static double[][] A3;
    static double[][] A4;
    static double[][] B1;
    static double[][] B2;
    static double[][] B3;
    static double[][] B4;

    static String[] hilos = { "localhost", "localhost", "localhost", "localhost" };

    static class Worker extends Thread {
        int hilo;

        Worker(int hilo) {
            this.hilo = hilo;
        }

        @Override
        public void run() {
            try {
                String url = "rmi://" + hilos[hilo - 1] + "/prueba";
                InterfaceRMI r = (InterfaceRMI) Naming.lookup(url);
                switch (hilo) {
                    case 1:
                        double[][] C1 = r.multiplica_matrices(A1, B1, N);
                        double[][] C2 = r.multiplica_matrices(A1, B2, N);
                        double[][] C3 = r.multiplica_matrices(A1, B3, N);
                        double[][] C4 = r.multiplica_matrices(A1, B4, N);
                        synchronized (obj) {
                            acomoda_matriz(C, C1, 0, 0);
                            acomoda_matriz(C, C2, 0, N / 4);
                            acomoda_matriz(C, C3, 0, N / 2);
                            acomoda_matriz(C, C4, 0, (3 * N) / 4);
                        }

                        break;

                    case 2:
                        double[][] C5 = r.multiplica_matrices(A2, B1, N);
                        double[][] C6 = r.multiplica_matrices(A2, B2, N);
                        double[][] C7 = r.multiplica_matrices(A2, B3, N);
                        double[][] C8 = r.multiplica_matrices(A2, B4, N);
                        synchronized (obj) {

                            acomoda_matriz(C, C5, N / 4, 0);
                            acomoda_matriz(C, C6, N / 4, N / 4);
                            acomoda_matriz(C, C7, N / 4, N / 2);
                            acomoda_matriz(C, C8, N / 4, (3 * N) / 4);
                        }
                        break;

                    case 3:

                        double[][] C9 = r.multiplica_matrices(A3, B1, N);
                        double[][] C10 = r.multiplica_matrices(A3, B2, N);
                        double[][] C11 = r.multiplica_matrices(A3, B3, N);
                        double[][] C12 = r.multiplica_matrices(A3, B4, N);
                        synchronized (obj) {
                            acomoda_matriz(C, C9, N / 2, 0);
                            acomoda_matriz(C, C10, N / 2, N / 4);
                            acomoda_matriz(C, C11, N / 2, N / 2);
                            acomoda_matriz(C, C12, N / 2, (3 * N) / 4);
                        }

                        break;

                    case 4:

                        double[][] C13 = r.multiplica_matrices(A4, B1, N);
                        double[][] C14 = r.multiplica_matrices(A4, B2, N);
                        double[][] C15 = r.multiplica_matrices(A4, B3, N);
                        double[][] C16 = r.multiplica_matrices(A4, B4, N);
                        synchronized (obj) {
                            acomoda_matriz(C, C13, (3 * N) / 4, 0);
                            acomoda_matriz(C, C14, (3 * N) / 4, N / 4);
                            acomoda_matriz(C, C15, (3 * N) / 4, N / 2);
                            acomoda_matriz(C, C16, (3 * N) / 4, (3 * N) / 4);
                        }

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static double[][] separa_matriz(double[][] A, int inicio) {
        double[][] M = new double[N / 4][N];
        for (int i = 0; i < N / 4; i++) {
            for (int j = 0; j < N; j++) {
                M[i][j] = A[i + inicio][j];
            }
        }
        return M;
    }

    public static void printMatrix(double[][] m, String matrixName) {
        if (N == 8) {
            System.out.println("Matriz " + matrixName);
            for (double[] i : m) {
                for (double j : i) {
                    System.out.print(j + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
    }

    public static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna) {
        for (int i = 0; i < N / 4; i++) {
            for (int j = 0; j < N / 4; j++) {
                C[i + renglon][j + columna] = c[i][j];
            }
        }
    }

    public static void checksum(double[][] m) {
        double checksum = 0;
        for (double[] i : m) {
            for (double j : i) {
                checksum += j;
            }
        }
        System.out.println("Checksum: " + checksum);
    }

    public static void main(String args[]) throws InterruptedException, Exception {

        // inicializar matrices
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = i + 2 * j;
                B[i][j] = 3 * i - j;
                C[i][j] = 0;
            }
        }
        // trasponer la matriz B.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        /*
         * ________
         * 0 |___M1___|
         * N/4 |___M2___|
         * N/2 |___M3___|
         * 3*N/4 |___M4___|
         */
        // Separamos la matriz A en 4
        A1 = separa_matriz(A, 0);
        A2 = separa_matriz(A, N / 4);
        A3 = separa_matriz(A, N / 2);
        A4 = separa_matriz(A, 3 * N / 4);

        // Separamos la matriz B en 4
        B1 = separa_matriz(A, 0);
        B2 = separa_matriz(A, N / 4);
        B3 = separa_matriz(A, N / 2);
        B4 = separa_matriz(A, 3 * N / 4);

        Worker[] cliente = new Worker[4];// se instancia a worker
        // Inicializamos a worker para que se conecte al nodo indicado
        for (int i = 0; i < 4; i++) {
            cliente[i] = new Worker(i + 1);
            cliente[i].start();
        }
        // Permitimos que los hilos se reincorporen ocupando join()
        for (int j = 0; j < 4; j++) {
            cliente[j].join();
        }

        // -------------------------------------------------
        // calcular y desplegar el checksum
        // printMatrix(C, "C");
        System.out.println();
        checksum(C);
    }
}