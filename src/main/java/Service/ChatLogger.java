package Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatLogger extends Thread {

    JSONMessage jsonMessage;
    private static File filePath = new File("E:\\chat_log.json.txt");

    public ChatLogger(JSONMessage jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    @Override
    public void run() {
        synchronized (ChatLogger.class) {
            saveMessage(jsonMessage);
        }
    }

    public static void saveMessage(JSONMessage message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (filePath.length() > 0) {
                writer.newLine();
            }
            writer.write(message.toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
