package Modbus;

public class Station {
    public int id;
    public String host;
    public int port;
    public MasterCon con = null;

    public Station(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }
}
