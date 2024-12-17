package Server;

import Service.ChatLogger;
import Service.FileUtil;
import Service.JSONMessage;
import Service.TimeUtil;

import java.io.*;
import java.net.Socket;

import java.util.List;

import static Server.ServerManager.clientSocketsList;

public class IOClientHandler implements Runnable {

    private Socket currentSocket;


    public IOClientHandler(Socket socket) {
        this.currentSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()));
            DataOutputStream outputToClient;
            FileUtil fileUtil = new FileUtil();
            String messageFromClient;

            while ((messageFromClient = inputFromClient.readLine()) != null) {
                System.out.println("Received: " + messageFromClient);
                JSONMessage parseJsonMessageFromClient = JSONMessage.fromJSON(messageFromClient);

                for (Socket socket : clientSocketsList) {
                    outputToClient = new DataOutputStream(socket.getOutputStream());

                    if (socket != currentSocket) {
                        String type = parseJsonMessageFromClient.getMessage();
                        switch (type.trim()) {
                            case "1":
                            case "history":

                                type = "history";

                                List<String> listChatHistory = fileUtil.loadHistory();
                                JSONMessage parseJsonMessageFromHistory;

                                // change socket to user who request history data
                                outputToClient = new DataOutputStream(currentSocket.getOutputStream());

                                for (String message : listChatHistory) {
                                    parseJsonMessageFromHistory = JSONMessage.fromJSON(message);
                                    parseJsonMessageFromHistory.setType(type);

                                    outputToClient.writeBytes(parseJsonMessageFromHistory.toJSON() + "\n");
                                }
                                break;

                            default:

                                FileUtil initializeLatestID = new FileUtil();
                                initializeLatestID.start();

                                try {
                                    // wait for the thread to complete to ensure the ID has been updated
                                    initializeLatestID.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                type = "current";
                                String ID = initializeLatestID.getGeneratedID();
                                String username = parseJsonMessageFromClient.getSender();
                                String message = parseJsonMessageFromClient.getMessage();
                                String timestamp = TimeUtil.getCurrentTimestamp();

                                JSONMessage jsonMessage = new JSONMessage(type, ID, username, message, timestamp);

                                Thread threadSaveJsonMessageToFile = new ChatLogger(jsonMessage);
                                threadSaveJsonMessageToFile.start();

                                outputToClient.writeBytes(jsonMessage.toJSON() + "\n");
                                break;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Client disconnected: " + currentSocket.getInetAddress());
        } finally {
            synchronized (clientSocketsList) {
                clientSocketsList.remove(currentSocket);
            }
            try {
                currentSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

