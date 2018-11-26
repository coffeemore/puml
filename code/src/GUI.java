import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * 
 * @author developer
 * Klasse für die grafische Oberfläche
 */
public class GUI
{
    private JFrame frame;

    /**
     * Konstruktor
     */
    GUI()
    {
	initialize();
    }

    /**
     * Zeigt die Grafische Oberlfäche an
     */
    void showGUI()
    {
	// Startet grafische Oberfläche
	EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		try
		{
		    GUI window = new GUI();
		    window.frame.setVisible(true);
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }// endmethod run
	});
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
	frame = new JFrame();
	frame.setBounds(100, 100, 450, 300);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	JButton btnNewButton = new JButton("New button");
	btnNewButton.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent arg0)
	    {
		System.out.println("Button pressed");
		PUMLgenerator.parser.parse("MySourceCode");
		// System.out.println(PUMLgenerator.outputPUML.getPUML());
	    }
	});
	frame.getContentPane().add(btnNewButton, BorderLayout.CENTER);
    }
}
