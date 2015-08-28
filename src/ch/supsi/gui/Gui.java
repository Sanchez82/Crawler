package ch.supsi.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ch.supsi.IPRetriver;
import ch.supsi.MailCrawler;
import ch.supsi.SiteCrawler;
import ch.supsi.WhoIsRetriver;

import javax.swing.*;

import ch.supsi.Main;        


public class Gui extends JPanel implements ActionListener  {

	private static final long serialVersionUID = 1L;
	
	protected JTextField textField;
	protected JTextArea textArea;
	private String startUrl;
	private final static String newline = "\n";
	private Boolean isEmailSearchActive = true;
	private Boolean isSiteSearchActive = true;
	private Boolean isWhoIsActive = true;
	String time;


	private void addComponentsToPane(Container pane){

		//Header
		BorderLayout headerLayout = new BorderLayout();
		JPanel headerPanel = new JPanel(headerLayout);
		pane.add(headerPanel, BorderLayout.PAGE_START);
		//		
		//Search Panel	
		FlowLayout searchPaneLayout = new FlowLayout();
		JPanel searchPanel = new JPanel(searchPaneLayout);
		headerPanel.add(searchPanel,  BorderLayout.PAGE_START);
		//		
		textField = new JTextField(35);
		textField.addActionListener(this);
		textField.setText(startUrl);
		searchPanel.add(textField);

		JButton button = new JButton("Search");
		searchPanel.add(button);
		button.setActionCommand("SEARCH");
		button.addActionListener(this);

		//Option Panel
		FlowLayout optionLayout = new FlowLayout();
		JPanel optionPanel = new JPanel(optionLayout);
		headerPanel.add(optionPanel,  BorderLayout.CENTER);

		JCheckBox emailCheck;
		JCheckBox websiteCheck;
		JCheckBox whoisCheck;

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

		optionPanel.add(emailCheck);
		optionPanel.add(websiteCheck);
		optionPanel.add(whoisCheck);

		//Text Area
		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		pane.add(scrollPane, BorderLayout.CENTER);

		JLabel empty = new JLabel("   ");
		JLabel empty1 = new JLabel("   ");
		JLabel empty2 = new JLabel("   ");

		pane.add(empty1, BorderLayout.LINE_START);
		pane.add(empty2, BorderLayout.LINE_END);
		pane.add(empty, BorderLayout.PAGE_END);
	}

	private void createAndShowGUI() {

		//Create and set up the window.
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setTitle("Crawler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addComponentsToPane(frame.getContentPane());

		// Display the window.
		//frame.pack();
		frame.setSize(550, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		

		if (evt.getActionCommand() == "SEARCH") {
			search();
		} else if(evt.getActionCommand() == "SITE"){
			if(isSiteSearchActive){
				isSiteSearchActive = false;
			}else{
				isSiteSearchActive = true;
			}
			System.out.println("site "+isSiteSearchActive);
		}else if(evt.getActionCommand() == "EMAIL"){
			if(isEmailSearchActive){
				isEmailSearchActive = false;
			}else{
				isEmailSearchActive = true;
			}
			System.out.println("email "+isEmailSearchActive);
		}else if(evt.getActionCommand() == "WHOIS"){
			if(isWhoIsActive){
				isWhoIsActive = false;
			}else{
				isWhoIsActive = true;
			}
			System.out.println("whois "+isWhoIsActive);
			}
	}

	private void search(){
		
		String site = textField.getText();
		IPRetriver ipr = new IPRetriver();
		ipr.getIP(site);
		
//		try {
//			//Reset DataBase before new search
//			Main.db.runSql2("TRUNCATE Record;");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		if(isSiteSearchActive){
			searchSite();
		}
		if(isEmailSearchActive){
			searchEmail();
		}if(isWhoIsActive){
			searchWhoIs();
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
			//System.out.println("THE DOCTOR: "+rs.getString(1));
			String ipAdress = "SELECT IP FROM `crawlerDB`.`IPadress` where idIPadress = "+rs.getString(1);
			
			ResultSet rs2 = Main.db.runSql(ipAdress);
			rs2.next();
			//System.out.println("THE DOCTOR WHO: "+rs2.getString(1));
			WhoIsRetriver DrWho = new WhoIsRetriver();
			DrWho.getWhois(rs2.getString(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchEmail(){
		String site = textField.getText();

		try {
			MailCrawler mailCrawler = new MailCrawler();
			mailCrawler.processPage(site);

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		textArea.append(site+ newline);
		//		Getting the result sites form the db and append them to the text area
		String sql = "select * from Record";
		try {
			ResultSet rs = Main.db.runSql(sql);
			while(rs.next()){
				//	String t =  rs.getString("URL");
				textArea.append(rs.getString("URL")+ newline);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//textField.selectAll();
		//Make sure the new text is visible, even if there
		//was a selection in the text area.
		textArea.setCaretPosition(textArea.getDocument().getLength());	
	}
	private void searchSite(){
		
		//Time started the seach for DB 
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		time = sdf.format(cal.getTime());
		String site = textField.getText();

		
		try {
			SiteCrawler siteCrawler = new SiteCrawler();
			siteCrawler.setRecursiveOn(100);
			siteCrawler.processPage(site,time);

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		//fist site
		//textArea.append(site+ newline);

		//		Getting the result sites form the db and append them to the text area
		String sql = "select * from Record where dateSearch like '"+time+"'";;
		try {
			ResultSet rs = Main.db.runSql(sql);
			while(rs.next()){
				//	String t =  rs.getString("URL");
				textArea.append(rs.getString("URL")+ newline);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textArea.setCaretPosition(textArea.getDocument().getLength());
		System.out.println("search finished");
	}

	public Gui(String URL){
		super(new GridBagLayout());
		this.startUrl = URL;
		//Schedule a job for the event-dispatching thread:

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
