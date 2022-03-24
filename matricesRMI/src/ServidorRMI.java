import java.rmi.Naming;

public class ServidorRMI {

    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost/ejemplo";
        ClaseRMI obj = new ClaseRMI();
        Naming.rebind(url, obj);
    }
}
