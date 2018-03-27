package test.classes;

import Modbus.Message;
import Modbus.Station;

import java.util.Map;
import java.util.Queue;

public abstract class TestBase {
    public abstract void run(Queue<Message> in, Queue<String> out, Map<Integer, Station> stations, int param);
}
