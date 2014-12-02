package cz.gymnasiumkladno.jnovel;

import javax.swing.*;

public class Main {
	public static void main(String[] args){
		try {
			String gtkname = null;
			for(UIManager.LookAndFeelInfo name:UIManager.getInstalledLookAndFeels())
			{
				if("GTK+".equals(name.getName())) {
					gtkname = name.getClassName();
				}
			}
			if(gtkname!=null) {
				UIManager.setLookAndFeel(gtkname);
			}
		}catch(Exception ignored){
			System.err.println("Cannot set GTK+ look&feel");
		}
		new MainWindow();
	}
}
