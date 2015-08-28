package ch.supsi;

import org.apache.commons.net.whois.WhoisClient;

/**
 * @author ISanchez
 *
 *Ripe query is better with the ip adress. 
 */
public class WhoisRipe extends WhoIsRetriver{
	
	String ripe = "whois.ripe.net";
	int port = WhoisClient.DEFAULT_PORT;

	public WhoisRipe(String ipadress, DB db) {
		super("whois.ripe.net", WhoisClient.DEFAULT_PORT, db);
//		String info = this.getWhois(ipadress, ipadress);
//		System.out.println(info);
	}
	

}
