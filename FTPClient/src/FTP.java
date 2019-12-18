import java.io.Console;
import java.util.*;
import com.jcraft.jsch.*;

public class FTP {
	
	private String user;
	
	private String host;
	
	private String password;
	
	private int port = 22;
	
	private Session session;
	
	public FTP(String user, String host, String password) {
		this.user = user;
		this.host = host;
		this.password = password;
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
		System.out.println("SFTP Client\nVersion: 0.1\nAuthor: Hayes Derbyshire\n");
	}
	
	public static void main(String[] args) throws JSchException {
		String user, host, pass;
		int port;
		
		Console con = null;
		
		printHeader();
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Host: ");
		host = input.next();
		
		System.out.print("Username: ");
		user = input.next();
		
		System.out.print("Password: ");
		con = System.console();
		char[] pwd = con.readPassword();
		
		pass = new String(pwd);
		
		input.close();
		
		FTP ftp = new FTP(user, host, pass);
		
		ftp.connect();
	}
}