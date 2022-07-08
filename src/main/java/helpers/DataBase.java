package helpers;

public interface DataBase {

    String getClient(String nickname);

    void saveClient(String nickname, SecuredPassword password);
}
