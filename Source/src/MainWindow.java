import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

// Main class: handles GUI, user interaction.

public class MainWindow extends JFrame implements ActionListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	// Declare menu bar items

	JMenuBar menubar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem file_new = new JMenuItem("New");
	JMenuItem file_open = new JMenuItem("Open...");
	JMenuItem file_save = new JMenuItem("Save Shape...");
	JMenuItem file_save2 = new JMenuItem("Save Sine...");
	JMenuItem file_save3 = new JMenuItem("Save Cosine...");
	JMenuItem file_exit = new JMenuItem("Exit");
	JMenu edit = new JMenu("Edit");
	JMenuItem edit_circle = new JMenuItem("Draw Circle");
	JMenuItem edit_square = new JMenuItem("Draw Square");
	JMenuItem edit_polygon = new JMenuItem("Draw Polygon...");
	JMenuItem edit_clear = new JMenuItem("Clear");
	JMenu view = new JMenu("View");
	JCheckBoxMenuItem view_grid = new JCheckBoxMenuItem("Show Grid");
	JMenu help = new JMenu("Help");
	JMenuItem help_viewhelp = new JMenuItem("View Help");
	JMenuItem help_about = new JMenuItem("About Sinusoidesque");

	// Keep track of current file directory for ease of use

	String currentDirectory = null;

	// Where to draw things

	CirclePanel cpanel;
	SinusoidPanel sine;
	SinusoidPanel cosine;

	// Constructor: handles GUI initialization

	public MainWindow() {

		// Set up window, add icon

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainWindow.class.getResource("sinewave_icon.png")));

		// Set up menu bar

		file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_DOWN_MASK));
		file_new.addActionListener(this);
		file.add(file_new);
		file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_DOWN_MASK));
		file_open.addActionListener(this);
		file.add(file_open);
		file.addSeparator();
		file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK));
		file_save.addActionListener(this);
		file.add(file_save);
		file_save2.addActionListener(this);
		file.add(file_save2);
		file_save3.addActionListener(this);
		file.add(file_save3);
		file.addSeparator();
		file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				KeyEvent.CTRL_DOWN_MASK));
		file_exit.addActionListener(this);
		file.add(file_exit);
		menubar.add(file);
		edit_circle.addActionListener(this);
		edit.add(edit_circle);
		edit_square.addActionListener(this);
		edit.add(edit_square);
		edit_polygon.addActionListener(this);
		edit.add(edit_polygon);
		edit.addSeparator();
		edit_clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
				KeyEvent.CTRL_DOWN_MASK));
		edit_clear.addActionListener(this);
		edit.add(edit_clear);
		menubar.add(edit);
		view_grid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				KeyEvent.CTRL_DOWN_MASK));
		view_grid.addActionListener(this);
		view.add(view_grid);
		menubar.add(view);
		help_viewhelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				KeyEvent.CTRL_DOWN_MASK));
		help_viewhelp.addActionListener(this);
		help.add(help_viewhelp);
		help.addSeparator();
		help_about.addActionListener(this);
		help.add(help_about);
		menubar.add(help);
		setJMenuBar(menubar);

		// Add and arrange panels

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		cpanel = new CirclePanel();
		cpanel.setBorder(BorderFactory.createTitledBorder("Unit Shape"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 2;
		cpanel.addMouseListener(this);
		cpanel.addMouseMotionListener(this);
		add(cpanel, c);

		sine = new SinusoidPanel();
		sine.setBorder(BorderFactory
				.createTitledBorder("Resulting Sine Function"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 1;
		add(sine, c);

		cosine = new SinusoidPanel();
		cosine.setBorder(BorderFactory
				.createTitledBorder("Resulting Cosine Function"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		add(cosine, c);

		// Finish creating window

		setTitle(cpanel.imageName + " - Sinusoidesque");
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// Refreshes sine / cosine function displays

	public void displayFunctions() {
		BufferedImage[] pics = cpanel.calculateFunctions();
		sine.function_image = pics[0];
		cosine.function_image = pics[1];
	}

	// Handles menu bar item actions

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == file_new) { // Create new drawing
			cpanel.createNewImage();
			sine.createNewImage();
			cosine.createNewImage();
			cpanel.imageName = "Untitled";
			setTitle(cpanel.imageName + " - Sinusoidesque");
			repaint();
		}
		if (e.getSource() == file_open) { // Open image
			BufferedImage img = open();
			if (img != null) {
				cpanel.image = img;
				displayFunctions();
				repaint();
			}
		}
		if (e.getSource() == file_save) { // Save main image
			save(cpanel.image, true);
		}
		if (e.getSource() == file_save2) { // Save sine function
			save(sine.getImage(), false);
		}
		if (e.getSource() == file_save3) { // Save cosine function
			save(cosine.getImage(), false);
		}
		if (e.getSource() == file_exit) { // Close program
			System.exit(0);
		}
		if (e.getSource() == edit_circle) { // Draw circle
			cpanel.drawCircle();
			displayFunctions();
			repaint();
		}
		if (e.getSource() == edit_square) { // Draw square
			cpanel.drawSquare();
			displayFunctions();
			repaint();
		}
		if (e.getSource() == edit_polygon) { // Draw polygon
			cpanel.drawPolygon(getInput(3, "Number of sides (2 < x < 101) ?"));
			displayFunctions();
			repaint();
		}
		if (e.getSource() == edit_clear) { // Clear everything
			cpanel.createNewImage();
			sine.createNewImage();
			cosine.createNewImage();
			repaint();
		}
		if (e.getSource() == view_grid) { // Toggle grid
			cpanel.showGrid = view_grid.getState();
			repaint();
		}
		if (e.getSource() == help_viewhelp) { // Show help
			showHelpDialog();
		}
		if (e.getSource() == help_about) { // Show about info
			showAboutDialog();
		}
	}

	// Asks user for number of polygon sides. Returns 0 if invalid.

	public int getInput(int originalValue, String message) {

		// Show dialog

		String code = JOptionPane.showInputDialog(null, message, originalValue);

		// Handle returned value

		if (code != null) {
			try {
				originalValue = Integer.parseInt(code);
				if (originalValue < 3) { // A triangle is the smallest possible
											// polygon
					JOptionPane.showMessageDialog(this,
							"Too few sides for a polygon!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return 0;
				}
				if (originalValue > 100) { // There's really no need to have a
											// polygon with > 100 sides
					JOptionPane.showMessageDialog(this, "Too many sides!",
							"Error", JOptionPane.ERROR_MESSAGE);
					return 0;
				}
				return originalValue;
			} catch (NumberFormatException n) { // Catch all non-numeric inputs
				JOptionPane.showMessageDialog(this, "Not a valid number!",
						"Error", JOptionPane.ERROR_MESSAGE);
				return 0;
			}
		} else {
			return 0;
		}
	}

	// Displays help information to the user

	public void showHelpDialog() {
		String helpText = "This program calculates alternative sine and cosine functions.\n"
				+ "Draw any figure in the cartesian plane on the right. The resulting\n"
				+ "\"trigonometric\" functions, calculating using the nearest point to\n"
				+ "the origin at any angle, will appear on the right.\n"
				+ "\n"
				+ "A selection of simple figures appears under the EDIT menu. More\n"
				+ "complex figures (drawn as a black outline) can be imported as\n"
				+ "an image. The PNG image file format is recommended.\n\n";
		JOptionPane.showMessageDialog(this, helpText, "Help",
				JOptionPane.QUESTION_MESSAGE);
	}

	// Displays information about the program to the user

	public void showAboutDialog() {
		String aboutText = "Sinusoidesque v1.0\n"
				+ "Created by Daniel Teal - Fall 2014\n"
				+ "Logic, Reasoning, and Proof by Charlie Barnes\n"
				+ "\n"
				+ "Mr. Barnes personally believes the definitions of sine and cosine\n"
				+ "were probably arbitrary but serendipitous inventions. He thinks \n"
				+ "some bored Greek kid was just cooking up strange functions for fun\n"
				+ "one day around 250 BC, and sine and cosine happened to be two of\n"
				+ "them. It just so happened they turned out to be beautiful and so\n"
				+ "important as to be utterly inescapable in math and physics.\n"
				+ "\n"
				+ "What if, instead of looking at the coordintates of intersections\n"
				+ "on the unit circle, we looked at intersections on the unit square?\n"
				+ "Or the shape of a sunflower?\n\n";
		JOptionPane.showMessageDialog(this, aboutText, "About Sinusoidesque",
				JOptionPane.PLAIN_MESSAGE);
	}

	// Saves an image to file

	public void save(BufferedImage img, boolean changeFilename) {

		// Display file choosing dialog

		JFileChooser jfc = new JFileChooser(currentDirectory);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(new ImageFileFilter());
		int result = jfc.showSaveDialog(this);

		// Handle result

		if (result == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File file = jfc.getSelectedFile();
		try {
			String formatname = new String(file.getName().substring(
					file.getName().lastIndexOf(".") + 1));

			// Remove transparency for JPG and BMP files

			if (formatname.equalsIgnoreCase("jpg")
					|| formatname.equalsIgnoreCase("jpeg")
					|| formatname.equalsIgnoreCase("bmp")) {
				BufferedImage opaqueImage = new BufferedImage(img.getWidth(),
						img.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = opaqueImage.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.white);
				g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
				g2d.drawImage(img, 0, 0, null);
				g2d.dispose();
				img = opaqueImage;
			}

			// Write image to file

			ImageIO.write(img, formatname, new File(file.getPath()));

			// Change window name if saving main image

			if (changeFilename) {
				cpanel.imageName = file.getName().substring(0,
						file.getName().lastIndexOf("."));
				setTitle(cpanel.imageName + " - Sinusoidesque");
			}

			// Keep track of current directory

			currentDirectory = file.getAbsolutePath().substring(0,
					file.getAbsolutePath().lastIndexOf("\\") + 1);
		} catch (Exception e) { // Handle errors
			JOptionPane.showMessageDialog(this, "Could not save file.",
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// Opens an image file

	public BufferedImage open() {

		// Display file choosing dialog

		BufferedImage img;
		JFileChooser jfc = new JFileChooser(currentDirectory);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(new ImageFileFilter());
		int result = jfc.showOpenDialog(this);

		// Handle result

		if (result == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		File file = jfc.getSelectedFile();
		try {
			img = ImageIO.read(file);

			// Scale image to fit

			int width = img.getWidth();
			int height = img.getHeight();
			BufferedImage finalImage = new BufferedImage(500, 500,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = finalImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			if (height > width) {
				g2d.drawImage(img, (500 - 500 * width / height) / 2, 0, 500
						* width / height, 500, null);
			} else {
				g2d.drawImage(img, 0, (500 - 500 * height / width) / 2, 500,
						500 * height / width, null);
			}
			g2d.dispose();

			// Change window name

			cpanel.imageName = file.getName().substring(0,
					file.getName().lastIndexOf("."));
			setTitle(cpanel.imageName + " - Sinusoidesque");

			// Keep track of current directory

			currentDirectory = file.getAbsolutePath().substring(0,
					file.getAbsolutePath().lastIndexOf("\\") + 1);
			return finalImage;
		} catch (Exception e) { // Handle errors
			JOptionPane.showMessageDialog(this, "Could not open file.",
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}

	// Allows open/save dialogs to select only image files

	public class ImageFileFilter extends FileFilter {

		private final String[] okFileExtensions = new String[] { "jpg", "jpeg",
				"png", "gif", "bmp" };

		public boolean accept(File file) {
			for (String extension : okFileExtensions) { // Images are OK
				if (file.getName().toLowerCase().endsWith(extension)) {
					return true;
				}
			}
			if (file.isDirectory()) { // It helps the user to see folders
				return true;
			}
			return false;
		}

		public String getDescription() {
			return "*.bmp, *.gif, *.jpeg, *.png";
		}

	}

	// Main method. Entry point of program.

	public static void main(String[] args) {

		// Set "look-and-feel" of GUI to system default

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Create one new window

		new MainWindow();
	}

	// Handle mouse movement for drawing

	// Start line on mouse press

	public void mousePressed(MouseEvent e) {
		cpanel.drawPoint(e.getX(), e.getY());
		displayFunctions();
		repaint();
	}

	// Continue line when mouse dragged

	public void mouseDragged(MouseEvent e) {
		cpanel.drawLine(e.getX(), e.getY());
		displayFunctions();
		repaint();
	}

	// Extra functions required to use mouse interface

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
