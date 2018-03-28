package main.models;

import java.util.Queue;

public class SimpleTest extends TestBase {
    public void run(Queue<String> out, Station station, int param) {
        boolean isSetInitial = false;
        boolean isSetNew = false;

        if (station.isConnected()) {
            out.add("Связь со станцией " + station.getID() + " установлена");
            try {
                isSetInitial = (station.readCoils(0, 1))[0];
                station.writeSingleCoil(0, !isSetInitial);
                isSetNew = isSetInitial;
                Thread.sleep(1000);
                isSetNew = (station.readCoils(0, 1))[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Проверка на то что бит выставился и param четный.
            if (isSetInitial == !isSetNew && param % 2 == 0)
                out.add("Изделие успешно модифицировано");
            else
                out.add("Изделие модифицировано некорректно");
        } else {
            out.add("Нет связи со станцией " + station.getID() );
            return;
        }
    }

}
