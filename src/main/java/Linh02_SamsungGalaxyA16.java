import Client.ClientManager;

import java.io.IOException;

public class Linh02_SamsungGalaxyA16 {
    public static void main(String[] args) throws IOException {
        String userName = "Linh02";
        ClientManager clientManager = new ClientManager(userName);
        clientManager.setUp();
    }
}