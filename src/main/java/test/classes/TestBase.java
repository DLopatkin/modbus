package test.classes;

import Modbus.ModbusConnection;

import java.util.Queue;

public abstract class TestBase {
    public abstract void run(Queue<String> out, ModbusConnection station, int param);
}
