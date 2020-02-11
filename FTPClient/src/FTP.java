import java.io.Console;
import java.util.*;
import com.jcraft.jsch.*;

/**
 * 
 * SFTP command line program that gives the user the freedom
 * to exchange files between their local and remote machines.
 * 
 * Functionality:
 * - Upload
 * - Download
 * - Quit
 * 
 * @author Hayes Derbyshire
 * @version 0.1
 *
 */
public class FTP {
	
	private String user;
	
	private String host;
	
	private String password;
	
	private int port = 22;
	
	private Session session;
	
	private static boolean connected = false;
	
	public static final String ANSI_RED = "\u001B[31m";
	
	public static final String ANSI_GREEN = "\u001B[32m";
	
	public static final String ANSI_RESET = "\u001B[0m";
	
	/**
	 * Constructor for an FTP instance.
	 * @param user username
	 * @param host hostname
	 * @param password pass
	 */
	public FTP(String user, String host, String password) {
		this.user = user;
		this.host = host;
		this.password = password;
	}
	
	/**
	 * Attempts to connect to the hostname.
	 * @throws JSchException
	 */
	public void connect() throws JSchException {
        JSch jsch = new JSch();
        
        session = jsch.getSession(user, host, port);
        session.setPassword(password);

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }
	
	/**
	 * Uploads the source file to specified destination.
	 * @param source src file
	 * @param destination dest file
	 * @throws JSchException
	 * @throws SftpException
	 */
	public void upload(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.put(source, destination);
        sftpChannel.exit();
    }

	/**
	 * Downloads a source file to specified destination.
	 * @param source src file
	 * @param destination dest file
	 * @throws JSchException
	 * @throws SftpException
	 */
    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    /**
     * Disconnects from hostname.
     */
    public void disconnect() {
        if (session != null) {
            session.disconnect();
            System.out.println(ANSI_GREEN + "Successfully disconnected" + ANSI_RESET);
        }
    }
	
    /**
     * Prints a header for program.
     */
	public static void printHeader() {
		System.out.println("SFTP Client\nVersion: 0.1\nAuthor: Hayes Derbyshire\nActions:\n"
				+ "Upload   - u src dest\n"
				+ "Download - d src dest\n"
				+ "Quit     - q\n");
	}
	
	/**
	 * Start of program.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		String user, host, pass;
		
		Console con = null;
		
		FTP ftp = null;
		
		printHeader();
		
		Scanner input = new Scanner(System.in);
		
		while (!connected) {
			System.out.print("Host: ");
			host = input.next();
			
			System.out.print("Username: ");
			user = input.next();
			
			System.out.print("Password: ");
			con = System.console();
			char[] pwd = con.readPassword();
			pass = new String(pwd);
			
			// Make a new instance of FTP
			ftp = new FTP(user, host, pass);
			
			try {
				// Attempt to establish a connection to server
				ftp.connect();
				System.out.printf(ANSI_GREEN + "Connected to %s\n" + ANSI_RESET, host);
				connected = true;
			} catch (JSchException e) {
				System.out.println(ANSI_RED + "Connection failed\n" + ANSI_RESET);
			}
		}
		
		// Flow of control for client-side.
		// Repeats until program termination.
		while ( true ) {
			String action, src, dest;
			
			System.out.print("Action: ");
			action = input.next();
			
			if (action.equals("q")) {
				ftp.disconnect();
				input.close();
				System.exit(1);
			}
			
			src = input.next();
			dest = input.next();
			
			if (!action.equals("d") && !action.equals("u")) {
				throw new IllegalArgumentException(ANSI_RED + "Usage:\n"
						                + "Upload   - u src dest\n" 
										+ "Download - d src dest\n" 
										+ "Quit     - q\n" + ANSI_RESET);
			} else if (action.equals("d")) {
				try {
					ftp.download(src, dest);
					System.out.printf(ANSI_GREEN + "Successfully downloaded %s to %s\n" + ANSI_RESET, src, dest);
				} catch (JSchException | SftpException e) {
					System.out.println(ANSI_RED + "Error: File not found" + ANSI_RESET);
				}
			} else if (action.equals("u")) {
				try {
					ftp.upload(src, dest);
					System.out.printf(ANSI_GREEN + "Successfully uploaded %s to %s\n" + ANSI_RESET, src, dest);
				} catch (JSchException | SftpException e) {
					System.out.println(ANSI_RED + "Error: File not found" + ANSI_RESET);
				}
			}
		}
	}
}