import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	private static JFrame frame;
	private JTextField textFBIIp;
	private JLabel lblCia;
	private JTextField textCia;
	JFileChooser chooser = new JFileChooser();
	private JButton sendButton;
	String appPath;
	private JProgressBar progressBar;
	JLabel infoLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			appPath = new File(".").getCanonicalPath();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		frame = new JFrame();
		frame.setTitle("FBI-Network-App");
		frame.setBounds(100, 100, 450, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblFbiIp = new JLabel("FBI IP");
		lblFbiIp.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblFbiIp);

		textFBIIp = new JTextField();
		textFBIIp.setBounds(50, 8, 86, 20);
		frame.getContentPane().add(textFBIIp);
		textFBIIp.setColumns(10);

		lblCia = new JLabel("CIA");
		lblCia.setBounds(10, 36, 46, 14);
		frame.getContentPane().add(lblCia);

		textCia = new JTextField();
		textCia.setBounds(50, 33, 262, 20);
		frame.getContentPane().add(textCia);
		textCia.setColumns(10);
		JButton chooseFile = new JButton("...");
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser.setCurrentDirectory(new File(appPath));
				int result = chooser.showOpenDialog(chooseFile);
				if (result == JFileChooser.APPROVE_OPTION) {
					textCia.setText(chooser.getSelectedFile().getName());
				}
			}
		});
		chooseFile.setBounds(322, 32, 24, 23);
		frame.getContentPane().add(chooseFile);

		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					 String args[] = {textFBIIp.getText(),
					 chooser.getSelectedFile().getPath()};
					 Sockfile sockfile = new Sockfile(args, infoLabel, progressBar);
					 sockfile.run();
//					Runtime.getRuntime().exec("java -jar sockfile.jar " + textFBIIp.getText() + " " + chooser.getSelectedFile().getPath());
				} catch (NullPointerException e1) {
					if (textFBIIp.getText().isEmpty())
						JOptionPane.showMessageDialog(frame, "No IP Selected");
					if (textCia.getText().isEmpty())
						JOptionPane.showMessageDialog(frame, "No CIA Selected");
				}
//				catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
		});
		sendButton.setBounds(356, 7, 68, 46);
		frame.getContentPane().add(sendButton);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 86, 414, 14);
		progressBar.setValue(0);
		frame.getContentPane().add(progressBar);

		infoLabel = new JLabel("New label");
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setBounds(10, 61, 414, 14);
		frame.getContentPane().add(infoLabel);
	}

	public static JFrame getFrame() {
		return frame;
	}
}
