import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Edoardo Ermini
 */
public interface Congress extends Remote {

    void registerSpeaker(int day, int session, String name) throws RemoteException;
    String getProgram() throws RemoteException;
}
