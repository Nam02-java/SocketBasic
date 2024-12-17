package Service;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil extends Thread {
    private List<String> list = new ArrayList<>();
    private File filePath = new File("E:\\chat_log.json.txt");
    private int startLineValue = 0;

    private static String generatedID;


    @Override
    public void run() {
        synchronized (FileUtil.class) {
            try {
                generatedID = updateID(String.valueOf(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String updateID(String filePath) throws IOException {
        String lastLine = null;

        // read file -> get last line
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                lastLine = currentLine;
            }
        }

        // if file is empty
        if (lastLine == null || lastLine.isEmpty()) {
            return "1";
        }

        // convert last line to JSON
        JSONMessage lastMessage = JSONMessage.fromJSON(lastLine);

        // get ID from JSON -> ID+=1
        String id = lastMessage.getID();
        if (id != null && id.matches("\\d+")) {
            int idNumber = Integer.parseInt(id);
            return String.valueOf(idNumber + 1);
        }
        return "1";
    }

    public List<String> loadHistory() {
        list.clear();
        int linesToRead = 5;
        int startLine = startLineValue;
        int linesRead = 0;
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            long fileLength = raf.length();
            long pointer = fileLength - 1;
            StringBuilder sb = new StringBuilder();
            boolean isLineEmpty = true;

            while (pointer >= 0 && linesRead < startLine + linesToRead) {
                raf.seek(pointer);
                char c = (char) raf.read();

                if (c == '\n' || pointer == 0) {
                    if (sb.length() > 0 || pointer == 0) {
                        if (pointer == 0 && c != '\n') {
                            sb.append(c);
                        }

                        if (linesRead >= startLine && linesRead < startLine + linesToRead) {
                            list.add(sb.reverse().toString().trim());
                        }

                        sb.setLength(0);
                        linesRead++;
                        isLineEmpty = true;
                    }
                } else {
                    sb.append(c);
                    isLineEmpty = false;
                }
                pointer--;
            }

            if (sb.length() > 0 && linesRead >= startLine && linesRead < startLine + linesToRead) {
                list.add(sb.reverse().toString().trim());
            }

            Collections.reverse(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        startLineValue = startLine + linesToRead;
        return list;
    }


    // getter to get latest ID
    public static String getGeneratedID() {
        return generatedID;
    }
}

