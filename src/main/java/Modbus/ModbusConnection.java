package Modbus;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ModbusConnection {

    private int ID;
    private int port;
    private String IPAddress;


    private ModbusMaster master;

    public int getID() {
        return ID;
    }

    /**
     *Constructor
     * @param ip slave's ip
     * @param port port
     */
    public ModbusConnection(int id, String ip, int port) {
        this.ID = id;
        this.IPAddress = ip;
        this.port = port;
    }

    /**
     *This method is establishing a connection with slave by tcp.
     * @throws UnknownHostException
     * @throws ModbusIOException
     */
    public void connect() throws UnknownHostException, ModbusIOException {
        TcpParameters tcpParameters = new TcpParameters();
        tcpParameters.setHost(InetAddress.getByName(IPAddress));
        tcpParameters.setKeepAlive(true);
        tcpParameters.setPort(port);

        master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        Modbus.setAutoIncrementTransactionId(true);

        master.connect();
    }

    /**
     *This method is closing tcp connection.
     * @throws ModbusIOException
     */
    public void disconnect() throws ModbusIOException {
        master.disconnect();
    };

    /**
     * Method for checking connection.
     * @return True if connected.
     */
    public boolean isConnected() {
        return master.isConnected();
    }

    /**
     *This method used for reading Input Registers data.
     * @param startAddress the number of the first register to be read.
     * @param quantity quantity of registers to be read.
     * @return int[] array with register's values
     * @throws Exception
     */
    public int[] readInputRegisters(int startAddress, int quantity) throws Exception {
        return master.readInputRegisters(ID, startAddress, quantity);
    }

    /**
     *This method used for reading Holding Registers data.
     * @param startAddress the number of the first register to be read.
     * @param quantity quantity of registers to be read.
     * @return int[] array with register's values
     * @throws Exception
     */
    public int[] readHoldingRegisters(int startAddress, int quantity) throws Exception {
        return  master.readHoldingRegisters(ID, startAddress,quantity);
    }

    /**
     *This method used for writing data in Holding Register.
     * @param startAddress destination register number.
     * @param register value of this register.
     * @throws Exception
     */
    public void writeSingleRegister(int startAddress, int register) throws Exception {
        master.writeSingleRegister(ID, startAddress, register);
    }

    /**
     *This method used for writing data in Holding Registers.
     * @param startAddress first destination register number.
     * @param registers values of those registers.
     * @throws Exception
     */
    public void writeMultipleRegisters(int startAddress, int[] registers) throws Exception {
        master.writeMultipleRegisters(ID, startAddress, registers);
    }

    /**
     *This method used for reading Discreate Inputs data.
     * @param startAddress the number of the first register to be read.
     * @param quantity quantity of registers to be read.
     * @return boolean[] values of those registers.
     * @throws Exception
     */
    public boolean[] readDiscreteInputs(int startAddress, int quantity) throws Exception {
        return master.readDiscreteInputs(ID, startAddress, quantity);
    }

    /**
     *This method used for reading Coils data.
     * @param startAddress the number of the first register to be read.
     * @param quantity quantity of registers to be read.
     * @return boolean[] values of those registers.
     * @throws Exception
     */
    public boolean[] readCoils(int startAddress, int quantity) throws Exception {
        return master.readCoils(ID, startAddress, quantity);
    }

    /**
     *This method used for writing data in Coil.
     * @param startAddress destination register number.
     * @param flag value of this register.
     * @throws Exception
     */
    public void writeSingleCoil(int startAddress, boolean flag) throws Exception {
        master.writeSingleCoil(ID, startAddress, flag);
    }

    /**
     * This method used for writing data in Coils.
     * @param startAddress first destination register number.
     * @param coils values of those registers.
     * @throws Exception
     */
    public void writeMultipleCoils(int startAddress, boolean[] coils) throws Exception {
        master.writeMultipleCoils(ID, startAddress, coils);
    }
}
