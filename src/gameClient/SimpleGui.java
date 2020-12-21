package gameClient;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


/**
 * This class represents a simple login window.
 */
public class SimpleGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton buttonPress = new JButton("Press");
	private JTextField inputText = new JTextField("",5);
	private JLabel label = new JLabel("  ID:");
	private JLabel label2 = new JLabel("  Scenario:");
	private JTextField inputText2 = new JTextField("",5);
	private long id = 0;
	private int scenario = 0;
	private boolean succeed;
	
	
	/**
	 * return the id of the user
	 * @return	long
	 */
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Return the chosen scenario
	 * @return int
	 */
	public int getScenario() {
		return scenario;
	}

	public void setScenario(int scenario) {
		this.scenario = scenario;
	}

	/**
	 * default constructor
	 */
	public SimpleGui()
	{
		super("Login window");
		this.setBounds(200,200,300,250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container window = this.getContentPane();
		window.setBackground(Color.white);
	    window.setLayout(new GridLayout(5,2));
		window.add(label);
		window.add(inputText);
		window.add(label2);
		window.add(inputText2);		
		buttonPress.addActionListener(this);
		window.add(buttonPress);		
		this.succeed = false;
	}
	
	/** check if the userr pressed the button
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==buttonPress)
		{		
			if(inputText.getText().length()>0 && inputText2.getText().length()>0)
			{
				//The source of .matches(".*[a-z].*")-
				//https://stackoverflow.com/questions/24086968/tell-if-string-contains-a-z-chars/24087063
				if(inputText.getText().matches(".*[a-z].*") ||inputText2.getText().matches(".*[a-z].*") )
				{
					String message = "You entered worng, Please write again and enter right this time!";
					JOptionPane.showMessageDialog(null, message, "Output Message",
		    	                          JOptionPane.PLAIN_MESSAGE);
				}
				else
				{
					id = Long.parseLong(inputText.getText());
					System.out.println("ID: "+id);			
					scenario = Integer.parseInt(inputText2.getText());
					System.out.println("Scenario: "+scenario);
					this.succeed = true;				
					super.dispose();
				}	
			}
			else
			{
				String message = "You entered worng, Please write again and enter right";
				JOptionPane.showMessageDialog(null, message, "Output Message",
	    	                          JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}
}
