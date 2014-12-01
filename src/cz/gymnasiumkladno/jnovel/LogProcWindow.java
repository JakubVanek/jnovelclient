package cz.gymnasiumkladno.jnovel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.Scanner;

/**
 * Created by kuba on 26.11.14.
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
				while (ok) {
					try {
						final String append = stdout.readLine();
						if (append == null)
							throw new Exception();
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								appendText(append);
							}
						});
					} catch (Exception ignored) {
						ok = false;
					}
				}
				ok = true;
				appendText("\n");
				while (ok) {
					try {
						final String append = stderr.readLine();
						if (append == null)
							throw new Exception();
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								appendText(append);
							}
						});
					} catch (Exception ignored) {
						ok = false;
					}
				}
				try {
					stdout.close();
					stderr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (p.exitValue() == 0) {
					Success = true;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							appendText("\n--- Hotovo :) ---");
						}
					});
				}
				else {
					Success = false;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							appendText("\n--- Nastala chyba :( ---");
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
