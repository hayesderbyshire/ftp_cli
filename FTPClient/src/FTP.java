import java.io.Console;
import java.util.*;
import com.jcraft.jsch.*;

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
	
	public FTP(String user, String host, String password) {
		this.user = user;
		this.host = host;
		this.password = password;
	}
	
	public void connect() throws JSchException {
        JSch jsch = new JSch();
        
        session = jsch.getSession(user, host, port);
        session.setPassword(password);

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }
	
	public void upload(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.put(source, destination);
        sftpChannel.exit();
    }

    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect();
            System.out.println(ANSI_GREEN + "Successfully disconnected" + ANSI_RESET);
			System.exit(1);
        }
    }
	
	public static void printHeader() {
		System.out.println("SFTP Client\nVersion: 0.1\nAuthor: Hayes Derbyshire\nActions:\n"
				+ "Upload   - u src dest\n"
				+ "Download - d src dest\n"
				+ "Quit     - q\n");
	}
	
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
			
			ftp = new FTP(user, host, pass);
			
			try {
				ftp.connect();
				System.out.printf(ANSI_GREEN + "Connected to %s\n" + ANSI_RESET, host);
				connected = true;
			} catch (JSchException e) {
				System.out.println(ANSI_RED + "Connection failed\n" + ANSI_RESET);
			}
		}

		String action = null, src, dest;
		
		while ( true ) {
			System.out.print("Action: ");
			action = input.next();
			if (action.equals("q")) {
				ftp.disconnect();
			}
			src = input.next();
			dest = input.next();
			
			if (!action.equals("d") && !action.equals("u")) {
				throw new IllegalArgumentException(ANSI_RED + "Usage: d or u\n" + ANSI_RESET);
			} else if (action.equals("d")) {
				try {
					ftp.download(src, dest);
					System.out.printf(ANSI_GREEN + "Successfully downloaded %s to %s\n" + ANSI_RESET, src, dest);
				} catch (JSchException | SftpException e) {
					System.out.println(ANSI_RED + "Error: File not found" + ANSI_RESET);
					continue;
				}
			} else if (action.equals("u")) {
				try {
					ftp.upload(src, dest);
					System.out.printf(ANSI_GREEN + "Successfully uploaded %s to %s\n" + ANSI_RESET, src, dest);
				} catch (JSchException | SftpException e) {
					System.out.println(ANSI_RED + "Error: File not found" + ANSI_RESET);
					continue;
				}
			}
		}
	}
}