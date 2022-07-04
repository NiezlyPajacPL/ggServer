package managers;

import helpers.DateAndTime;


public class SubtitlesPrinter {

    DateAndTime dateAndTime = new DateAndTime();

    //SERVER
    public void printLogServerStarted(){println(dateAndTime.getCurrentTime() + ": SERVER HAS STARTED.");}

    //REGISTRATION
    public void printLogClientRegistered(String nickname, ConnectionData connectionData){println(dateAndTime.getCurrentTime() +
            ": Registered client " + nickname + " -- IP: " + connectionData.getInetAddress() + ":" + connectionData.getPort());}

    public void printLogClientFailedRegistration(String nickname,ConnectionData connectionData){println(dateAndTime.getCurrentTime() +
            ": Someone with IP:   "  + connectionData.getInetAddress() + ":" + connectionData.getPort() + " -- tried to register on used nickname " + "(" + nickname + ")");}

    public void printLogClientRegistrationFailedCommand(ConnectionData connectionData){println(dateAndTime.getCurrentTime() +
            ": Someone with IP:   "  + connectionData.getInetAddress() + ":" + connectionData.getPort() + " -- tried to register but used command wrongly.");}

    public void printLogGeneratedPassword(){println(dateAndTime.getCurrentTime() + ": Generated secured password.");}

    //LOGIN
    public void printLogClientLoggedIn(String nickname){println(dateAndTime.getCurrentTime() + ": Client - " + nickname + " successfully logged in." );}

    public void printLogClientLoggedOut(String nickname){println(dateAndTime.getCurrentTime() + ": Client: " + nickname + "logged out.");}

    public void printLogClientDoesNotExist(String name){ println(dateAndTime.getCurrentTime() +" " +  name + " does NOT exist in DB");}

    public void printLogClientFoundInDB(String nickname){println(dateAndTime.getCurrentTime() + " Client " + nickname + " found in data base");}

    //OTHERS

    public void printLogUsersListRequest(){println(dateAndTime.getCurrentTime() + ": Received users list request.");}

    public void printLogSuccessfullySentMessage(String sender, String receiver,String message){println(dateAndTime.getCurrentTime() +
            ": Client: " + sender + " successfully sent message to:  " + receiver + "/ Content: " + message);}

    public void printLogMessageNotSent(String sender, String receiver){println(dateAndTime.getCurrentTime() + ": Client unsuccessfully tried to send message to " + receiver);}

    //PRIVATE
    private void println(String text){System.out.println(text);}

    private void print(String text){System.out.print(text);}
}
