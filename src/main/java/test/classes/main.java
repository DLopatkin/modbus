package test.classes;

import Modbus.Station;
import Modbus.Slave;

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

        Station station = new Station(1, "localhost", 7777);


        Queue<String> in = new LinkedList<String>();
        Queue<String> out = new LinkedList<String>();
        in.add("Set");
        in.add("Confirm");
        in.add("Exit");

        test.run(in, out, station,0);

        while (!out.isEmpty()) {
            System.out.println(out.poll());
        }

        System.exit(0);
    }
}
