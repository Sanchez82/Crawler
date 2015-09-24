package ch.supsi.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		saveLinePanel.add(save);
		
		
		scynthePanel.add(saveLinePanel, BorderLayout.PAGE_END);
		
		add(scynthePanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "SAVE") {
			save();
		}
	}

	private void save() {
		// TODO Auto-generated method stub
		
	}
}