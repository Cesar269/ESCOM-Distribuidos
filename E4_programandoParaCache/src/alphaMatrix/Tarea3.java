package alphaMatrix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Tarea3 {

    static int N=1000;
    static long A[][] = new long[N][N];
    static long B[][] = new long[N][N];
    static long C[][] = new long[N][N];

    public static void transpMatrixB() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                long x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
    }

    public static void printMatrix(long[][] m, String matrixName) {
        if(N == 8){
            System.out.println("Matriz " + matrixName);
            for (long[] i : m) {
                for (long j : i) {
                    System.out.print(j + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
    }

    public static long checksum(long[][] m) {
        long checksum = 0;
        for (long[] i : m)
            for (long j : i)
                checksum += j;

        return checksum;
    }

    public static void cliente(int N) throws IOException {
        try {
            Socket conexion = null;
            for (int nodo = 1; nodo < 5; nodo++) {
                for (;;) {
                    try {
                        System.out.println("Intento de conexión a: " + (50000 + nodo));
                        conexion = new Socket("localhost", 50000 + nodo);
                        break;
                    } catch (Exception ex) {
                        Thread.sleep(1000);
                    }
                }
                System.out.println("Conectado al Nodo: " + nodo);
                DataOutputStream DOS = new DataOutputStream(conexion.getOutputStream());
                ByteBuffer b = ByteBuffer.allocate(8 * (N / 2) * N);
                ByteBuffer a = ByteBuffer.allocate(8 * (N / 2) * N);

                switch (nodo) {
                    case 1:
                        // A1
                        for (int i = 0; i < N / 2; i++) // row
                            for (int j = 0; j < N; j++) // col
                                a.putLong(A[i][j]);

                        // B1
                        for (int i = 0; i < N / 2; i++)
                            for (int j = 0; j < N; j++)
                                b.putLong(B[i][j]);

                        break;
                    case 2:
                        // A1
                        for (int i = 0; i < N / 2; i++)
                            for (int j = 0; j < N; j++)
                                a.putLong(A[i][j]);
                        // B2
                        for (int i = N / 2; i < N; i++)
                            for (int j = 0; j < N; j++)
                                b.putLong(B[i][j]);

                        break;
                    case 3:
                        // A2
                        for (int i = N / 2; i < N; i++)
                            for (int j = 0; j < N; j++)
                                a.putLong(A[i][j]);

                        // B1
                        for (int i = 0; i < N / 2; i++)
                            for (int j = 0; j < N; j++)
                                b.putLong(B[i][j]);

                        break;
                    case 4:
                        // A2
                        for (int i = N / 2; i < N; i++)
                            for (int j = 0; j < N; j++)
                                a.putLong(A[i][j]);
                        // B2
                        for (int i = N / 2; i < N; i++)
                            for (int j = 0; j < N; j++)
                                b.putLong(B[i][j]);

                        break;
                }

                byte[] ba = a.array();
                byte[] bb = b.array();

                DOS.write(ba);
                DOS.write(bb);

                Thread.sleep(1000);

                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                byte[] c = new byte[8 * (N / 2) * (N / 2)];
                nuevoRead(entrada, c, 0, 8 * (N / 2) * (N / 2));
                ByteBuffer bc = ByteBuffer.wrap(c);

                switch (nodo) {
                    case 1:
                        //C1
                        for (int i=0; i<N/2; i++)
                            for (int j=0; j<N/2; j++)
                                C[i][j]=bc.getLong();

                        break;
                    case 2:
                        //C2
                        for (int i=0; i<N/2; i++)
                            for (int j=N/2; j<N; j++)
                                C[i][j]=bc.getLong();

                        break;
                    case 3:
                        //C3
                        for (int i=N/2; i<N; i++)
                            for (int j=0; j<N/2; j++)
                                C[i][j]=bc.getLong();

                        break;
                    case 4:
                        //C4
                        for (int i=N/2; i<N; i++)
                            for (int j=N/2; j<N; j++)
                                C[i][j]=bc.getLong();

                        break;
                }


                DOS.close();
                conexion.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void servidor(int nodo, int N) throws IOException {
        try {

            ServerSocket servidor = new ServerSocket(50000 + nodo);
            System.out.println("Esperando conexion en el nodo: " + nodo);
            Socket conexion = servidor.accept();

            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            byte[] a = new byte[8 * N * (N / 2)];
            nuevoRead(entrada, a, 0, 8 * N * (N / 2));

            ByteBuffer ba = ByteBuffer.wrap(a);

            for (int i = 0; i < N / 2; i++) {
                for (int j = 0; j < N; j++) {
                    A[i][j] = ba.getLong();
                }
            }

            byte[] b = new byte[8 * N * (N / 2)];
            nuevoRead(entrada, b, 0, 8 * N * (N / 2));

            ByteBuffer bb = ByteBuffer.wrap(b);

            for (int i = 0; i < N / 2; i++) {
                for (int j = 0; j < N; j++) {
                    B[i][j] = bb.getLong();
                }
            }

            for (int i = 0; i < N / 2; i++)
                for (int j = 0; j < N; j++)
                    for (int k = 0; k < N; k++)
                        C[i][j] += A[i][k] * B[j][k];

            printMatrix(A, "A");
            printMatrix(B, "B");
            printMatrix(C, "C");

            // SECCION DE ENVIO
            DataOutputStream DOS = new DataOutputStream(conexion.getOutputStream());
            ByteBuffer c = ByteBuffer.allocate(8 * (N / 2) * (N / 2));
            for (int i = 0; i < N / 2; i++) // row
                for (int j = 0; j < N / 2; j++) // col
                    c.putLong(C[i][j]);

            byte[] bc = c.array();

            DOS.write(bc);
            Thread.sleep(1000);
            DOS.close();

            servidor.close();
            entrada.close();
            conexion.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    static void nuevoRead(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int nodo = Integer.valueOf(args[0]);
        N = Integer.valueOf(args[1]);


        if (args.length != 2) {
            System.err.println("Se debe pasar como parametro el nodo y la ip");
            System.exit(1);
        }
        // Nodo 0: inicializar matrices A, B y C(matriz llena de unos)
        if (nodo == 0) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    A[i][j] = i + 5 * j;
                    B[i][j] = 5 * i - j;
                    C[i][j] = 0;
                }
            }
            // Imprimimos Matríz
            printMatrix(A, "A");
            printMatrix(B, "B");
            // Transponemos matrices
            transpMatrixB();

            printMatrix(B, "B");

            cliente(N);

            printMatrix(C, "C");

            System.out.println("CHECKSUM : "+checksum(C));

        } else if (nodo > 0 && nodo < 5) {
            servidor(nodo, N);
        }
    }
}
