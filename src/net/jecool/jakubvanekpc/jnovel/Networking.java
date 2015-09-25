package net.jecool.jakubvanekpc.jnovel;

public class Networking {
	public static Process connect(String serverName, String dnsName, String context, String user, String password, String mountpath, boolean tcp) {
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
}
