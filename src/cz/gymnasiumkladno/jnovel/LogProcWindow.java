package cz.gymnasiumkladno.jnovel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ResourceBundle;


/**
 * Process output viewer
 * @author Jakub Vaněk
 */
public class LogProcWindow extends JDialog {
	private JTextArea textArea1;
	private JPanel root;
	private JButton OKButton;
	boolean ok = true;
	public boolean Success=false;
	public LogProcWindow(final Window owner, final Process p){
		super(owner,"Process:");
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
				final BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
				final BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				try {
					p.waitFor();
				} catch (Exception ignored) {
					return;
				}

				String s;
				try {
					while ((s = stdout.readLine()) != null) {
						final String a = s;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								appendText(a + '\n');
							}
						});
					}
					int chari;
					final StringBuilder b = new StringBuilder();
					while ((chari = stdout.read()) != -1) {
						b.append((char) chari);
					}
					if (b.length() != 0)
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								appendText(b.append('\n').toString());
							}
						});
				}catch(Exception e){e.printStackTrace();}


				try {
					while ((s = stderr.readLine()) != null) {
						final String a = s;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								appendText(a + '\n');
							}
						});
					}
					int chari;
					final StringBuilder b = new StringBuilder();
					while ((chari = stderr.read()) != -1) {
						b.append((char) chari);
					}
					if (b.length() != 0)
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								appendText(b.append('\n').toString());
							}
						});
				}catch(Exception e){e.printStackTrace();}

				try {
					stdout.close();
					stderr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				final ResourceBundle bundle = ResourceBundle.getBundle("Strings");
				if (p.exitValue() == 0) {
					Success = true;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							appendText(bundle.getString("Logger.success"));
						}
					});
				}
				else {
					Success = false;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							appendText(bundle.getString("Logger.fail"));
						}
					});
				}
			}
		});
		worker.start();
	}
	public void appendText(String text){
		textArea1.append(text);
	}
}
