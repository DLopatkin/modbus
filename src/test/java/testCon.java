import static java.lang.Thread.sleep;

public class testCon extends MasterCon {

    public testCon(String ip, int port, int id) {
        super(ip, port, id);
    }

    @Override
    public void process() {
        try {
            System.out.println("================TEST ONE================");
            int[] value0 = readInputRegisters(0, 2);
            System.out.println("Values:\nreg0: " + value0[0] + "\nreg1: " + value0[1]);

            int[] value1 = readInputRegisters(8, 2);
            System.out.println("Values:\nreg8: " + value1[0] + "\nreg9: " + value1[1]);

            sleep(4000);

            System.out.println("================TEST TWO================");
            int[] value2 = readHoldingRegisters(0, 2);
            System.out.println("Values:\nin_reg0: " + value2[0] + "\nin_reg1: " + value2[1]);

            int[] res = new int[2];
            res[0] = value2[1];
            res[1] = value2[0];
            writeMultipleRegisters(0, res);
            int[] value3 = readHoldingRegisters(0, 2);
            System.out.println("Values:\nin_reg0: " + value3[0] + "\nin_reg1: " + value3[1]);
            sleep(4000);

            System.out.println("================TEST THREE================");
            boolean[] bvalue0 = readDiscreteInputs(0, 5);
            System.out.println("Values:\ncoil0: " + bvalue0[0] + "\ncoil1: " + bvalue0[1]
                    + "\ncoil2: " + bvalue0[2]+ "\ncoil3: " + bvalue0[3]
                    + "\ncoil4: " + bvalue0[4]);

            sleep(4000);

            System.out.println("================TEST FOUR================");
            boolean[] bvalue1 = readCoils(0, 2);
            System.out.println("Values:\nin_coil0: " + bvalue1[0] + "\nin_coil1: " + bvalue1[1]);

            writeSingleCoil(0, false);

            boolean[] bvalue2 = readCoils(0, 2);
            System.out.println("Values:\nin_coil0: " + bvalue2[0] + "\nin_coil1: " + bvalue2[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
