import Server.ServerManager;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws IOException {
        ServerManager serverManager = new ServerManager();
        serverManager.startServer();
    }
}
