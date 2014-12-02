package cz.gymnasiumkladno.jnovel;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by kuba on 25.11.14.
 */
public class MainWindow extends JFrame {
	private JPanel rootPanel;
	private JTextField username;
	private JPasswordField password;
	private JLabel img;
	private JButton Login;
	private JButton nastaveníButton;
	private JButton stornoButton;
	private JTabbedPane tabbedPane1;
	private JComboBox<String> kontext;
	private JComboBox<String> server;
	private JComboBox<String> serverip;
	private JComboBox<String> mountpoint;
	private JButton getkontext;
	private JButton getmountpoint;
	private JCheckBox onlytcp;
	private JButton disconnect;
	private JSeparator separator;
	private Preferences prefs;

	ResourceBundle bundle;
	public MainWindow()
	{
		super("JNovel Client");
		prefs = Preferences.userNodeForPackage(cz.gymnasiumkladno.jnovel.MainWindow.class);
		setContentPane(rootPanel);
		try{
			BufferedImage image=ImageIO.read(ClassLoader.getSystemResourceAsStream("logo.png"));
			ImageIcon icon = new ImageIcon(image,"Java Novel client for Linux\n\t(C) Jakub Vaněk 2014");
			img.setText("");
			img.setIcon(icon);
		} catch(Exception e) {
			img.removeAll();
			img.setText("Java Novel client");
		}
		try{
			BufferedImage icon=ImageIO.read(ClassLoader.getSystemResourceAsStream("icon.png"));
			setIconImage(icon);

		} catch(Exception ignored) {
		}
		bundle = ResourceBundle.getBundle("Strings");
		setTitle(bundle.getString("Main.title"));
		pack();
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				savePrefs();
				super.windowClosing(e);
			}
		});
		loadPrefs();
		setVisible(true);
		Login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Process proc = Networking.connect((String) server.getSelectedItem(),
						(String) serverip.getSelectedItem(),
						(String) kontext.getSelectedItem(),
						username.getText(),
						password.getText(), (String) mountpoint.getSelectedItem(), onlytcp.isSelected());
				LogProcWindow w = new LogProcWindow(MainWindow.this,proc);
				if(w.Success) {
					savePrefs();
					dispose();
				}
			}
		});
		stornoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePrefs();
				dispose();
			}
		});
		nastaveníButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane1.setVisible(!tabbedPane1.isVisible());
				tabbedPane1.setEnabled(!tabbedPane1.isEnabled());
				separator.setVisible(!separator.isVisible());
				separator.setEnabled(!separator.isEnabled());
				if(!tabbedPane1.isVisible()){
					nastaveníButton.setText(bundle.getString("Main.expandSet"));
				}else{
					nastaveníButton.setText(bundle.getString("Main.collapseSet"));
				}
				pack();
			}
		});




		getkontext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ContextTree.getContextName(MainWindow.this,(String)serverip.getSelectedItem());
				savePrefs();
			}
		});
		getmountpoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle(bundle.getString("Main.getmount"));
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if(chooser.showOpenDialog(MainWindow.this)==JFileChooser.APPROVE_OPTION){
					mountpoint.addItem(chooser.getSelectedFile().toString());
					mountpoint.getModel().setSelectedItem(chooser.getSelectedFile().toString());
					savePrefs();
				}
			}
		});

		onlytcp.addActionListener(new ActionListener() {
			                          @Override
			                          public void actionPerformed(ActionEvent e) {
				                          savePrefs();
			                          }});
		kontext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addActual(kontext);
			}
		});
		server.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addActual(server);
			}
		});
		serverip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addActual(serverip);
			}
		});
		mountpoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addActual(mountpoint);
			}
		});
		disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Process proc = Networking.disConnect((String)mountpoint.getSelectedItem());
				LogProcWindow w = new LogProcWindow(MainWindow.this,proc);
			}
		});
	}

	private void addActual(JComboBox<String> field) {
		String path = (String)field.getSelectedItem();
		DefaultComboBoxModel<String> items = (DefaultComboBoxModel<String>)field.getModel();
		if(items.getIndexOf(path)==-1)
			field.addItem(path);
		field.getModel().setSelectedItem(path);
		savePrefs();
	}

	private void addField(JComboBox<String> field, String value) {
		DefaultComboBoxModel<String> items = (DefaultComboBoxModel<String>)field.getModel();
		if(items.getIndexOf(value)==-1)
			field.addItem(value);
		field.getModel().setSelectedItem(value);
		savePrefs();
	}
	private void loadPrefs(){
		String user = prefs.get("user","");
		String ctx = prefs.get("context","");
		String srv = prefs.get("server","");
		String srvip = prefs.get("serverip","");
		String mount = prefs.get("mountpoint","/mnt/ncp");
		boolean tcp = prefs.getBoolean("tcp", true);
		username.setText(user);
		addField(kontext, ctx);
		addField(server, srv);
		addField(serverip, srvip);
		addField(mountpoint,mount);
		onlytcp.setSelected(tcp);
	}
	private void savePrefs(){
		String user = username.getText();
		String ctx = (String)kontext.getSelectedItem();
		String srv = (String)server.getSelectedItem();
		String srvip = (String)serverip.getSelectedItem();
		String mount = (String)mountpoint.getSelectedItem();
		if(user == null)
			user = "";
		if(ctx == null)
			ctx = "";
		if(srv == null)
			srv = "";
		if(srvip == null)
			srvip = "";
		if(mount == null)
			mount = "";
		prefs.put("user",user);
		prefs.put("context",ctx);
		prefs.put("server",srv);
		prefs.put("serverip",srvip);
		prefs.put("mountpoint",mount);
		prefs.putBoolean("tcp",onlytcp.isSelected());
	}
	public void setContext(final String context){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				addField(kontext,context);
			}
		});
	}
}
