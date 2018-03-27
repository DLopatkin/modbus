package test.classes;

import Modbus.MasterCon;
import Modbus.Message;
import Modbus.Station;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;

import java.util.Map;
import java.util.Queue;

public class SimpleTest extends TestBase {
    public void run(Queue<Message> in, Queue<String> out, Map<Integer, Station> stations, int param) {
        Message msg;
        MasterCon con = null;

        do {
            while(in.isEmpty());

            msg = in.poll();
            if (msg.command.equals("Set")) {

                Station station = stations.get(msg.id);
                if (station.con == null) {
                    station.con = new MasterCon(station.host, station.port);
                }
                con = station.con;

                boolean isSetInitial = false;
                boolean isSetNew = false;
                try {
                    con.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (con.isConnected())
                    out.add("Связь с изделием установлена");
                else {
                    out.add("Нет связи с изделием");
                    return;
                }
                try {
                    isSetInitial = (con.readCoils(0,1))[0];
                    con.writeSingleCoil(0,!isSetInitial);
                    isSetNew = isSetInitial;
                    Thread.sleep(1000);
                    isSetNew = (con.readCoils(0,1))[0];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isSetInitial == !isSetNew)
                    out.add("Изделие успешно модифицировано");
                else
                    out.add("Изделие модифицировано некорректно");
            } else if (msg.command.equals("Confirm")) {
                Station station = stations.get(msg.id);

                try {
                    station.con.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }

        } while (msg.command.equals("Exit") == false);
    }
}
