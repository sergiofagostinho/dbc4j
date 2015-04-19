
package pt.unl.fct.di.dbc4j.examples;

/**
 * An example class.
 * @author SÃ©rgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class User {

	protected String name;
	protected String address;
	protected Integer phoneNumber;

	private boolean invariant() {
		return address != null && name != null && phoneNumber != null;
	}

	/**
	 * 
	 *
	 */
	public User() {
		this("John Doe", "Elm Street", 555666999);
	}

	/**
	 * 
	 * @param name
	 * @param address
	 * @param phoneNumber
	 */
	public User(String name, String address, Integer phoneNumber) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 
	 */
	public String toString() {
		return "name: " + name + "\naddress: " + address + "\nphone number: " + phoneNumber;
	}

	/**
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	private boolean preSetAddress() {
		//System.out.println("up");
		return !name.equals("John Doe");
	}
	
	/**
	 * 
	 * @param address
	 * @param zipCode
	 */
	public void setAddress(String address, String zipCode) {
		this.address = address + zipCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}	
	private boolean postSetName() {
		return this.name != null;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public int getPhoneNumber() {
		return phoneNumber;
	}
	private boolean preSetPhoneNumber(String phoneNumber) {
		return (phoneNumber.length() == 9); 
	}

	/**
	 * 
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = Integer.parseInt(phoneNumber);
	}
	private boolean preSetPhoneNumber(int phoneNumber) {
		return phoneNumber > 0; 
	}

	/**
	 * 
	 * @param phoneNumber
	 */
	public void setPhoneNumber(int phoneNumber) {
		setPhoneNumber(new Integer(phoneNumber).toString());
	}
}
