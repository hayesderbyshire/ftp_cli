import java.io.Console;
import java.util.*;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class FTP {
	
	private String user;
	
	private String host;
	
	private String password;
	
	private int port;
	
	private Session session;
	
	public FTP(String user, String host, String password, int port) {
		this.user = user;
		this.host = host;
		this.password = password;
		this.port = port;
	}
	
	public void connect() throws JSchException {
        JSch jsch = new JSch();

        // Uncomment the line below if the FTP server requires certificate
        // jsch.addIdentity("private-key-path);

        //session = jsch.getSession(server);

        // Comment the line above and uncomment the two lines below if the FTP server requires password
        session = jsch.getSession(user, host, port);
        session.setPassword(password);

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }
	
	public static void printHeader() {
		System.out.println("FTP Client\nVersion: 0.1\nAuthor: Hayes Derbyshire\nUsage: Enter hostname, user, password, and port (22 for SFTP).");
	}
	
	public static void main(String[] args) throws JSchException {
		String user, host, pass;
		int port;
		
		printHeader();
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Host: ");
		host = input.next();
		
		System.out.println("Username: ");
		user = input.next();
		
		System.out.println("Password: ");
		Console con = System.console();
		char[] pwd = con.readPassword();
		
		pass = new String(pwd);
		
		System.out.println("Port: ");
		port = input.nextInt();
		
		input.close();
		
		FTP ftp = new FTP(user, host, pass, port);
		
		ftp.connect();
	}
}