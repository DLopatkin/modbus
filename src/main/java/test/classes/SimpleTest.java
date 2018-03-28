package test.classes;

import Modbus.MasterCon;
import Modbus.Station;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;

import java.util.Map;
import java.util.Queue;

public class SimpleTest extends TestBase {
    public void run(Queue<String> in, Queue<String> out, Map<Integer, Station> stations, int param) {
        String[] msg;
        int id;
        MasterCon con = null;

        do {
            while(in.isEmpty());

            msg = in.poll().split("_");
            if (msg[0].equals("Set")) {
                id = Integer.parseInt(msg[1]);

                Station station = stations.get(id);
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
                //Проверка на то что бит выставился и param четный.
                if ( isSetInitial == !isSetNew && param % 2 == 0 )
                    out.add("Изделие успешно модифицировано");
                else
                    out.add("Изделие модифицировано некорректно");
            } else if (msg[0].equals("Confirm")) {
                id = Integer.parseInt(msg[1]);
                Station station = stations.get(id);

                try {
                    station.con.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }

        } while (!msg[0].equals("Exit"));
    }
}
