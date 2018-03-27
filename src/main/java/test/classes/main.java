package test.classes;

import Modbus.Message;
import Modbus.Slave;
import Modbus.Station;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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

        Map<Integer, Station> stats = new HashMap<Integer, Station>();
        stats.put(1, new Station(1, "localhost", 7777));

        Queue<Message> in = new LinkedList<Message>();
        Queue<String> out = new LinkedList<String>();
        in.add(new Message("Set", 1));
        in.add(new Message("Set", 1));
        in.add(new Message("Confirm", 1));
        in.add(new Message("Exit", 0));

        test.run(in, out, stats,0);

        while (!out.isEmpty()) {
            System.out.println(out.poll());
        }

        System.exit(0);
    }
}
