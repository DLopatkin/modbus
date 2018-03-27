import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.data.ModbusCoils;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlave;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Slave extends Thread {
    private int port;

    public Slave(int port){
        this.port = port;
    }

    public void run() {
        try {

            final ModbusSlave slave;

            TcpParameters tcpParameters = new TcpParameters();

            tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setKeepAlive(true);
            tcpParameters.setPort(port);

            slave = ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);
            //Modbus.setLogLevel(Modbus.LogLevel.LEVEL_VERBOSE);
            slave.setReadTimeout(10000);

            MyOwnDataHolder dh = new MyOwnDataHolder();
            dh.addEventListener(new ModbusEventListener() {
                public void onWriteToSingleCoil(int address, boolean value) {
                    //System.out.print("onWriteToSingleCoil: address " + address + ", value " + value);
                }

                public void onWriteToMultipleCoils(int address, int quantity, boolean[] values) {
                    //System.out.print("onWriteToMultipleCoils: address " + address + ", quantity " + quantity);
                }

                public void onWriteToSingleHoldingRegister(int address, int value) {
                    //System.out.print("onWriteToSingleHoldingRegister: address " + address + ", value " + value);
                }

                public void onWriteToMultipleHoldingRegisters(int address, int quantity, int[] values) {
                    //System.out.print("onWriteToMultipleHoldingRegisters: address " + address + ", quantity " + quantity);
                }
            });

            slave.setDataHolder(dh);
            ModbusHoldingRegisters hr = new ModbusHoldingRegisters(10);
            hr.set(0, 12345);
            hr.set(1, 6420);

            hr.set(9, 12345);
            hr.set(8, 6420);

            ModbusHoldingRegisters ir = new ModbusHoldingRegisters(2);

            ir.set(0, 0);
            ir.set(1, 7777);

            ModbusCoils cs = new ModbusCoils(5);

            cs.set(0, true);
            cs.set(1, false);
            cs.set(2, true);
            cs.set(3, false);
            cs.set(4, true);

            ModbusCoils ic = new ModbusCoils(2);

            ic.set(0, true);
            ic.set(1, false);

            slave.getDataHolder().setHoldingRegisters(ir);
            slave.getDataHolder().setCoils(ic);
            slave.getDataHolder().setInputRegisters(hr);
            slave.getDataHolder().setDiscreteInputs(cs);
            slave.setServerAddress(1);
            /*
             * using master-branch it should be #slave.open();
             */

            slave.listen();

            /*
             * since 1.2.8
             */
            if (slave.isListening()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        synchronized (slave) {
                            slave.notifyAll();
                        }
                    }
                });

                synchronized (slave) {
                    slave.wait();
                }

                /*
                 * using master-branch it should be #slave.close();
                 */
                slave.shutdown();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface ModbusEventListener {
        void onWriteToSingleCoil(int address, boolean value);

        void onWriteToMultipleCoils(int address, int quantity, boolean[] values);

        void onWriteToSingleHoldingRegister(int address, int value);

        void onWriteToMultipleHoldingRegisters(int address, int quantity, int[] values);
    }

    public static class MyOwnDataHolder extends DataHolder {

        final List<ModbusEventListener> modbusEventListenerList = new ArrayList<ModbusEventListener>();

        public MyOwnDataHolder() {
        }

        public void addEventListener(ModbusEventListener listener) {
            modbusEventListenerList.add(listener);
        }

        public boolean removeEventListener(ModbusEventListener listener) {
            return modbusEventListenerList.remove(listener);
        }

        @Override
        public void writeHoldingRegister(int offset, int value) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToSingleHoldingRegister(offset, value);
            }
            super.writeHoldingRegister(offset, value);
        }

        @Override
        public void writeHoldingRegisterRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToMultipleHoldingRegisters(offset, range.length, range);
            }
            super.writeHoldingRegisterRange(offset, range);
        }

        @Override
        public void writeCoil(int offset, boolean value) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToSingleCoil(offset, value);
            }
            super.writeCoil(offset, value);
        }

        @Override
        public void writeCoilRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToMultipleCoils(offset, range.length, range);
            }
            super.writeCoilRange(offset, range);
        }
    }
}
