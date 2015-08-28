package ch.supsi;
import java.net.*;

public class IPRetriver {
	
	public IPRetriver() {
		super();
	}

	public String getIP(String url){
		String ip = "";
		try {
			InetAddress address = InetAddress.getByName(new URL(url).getHost());
			ip = address.getHostAddress();
		} catch (UnknownHostException | MalformedURLException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
}
