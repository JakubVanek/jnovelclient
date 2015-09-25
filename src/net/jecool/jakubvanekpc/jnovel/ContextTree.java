package net.jecool.jakubvanekpc.jnovel;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * NDS tree explorer
 * @author Jakub Vaněk
 */
public class ContextTree extends JDialog {
	public final Object lck;
	private JTree tree1;
	private JPanel root;
	private JButton vybratButton;
	private JButton stornoButton;
	boolean selected = false;
	public ContextTree(final MainWindow owner, final String server){
		super(owner);
		lck = new Object();
		setContentPane(root);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				synchronized (lck) {
					lck.notify();
				}
			}
		});
		DefaultTreeModel model = (DefaultTreeModel) tree1.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		root.setUserObject("[ROOT]");
		fillTree(server, root);
		model.setRoot(root);
		model.nodeChanged(root);
		tree1.setModel(model);
		tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		stornoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = false;
				dispose();
			}
		});
		vybratButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = true;
				owner.setContext(getSelection());
				dispose();
			}
		});
		pack();
		setVisible(true);
	}
	String getSelection() {
		if(selected){
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)tree1.getSelectionPath().getLastPathComponent();
			StringBuilder build = new StringBuilder();
			DefaultMutableTreeNode actual = node;
			while(actual!=null && !actual.getUserObject().equals("[ROOT]")){
				build.append((String)actual.getUserObject()).append('.');
				actual = (DefaultMutableTreeNode)actual.getParent();
			}
			if(build.length()>0)
				return build.substring(0,build.length()-1);
			else
				return "";
		}
		else
			return "";
	}
	public static void getContextName(final MainWindow owner, final String server) {
		new ContextTree(owner,server);
	}
	private void fillTree(String server, DefaultMutableTreeNode root){
		fillSubTree(server, root);
	}
	private void fillSubTree(String server, DefaultMutableTreeNode rNode){
		String path = getPath(rNode);
		String[] ctxs = listCtxs(server,path);
		for(String ctx: ctxs){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(ctx);
			rNode.add(node);
			fillSubTree(server, node);
		}
	}
	private String getPath(DefaultMutableTreeNode node){
		TreeNode[] path = node.getPath();
		StringBuilder str = new StringBuilder();
		for(int i = path.length-1; i > 0;i--){
			str.append(path[i]);
			if(i>1)
				str.append('.');
		}
		return str.toString();
	}
	public static String[] listCtxs(String server, String location){
		List<String> subcontexts = new LinkedList<>();
		String[] CMD;
		if(!location.equals(""))
			CMD = new String[]{"ncplist","-S",server,"-o",location, "-c", location, "-A", "-Q"};
		else
			CMD = new String[]{"ncplist","-S",server,"-A", "-Q"};
		String data= getProcessOutput(CMD);
		if(data != null) {
			String[] lines = data.split("\n");
			for (String line : lines) {
				String[] parts = line.split("=");
				if (parts[0].equals("O") || parts[0].equals("OU"))
					subcontexts.add(parts[1]);
			}
		}
		return subcontexts.toArray(new String[subcontexts.size()]);
	}
	public static String getProcessOutput(String[] cmd){
		try {
			ProcessBuilder b = new ProcessBuilder(cmd);
			Process p;
			try {
				p = b.start();
			}catch(IOException ignored){
				return null;
			}
			p.waitFor();
			if (p.exitValue() != 0)
				return null;
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder build = new StringBuilder();
			String s;
			while ((s = r.readLine()) != null) {
				build.append(s).append('\n');
			}
			r.close();
			return build.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
