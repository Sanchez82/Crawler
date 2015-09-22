package ch.supsi;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PeopleSearch {

	public PeopleSearch() {

	}

	public ArrayList<String> searchImages(String name, String surname){
		ArrayList<String> imageResult = new ArrayList<String>();

		for(int i = 1; i < 9; i++){
			imageResult.addAll(searchImages(name, surname, i));
		}
		ArrayList<String> sorted = searchForDoubleImages(imageResult);
		return sorted;
	}

	/**
	 * @param name
	 * @param surname
	 * @param number is a google param that allow to narrow the search
	 * @return
	 */
	private ArrayList<String> searchImages(String name, String surname, int number){
		String URL = "http://ajax.googleapis.com/ajax/services/search/images?v=2.0&q="+name+"+"+surname+"&start="+number;
		//String URL = "http://ajax.googleapis.com/ajax/services/search/images?v=2.0&q="+name+"+"+surname;
		String[] googleResult = null;
		ArrayList<String> imageResult = new ArrayList<String>();
		try{
			Document doc = Jsoup.connect(URL).get();

			String result = doc.toString();
			googleResult = result.split(",");

			for(int i=0; i< googleResult.length; i++){

				if(googleResult[i].contains("jpeg") || googleResult[i].contains("jpg")){
					String[] t = googleResult[i].split("\":\"");
					imageResult.add(t[1].replace("\"",""));
				}
			}
		}catch(Exception e){

		}
		ArrayList<String> sorted = searchForDoubleImages(imageResult);
		return sorted;

	}



	private ArrayList<String> searchForDoubleImages(ArrayList<String> unsorted){
		ArrayList<String> sortedList = new ArrayList<String>();
		Boolean hit = false;
		for(int i= 0; i < unsorted.size(); i++){
			hit = false;
			if(sortedList.size()==0){
				sortedList.add(unsorted.get(i));
			}else{
				for(int j =0; j < sortedList.size(); j++){
					if(unsorted.get(i).equalsIgnoreCase(sortedList.get(j))){
						hit = true;
					}	
				}
				if(!hit){
					sortedList.add(unsorted.get(i));
				}
			}
		}
		return sortedList;
	}

	public ArrayList<String> searchLinkedin(String name, String surname){
		ArrayList<String> results = new ArrayList<String>();
		//String URL = "http://ajax.googleapis.com/ajax/services/search/images?v=2.0&q="+name+"+"+surname+"&start=8";
		String URL = "https://www.linkedin.com/pub/dir/?first="+name+"&last="+surname+"&search=Cerca";

		try{
			Document doc = Jsoup.connect(URL).get();

			//System.out.println(doc);
			System.out.println("LinkedIn");
			
			String job = getInfo(doc, "title");
			results.add(job);
			System.out.println("job: "+job);

			String location = getInfo(doc, "location");
			results.add(location);
			System.out.println("basic: "+location);

			String currentJob = getInfo(doc, "current-content");
			results.add(currentJob);
			System.out.println("CurrentJob: "+currentJob);

			String education = getInfo(doc, "education-content");
			results.add(education);
			System.out.println("Education: "+education);

		}catch(Exception e){
			//e.printStackTrace();
		}
		return results;
	}
	
	//TODO separare bene le informazioni
	private String getInfo(Document doc, String selector){

		Elements element = doc.getElementsByClass(selector);
		String r="";
		for(Element link: element){
			//System.err.println(link.text());
			r= r+link.text()+"\t";
		}
		return r;
	}
	
	public ArrayList<String> searchLocal(String name, String surname){
		ArrayList<String> results = new ArrayList<String>();
		String URL = "http://tel.local.ch/it/q?what="+name+"+"+surname+"&where=&rid=NlPj";
		try{
			Document doc = Jsoup.connect(URL).get();

			System.out.println("Local");

			String details = getInfo(doc, "details-entry-title-link");
			System.out.println("details: "+details);
			results.add(details);

			String location = getInfo(doc, "address");
			results.add(location);
			System.out.println("adress: "+location);
			
			String tel = getInfo(doc, "number");
			results.add(tel);
			System.out.println("Tel:"+tel);
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}
//TODO ricerca nei social network
	public ArrayList<String> searchPresenceOnSocialNetwork(String name, String surname){ 
		ArrayList<String> results = new ArrayList<String>();

		String URL = "http://tel.local.ch/it/q?what="+name+"+"+surname+"&where=&rid=NlPj";
		try{
			Document doc = Jsoup.connect(URL).get();

			//System.out.println(doc);

			String details = getInfo(doc, "details-entry-title-link");
			System.out.println("details: "+details);

			String location = getInfo(doc, "address");
			System.out.println("adress: "+location);

			String tel = getInfo(doc, "number");
			System.out.println("Tel:"+tel);
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;

	}

}