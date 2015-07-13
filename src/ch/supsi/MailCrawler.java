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
//TODO parsare anche le mail in javascript
public class MailCrawler {

	private DB db;
	private int emailNumber=0;

	public MailCrawler(DB db) {
		super();
		this.db = db;
	}

	public void processPage(String URL) throws SQLException, IOException{

		//get useful information
		Document doc = Jsoup.connect(URL).get();
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

				if(!alreadyVisitedMail(email)){
					String sql;

					//store the URL to database to avoid parsing again
					sql = "INSERT INTO  `crawlerDB`.`Mail` " + "(`eMail`) VALUES " + "(?);";
					PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					stmt.setString(1, email);
					stmt.execute();
					System.out.println(email);
				}


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

	private boolean alreadyVisitedMail(String email) throws SQLException{
		String sql = "select * from Mail where eMail = '"+email+"'";
		ResultSet rs = db.runSql(sql);
		if(rs.next()){
			return true;
		}else{
			return false;
		}

	}




}
