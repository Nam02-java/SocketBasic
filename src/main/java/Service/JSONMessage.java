package Service;

import com.google.gson.Gson;

public class JSONMessage {

    private String type;
    private String ID;
    private String sender;
    private String message;
    private String timestamp;

    public JSONMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public JSONMessage(String type, String ID, String sender, String message, String timestamp) {
        this.type = type;
        this.ID = ID;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
    // Getters

    public String getType() {
        return type;
    }

    public String getID() {
        return ID;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // setters
    public void setType(String type) {
        this.type = type;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // convert object to JSON
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // create JSON from object (string)
    public static JSONMessage fromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JSONMessage.class);
    }
}
