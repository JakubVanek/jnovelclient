package cz.gymnasiumkladno.jnovel;

import javax.swing.*;

public class Main {
	public static void main(String[] args){
		try {
			String gtkname = null;
			String nimbusname = null;
			for(UIManager.LookAndFeelInfo name:UIManager.getInstalledLookAndFeels())
			{
				if("GTK+".equals(name.getName())) {
					gtkname = name.getClassName();
				}

				if("Nimbus".equals(name.getName())) {
					nimbusname = name.getClassName();
				}
			}
			if(gtkname!=null){
				UIManager.setLookAndFeel(gtkname);
				System.out.println("GTK look and feel selected");
			}
			else if(gtkname==null&&nimbusname!=null) {
				UIManager.setLookAndFeel(nimbusname);
				System.out.println("Nimbus look and feel selected");
			}

		}catch(Exception ignored){
			System.err.println("Cannot set GTK+ look&feel");
		}
		MainWindow m = new MainWindow();
	}
}
