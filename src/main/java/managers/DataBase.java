package managers;

import helpers.ClientLoginInfo;

public interface DataBase {

    ClientLoginInfo getClient(String nickname);

    void saveClient(ClientLoginInfo clientLoginInfo);
}
