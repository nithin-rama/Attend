package michael.attend;

import java.util.ArrayList;

public class ListData {

    public String title;
    public String latitude;
    public String longitude;
    public String description;
    public String hostName;
    public String sessionTime;
    public String sessionDate;
    public String numEvents;
//    public String hostKey;
//    public String users;
    public boolean inSession;

    ArrayList<User> attendees;
    ArrayList<ListData> studentGroups;
}
