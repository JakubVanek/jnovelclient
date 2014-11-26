package cz.gymnasiumkladno.jnovel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by kuba on 26.11.14.
 */
public class LogWindow extends JDialog {
	private JTextArea textArea1;
	private JPanel root;
	private JButton OKButton;
	public LogWindow(final Window owner, final String server, final String serverip, final String context, final String username, final String password, final String mountpoint, final boolean tcp){
		super(owner,"Připojení");
		setContentPane(root);
		pack();
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		OKButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		Thread worker = new Thread(new Runnable() {
			@Override
			public void run() {
				Process p = Networking.connect(server,serverip,username,context,password,mountpoint,tcp);
				final Scanner s = new Scanner(p.getInputStream());
				final Scanner e = new Scanner(p.getErrorStream());
				try{p.waitFor();}catch(Exception ignored){return;}
				while(s.hasNextLine() || e.hasNextLine()){
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								if(s.hasNextLine())
									appendText(s.nextLine());
								if(e.hasNextLine())
									appendText(e.nextLine());
							}
						});
				}
			}
		});
		worker.start();
	}
	public void setText(String text){
		textArea1.setText(text);
	}
	public void appendText(String text){
		textArea1.append(text);
	}
	static boolean isRunning(Process process) {
    try {
        process.exitValue();
        return false;
    } catch (Exception e) {
        return true;
    }
}
}
