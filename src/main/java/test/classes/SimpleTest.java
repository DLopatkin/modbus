package test.classes;

import Modbus.Station;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;

import java.util.Queue;

public class SimpleTest extends TestBase {
    public void run(Queue<String> in, Queue<String> out, Station station, int param) {
        String msg;

        do {
            while(in.isEmpty());

            msg = in.poll();

            if (msg.equals("Set")) {

                boolean isSetInitial = false;
                boolean isSetNew = false;
                try {
                    station.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (station.isConnected())
                    out.add("Связь со станцией " + station.getId() + " установлена");
                else {
                    out.add("Нет связи с станцией " + station.getId() );
                    return;
                }
                try {
                    isSetInitial = (station.readCoils(0,1))[0];
                    station.writeSingleCoil(0,!isSetInitial);
                    isSetNew = isSetInitial;
                    Thread.sleep(1000);
                    isSetNew = (station.readCoils(0,1))[0];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Проверка на то что бит выставился и param четный.
                if ( isSetInitial == !isSetNew && param % 2 == 0 )
                    out.add("Изделие успешно модифицировано");
                else
                    out.add("Изделие модифицировано некорректно");
            } else if(msg.equals("Confirm")) {
                try {
                    station.disconnect();
                    out.add("Соединение со станцией " + station.getId() + " закрыто.");
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }

        } while (!msg.equals("Exit"));
    }
}
