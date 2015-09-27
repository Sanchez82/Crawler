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
	//private int count= 0;

	public SiteCrawler() {
		super();
		this.db = Main.db;
	}

	public void processPage(String URL, String searchTime, int deepOfSearch) throws SQLException, IOException{
		setRecursiveOn(deepOfSearch);
		processPage(URL, searchTime);
	}

	public void processPage(String URL, String searchTime) throws SQLException, IOException{
		boolean isNotToMaxSites = true;
		//TODO impostare controllo
		boolean validURL= isValidURL(URL);

		//check if the given URL is already in database
		String sql2 = "SELECT COUNT(*) FROM Record where dateSearch = '"+searchTime+"'";
		ResultSet rs2 = db.runSql(sql2);
		rs2.next();
		Integer size = Integer.parseInt(rs2.getString(1));

		String sql = "select * from Record where URL = '"+URL+"' and dateSearch = '"+searchTime+"'";
		ResultSet rs = db.runSql(sql);

		//This part check if the search is recursive and how far it should go
		if(isRecursiveSearchOn){
			if(size == maxSeach){
				isNotToMaxSites = false;
			}
		}
		//here if the query sql has a result id means that the site is already in the data base;
		if(rs.next()){
			//			count--;
		}else if(isNotToMaxSites){

			if(validURL){
				//get the ip Adress of the URL
				IPRetriver ipR = new IPRetriver();
				String ip = ipR.getIP(URL);

				String sqlip = "SELECT * FROM `crawlerDB`.`IPadress` where IP = '"+ip+"'";
				ResultSet rs3 = db.runSql(sql);
				//if true the ip already in the database if not the add to new IP to the DB
				int ipId;
				if(rs3.next()){
					ipId = rs3.getInt(1);
				}else{
					//store the IP address to database to avoid parsing again

					sql = "INSERT INTO  `crawlerDB`.`IPadress` " + "(`IP`) VALUES " + "(?);";
					PreparedStatement stmtip = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					stmtip.setString(1, ip);
					stmtip.execute();	
					
					//get the fkid of the new IP
					rs3 = db.runSql(sqlip);
					rs3.next();
					ipId = rs3.getInt(1);
				}


				//store the URL to database to avoid parsing again
				sql = "INSERT INTO  `crawlerDB`.`Record` " + "(`URL`, `dateSearch`, `fkIp`) VALUES " + "(?,?,?);";
				PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, URL);
				stmt.setString(2, searchTime);
				stmt.setInt(3, ipId);
				stmt.execute();

				try{
					Document doc = Jsoup.connect(URL).get();
					Elements sites = doc.select("a[href]");

					for(Element link: sites){
						if(link.attr("href").contains("http")){
							//System.out.println(link.attr("href"));
						}
						if(isRecursiveSearchOn){
							processPage(link.attr("abs:href"), searchTime);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	private boolean isValidURL(String URL) {
		if(URL.contains("http://") || URL.contains("https://")){
			return true;
		}
		return false;
	}

	private void setRecursive(Boolean b){
		this.isRecursiveSearchOn = b;
	}

	private void setRecursiveOn(int limitSites){
		this.maxSeach = limitSites;
		setRecursive(true);
	}

	public void setRecursiveOff(){
		setRecursive(false);

	}
}
