package Client;

import Service.JSONMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.out;

public class ClientManager {
    private String username;
    private BlockingQueue<String> historyQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();


    public ClientManager(String username) {
        this.username = username;
    }

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public void setUp() throws IOException {

        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

        BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream outputToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));


        new Thread(() -> {
            String messageFromServer;
            JSONMessage jsonMessage;
            try {
                while ((messageFromServer = inputFromServer.readLine()) != null) {
                    jsonMessage = JSONMessage.fromJSON(messageFromServer);
                    String typeMessage = jsonMessage.getType();

                    out.println(messageFromServer);

                    switch (typeMessage) {

                        case "history":
                            historyQueue.add(messageFromServer);
                            break;
                        case "current":
                            messageQueue.add(messageFromServer);
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection to server lost.");
            }
        }).start();

        new Thread(() -> {
            String messageToServer;
            try {
                while ((messageToServer = userInput.readLine()) != null) {
                    JSONMessage jsonMessage = new JSONMessage(username, messageToServer);
                    outputToServer.writeBytes(jsonMessage.toJSON() + "\n");
                }
            } catch (IOException e) {
                System.err.println("Connection to server lost.");
            }
        }).start();
    }

    private void printMessageQueue(BlockingQueue<String> messageQueue) {
        for (String message : messageQueue) {
            System.out.println(message);
        }
        messageQueue.clear();
    }

    private void printHistoryQueue(BlockingQueue<String> historyQueue) {
        for (String message : historyQueue) {
            System.out.println(message);
        }
        historyQueue.clear();
    }
}
