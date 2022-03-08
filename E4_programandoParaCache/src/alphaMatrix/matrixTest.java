package alphaMatrix;

public class matrixTest {
    static int N = 8;
    static long A[][] = new long[N][N];
    static long B[][] = new long[N][N];
    static long C[][] = new long[N][N];

    public static void printMatrix (long[][] m, String matrixName) {
        System.out.println("Matriz " + matrixName);
        for (long[] i : m) {
            for (long j : i) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }
    
    public static void transpMatrixB(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                long x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }
    public static void main(String[] args){
            //inicializar matrices A y B
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                    A[i][j] = i+5*j;
                    B[i][j] = i-5*j;
                    C[i][j] = 1;
                }
            }
        printMatrix(A,"A");
        printMatrix(B,"B");
        printMatrix(C,"C");

        transpMatrixB();
        printMatrix(B,"B");

    }
}
