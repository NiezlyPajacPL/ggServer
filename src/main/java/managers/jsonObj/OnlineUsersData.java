package managers.jsonObj;

import java.util.ArrayList;

public class OnlineUsersData {
    public Type type;
//    public String users;
    public ArrayList<String> usersList;

    public OnlineUsersData(Type type, ArrayList<String> usersList) {
        this.type = type;
        this.usersList = usersList;
    }
}
