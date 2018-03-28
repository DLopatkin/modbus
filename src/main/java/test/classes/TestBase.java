package test.classes;

import Modbus.Station;

import java.util.Queue;

public abstract class TestBase {
    public abstract void run(Queue<String> in, Queue<String> out, Station station, int param);
}
