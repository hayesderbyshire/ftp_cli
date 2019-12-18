import java.util.*;
import java.io.Console;

/**
 * Simple FTP Client.
 * 
 * Supports functionality for connnecting and disconnecting to a remote server over SFTP,
 * as well as download and upload of files.
 * 
 * @author Hayes Derbyshire (hcderbys)
 * 
 * @version 0.1
 */
public class FTP {
	
	private String hostId;
	
	private String userId;
	
	private String pass;
	
	private int port;
	
	/**
	 * Constructor for an instance of FTP;
	 * @param hostId
	 * @param userId
	 * @param pass
	 * @param port
	 */
	public FTP(String hostId, String userId, String pass, int port) {
		this.hostId = hostId;
		this.userId = userId;
		this.pass = pass;
		this.port = port;
	}
	
	/**
	 * Prints a header for the program.
	 */
	public static void printHeader() {
		System.out.println("FTP Client\nVersion: 0.1\nAuthor: Hayes Derbyshire\nUsage: Enter host, user, password (if applicable) and port.\n");
	}
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		
		String host, user, pass;
		int port;

		printHeader();
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Host: ");
		host = input.next();
		
		System.out.print("User: ");
		user = input.next();
		
		System.out.print("Password: ");
		Console cons = System.console();
		char[] passwd = cons.readPassword();
		pass = new String(passwd);
		
		System.out.print("Port: ");
		port = input.nextInt();
		
		input.close();
		
		FTP ftp = new FTP(host, user, pass, port);
		
	}
	
}