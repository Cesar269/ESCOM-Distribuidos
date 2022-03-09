package alphaMatrix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Matrix {

    /*
    *       C1 = A1 x B1
    *       C2 = A1 x B2
    *       C3 = A2 x B1
    *       C4 = A2 x B2
    *        ________     _________
    *       |        |   |         |        ---------------
    *       |   A1   |   |    B1   |        |  C1  |   C2 |
    *       |________| x |_________| =      --------------
    *       |        |   |         |        |  C3  |   C4 |
    *       |   A2   |   |    B2   |        ---------------
    *       |________|   |_________|
    * */

    static int N = 8;
    static long A[][] = new long[N][N];
    static long B[][] = new long[N][N];
    static long C[][] = new long[N][N];

    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run() {
            try {
                //Streams de entrada y salida
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                //recibe el nodo
                int nodo = entrada.readInt();

                switch(nodo) {
                    case 1:
                        //A1
                        for (int i=0; i<N/2; i++) // row
                            for (int j=0; j<N; j++) // col
                                salida.writeLong(A[i][j]);

                        //B1
                        for (int i=0; i<N/2; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(B[i][j]);
                        //C1
                        for (int i=0; i<N/2; i++)
                            for (int j=0; j<N/2; j++)
                                C[i][j]=entrada.readLong();

                        break;
                    case 2:
                        //A1
                        for (int i=0; i<N/2; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(A[i][j]);
                        //B2
                        for (int i=N/2; i<N; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(B[i][j]);

                        //C2
                        for (int i=0; i<N/2; i++)
                            for (int j=N/2; j<N; j++)
                                C[i][j]=entrada.readLong();
                        break;
                    case 3:
                        //A2
                        for (int i=N/2; i<N; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(A[i][j]);

                        //B1
                        for (int i=0; i<N/2; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(B[i][j]);
                        //C3
                        for (int i=N/2; i<N; i++)
                            for (int j=0; j<N/2; j++)
                                C[i][j]=entrada.readLong();

                        break;
                    case 4:
                        //A2
                        for (int i=N/2; i<N; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(A[i][j]);
                        //B2
                        for (int i=N/2; i<N; i++)
                            for (int j=0; j<N; j++)
                                salida.writeLong(B[i][j]);
                        //C4
                        for (int i=N/2; i<N; i++)
                            for (int j=N/2; j<N; j++)
                                C[i][j]=entrada.readLong();
                        break;
                }

                salida.close();
                entrada.close();
                conexion.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
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
    public static long checksum(long[][] m) {
        long checksum = 0;
        for (long[] i : m)
            for (long j : i)
                checksum += j;

        return checksum;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        int nodo = Integer.valueOf(args[0]);

        if (args.length != 2) {
            System.err.println("Se debe pasar como parametro el nodo y la ip");
            System.exit(1);
        }
        // Nodo 0: inicializar matrices A, B y C(matriz llena de unos)
        if (nodo == 0) {
            for (int i=0; i<N; i++) {
                for (int j=0; j<N; j++) {
                    A[i][j] = i + 5 *j;
                    B[i][j] = 5 * i - j;
                    C[i][j] = 0;
                }
            }
            //Imprimimos Matríz
            printMatrix(A,"A");
            printMatrix(B,"B");
            //Transponemos matrices
            transpMatrixB();

            ServerSocket servidor = new ServerSocket(50000);

            Worker W[] = new Worker[4];

            for (int i=0; i<4; i++) {
                Socket conexion = servidor.accept();
                W[i] = new Worker(conexion);
                W[i].start();
            }
            for (int i=0; i<4; i++)
                W[i].join();

            printMatrix(C,"C");

            checksum(C);
            servidor.close();
        }else if (nodo>0 && nodo<5) {
            //Aquí ira lo que haran los otros nodos
        }
    }
}
