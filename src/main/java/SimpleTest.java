import TestClasses.Station;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;

import java.util.Map;
import java.util.Queue;

public class SimpleTest extends TestClasses.TestBase {
    public void run(Queue<String> in, Queue<String> out, Map<Integer, Station> stations, int param) {
        String msg;

        MasterCon con = null;

        do {
            while(in.isEmpty());

            msg = in.poll();
            if (msg.equals("Set")) {

                Station station = stations.get(1);
                con = new MasterCon(station.host, station.port);

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
            }

        } while (msg.equals("Confirm") == false);
        try {
            con.disconnect();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        }
    }
}
