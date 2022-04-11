package Cliente;

import java.util.Scanner;
import com.google.gson.reflect.TypeToken;

public class Cliente {
    static String dirIP;
    static String usrData[] = new String [8];
    static String arrDatos[]={"Email:","Nombre: ","Apellido paterno: ","Apellido Materno: ",
            "Fecha de nacimiento(YYYY-MM-DD): ","Telefono","Genero(M/F)"};

    public static void printMenu(){
        System.out.println("\na. Alta usuario"); System.out.println("b. Consulta usuario");
        System.out.println("c. Borra usuario");  System.out.println("d. Salir\n\n");
        System.out.println("Opcion: ");
    }

    public static void altaUsuario() throws Exception{
        Scanner input = new Scanner (System.in);
        System.out.println("ALTA USUARIO");
        for (int i = 0; i < arrDatos.length; i++) {
            System.out.println(arrDatos[i]);
            usrData[i]= input.nextLine();
        }
        Usuario usuario = new Usuario(usrData[0],usrData[1],usrData[2],usrData[3],usrData[4],usrData[5],usrData[6]);
        String j = conexionHTTP.serializeJson(usuario);
        String respuesta = conexionHTTP.opciones("alta","usuario",j,dirIP);

        if(respuesta.equals(""))
            System.out.println("OK");
    }

    public static void consultaUsuario()throws Exception{
        Scanner input = new Scanner (System.in), modifica = new Scanner(System.in);

        System.out.println("COSNULTA");
        System.out.println("Email : ");
        String email = input.nextLine();

        String respuesta = conexionHTTP.opciones("consulta","email",email,dirIP);
        Usuario usuario = conexionHTTP.deserializeJson(respuesta,new TypeToken<Usuario>(){}.getType());
        System.out.println(usuario.toString());

        System.out.println("¿Desea modificar usuario?(s/n)");
        String opcion = modifica.nextLine();

        if(opcion.equals("S")||opcion.equals("s"))
            modificarUsuario(usuario);
    }

    public static void modificarUsuario(Usuario tempUsr)throws Exception{
        String innerArr[] ={tempUsr.nombre, tempUsr.apellido_paterno, tempUsr.apellido_materno,
                tempUsr.fecha_nacimiento, tempUsr.telefono, tempUsr.genero,tempUsr.email},temp="";
        Scanner input = new Scanner (System.in);

        System.out.println("MODIFICAR USUARIO");

        for (int i = 1; i < innerArr.length; i++){
            System.out.println(arrDatos[i]);
            temp = input.nextLine();
            usrData[i-1] = (temp.equals(""))?innerArr[i-1]:temp;
        }

        UsuarioM usuario = new UsuarioM(usrData[0],usrData[1],usrData[2],usrData[3],
                                      usrData[4],usrData[5],tempUsr.email);

        String j = conexionHTTP.serializeJson(usuario);
        String respuesta = conexionHTTP.opciones("modifica","usuario",j,dirIP);

        if(respuesta.equals(""))
            System.out.println("OK");
    }

    public static void borrarUsuario()throws Exception{
        Scanner input = new Scanner (System.in);
        System.out.println("BORRAR USUARIO");
        System.out.println("Email : ");
        String email = input.nextLine();

        String respuesta = conexionHTTP.opciones("borra","email",email,dirIP);
        if(respuesta.equals(""))
            System.out.println("OK");
    }

    public static void main(String[] args)throws Exception{
        Scanner input = new Scanner (System.in);
        char option;
        System.out.print("Ingresa la IP: ");
        dirIP = input.nextLine ();

        while(true){
            printMenu();
            option = input.next().charAt(0);
            switch (option){
                case 'a': altaUsuario();
                    break;
                case 'b':  consultaUsuario();
                    break;
                case 'c':  borrarUsuario();
                    break;
                case 'd': System.exit(0);
                    break;
                default: System.out.println("Elige dentro de las opciones permitidas");
            }
        }
    }
}
