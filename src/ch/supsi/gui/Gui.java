package ch.supsi.gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import ch.supsi.SiteCrawler;

import javax.swing.*;

import ch.supsi.Main;        


public class Gui extends JPanel implements ActionListener  {

	protected JTextField textField;
	protected JTextArea textArea;
	private String startUrl;
	private final static String newline = "\n";

	
	private void addComponentsToPane(Container pane){
		
		BorderLayout headerLayout = new BorderLayout();
		JPanel headerPanel = new JPanel(headerLayout);
		
			
		FlowLayout searchPaneLayout = new FlowLayout();
		JPanel searchPanel = new JPanel(searchPaneLayout);
		pane.add(searchPanel,  BorderLayout.PAGE_START);
		
		textField = new JTextField(35);
		textField.addActionListener(this);
		textField.setText(startUrl);
		searchPanel.add(textField);
		
		
		JButton button = new JButton("Search");
        searchPanel.add(button);
        button.addActionListener(this);
        
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
		String site = textField.getText();
		
		try {
			//Reset DataBase before new search
			Main.db.runSql2("TRUNCATE Record;");
			
			SiteCrawler siteCrawler = new SiteCrawler(Main.db);
			siteCrawler.setRecursiveOn(100);
			siteCrawler.processPage(site);
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		textArea.append(site+ newline);
		
//		Getting the result sites form teh db and append them to the text area
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
