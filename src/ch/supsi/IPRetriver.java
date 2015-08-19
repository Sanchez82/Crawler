package ch.supsi;
import java.net.*;

public class IPRetriver {

	private String ip;

	public IPRetriver(String ip) {
		super();
		this.ip = ip;
	}
	
	public void getIP(String url){
		try {
			InetAddress address = InetAddress.getByName(new URL(url).getHost());
			String ip = address.getHostAddress();
			System.out.println(ip);
		} catch (UnknownHostException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
