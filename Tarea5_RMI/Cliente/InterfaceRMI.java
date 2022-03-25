
import java.rmi.*;

public interface InterfaceRMI extends Remote {
    float[][] multiplica_matrices(float[][] A, float[][] B, int N) throws RemoteException;

}
