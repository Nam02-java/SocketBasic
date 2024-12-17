package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerManager {
    private static final int PORT = 8080;
    public static ArrayList<Socket> clientSocketsList = new ArrayList<>();

    public void startServer() throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {

            Socket clientSocket = serverSocket.accept();
            clientSocketsList.add(clientSocket);
            System.out.println("New client connected : " + clientSocket.getInetAddress());

            new Thread(new IOClientHandler(clientSocket)).start();
        }
    }
}
