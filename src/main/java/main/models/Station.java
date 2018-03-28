package main.models;

/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 * 
 * This is an automatic generated file. It will be regenerated every time 
 * you generate persistence class.
 * 
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: DuKe TeAm
 * License Type: Purchased
 */
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="Station")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Station implements Serializable {

	private static final long serialVersionUID = -7660860122810596339L;

	public Station() {
	}
	
	@Column(name="ID", nullable=false, unique=true)	
	@Id	
	@GeneratedValue(generator="VC0A80067161D12BF8F80E577")	
	@org.hibernate.annotations.GenericGenerator(name="VC0A80067161D12BF8F80E577", strategy="native")	
	private int ID;
	
	@JsonIgnore
	@ManyToOne(targetEntity=Post.class, fetch=FetchType.LAZY)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="PostID", referencedColumnName="ID") })	
	@org.hibernate.annotations.LazyToOne(value=org.hibernate.annotations.LazyToOneOption.NO_PROXY)	
	private Post post;
	
	@Column(name="Name", nullable=false)	
	private String name;
	
	@Column(name="Type", nullable=false, length=2)	
	private int type;
	
	@Column(name="IPAddress", nullable=false, length=128)	
	private String IPAddress;
	
	@Column(name="Port", nullable=true, length=4)	
	private Integer port;
	
	/*@OneToMany(mappedBy="station", targetEntity=TaskLog.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private java.util.Set taskLog = new java.util.HashSet();
	*/
	/*private void setID(int value) {
		this.ID = value;
	}*/
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(int value) {
		this.type = value;
	}
	
	public int getType() {
		return type;
	}
	
	public void setIPAddress(String value) {
		this.IPAddress = value;
	}
	
	public String getIPAddress() {
		return IPAddress;
	}
	
	public void setPort(int value) {
		this.port = value;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPost(Post value) {
		this.post = value;
	}
	
	public Post getPost() {
		return post;
	}
	
	/*public void setTaskLog(java.util.Set value) {
		this.taskLog = value;
	}
	
	public java.util.Set getTaskLog() {
		return taskLog;
	}
	*/
	
	public String toString() {
		return String.valueOf(getID());
	}

	private ModbusMaster master;

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
