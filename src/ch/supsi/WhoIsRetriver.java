package ch.supsi;
import java.io.IOException;
import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;

public class WhoIsRetriver {

	private DB db;

	private String[] serviceArray;
	private String[] queryArray;
	private int[] portArray;

	private static Pattern pattern;
	private Matcher matcher;

	// regex parser
	private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
	static {
		pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
	}


//	public WhoIsRetriver() {
//		this.db = Main.db;
//		try {
//			initServices();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		for(int i=0; i<serviceArray.length;i++ ){
//			System.out.println("Service "+i+" "+serviceArray[i]+" "+ queryArray[i]+" "+ portArray[i]);
//		}
//		//		//System.out.println("who is:"+getWhoisInternic("mkyong.com"));
//		//		System.out.println("who is:"+getWhoisInternic("195.176.55.36"));
//		//		System.out.println("Done");
//
//		//System.out.println(getWhois("195.176.55.36","www.supsi.ch"));
//		//System.out.println("Done");
//	}
	
	
	public WhoIsRetriver() {
		this.db = Main.db;
		try {
			initServices();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<serviceArray.length;i++ ){
			System.out.println("Service "+i+" "+serviceArray[i]+" "+ queryArray[i]+" "+ portArray[i]);
		}
	
		//		//System.out.println("who is:"+getWhoisInternic("mkyong.com"));
		//		System.out.println("who is:"+getWhoisInternic("195.176.55.36"));
		//		System.out.println("Done");

		//System.out.println(getWhois("195.176.55.36","www.supsi.ch"));
		//System.out.println("Done");
	}




	private void initServices() throws SQLException {
		String sqlNumberOfWhoisServers;
		String sqlServiceName;
		//get the info from the database

		sqlNumberOfWhoisServers = "SELECT COUNT(*) FROM `crawlerDB`.`whoisServer`";
		ResultSet rs2 = db.runSql(sqlNumberOfWhoisServers);
		rs2.next();
		Integer size = Integer.parseInt(rs2.getString(1));

		this.portArray = new int[size];
		this.queryArray = new String[size];
		this.serviceArray = new String[size];


		//		Getting the result sites form the db and append them to the text area
		sqlServiceName = "SELECT * FROM `crawlerDB`.`whoisServer`";

		ResultSet rs = Main.db.runSql(sqlServiceName);
		int i = 0;
		while(rs.next()){
			this.serviceArray[i] = rs.getString("name");
			this.queryArray[i] = rs.getString("queryFormat");
			this.portArray[i]= rs.getInt("portNumber");
			i++;
		}
	}

	/**
	 * @param query
	 * This method take the query for the service since every service has his own dictionary 
	 * every class should know how it is
	 * @return
	 */
	private String getWhois(String domainName,String service, String query, int port) {
		StringBuilder result = new StringBuilder("");
		WhoisClient whois = new WhoisClient();
		try {

			//		  whois.connect(WhoisClient.DEFAULT_HOST);
			//		  whois.connect("whois.networksolutions.com",  WhoisClient.DEFAULT_PORT);
			
			//whois.connect(this.service, this.port);
			whois.connect(service, port);

			// whois =google.com
			// String whoisData1 = whois.query("=" + domainName);
			// String whoisData1 = whois.query("-i ns 195.176.55.36");
			//mkyong.com
		//	String whoisData1 = whois.query(query+"195.176.55.36");
			String whoisData1 = whois.query(query+domainName);

			// append first result
			result.append(whoisData1);
			whois.disconnect();

			// get the google.com whois server - whois.markmonitor.com
			String whoisServerUrl = getWhoisServer(whoisData1);
			//			if (!whoisServerUrl.equals("")) {
			//
			//				// whois -h whois.markmonitor.com google.com
			//				String whoisData2 = queryWithWhoisServer(domainName, whoisServerUrl);
			//
			//				// append 2nd result
			//				result.append(whoisData2);
			//			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}


	public void getWhois(String domainName) {
	
		for(int i=0; i<serviceArray.length; i++){
			String result = getWhois(domainName, serviceArray[i], queryArray[i], portArray[i]);
			System.out.println(serviceArray[i]);
			System.out.println(result);
		}
	}

	//Parser di Whois 
	private String getWhoisServer(String whois) {

		String result = "";
		matcher = pattern.matcher(whois);

		// get last whois server
		while (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}


	//USELESS
	//	private String queryWithWhoisServer(String domainName, String whoisServer) {
	//
	//		String result = "";
	//		WhoisClient whois = new WhoisClient();
	//		try {
	//
	//			whois.connect(whoisServer);
	//			result = whois.query(domainName);
	//			whois.disconnect();
	//
	//		} catch (SocketException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//		return result;
	//	}


		public String getWhoisInternic(String domainName) {
	
			StringBuilder result = new StringBuilder("");
	
			WhoisClient whois = new WhoisClient();
			try {
	
				//default is internic.net
				whois.connect(WhoisClient.DEFAULT_HOST);
				//
				//whois.connect("whois.internic.net");
				//whois.connect("whois.ripe.net");
				String whoisData1 = whois.query("=" + domainName);
				result.append(whoisData1);
				whois.disconnect();
	
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			return result.toString();
		}

}
