package ch.supsi;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import ch.supsi.gui.Gui;


public class Main {
	public static DB db = new DB();
	//	private static String site = "http://www.supsi.ch";

	@SuppressWarnings("unused")
	private static String[] mySites = { "http://www.supsi.ch/home.html", 
		"http://www.supsi.ch/home/strumenti/rubrica.html", 
		"http://www.supsi.ch/home/strumenti/azindex.html",
		"http://www.supsi.ch/home/strumenti/area-riservata.html",
		"http://www.supsi.ch/home/supsi.html",
		"http://www.supsi.ch/home/bachelor-diploma-master.html",
		"http://www.supsi.ch/home/fc.html",
		"http://www.supsi.ch/home/ricerca.html",
		"http://www.supsi.ch/home/comunica/eventi/2015/2015-06-121.html",
		"http://www.supsi.ch/home/comunica/eventi/2015/2015-06-06.html",
		"http://www.supsi.ch/home/comunica/ultime-news.html",
		"http://www.supsi.ch/home/comunica/news/2015/2015-05-21.html",
		"http://www.supsi.ch/home/comunica/eventi/2015/2015-05-041.html",
		"http://www.supsi.ch/home/bachelor-diploma-master/fashion-luxury.html",
		"http://www.supsi.ch/home/comunica/news/2015/2015-02-19.html",
		"http://www.supsi.ch/home/comunica/news/2014/2014-08-21.html",
		"http://www.supsi.ch/home/comunica/news/2014/2014-10-01.html",
		"http://www.supsi.ch/home/comunica/comunicati-stampa/2015/2015-05-26.html",
		"http://www.supsi.ch/home/comunica/news/2015/2015-01-19.html",
		"http://www.supsi.ch/home/comunica.html"
	};


	//private static String site = "http://www.supsi.ch/home_en/ricerca.html";
	private static String site = "http://f-diamante.ch/";

	//	private static String site = "http://www.swisscom.com";
	//	private static String site = "http://9gag.com";
	//	private static String site = "http://www.google.ch";
	//	private static String site = "http://www.mit.edu";
	//	private static String site = "http://www.w3schools.com";
	//	private static String site = "http://www.facebook.com";



	public static void main(String[] args) throws SQLException, IOException {

		new Gui(site);
		initScytheFile();

		WebSearch ws = new WebSearch();
		ws.googleSearch();


		//		PeopleSearch ps = new PeopleSearch();
		//		ps.searchLinkedin("Sascha", "Dominguez");
		//		ps.searchLinkedin("Sandro", "Pedrazzini");
		//		ps.searchLocal("Sandro", "Pedrazzini");
		//		
		//new WhoIsRetriver("www.google.ch");
		//		http://www.fernfachhochschule.ch/ffhs
		//			http://www.teatrodimitri.ch/scuola/?lang=en
		//			http://www.swissuniversities.ch
		//		


		//		WhoIsRetriver DrWho = new WhoIsRetriver();
		//		DrWho.getWhois("195.176.55.36");
		//System.out.println(DrWho.getWhoisInternic("mkyong.com"));


		//		PortScannerTest
		//		long start = System.currentTimeMillis();
		//		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");    
		//		Date resultdate = new Date(start);
		//		System.out.println(sdf.format(resultdate));
		//		//pS.scanPorts("195.176.55.36");
		//		PortScanner pS = new PortScanner();
		//		long end = System.currentTimeMillis();  
		//		Date entdate = new Date(end);
		//		System.out.println(sdf.format(entdate));

		//		This part is to test the correct functionig of siteCrawler without GUI
		//		SiteCrawler siteCrawler = new SiteCrawler(db);
		//		siteCrawler.setRecursiveOn(100);
		//		siteCrawler.processPage(site);


		//		This part is to test the correct functionig of MailCrawler without GUI
		//		MailCrawler mailCrawlerTest = new MailCrawler();
		//		mailCrawlerTest.processPage(site);

		//		for(int i = 0; i < mySites.length; i++){
		//			System.out.println("Site: number: "+i+" name: "+mySites[i]);
		//			processPage(mySites[i]);
		//		}

	}

	private static void initScytheFile() {
		//social media forums development "commerce blogs email");
		ArrayList<String> categories = new ArrayList<String>(Arrays.asList("social", "media", "forums", "development", "commerce", "blogs", "email"));
		for(int i = 0; i< categories.size(); i++){
			File file = new File("result"+categories.get(i)+".txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}