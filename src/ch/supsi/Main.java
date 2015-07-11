package ch.supsi;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.supsi.gui.Gui;


public class Main {
	public static DB db = new DB();
//	private static String site = "http://www.supsi.ch";
	private static int emailNumber=0;
	
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
	
	
	/*
	http://www.supsi.ch/home.html
		http://www.supsi.ch/home/strumenti/rubrica.html
		http://www.supsi.ch/home/strumenti/azindex.html
		
		"http://www.supsi.ch/home/strumenti/area-riservata.html",
		"http://www.supsi.ch/home/supsi.html",
		"http://www.supsi.ch/home/bachelor-diploma-master.html", Qui c'è un indirizzo
		"http://www.supsi.ch/home/fc.html", Qui c'è un  indirizzo
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
		"http://www.supsi.ch/home/comunica.html",
		*
		*/
	private static String site = "http://www.supsi.ch/home_en/ricerca.html";
	//	private static String site = "http://www.swisscom.com";
		//private static String site = "http://9gag.com";
	//private static String site = "http://www.google.ch";
	//private static String site = "http://www.mit.edu";
	//private static String site = "http://www.w3schools.com";
	//	private static String site = "http://www.facebook.com";



	public static void main(String[] args) throws SQLException, IOException {
		db.runSql2("TRUNCATE Record;");
		
		//TODO riattivare new Gui(site); 
		
		
		
//		This part is to test the correct functionig of siteCrawler without GUI
//		SiteCrawler siteCrawler = new SiteCrawler(db);
//		siteCrawler.setRecursiveOn(100);
//		siteCrawler.processPage(site);
		
		
		MailCrawler mailCrawlerTest = new MailCrawler(db);
		mailCrawlerTest.processPage(site);
		
//		for(int i = 0; i < mySites.length; i++){
//			System.out.println("Site: number: "+i+" name: "+mySites[i]);
//			processPage(mySites[i]);
//		}
		 //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        
		
		//processPage(site);

	}

	public static void processPage(String URL) throws SQLException, IOException{
		//check if the given URL is already in database
		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = db.runSql(sql);
		
		if(rs.next()){

		}else{
			//store the URL to database to avoid parsing again
			sql = "INSERT INTO  `crawlerDB`.`Record` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();

			//get useful information
			Document doc = Jsoup.connect(site).get();
			//Filter info
//						if(doc.text().contains("href")){
//							System.out.println(URL);
//						}


//			System.out.println(URL);
			//get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");
			//Elements questions = doc.select("a[href=\"mailto\"");
			
			for(Element link: questions){
				//System.out.println(questions.get(i));

				String e5 = link.attr("abs:href");
				//System.out.println(e5);
				
				if(e5.contains("mail")){
					emailNumber++;
					System.out.println("--- this is how we start");
					System.out.println(e5 + "\n\n");
					String email = e5.replace("mailto:st.www", "");

//					// remove the xml encoding
//					System.out.println("---Remove XML encoding\n");
//					String email = org.jsoup.parser.Parser.unescapeEntities(e5.toString(), false);
//					System.out.println(email + "this \n\n\n\n");
//
//					// remove the concatunation with ' + '
//					System.out.println("--- Remove concatunation (all: ' + ')");
//					email = email.replaceAll("' \\+ '", "");
//					System.out.println(email + "\n\n\n\n");
//
//					// extract the email address variables
//					System.out.println("--- Remove useless lines");
//					Matcher matcher = Pattern.compile("var addy.*var addy", Pattern.MULTILINE + Pattern.DOTALL).matcher(email);
//					matcher.find();
//
//					email = matcher.group();
//					System.out.println(email + "\n\n\n\n");
//
//					// get the to string enclosed by '' and concatunate
//					System.out.println("--- Extract the email address");
//					matcher = Pattern.compile("'(.*)'.*'(.*)'", Pattern.MULTILINE + Pattern.DOTALL).matcher(email);
//					matcher.find();

					//email = matcher.group(1) + matcher.group(2);
					System.out.println("email parsed");
					System.out.println(email);
				}


			
			}

			//			for(Element link: questions){
			//				processPage(link.attr("abs:href"));
			////				if(link.attr("href").contains(site))
			//				if(link.attr("href").contains("mailto"));{
			////					processPage(link.attr("abs:href"));
			//					//System.out.println(URL);
			//				}
			//			}



			//			final String url = "http://poslovno.com/kategorije.html?sobi2Task=sobi2Details&catid=71&sobi2Id=20001";
			//			final Document doc2 = Jsoup.connect(site).get();
			//			final Element e5 = doc2.getElementById("href");

			System.out.println(emailNumber);
		}
	}
}