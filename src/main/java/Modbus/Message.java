package Modbus;

public class Message {
    public String command;
    public int id;

    public Message(String command, int id) {
        this.command = command;
        this.id = id;
    }
}
