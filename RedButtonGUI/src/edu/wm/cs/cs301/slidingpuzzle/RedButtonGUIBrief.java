package edu.wm.cs.cs301.slidingpuzzle; 
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 

/**
 * This class contains a trivial GUI that extends from the classic Hello World example.
 * Its main purpose is to demo various concepts of Java.
 * Note that the huge amount of comments are for the sole purpose of explaining Java features,
 * this is in no way representative of regular code documentation
 * 
 * @author Kemper
 *
 */
class RedButtonGUIBrief extends JPanel {

	public static void main(String[] args) {
		System.out.println("Hello World!") ;
		
		if (0 < args.length) {
			for (int i = 0 ; i < args.length ; i++) {
				String str = i + "-th command line parameter: " + args[i] ;
				System.out.println(str);
			}
			int i = 0 ; 
			while (i < args.length) {
				System.out.println(i + "-th command line parameter: " + args[i]);
				i++ ; 
			}
			i = 0 ; 
			do {
				System.out.println(i + "-th command line parameter: " + args[i]);
				i = i + 1 ;
			} while(i < args.length) ;
		}
		else {
			System.out.println("No parameters given") ;
		}
		
		if (0 < args.length && ("red".equals(args[0]) || "green".equals(args[0]))) {
			final JFrame window = new JFrame("The Button Frame");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			RedButtonGUIBrief gui = new RedButtonGUIBrief() ;
			gui.setColor(args[0]) ;
			window.setContentPane(gui);
			window.pack(); 
			boolean b = true ; 
			window.setVisible(b);  
			window.setResizable(true);
		}
	}
	
	private Color color ; // a reference to a Color object in order to memorize the color setting of our button
	private JButton button ; // a reference to a Button object that this class puts on the screen
	private int count = 0; // a counter that remembers how often our button was clicked, it is initialized to zero
	
	/**
	 * Constructor creates graphic component, a button and adds it to the JPanel this class inherits from.
	 */
	public RedButtonGUIBrief() {
		button = new JButton();
		ActionListener listener = new Listener(); 
		button.addActionListener(listener); 
		add(button) ;
		color = Color.CYAN ; 
	}
	public void setColor(String string) {
		if ("red".equals(string)) {
			color = Color.RED ; 
			button.setText("RED BUTTON");
		}
		else if (string.equals("green")) {
			color = Color.GREEN ; 
			button.setText("GREEN BUTTON");
		}
		else {
			color = Color.CYAN ;
			button.setText("DEFAULT BUTTON");
		}
		button.setForeground(color);
	}
	/**
	 * Internal class to set up a listener for a button.
	 */
	private class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button) {
				if (color.equals(Color.RED)) 
					redButtonResponse(); 
				else 
					System.out.println("Only a red button matters to me ...");
			}
		}
		private void redButtonResponse() {
			switch(count) {
			case 0 : 
				System.out.println("Please do not press this button again!"); 
				count++; 
				break; 
			case 1: 
				System.out.println("Sure, you are one of those guys who couldn't resist!");
				count++;
				break;
			case 2:
				System.out.println("Still pressing the red button?");
				System.out.println("One more time and you will execute a call for /bin/rm -rf ~ in the background!");
				count++;
				break;
			case 3: 
				System.out.print("You are really not easy to scare ");
			case 4:
			case 5: 
				System.out.println("... but isn't this getting boring after a while?");
				count++;
				break;
			default: 
				System.out.println("Why don't you check the origin of this at\n https://www.youtube.com/watch?feature=player_detailpage&v=kLC8qPNU6_0");
				System.out.println("Thank you for making a simple program really happy, good bye!");
				System.exit(ABORT); 
				break; 
			}
		}
	} 
}