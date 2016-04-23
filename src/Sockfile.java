import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class Sockfile {
	public Socket socket = null;
	public DataOutputStream out = null;
	public FileInputStream fileIn = null;
	public String args[];
	public JLabel infoLabel;
	public JProgressBar progressBar;

	public Sockfile(String args[], JLabel infoLabel, JProgressBar progressBar) {
		this.args = args;
		this.infoLabel = infoLabel;
		this.progressBar = progressBar;
	}

	public void run() {
		new Thread(new Runnable() {
			public void run() {
				if (args.length < 2) {
					System.err.println("Usage: java -jar sockfile.jar <ip> <files...>");
					return;
				}

				Socket socket = null;
				DataInputStream in = null;
				DataOutputStream out = null;
				try {
					socket = new Socket(args[0], 5000);
					in = new DataInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());

					out.writeInt(args.length - 1);
				} catch (IOException e) {
					try {
						socket.close();
					} catch (Exception e1) {
					}

					System.err.println("Failed to open socket and send header.");
					e.printStackTrace();
					return;
				}

				System.out.println("Sending files...");

				for (int i = 1; i < args.length; i++) {
					FileInputStream fileIn = null;
					try {
						File file = new File(args[i]);
						if (!file.exists()) {
							System.err.println("File \"" + file.getName() + "\" does not exist.");
							continue;
						}

						int ack = in.readByte();
						if (ack == 0) {
							System.out.println("Send cancelled by remote.");
							break;
						}

						fileIn = new FileInputStream(file);

						System.out.println("Sending info for \"" + file.getName() + "\"...");
						out.writeLong(file.length());

						System.out.println("Sending data for \"" + file.getName() + "\"...");
						byte buffer[] = new byte[1024 * 256];
						int length = 0;
						while ((length = fileIn.read(buffer)) != -1) {
							out.write(buffer, 0, length);
						}

						System.out.println("File \"" + file.getName() + "\" sent successfully.");

						if (i == args.length - 1) {
							System.out.println("All files sent successfully.");
						}
					} catch (IOException e) {
						System.err.println("Failed to send file \"" + args[i] + "\".");
						e.printStackTrace();
						return;
					} finally {
						try {
							fileIn.close();
						} catch (Exception e) {
						}
					}
				}

				try {
					socket.close();
				} catch (Exception e) {
				}
			}
		});
	}
}