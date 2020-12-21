import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * @author Edoardo Ermini
 */
public class CongressServer extends RemoteServer implements Congress {
    
    /* sessions is a matrix where
     * each row is a day and
     * each column is a session for that day
     */
    ArrayList<ArrayList<Session>> sessions;

    public CongressServer() throws RemoteException {
        this.sessions = new ArrayList<ArrayList<Session>>(3);

        // initializing sessions
        for (int i = 0; i < 3; i++) {
            this.sessions.add(i, new ArrayList<Session>(12));

            for (int j = 0; j < 12; j++) {
                this.sessions.get(i).add(j, new Session());
            }
        }
    }

    @Override
    public synchronized void registerSpeaker(int day, int session, String name) throws RemoteException {

        if (day < 0 || day > 2) {
            throw new RemoteException("Selected day doesn't exist");
        }

        if (session < 0 || session > 11) {
            throw new RemoteException("Selected session doesn't exits");
        }

        if (name.trim().equals("")) {
            throw new RemoteException("Insert a valid name like John Doe");
        }

        this.sessions.get(day).get(session).addSpeaker(name);
    }

    @Override
    public synchronized String getProgram() throws RemoteException {
        StringBuilder program = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            program.append("Day "+ i + ":\n");

            for (int j = 0; j < 12; j++) {
                program.append("\tsession "+ j + ":\n");

                for (String speaker : sessions.get(i).get(j).getSpeakers()) {
                    program.append("\t\tspeaker: " + speaker + "\n");
                }
            }
        }

        return program.toString();
    }

    public static void main(String[] args) {

        if (args.length < 1 || args.length > 1) {
            System.out.println("Usage: CongressServer port");
            System.out.println("Exiting...");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error on port argument");
            System.out.println("Exiting...");
            return;
        }

        try {
            CongressServer cs = new CongressServer();
            Congress stub = (Congress) UnicastRemoteObject.exportObject(cs, 0);

            LocateRegistry.createRegistry(port);
            Registry reg = LocateRegistry.getRegistry(port);

            reg.rebind("CONGRESS-SERVER", stub);

            System.out.println("[+] Server is up");
        
        } catch (RemoteException e) {
            System.out.println("Communication error " + e.toString());
        }
    }
    

}