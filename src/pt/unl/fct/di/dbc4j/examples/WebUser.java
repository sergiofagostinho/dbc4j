
package pt.unl.fct.di.dbc4j.examples;

import pt.unl.fct.di.dbc4j.ContractMemory;

/**
 * An example class.
 * @author SÃ©rgio Agostinho (sergioag@di.fct.unl.pt)
 *
 */
public class WebUser extends User {

	private int id;
	private String[] notes;
	
	/**
	 * 
	 *
	 */
	public WebUser() {
		super();
		this.id = 0;
		this.notes = new String[10];
	}

	/**
	 * 
	 * @param name
	 * @param address
	 * @param phoneNumber
	 */
	public WebUser(String name, String address, Integer phoneNumber) {
		super(name, address, phoneNumber);
		this.id = 0;
		this.notes = new String[10];
	}

	/**
	 * 
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @param id
	 * @param notes
	 */
	private WebUser(String name, String address, Integer phoneNumber, int id, String[] notes) {
		super(name, address, phoneNumber);
		this.id = id;
		this.notes = notes;
	}
	
	/**
	 * 
	 * @param id
	 */
	public WebUser(int id) {
		super();
		this.id = id;
	}
	private boolean preWebUser(int id) {
		return id >= 0;
	}

	/**
	 * 
	 */
	public void setAddress(String address) {
		return;
	}
	private boolean preSetAddress(String address) {
		return address.startsWith("http://");
	}

	/**
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}
	private boolean postSetName() {
		return address != null;
	}

	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
		WebUser foo = new WebUser();
		if (id > 0 && id < 10) {					
			foo.setId(id+1);
		}		
		if (id == 10)
			foo.setId(-1);
	}
	private boolean preSetId() {
		//ContractMemory.remember(this);
		//System.out.println("remember" + id);
		
		ContractMemory.observe("WebUser.id", id);
		//System.out.println("observe: " + id);
		
		return true;
	}
	private boolean postSetId() {
		//WebUser old = (WebUser) ContractMemory.old(this);
		//System.out.println("old: " + old.id);
		//return this.id > old.id;
		
		int oldId = (Integer) ContractMemory.attribute("WebUser.id");
		//System.out.println("attribute: " + oldId);
		return this.id > oldId;
	}
	
	/**
	 * 
	 * @param number
	 * @return
	 */
	public static int dumbMethod(int number) {
		// do nothing
		return 0;
	}
	private static boolean postDumbMethod(int result, int number) {
		return number == 14;
	}
	
	/**
	 * 
	 * @param number
	 */
	public static void lessDumbMethod(int number) {
		//return null;
	}
	private static boolean postLessDumbMethod(int number) {
		return number == 14;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getNotes() {
		return notes;
	}
	
	/**
	 * 
	 * @param notes
	 */
	public void setNotes(String[] notes) {
		this.notes = notes;
	}
	private boolean preSetNotes(String[] notes) {
		return notes.length > 0;
	}
	
	/**
	 * 
	 */
	public WebUser clone() {
		return new WebUser(name, address, phoneNumber, id, notes);
	}
}
