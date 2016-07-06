package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class TrickBoard extends JPanel 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private JLabel title;
	private JLabel team1;
	private JLabel team2;
	private JLabel trickLabel1;
	private JLabel trickLabel2;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public TrickBoard(int panelSize)
	{
		super();
		
		setSize(panelSize, panelSize);
		setLocation(750, 400);
		setVisible(true);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
        
        title = new JLabel("Tricks Taken");
        constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(title, constraints);
		add(title);
        
		team1 = new JLabel("Team 1");
        constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(team1, constraints);
		add(team1);
		
		trickLabel1 = new JLabel("");
        constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(trickLabel1, constraints);
		add(trickLabel1);
		
		team2 = new JLabel("Team 2");
        constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(team2, constraints);
		add(team2);
		
		trickLabel2 = new JLabel("");
        constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(trickLabel2, constraints);
		add(trickLabel2);
		
		UpdateTricks(0, 0);
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public void UpdateTricks(int tricks1, int tricks2)
	{
		trickLabel1.setText(""+tricks1);
		trickLabel2.setText(""+tricks2);
	}
}
