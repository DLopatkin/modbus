import TestClasses.Slave;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ModbusTest {

    MasterCon con = new MasterCon("localhost",7777);
    static Slave slave = new Slave(7777);

    @BeforeAll
    static void setup() {
        slave.start();
    }

    @AfterAll
    static void unset() {
        slave.stop();
    }

    @BeforeEach
    void connect() {
        try {
            con.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void disconnect() {
        try {
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInputRegisters(){
        try {
            System.out.println("TEST: INPUT REGISTERS");
            System.out.println("Reading from input registers.");
            int[] value0 = con.readInputRegisters(0, 2);
            assertEquals(value0[0], 12345);
            assertEquals(value0[1], 6420);
            int[] value1 = con.readInputRegisters(8, 2);
            assertEquals(value1[0], 6420);
            assertEquals(value1[1], 12345);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test
    public void testHoldingRegisters(){
        try {
            System.out.println("TEST: HOLDING REGISTERS");
            System.out.println("Reading from holding registers.");
            int[] value0 = con.readHoldingRegisters(0, 2);
            assertEquals(value0[0],0);
            assertEquals(value0[1],7777);
            int[] res = new int[2];
            res[0] = value0[1];
            res[1] = value0[0];
            System.out.println("Writing to holding registers.");
            con.writeMultipleRegisters(0, res);
            int[] value1 = con.readHoldingRegisters(0, 2);
            assertEquals(value1[0],7777);
            assertEquals(value1[1],0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test
    public void testDiscreteInputs(){
        try {
            System.out.println("TEST: DISCRETE INPUTS");
            System.out.println("Reading from discrete inputs.");
            boolean[] bvalue0 = con.readDiscreteInputs(0, 5);
            assertEquals(bvalue0[0], true);
            assertEquals(bvalue0[1], false);
            assertEquals(bvalue0[2], true);
            assertEquals(bvalue0[3], false);
            assertEquals(bvalue0[4], true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test
    public void testCoils(){
        try {
            System.out.println("TEST: COILS");
            System.out.println("Reading from coils.");
            boolean[] bvalue = con.readCoils(0, 2);
            assertEquals(bvalue[0],true);
            assertEquals(bvalue[1],false);
            System.out.println("Writing to coils.");
            con.writeSingleCoil(0, false);
            boolean[] bvalue1 = con.readCoils(0, 2);
            assertEquals(bvalue1[0],false);
            assertEquals(bvalue1[1],false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
