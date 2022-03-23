import java.rmi.*;
public interface InterfaceRMI extends Remote {
    double[][] multiplica_matrices(double[][] A, double[][] B, int N) throws RemoteException;
}

