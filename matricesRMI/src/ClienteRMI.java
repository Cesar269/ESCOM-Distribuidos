import java.rmi.Naming;

public class ClienteRMI {
    static int N = 8;
    static double A[][] = new double[N][N];
    static double B[][] = new double[N][N];
    static double C[][] = new double[N][N];

    public static double[][] separa_matriz(double[][] A, int inicio) {
        double[][] M = new double[N/4][N];
        for (int i=0; i<N/4; i++) {
            for (int j=0; j<N; j++) {
                M[i][j] = A[i+inicio][j];
            }
        }
        return M;
    }

    public static void printMatrix(double[][] m, String matrixName) {
        if(N == 8){
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

    public static double[][] multiplica_matrices(double[][] A, double[][] B) {
        double[][] C = new double[N/4][N/4];
        for (int i=0; i<N/4; i++) {
            for (int j=0; j<N/4; j++)
                for (int k=0; k<N; k++)
                    C[i][j] += A[i][k] * B[j][k];
        }
        return C;
    }
    public static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna) {
        for (int i=0; i<N/4; i++) {
            for (int j=0; j<N/4; j++) {
                C[i+renglon][j+columna] = c[i][j];
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

    public static void main(String[] args) throws Exception {

        //URL del servidor RMI
        String url = "rmi://10.0.0.5/ejemplo";
        InterfaceRMI r = (InterfaceRMI) Naming.lookup(url);

        String url1 = "rmi://10.0.0.6/ejemplo";
        InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(url1);

        String url2 = "rmi://10.0.0.7/ejemplo";
        InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(url2);

        String url3 = "rmi://10.0.0.8/ejemplo";
        InterfaceRMI r3 = (InterfaceRMI)Naming.lookup(url3);

        //inicializar matrices
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = i + 2 * j;
                B[i][j] = 3 * i - j;
                C[i][j] = 0;
            }
        }
        //trasponer la matriz B.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        /*          ________
         *     0   |___M1___|
         *    N/4  |___M2___|
         *    N/2  |___M3___|
         *   3*N/4 |___M4___|
         */
        //Separamos la matriz A en 4
        double[][] A1 = separa_matriz(A, 0);
        double[][] A2 = separa_matriz(A, N / 4);
        double[][] A3 = separa_matriz(A, N / 2);
        double[][] A4 = separa_matriz(A, 3 * N / 4);

        //Separamos la matriz B en 4
        double[][] B1 = separa_matriz(A, 0);
        double[][] B2 = separa_matriz(A, N / 4);
        double[][] B3 = separa_matriz(A, N / 2);
        double[][] B4 = separa_matriz(A, 3 * N / 4);

        /*          ________     ________      _________________
         *     0   |___A1___|   |___B1___|    |C1  C2  C3  C4  |
         *    N/4  |___A2___| x |___B2___| =  |C5  C6  C7  C8  |
         *    N/2  |___A3___|   |___B3___|    |C9  C10 C11 C12 |
         *   3*N/4 |___A4___|   |___B4___|    |C13_C14_C15_C16_|
         */

        double[][] C1 = multiplica_matrices(A1, B1);
        double[][] C2 = multiplica_matrices(A1, B2);
        double[][] C3 = multiplica_matrices(A1, B3);
        double[][] C4 = multiplica_matrices(A1, B4);

        double[][] C5 = multiplica_matrices(A2, B1);
        double[][] C6 = multiplica_matrices(A2, B2);
        double[][] C7 = multiplica_matrices(A2, B3);
        double[][] C8 = multiplica_matrices(A2, B4);

        double[][] C9  = multiplica_matrices(A3, B1);
        double[][] C10 = multiplica_matrices(A3, B2);
        double[][] C11 = multiplica_matrices(A3, B3);
        double[][] C12 = multiplica_matrices(A3, B4);

        double[][] C13 = multiplica_matrices(A4, B1);
        double[][] C14 = multiplica_matrices(A4, B2);
        double[][] C15 = multiplica_matrices(A4, B3);
        double[][] C16 = multiplica_matrices(A4, B4);

        /*
         *          0  N/4  N/2  3*N/4
         *         _________________
         *   0    |C1 |C2  |C3  |C4 |
         *   N/4  |C5 |C6  |C7  |C8 |
         *   N/2  |C9 |C10 |C11 |C12|
         *  3*N/4 |C13|C14 |C15 |C16|
         *         -----------------
         * */
        acomoda_matriz(C, C1, 0,0);
        acomoda_matriz(C, C2, 0,N/4);
        acomoda_matriz(C, C3, 0,N/2);
        acomoda_matriz(C, C4, 0,(3*N)/4);

        acomoda_matriz(C, C5, N / 4, 0);
        acomoda_matriz(C, C6, N / 4, N/4);
        acomoda_matriz(C, C7, N / 4, N/2);
        acomoda_matriz(C, C8, N / 4, (3*N)/4);

        acomoda_matriz(C, C9 , N / 2, 0);
        acomoda_matriz(C, C10, N / 2, N/4);
        acomoda_matriz(C, C11, N / 2, N/2);
        acomoda_matriz(C, C12, N / 2, (3*N)/4);

        acomoda_matriz(C, C13 ,(3*N)/4, 0);
        acomoda_matriz(C, C14, (3*N)/4, N/4);
        acomoda_matriz(C, C15, (3*N)/4, N/2);
        acomoda_matriz(C, C16, (3*N)/4, (3*N)/4);


        //-------------------------------------------------
        //calcular y desplegar el checksum
        //printMatrix(C,"C");
        System.out.println();
        checksum(C);

    }
}
