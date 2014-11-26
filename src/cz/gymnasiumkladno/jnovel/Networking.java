package cz.gymnasiumkladno.jnovel;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class Networking {
	public static Process connect(String serverName, String dnsName, String user, String context, String password, String mountpath, boolean tcp) {
		try {
			String[] CMD;
			if(!password.isEmpty()) {
				if (tcp)
					CMD = new String[]{"ncpmount", "tcp", "-S", serverName, "-A", dnsName, "-U", user + "." + context, "-P", password, mountpath};
				else
					CMD = new String[]{"ncpmount", "-S", serverName, "-A", dnsName, "-U", user + "." + context, "-P", password, mountpath};
			}else{
				if (tcp)
					CMD = new String[]{"ncpmount", "tcp", "-S", serverName, "-A", dnsName, "-U", user + "." + context, "-n", mountpath};
				else
					CMD = new String[]{"ncpmount", "-S", serverName, "-A", dnsName, "-U", user + "." + context, "-n", mountpath};

			}
			ProcessBuilder ncp = new ProcessBuilder(CMD);
			return ncp.start();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static Process disConnect(String mountpath) {
		try {
			String[] CMD = {"ncpumount", mountpath};
			ProcessBuilder ncp = new ProcessBuilder(CMD);
			return ncp.start();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/* // FIXME dodělat
	public static String[] listObjects(String server, String path){
		try {
			String[] CMD;
			if (path.isEmpty())
				CMD = new String[]{"ncplist", "-S", server};
			else
				CMD = new String[]{"ncplist", "-S", server, "-o", path};
			ProcessBuilder ncp = new ProcessBuilder(CMD);
			Process p = ncp.start();
			Scanner stdout = new Scanner(p.getInputStream());
			//Scanner stderr = new Scanner(p.getErrorStream());
			p.waitFor();
			List<String> data;
			while(stdout.hasNextLine()) {
				String line = stdout.nextLine();
				if(line.matches(""))
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
}
