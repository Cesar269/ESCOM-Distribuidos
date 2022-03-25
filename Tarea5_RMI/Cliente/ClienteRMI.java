import java.rmi.Naming;

public class ClienteRMI {
    static final Object obj = new Object();
    static int N = 8;
    static float A[][] = new float[N][N];
    static float B[][] = new float[N][N];
    static float C[][] = new float[N][N];
    static float[][] A1;
    static float[][] A2;
    static float[][] A3;
    static float[][] A4;
    static float[][] B1;
    static float[][] B2;
    static float[][] B3;
    static float[][] B4;

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
                        float[][] C1 = r.multiplica_matrices(A1, B1, N);
                        float[][] C2 = r.multiplica_matrices(A1, B2, N);
                        float[][] C3 = r.multiplica_matrices(A1, B3, N);
                        float[][] C4 = r.multiplica_matrices(A1, B4, N);
                        synchronized (obj) {
                            acomoda_matriz(C, C1, 0, 0);
                            acomoda_matriz(C, C2, 0, N / 4);
                            acomoda_matriz(C, C3, 0, N / 2);
                            acomoda_matriz(C, C4, 0, (3 * N) / 4);
                        }

                        break;

                    case 2:
                        float[][] C5 = r.multiplica_matrices(A2, B1, N);
                        float[][] C6 = r.multiplica_matrices(A2, B2, N);
                        float[][] C7 = r.multiplica_matrices(A2, B3, N);
                        float[][] C8 = r.multiplica_matrices(A2, B4, N);
                        synchronized (obj) {

                            acomoda_matriz(C, C5, N / 4, 0);
                            acomoda_matriz(C, C6, N / 4, N / 4);
                            acomoda_matriz(C, C7, N / 4, N / 2);
                            acomoda_matriz(C, C8, N / 4, (3 * N) / 4);
                        }
                        break;

                    case 3:

                        float[][] C9 = r.multiplica_matrices(A3, B1, N);
                        float[][] C10 = r.multiplica_matrices(A3, B2, N);
                        float[][] C11 = r.multiplica_matrices(A3, B3, N);
                        float[][] C12 = r.multiplica_matrices(A3, B4, N);
                        synchronized (obj) {
                            acomoda_matriz(C, C9, N / 2, 0);
                            acomoda_matriz(C, C10, N / 2, N / 4);
                            acomoda_matriz(C, C11, N / 2, N / 2);
                            acomoda_matriz(C, C12, N / 2, (3 * N) / 4);
                        }

                        break;

                    case 4:

                        float[][] C13 = r.multiplica_matrices(A4, B1, N);
                        float[][] C14 = r.multiplica_matrices(A4, B2, N);
                        float[][] C15 = r.multiplica_matrices(A4, B3, N);
                        float[][] C16 = r.multiplica_matrices(A4, B4, N);
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

    public static float[][] separa_matriz(float[][] A, int inicio) {
        float[][] M = new float[N / 4][N];
        for (int i = 0; i < N / 4; i++) {
            for (int j = 0; j < N; j++) {
                M[i][j] = A[i + inicio][j];
            }
        }
        return M;
    }

    public static void printMatrix(float[][] m, String matrixName) {
        if (N == 8) {
            System.out.println("Matriz " + matrixName);
            for (float[] i : m) {
                for (float j : i) {
                    System.out.print(j + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
    }

    public static void acomoda_matriz(float[][] C, float[][] c, int renglon, int columna) {
        for (int i = 0; i < N / 4; i++) {
            for (int j = 0; j < N / 4; j++) {
                C[i + renglon][j + columna] = c[i][j];
            }
        }
    }

    public static void checksum(float[][] m) {
        float checksum = 0;
        for (float[] i : m) {
            for (float j : i) {
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
                float x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        /*
         *        ________
         * 0     |___M1___|
         * N/4   |___M2___|
         * N/2   |___M3___|
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