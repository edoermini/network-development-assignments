import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * @author Edoardo Ermini
 */
public class CongressClient {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 1) {
            System.out.println("Usage: CongressClient port");
            System.out.println("Exiting...");
            return;
        }

        Congress serverObject;
        int port;
        String prompt = "congress managing> ";
        boolean exit = false;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error on port argument");
            System.out.println("Exiting...");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        try {
            Registry reg = LocateRegistry.getRegistry(port);
            serverObject = (Congress) reg.lookup("CONGRESS-SERVER");

            System.out.println("Welcome, type help to see commands");

            while (!exit) {
                System.out.print(prompt);

                String command = scanner.nextLine().trim();

                switch (command) {
                    case "help":
                        System.out.println("Commands list:");
                        System.out.println("\t- register : will be registered given speaker in given session of given day");
                        System.out.println("\t- program : prints entire congress program");
                        System.out.println("\t- exit : exits");
                        break;

                    case "register":

                        System.out.print("Speaker name: ");
                        String name = scanner.nextLine();
                        int day;
                        int session;

                        try {
                            System.out.print("Select day: ");
                            day = Integer.parseInt(scanner.nextLine());

                            System.out.print("Select session: ");
                            session = Integer.parseInt(scanner.nextLine());
                        
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Error: register must have 2 arguments");
                            break;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            System.out.println("Syntax error");
                            break;
                        }  

                        try {
                            serverObject.registerSpeaker(day, session, name);
                        } catch (RemoteException e) {
                            System.out.println("Error: "+ e.getCause().getMessage() + ", type program to see available sessions or days");
                            break;
                        }

                        System.out.println("Speaker " + name + " successfully added to session " + session + " of day " + day);
                        break;
                    
                    case "program":
                        System.out.println(serverObject.getProgram());
                        break;

                    case "exit":
                        exit = true;
                        break;

                    
                    default:
                        System.out.println("Unknown command, type help to see available commands");
                        break;
                }
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
