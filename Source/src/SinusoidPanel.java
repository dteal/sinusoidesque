import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

// Displays sine or cosine function

public class SinusoidPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Image variables

	BufferedImage axes_image = new BufferedImage(500, 250,
			BufferedImage.TYPE_INT_ARGB);
	BufferedImage function_image;

	// Numeric variables

	int margin = 20; // Room around the edge of the drawing to leave blank
	int tickRadius = 10; // Half the height of a tick mark on x-axis

	// Graphics-related variables

	Stroke regularStroke = new BasicStroke(2, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);
	Stroke flatStroke = new BasicStroke(2, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_ROUND);

	// Constructor: sets panel size, initializes variables

	public SinusoidPanel() {
		setPreferredSize(new Dimension(500, 250));
		createAxes();
		createNewImage();
	}

	// Clears current picture of sinusoidal function

	public void createNewImage() {
		function_image = new BufferedImage(500, 250,
				BufferedImage.TYPE_INT_ARGB);
	}

	// Draws axes for future use

	public void createAxes() {
		Graphics2D g2d = axes_image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);
		g2d.setColor(Color.black);
		g2d.drawLine(margin, margin, margin, 250 - margin); // Y axis (x = 0)
		g2d.drawLine((250 - margin) / 2 + margin, 125 - tickRadius,
				(250 - margin) / 2 + margin, 125 + tickRadius); // x = PI/2
		g2d.drawLine(250, 125 - tickRadius, 250, 125 + tickRadius); // x = PI
		g2d.drawLine(250 + (250 - margin) / 2, 125 - tickRadius,
				250 + (250 - margin) / 2, 125 + tickRadius); // x = PI*3/2
		g2d.drawLine(500 - margin, 125 - tickRadius, 500 - margin,
				125 + tickRadius); // x = 2*PI
		g2d.setStroke(flatStroke);
		g2d.drawLine(margin, 125, 500 - margin, 125);
		g2d.dispose();
	}

	// Returns the image of the current sinusoidal function.

	public BufferedImage getImage() {
		BufferedImage result = new BufferedImage(500, 250,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = result.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(function_image, 0, 0, null);
		g2d.drawImage(axes_image, 0, 0, null);
		g2d.dispose();
		return result;
	}

	// Draws current image

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);

		// Draw sinusoidal function

		g2d.setClip(margin, margin, 500 - margin, 500 - margin);
		g2d.drawImage(function_image, 0, 0, null);

		// Draw axes

		g2d.setClip(0, 0, 500, 500);
		g2d.drawImage(axes_image, 0, 0, null);
	}

}
