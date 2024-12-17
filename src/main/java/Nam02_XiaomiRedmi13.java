import Client.ClientManager;

import java.io.IOException;

public class Nam02_XiaomiRedmi13 {

    public static void main(String[] args) throws IOException {
        String userName = "Nam02";
        ClientManager clientManager = new ClientManager(userName);
        clientManager.setUp();
    }
}
