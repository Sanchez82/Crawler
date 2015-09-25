package ch.supsi.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import ch.supsi.IPRetriver;
import ch.supsi.MailCrawler;
import ch.supsi.PeopleSearch;
import ch.supsi.PortScanner;
import ch.supsi.ScytheJava;
import ch.supsi.SiteCrawler;
import ch.supsi.WhoIsRetriver;

import javax.imageio.ImageIO;
import javax.swing.*;

import ch.supsi.Main;        



public class Gui extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	//Time started the seach for DB 
	private	Calendar cal;
	private SimpleDateFormat sdf;

	private JTextField textField;
	private JTextField depthOfTheSearch;
	private JTextArea siteTextArea;
	private JTextArea mailTextArea;
	private JTextArea whoIsTextArea;
	private JTextArea portScannerTextArea;
	private JTextArea peopleSearchTextArea;

	private JButton button;

	private String startUrl;
	private final static String newline = "\n";
	private Boolean isEmailSearchActive = true;
	private Boolean isSiteSearchActive = true;
	private Boolean isWhoIsActive = true;
	private Boolean isPortScanningActive = false;
	private String time;
	private ArrayList<String> mails;

	private JTextArea scytheTextArea;
	private int xdim = 200;
	private int ydim = 100;
	private int windowsX = 850;
	private int windowsY = 750;

	private boolean scytheSocialisActive = true;
	private boolean scytheMediaIsActive = false;
	private boolean scytheForumsIsActive = false;
	private boolean scytheDevelopmentIsActive = false;
	private boolean scytheCommerceIsActive = false;
	private boolean scytheBlogsIsActive = false;
	private boolean scytheEmailIsActive = false;

	private void addComponentsToPane(Container pane){

		//Header
		BorderLayout headerLayout = new BorderLayout();
		JPanel headerPanel = new JPanel(headerLayout);
		headerPanel.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
		pane.add(headerPanel, BorderLayout.PAGE_START);

		//Footer
		BorderLayout footerLayout = new BorderLayout();
		JPanel footerPanel = new JPanel(footerLayout);
		pane.add(footerPanel, BorderLayout.PAGE_END);


		//Search Panel	
		FlowLayout searchPaneLayout = new FlowLayout();
		JPanel searchPanel = new JPanel(searchPaneLayout);
		headerPanel.add(searchPanel,  BorderLayout.PAGE_START);
		//	

		textField = new JTextField(35);
		textField.addActionListener(this);
		textField.setText(startUrl);
		searchPanel.add(textField);

		depthOfTheSearch = new JTextField(5);
		depthOfTheSearch.addActionListener(this);
		depthOfTheSearch.setText("100");
		searchPanel.add(depthOfTheSearch);

		button = new JButton("Search");
		searchPanel.add(button);
		button.setActionCommand("SEARCH");
		button.addActionListener(this);

		//Option Panel
		FlowLayout optionLayout = new FlowLayout(FlowLayout.LEFT );
		JPanel optionPanel = new JPanel(optionLayout);
		headerPanel.add(optionPanel,  BorderLayout.CENTER);

		JCheckBox emailCheck;
		JCheckBox websiteCheck;
		JCheckBox whoisCheck;
		JCheckBox portScannerCheck;

		emailCheck = new JCheckBox("email");
		emailCheck.setMnemonic(KeyEvent.VK_C);
		emailCheck.setSelected(true);
		emailCheck.setActionCommand("EMAIL");
		emailCheck.addActionListener(this);

		websiteCheck = new JCheckBox("web site");
		websiteCheck.setMnemonic(KeyEvent.VK_C);
		websiteCheck.setSelected(true);
		websiteCheck.setActionCommand("SITE");
		websiteCheck.addActionListener(this);

		whoisCheck = new JCheckBox("whois");
		whoisCheck.setMnemonic(KeyEvent.VK_C);
		whoisCheck.setSelected(true);
		whoisCheck.setActionCommand("WHOIS");
		whoisCheck.addActionListener(this);

		portScannerCheck = new JCheckBox("port scanner");
		portScannerCheck.setMnemonic(KeyEvent.VK_C);
		portScannerCheck.setSelected(false);
		portScannerCheck.setActionCommand("PORT");
		portScannerCheck.addActionListener(this);

		optionPanel.add(emailCheck);
		optionPanel.add(websiteCheck);
		optionPanel.add(whoisCheck);
		optionPanel.add(portScannerCheck);


		//Result Text Area
		//Site
		siteTextArea = new JTextArea(5, 20);
		siteTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(siteTextArea);
		scrollPane.setPreferredSize(new Dimension(xdim, ydim));
		//pane.add(scrollPane, BorderLayout.CENTER);


		//e-mail
		mailTextArea = new JTextArea(5, 20);
		mailTextArea.setEditable(false);
		JScrollPane scrollPaneMail = new JScrollPane(mailTextArea);
		scrollPaneMail.setPreferredSize(new Dimension(xdim, ydim));
		pane.add(scrollPaneMail, BorderLayout.CENTER);


		//whois
		whoIsTextArea = new JTextArea(5, 20);
		whoIsTextArea.setEditable(false);
		JScrollPane scrollPaneWhois = new JScrollPane(whoIsTextArea);
		scrollPaneWhois.setPreferredSize(new Dimension(xdim, ydim));
		pane.add(scrollPaneWhois, BorderLayout.CENTER);

		//port Scanner
		portScannerTextArea = new JTextArea(5, 20);
		portScannerTextArea.setEditable(false);
		JScrollPane scrolloPaneportScanner = new JScrollPane(portScannerTextArea);
		scrolloPaneportScanner.setPreferredSize(new Dimension(xdim, ydim));
		pane.add(scrolloPaneportScanner, BorderLayout.CENTER);

		//port PeopleSearch
		peopleSearchTextArea = new JTextArea(5, 20);
		peopleSearchTextArea.setEditable(false);
		JScrollPane scrollPanePeopleSearch  = new JScrollPane(peopleSearchTextArea);
		scrollPanePeopleSearch.setPreferredSize(new Dimension(xdim, ydim));
		pane.add(scrollPanePeopleSearch, BorderLayout.CENTER);

		//scythe

		BorderLayout scyntheLayout = new BorderLayout();
		JPanel scynthePanel = new JPanel(scyntheLayout);

		scytheTextArea = new JTextArea(5, 20);
		scytheTextArea.setEditable(false);
		JScrollPane scrollPaneScythe  = new JScrollPane(scytheTextArea);
		scrollPaneScythe.setPreferredSize(new Dimension(xdim, ydim));
		scynthePanel.add(scrollPaneScythe, BorderLayout.CENTER);

		// scytheOption

		// option scytheButton

		JButton editListButton = new JButton("edit list");
		editListButton.setActionCommand("EDITLIST");
		editListButton.addActionListener(this);

		JButton testList = new JButton("test list");
		testList.setActionCommand("TESTLIST");
		testList.addActionListener(this);

		//option checkbox
		FlowLayout scytheOptionLayout = new FlowLayout();
		JPanel scytheOptionPanel = new JPanel(scytheOptionLayout);

		ArrayList<String> scytheOptions = new ArrayList<String>();
		scytheOptions.add("social");
		scytheOptions.add("media");
		scytheOptions.add("forums");
		scytheOptions.add("development");
		scytheOptions.add("commerce");
		scytheOptions.add("blogs");
		scytheOptions.add("email");	

		JCheckBox socialCheck = new JCheckBox("social");
		socialCheck.setMnemonic(KeyEvent.VK_C);
		socialCheck.setSelected(true);
		socialCheck.setActionCommand("SOCIAL");
		socialCheck.addActionListener(this);

		JCheckBox mediaCheck = new JCheckBox("media");
		mediaCheck.setMnemonic(KeyEvent.VK_C);
		mediaCheck.setSelected(false);
		mediaCheck.setActionCommand("MEDIA");
		mediaCheck.addActionListener(this);

		JCheckBox forumsCheck = new JCheckBox("forums");
		forumsCheck.setMnemonic(KeyEvent.VK_C);
		forumsCheck.setSelected(false);
		forumsCheck.setActionCommand("FORUM");
		forumsCheck.addActionListener(this);

		JCheckBox developmentCheck = new JCheckBox("development");
		developmentCheck.setMnemonic(KeyEvent.VK_C);
		developmentCheck.setSelected(false);
		developmentCheck.setActionCommand("DEVELOPMENT");
		developmentCheck.addActionListener(this);

		JCheckBox commerceCheck = new JCheckBox("commerce");
		commerceCheck.setMnemonic(KeyEvent.VK_C);
		commerceCheck.setSelected(false);
		commerceCheck.setActionCommand("COMMERCE");
		commerceCheck.addActionListener(this);

		JCheckBox blogsCheck = new JCheckBox("blogs");
		blogsCheck.setMnemonic(KeyEvent.VK_C);
		blogsCheck.setSelected(false);
		blogsCheck.setActionCommand("BLOGS");
		blogsCheck.addActionListener(this);

		JCheckBox scytheemailCheck = new JCheckBox("email");
		scytheemailCheck.setMnemonic(KeyEvent.VK_C);
		scytheemailCheck.setSelected(false);
		scytheemailCheck.setActionCommand("EMAILSCYTHE");
		scytheemailCheck.addActionListener(this);

		scytheOptionPanel.add(socialCheck);
		scytheOptionPanel.add(mediaCheck);
		scytheOptionPanel.add(forumsCheck);
		scytheOptionPanel.add(developmentCheck);
		scytheOptionPanel.add(commerceCheck);
		scytheOptionPanel.add(blogsCheck);
		scytheOptionPanel.add(scytheemailCheck);

		scytheOptionPanel.add(editListButton);
		scytheOptionPanel.add(testList);

		scynthePanel.add(scytheOptionPanel, BorderLayout.PAGE_START);	
		//Result Tab

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Links", scrollPane);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Mail", scrollPaneMail);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		tabbedPane.addTab("Whois", scrollPaneWhois);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		tabbedPane.addTab("Open Ports", scrolloPaneportScanner);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		tabbedPane.addTab("People Info", scrollPanePeopleSearch);
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

		tabbedPane.addTab("Scythe", scynthePanel);
		tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);

		//		tabbedPane.addTab("Images", initImagePanel());
		//		tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);



		//Add the tabbed pane to this panel.
		pane.add(tabbedPane, BorderLayout.CENTER);

		//The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);


		//Progress Bar

		//		progressBar = new JProgressBar(0, 100);
		//		progressBar.setValue(0);
		//		progressBar.setStringPainted(true);
		//		footerPanel.add(progressBar, BorderLayout.CENTER);


		//This labels are for the formatting style use only
		JLabel empty = new JLabel("   ");
		JLabel empty1 = new JLabel("   ");
		JLabel empty2 = new JLabel("   ");
		JLabel empty3 = new JLabel("   ");
		JLabel empty4 = new JLabel("   ");

		pane.add(empty1, BorderLayout.LINE_START);
		pane.add(empty2, BorderLayout.LINE_END);
		footerPanel.add(empty,BorderLayout.PAGE_END );
		pane.add(footerPanel, BorderLayout.PAGE_END);
		footerPanel.add(empty3, BorderLayout.LINE_START);
		footerPanel.add(empty4, BorderLayout.LINE_END);
	}

	private void createAndShowGUI() {

		//Create and set up the window.
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setTitle("Crawler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addComponentsToPane(frame.getContentPane());

		// Display the window.
		frame.setSize(windowsX, windowsY);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	//TODO mettere a posto
	private JPanel initImagePanel(){
		JPanel panel = null;
		PeopleSearch ps = new PeopleSearch();
		ArrayList<String> images = ps.searchImages("Sandro", "Pedrazzini");
		panel = new JPanel(false);
		panel.setLayout(new GridLayout(2, 4));

		for(int i = 0; i< images.size(); i++){


			BufferedImage myPicture = null;
			try {
				URL url = new URL(images.get(i));
				//myPicture = ImageIO.read(new File("/Users/ISanchez/Documents/workspaceLuna/Crawler/asd.png"));

				myPicture = ImageIO.read(url);
				//JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				//add(picLabel);
				//					picLabel.setHorizontalAlignment(JLabel.CENTER);
				//					panel.add(picLabel);

				ImageIcon icon = new ImageIcon(myPicture);
				JLabel picLabel = new JLabel() {

					private static final long serialVersionUID = 1L;

					@Override
					public void paintComponent (Graphics g) {
						super.paintComponent (g);
						if (icon != null) {
							g.drawImage (icon.getImage(), 0, 0, 150, 150, null);
						}
					}
				};
				picLabel.setHorizontalAlignment(JLabel.CENTER);
				panel.add(picLabel);

			} catch (IOException e) {
				//e.printStackTrace();
				System.err.println("problema uri immagine");
			}
		}

		return panel;
	}

	private JPanel createImagePanel(String[] images){
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel("prova");
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getActionCommand() == "SEARCH") {
			search();
		}else if(evt.getActionCommand() == "EDITLIST"){
			new ScytheEditGUI();
		}else if(evt.getActionCommand() == "TESTLIST"){
			scytheSearch();
			try {
				getDataOnReultScytheFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(evt.getActionCommand() == "SITE"){
			if(isSiteSearchActive){
				isSiteSearchActive = false;
			}else{
				isSiteSearchActive = true;
			}
		}else if(evt.getActionCommand() == "EMAIL"){
			if(isEmailSearchActive){
				isEmailSearchActive = false;
			}else{
				isEmailSearchActive = true;
			}
		}else if(evt.getActionCommand() == "WHOIS"){
			if(isWhoIsActive){
				isWhoIsActive = false;
			}else{
				isWhoIsActive = true;
			}
		}else if(evt.getActionCommand() == "PORT"){
			if(isPortScanningActive){
				isPortScanningActive = false;
			}else{
				isPortScanningActive = true;
			}
		}else if(evt.getActionCommand() == "SOCIAL"){
			if(scytheSocialisActive){
				scytheSocialisActive = false;
			}else{
				scytheSocialisActive = true;
			}
		}else if(evt.getActionCommand() == "MEDIA"){
			if(scytheMediaIsActive){
				scytheMediaIsActive = false;
			}else{
				scytheMediaIsActive = true;
			}
		}else if(evt.getActionCommand() == "FORUMS"){
			if(scytheForumsIsActive){
				scytheForumsIsActive = false;
			}else{
				scytheForumsIsActive = true;
			}
		}else if(evt.getActionCommand() == "DEVELOPMENT"){
			if(scytheDevelopmentIsActive){
				scytheDevelopmentIsActive = false;
			}else{
				scytheDevelopmentIsActive = true;
			}
		}else if(evt.getActionCommand() == "COMMERCE"){
			if(scytheCommerceIsActive){
				scytheCommerceIsActive = false;
			}else{
				scytheCommerceIsActive = true;
			}
		}else if(evt.getActionCommand() == "BLOGS"){
			if(scytheBlogsIsActive){
				scytheBlogsIsActive = false;
			}else{
				scytheBlogsIsActive = true;
			}
		}else if(evt.getActionCommand() == "EMAILSCYTHE"){
			if(scytheEmailIsActive){
				scytheEmailIsActive = false;
			}else{
				scytheEmailIsActive = true;
			}
		}
	}

	private void scytheSearch() {
		//		social media forums development "commerce blogs email");
		scytheTextArea.setText("");
		ScytheJava sj = new ScytheJava();
		if(scytheSocialisActive){
			sj.scytheSocial();
		}
		if(scytheMediaIsActive){
			sj.scytheMedia();
		}
		if(scytheForumsIsActive){
			sj.scytheForums();
		}
		if(scytheDevelopmentIsActive){
			sj.scytheDevelopment();
		}
		if(scytheCommerceIsActive){
			sj.scytheCommerce();
		}	
		if(scytheBlogsIsActive){
			sj.scytheBlogs();
		}
		if(scytheEmailIsActive){
			sj.scytheEmail();
		}
	}

	private void getDataOnReultScytheFile() throws IOException {
		ArrayList<String> categories = new ArrayList<String>(Arrays.asList("social", "media", "forums", "development", "commerce", "blogs", "email"));

		for(int i =0; i< categories.size(); i++){
			BufferedReader br = new BufferedReader(new FileReader("result"+categories.get(i)+".txt"));
			// StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				scytheTextArea.append(line);
				scytheTextArea.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
		}
	}

	private void search(){

		//Time started the seach for DB 
		cal = Calendar.getInstance();
		sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		time = sdf.format(cal.getTime());
		resetTextFields();

		String site = textField.getText();
		if(site.contains("http")){
			IPRetriver ipr = new IPRetriver();
			ipr.getIP(site);

			if(isSiteSearchActive){
				int nrSearch = 100;
				try{
					nrSearch = Integer.parseInt(depthOfTheSearch.getText());
				}catch(Exception e){
					nrSearch = 100;
				}

				searchSite(nrSearch);
			}
			if(isEmailSearchActive){
				searchEmail();
				//people search it works only if we have some e-mail adresses to check
				peopleSearch();
			}if(isWhoIsActive){
				searchWhoIs();
			}
			if(isPortScanningActive){
				ScanPorts();
				System.out.println("fine");
			}

		}else{
			personSearch(site);
		}

	}
	private void resetTextFields() {
		siteTextArea.setText("");
		mailTextArea.setText("");
		whoIsTextArea.setText("");
		portScannerTextArea.setText("");
		peopleSearchTextArea.setText("");
	}
	//TODO trovare un modo intelligente di usarla
	private void peopleSearch(){
		PeopleSearch ps= new PeopleSearch();
		ArrayList<String> parsedMails = parseMail();
		for(int i =0; i< parsedMails.size(); i++){
			String t = parsedMails.get(i);
			String[] baseInfo = t.split(Pattern.quote("."));

			if(baseInfo.length>1){
				personSearch(baseInfo[0], baseInfo[1]);
			}
		}

		//		if(isSiteSearchActive){
		//			ArrayList<String> parsedMails = parseMail();
		//			for(int i =0; i< parsedMails.size(); i++){
		//				String t = parsedMails.get(i);
		//				String[] baseInfo = t.split(Pattern.quote("."));
		//
		//				if(baseInfo.length>1){
		//					
		//					System.out.println("\n"+baseInfo[0]+" "+ baseInfo[1]);	
		//					ArrayList<String> localInfo = ps.searchLocal(baseInfo[0], baseInfo[1]);
		//					ArrayList<String> linkedInInfo = ps.searchLinkedin(baseInfo[0], baseInfo[1]);
		//					ArrayList<String> imageInfo = ps.searchImages(baseInfo[0], baseInfo[1]);
		//					
		//					peopleSearchTextArea.append(baseInfo[0]+" "+ baseInfo[1]+"\n");
		//					
		//					for(int j = 0; j< localInfo.size(); j++){
		//						//peopleSearchTextArea.append(localInfo.get(i)+"\n");
		//					}
		//					for(int j = 0; j< linkedInInfo.size(); j++){
		//						//peopleSearchTextArea.append(linkedInInfo.get(i)+"\n");
		//					}
		//				}
		//			}
		//
		//		}
		//		else{

		//}



	}
	private void personSearch(String name, String surname) {
		
		PeopleSearch ps = new PeopleSearch();
		ArrayList<ArrayList<String>> all = ps.searchAll(name, surname);

		ArrayList<String> localInfo = all.get(0);
		ArrayList<String> linkedInInfo = all.get(1);

		//		ArrayList<String> localInfo = ps.searchLocal(baseInfo[0], baseInfo[1]);
		//		ArrayList<String> linkedInInfo = ps.searchLinkedin(baseInfo[0], baseInfo[1]);
		ArrayList<String> imageInfo = ps.searchImages(name, surname);

		peopleSearchTextArea.append(name.substring(0, 1).toUpperCase() + name.substring(1)+""
				+ " "+surname.substring(0, 1).toUpperCase()+surname.substring(1)+"\n");

		peopleSearchTextArea.append("Local \n");
		printPesrsonSearchResult(localInfo);

		peopleSearchTextArea.append("Linkedin \n");
		printPesrsonSearchResult(linkedInInfo);

	}

	//TODO imageInfo
	private void personSearch(String names) {
		String[] baseInfo = names.split(" ");
		personSearch(baseInfo[0], baseInfo[1]);
	}

	private void printPesrsonSearchResult(ArrayList<String> infoToDisplay){
		ArrayList<String> sorted;
		sorted = sortInfo(infoToDisplay);
		for(int i = 0; i< sorted.size(); i++){
			peopleSearchTextArea.append(sorted.get(i)+"\n");
			//			peopleSearchTextArea.append(localInfo.get(i)+"\n");
		}
		peopleSearchTextArea.append("\n");

	}

	//TODO controlla che tutto funzioni correttamente
	private ArrayList<String> sortInfo(ArrayList<String> s){

		ArrayList<String> result = new ArrayList<String>();
		int maxInfoLinkedIn = 0;
		for(int i = 0; i< s.size(); i++){
			String line = s.get(i);
			String[] rawLine = line.split("ÇÇ");
			if(maxInfoLinkedIn < rawLine.length){
				maxInfoLinkedIn = rawLine.length;
			}
			for(int j =0; j< rawLine.length; j++){
				if(result.size() <= j){
					result.add(rawLine[j]);
				}else{
					String t = result.get(j);
					t = t+" "+rawLine[j];
					result.set(j,t);
				}	
			}
		}
		return result;
	}

	private ArrayList<String> parseMail() {
		ArrayList<String> pm = new ArrayList<String>();
		for(int i = 0; i < mails.size(); i++){
			if(mails.get(i).contains("@")){
				String mail = mails.get(i).replace("mailto:", "");
				String[] baseInfo =mail.split("@");
				pm.add(baseInfo[0]);
				//System.out.println(baseInfo[0]);	
			}
		}		
		return pm;
	}

	private void ScanPorts() {
		try {
			String sql = "SELECT fkIp FROM Record where dateSearch like '"+time+"'"
					+ "GROUP BY fkIP "
					+ "ORDER BY COUNT(*) DESC "
					+ "LIMIT 0,1";
			ResultSet rs = Main.db.runSql(sql);
			rs.next();

			String ipAdress = "SELECT IP FROM `crawlerDB`.`IPadress` where idIPadress = "+rs.getString(1);

			ResultSet rs2 = Main.db.runSql(ipAdress);
			rs2.next();
			//new PortScanner(rs2.getString(1));
			PortScanner ps= new PortScanner();
			portScannerTextArea.append("scanned port for IP: "+rs2.getString(1)+"\n");
			portScannerTextArea.append(ps.scanPorts(rs2.getString(1)));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method look for the most present ip and do the whois protocol
	 */
	private void searchWhoIs() {
		try {
			String sql = "SELECT fkIp FROM Record where dateSearch like '"+time+"'"
					+ "GROUP BY fkIP "
					+ "ORDER BY COUNT(*) DESC "
					+ "LIMIT 0,1";
			ResultSet rs = Main.db.runSql(sql);
			rs.next();

			String ipAdress = "SELECT IP FROM `crawlerDB`.`IPadress` where idIPadress = "+rs.getString(1);

			ResultSet rs2 = Main.db.runSql(ipAdress);
			rs2.next();

			WhoIsRetriver DrWho = new WhoIsRetriver();
			DrWho.getWhois(rs2.getString(1));

			whoIsTextArea.append(DrWho.getWhois(rs2.getString(1)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//TODO ricerca e-mails
	private void searchEmail(){
		String site = textField.getText();

		try {
			MailCrawler mailCrawler = new MailCrawler();

			//Here I check if I have more URL than one
			if(isSiteSearchActive){
				mailCrawler.processAllPages(time);
			}else{
				mailCrawler.processPage(site, time);
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		mails = new ArrayList<String>();
		String sql = "select * from Mail where date = '"+time+"'";
		try {
			ResultSet rs = Main.db.runSql(sql);
			while(rs.next()){
				//	String t =  rs.getString("URL");
				mailTextArea.append(rs.getString("eMail")+ newline);
				mails.add(rs.getString("eMail"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mailTextArea.setCaretPosition(mailTextArea.getDocument().getLength());	
	}
	private void searchSite(int deepNumber){

		String site = textField.getText();

		try {
			SiteCrawler siteCrawler = new SiteCrawler();
			siteCrawler.setRecursiveOn(deepNumber);
			siteCrawler.processPage(site,time);

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}

		//		Getting the result sites form the db and append them to the text area
		String sql = "select * from Record where dateSearch like '"+time+"'";;
		try {
			ResultSet rs = Main.db.runSql(sql);
			while(rs.next()){
				siteTextArea.append(rs.getString("URL")+ newline);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		siteTextArea.setCaretPosition(siteTextArea.getDocument().getLength());
		System.out.println("search finished");
	}

	public Gui(String URL){
		super(new GridBagLayout());
		this.startUrl = URL;

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public Gui() {
		super(new GridBagLayout());
		//Schedule a job for the event-dispatching thread:
		this.startUrl = "";
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});	
	}
}
