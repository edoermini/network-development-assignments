import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Edoardo Ermini
 */
public class Session {
    ArrayList<String> speakers;

    public Session() {
        this.speakers = new ArrayList<String>(5);
    }

    public void addSpeaker(String name) throws RemoteException {
        if (this.speakers.size() == 5) {
            throw new RemoteException("Unable to add speaker, this session has already 5 speakers");
        }

        if (this.hasSpeaker(name)) {
            throw new RemoteException("Speaker already added in session");
        }

        this.speakers.add(name);
    }

    public boolean hasSpeaker(String name) {
        return speakers.contains(name);
    }

    public ArrayList<String> getSpeakers() {
        ArrayList<String> ret = new ArrayList<String>();

        for (String speaker : this.speakers) {
            ret.add(new String(speaker));
        }

        return ret;
    }
}
