package managers.jsonObj;

import java.util.ArrayList;

public class OnlineUsersData {
    private final Type type;
    private final ArrayList<String> usersList;

    public OnlineUsersData(Type type, ArrayList<String> usersList) {
        this.type = type;
        this.usersList = usersList;
    }
}
