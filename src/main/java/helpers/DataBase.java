package helpers;

public interface DataBase {

    ClientLoginInfo getClient(String nickname);

    void saveClient(ClientLoginInfo clientLoginInfo);
}
