package test.classes;

import Modbus.ModbusConnection;
import Modbus.Slave;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class main {
    public static void main(String[] args) {
        SimpleTest test = new SimpleTest();

        Slave slv = new Slave(7777);
        slv.start();

        // Задержка для того что бы Modbus.Slave успел забиндить порт.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (slv.isAlive()) {
            System.out.println("Станция запущена");
        }

        ModbusConnection modbusConnection = new ModbusConnection(1, "localhost", 7777);


        Queue<String> out = new LinkedList<String>();

        // Установка соединения со станцией.
        try {
            modbusConnection.connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        }

        test.run(out, modbusConnection,0);

        while (!out.isEmpty()) {
            System.out.println(out.poll());
        }

        try {
            modbusConnection.disconnect();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
