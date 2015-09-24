package ch.supsi.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ScytheEditGUI extends JFrame implements ActionListener{

	/**
	 * serialVersionUID auto generated
	 */
	private static final long serialVersionUID = 2168210072089451317L;
	private static final int windowsX = 500;
	private static final int windowsY = 500;
	private JTextArea textArea;
	private int xdim = 200;
	private int ydim = 100;
	private String newline = "\n";

	public ScytheEditGUI(){
		super("Account.txt");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addComponentsToPane(getContentPane());
		//add(new JLabel("Empty JFrame"));
		pack();
		// Display the window.
		setSize(windowsX, windowsY);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private void addComponentsToPane(Container pane) {
		BorderLayout scyntheLayout = new BorderLayout();
		JPanel scynthePanel = new JPanel(scyntheLayout);

		textArea = new JTextArea(5, 20);
		textArea.setEditable(true);
		JScrollPane scrollPaneScythe  = new JScrollPane(textArea);
		scrollPaneScythe.setPreferredSize(new Dimension(xdim, ydim));
		scynthePanel.add(scrollPaneScythe, BorderLayout.CENTER);

		FlowLayout saveLineLayout = new FlowLayout(FlowLayout.RIGHT);
		JPanel saveLinePanel = new JPanel(saveLineLayout);
		JButton save = new JButton("save");
		save.setActionCommand("SAVE");
		save.addActionListener(this);
		
		JButton saveAndExit = new JButton("save and exit");
		saveAndExit.setActionCommand("SAVEANDEXIT");
		saveAndExit.addActionListener(this);
		
		saveLinePanel.add(save);
		saveLinePanel.add(saveAndExit);
		
		scynthePanel.add(saveLinePanel, BorderLayout.PAGE_END);
		add(scynthePanel);
		try {
			getDataOnAccountFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDataOnAccountFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("accountfile.txt"));
		// StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			textArea.append(line);
			textArea.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "SAVE") {
			save();
		}else if(e.getActionCommand() == "SAVEANDEXIT"){
			saveandExit();
		}
	}
	private void saveandExit() {
		save();
		dispose();
	}
	
	private void save() {
		try {

			String content = textArea.getText();
			File file = new File("filename.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}