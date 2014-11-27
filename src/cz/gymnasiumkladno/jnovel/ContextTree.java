package cz.gymnasiumkladno.jnovel;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kuba on 25.11.14.
 */
public class ContextTree extends JDialog {
	private JTree tree1;
	private JPanel root;
	private JButton vybratButton;
	private JButton stornoButton;
	boolean selected = false;
	public ContextTree(final Window owner, final String server){
		super(owner);
		setContentPane(root);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tree1.removeAll();

		for(String item: listCtxs(server,"")) {
			TreePath t = new TreePath(new String[]{item});
			tree1.addSelectionPath(t);
		}
		stornoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		vybratButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = true;
				dispose();
			}
		});
		pack();
		setVisible(true);
	}
	public static String getContextName(final Window owner, final String server) {
		ContextTree t = new ContextTree(owner,server);
		return null;
	}
	public static String[] listCtxs(String server, String location){/*
		List<String> subcontexts = new LinkedList<String>();
		String[] CMD;
		if(!location.equals(""))
			CMD = new String[]{"ncplist","-S",server,"-o",location, "-c", location, "-A", "-Q"};
		else
			CMD = new String[]{"ncplist","-S",server,"-o", "[Root]", "-c", "[Root]", "-A", "-Q"};
		String data= getProcessOutput(CMD);
		String[] lines = data.split("\n");
		for(String line: lines){
			String[] parts = line.split("=");
			if(parts[0].equals("O")||parts[0].equals("OU"))
				subcontexts.add(parts[1]);
		}
		return subcontexts.toArray(new String[subcontexts.size()]);*/
		return new String[]{"a","b","c","d"};
	}
	public static String getProcessOutput(String[] cmd){
		try {
			ProcessBuilder b = new ProcessBuilder(cmd);
			Process p = b.start();
			p.waitFor();
			if (p.exitValue() != 0)
				return null;
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder build = new StringBuilder();
			while(true){
				int i = r.read();
				if(i==-1)
					break;
				build.append((char)i);
			}
			r.close();
			return build.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
