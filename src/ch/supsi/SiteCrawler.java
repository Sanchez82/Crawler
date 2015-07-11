package ch.supsi;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//TODO Ottimizzare l'algoritmo di ricerca adesso ogni sito che incontro faccio una query sul DB
// Vantaggi: posso avare un set di siti infinito, svantaggio velocità.

/**
 * @author ISanchez
 *
 *This class con search trought a we page and get all the href links with http protocol
 *
 *
 */
public class SiteCrawler {
	private DB db;
	private boolean isRecursiveSearchOn = false;
	private int maxSeach;
	private static int count= 0;

	public SiteCrawler(DB db) {
		super();
		this.db = db;
	}

	public void processPage(String URL) throws SQLException, IOException{
		boolean isNotToMaxSites = true;
		
		System.out.println(count);
		count++;
		
		//check if the given URL is already in database
		String sql2 = "SELECT COUNT(*) FROM Record";
		ResultSet rs2 = db.runSql(sql2);
		rs2.next();
		Integer size = Integer.parseInt(rs2.getString(1));

		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = db.runSql(sql);
		
		//This part check if the seach is recursive and how far ti solud go
		if(isRecursiveSearchOn){
			if(size == maxSeach){
				isNotToMaxSites = false;
			}
		}
		
		//here if the query sql has a result id means that the site is already in the data base;
		if(rs.next()){
			count--;

		}else if(isNotToMaxSites){
			//store the URL to database to avoid parsing again
			sql = "INSERT INTO  `crawlerDB`.`Record` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();

			//get useful information
			try{
				Document doc = Jsoup.connect(URL).get();
				Elements questions = doc.select("a[href]");

				for(Element link: questions){
					if(link.attr("href").contains("http")){
						System.out.println(link.attr("href"));
					}
					if(isRecursiveSearchOn){
						processPage(link.attr("abs:href"));
					}
				}
			}catch(Exception e){

			}
		}
	}
	private void setRecursive(Boolean b){
		this.isRecursiveSearchOn = b;
	}

	public void setRecursiveOn(int limitSites){
		this.maxSeach = limitSites;
		setRecursive(true);
	}

	public void setRecursiveOff(){
		setRecursive(false);
		
	}
}
