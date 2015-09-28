package ch.supsi;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebSearch {

	String google = "https://www.google.ch/?gws_rd=ssl#q=";
	String search = "stackoverflow";
	String charset = "UTF-8";
	//String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
	String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0";

	public void googleSearch(){
		Elements links;
		
		try {
			
			links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select("a[href]");//.select("li.g>h3>a");
			System.out.println("gs");
			for (Element link : links) {
				String title = link.text();
				String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
				//url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

//				if (!url.startsWith("http")) {
//					continue; // Ads/news/etc.
//				}

				System.out.println("Title: " + title);
				System.out.println("URL: " + url);
			}
//			Document doc = Jsoup.connect(google).get();
//			Elements sites = doc.select("a[href]");
//
//			for(Element link: sites){
//				if(link.attr("href").contains("http")){
//					System.out.println(link.attr("href"));
//				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

}

