import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

// Creates the Cartesian plane & handles images

public class CirclePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Image variables

	String imageName = "Untitled";
	BufferedImage image; // Current drawing
	BufferedImage grid_image = new BufferedImage(500, 500, // Grid
			BufferedImage.TYPE_INT_ARGB);

	// Numeric variables

	int margin = 20; // Room around the edge of the drawing to leave blank
	int defaultRadius = 200; // Radius of the unit circle
	int grid_spacing = 20; // Space between grid lines
	boolean showGrid = false;
	int prevMouseX = 0; // Keep track of mouse position for drawing lines
	int prevMouseY = 0;

	// Graphics-related variables

	Stroke regularStroke = new BasicStroke(2, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND); // Used for normal drawing, axes
	Stroke gridStroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND); // Used for grid
	Stroke flatStroke = new BasicStroke(2, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_ROUND); // Used for drawing functions

	// Constructor: sets panel size, initializes variables.

	public CirclePanel() {
		setPreferredSize(new Dimension(500, 500));
		drawGrid();
		createNewImage();
	}

	// Make blank image (erases any previous drawing)

	public void createNewImage() {
		image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
	}

	// Draws grid for future use

	public void drawGrid() {
		Graphics2D g2d = grid_image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(gridStroke);
		g2d.setColor(Color.lightGray);
		for (int i = 0; i < (250 - margin) / grid_spacing + 1; i++) {
			g2d.drawLine(250 + i * grid_spacing, margin,
					250 + i * grid_spacing, 500 - margin);
			g2d.drawLine(margin, 250 + i * grid_spacing, 500 - margin, 250 + i
					* grid_spacing);
			if (i != 0) {
				g2d.drawLine(250 - i * grid_spacing, margin, 250 - i
						* grid_spacing, 500 - margin);
				g2d.drawLine(margin, 250 - i * grid_spacing, 500 - margin, 250
						- i * grid_spacing);
			}
		}
		g2d.dispose();
	}

	// Draws circle

	public void drawCircle() {
		createNewImage();
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);
		g2d.setColor(Color.black);
		g2d.drawOval(250 - defaultRadius, 250 - defaultRadius,
				defaultRadius * 2, defaultRadius * 2);
		g2d.dispose();
	}

	// Draws square

	public void drawSquare() {
		drawPolygon(4);
	}

	// Draws polygon

	public void drawPolygon(int sides) {
		if (sides == 0) { // Handle wrong user input
			return;
		}
		createNewImage();
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);
		g2d.setColor(Color.black);

		double offset = 3 * Math.PI / 2; // Make bottom side horizontal
		if (sides % 2 == 0) { // Make left & right sides vertical
			offset = Math.PI / sides;
		}
		double step = Math.PI * 2 / sides; // Angle between vertices
		double x, y, px, py;
		x = Math.cos(offset - step) * defaultRadius + 250;
		y = Math.sin(offset - step) * defaultRadius + 250;
		for (int i = 0; i < sides; i++) {
			px = x;
			py = y;
			x = Math.cos(step * i + offset) * defaultRadius + 250;
			y = Math.sin(step * i + offset) * defaultRadius + 250;
			g2d.drawLine((int) Math.round(x), (int) Math.round(y),
					(int) Math.round(px), (int) Math.round(py));
		}
		g2d.dispose();
	}

	// Draws single point (when mouse pressed)

	public void drawPoint(int x, int y) {
		prevMouseX = x;
		prevMouseY = y;
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);
		g2d.setClip(margin, margin, 500 - margin * 2, 500 * margin * 2);
		g2d.setColor(Color.black);
		g2d.drawRect(x, y, 0, 0);
		g2d.dispose();
	}

	// Draws line (when mouse dragged)

	public void drawLine(int x, int y) {
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);
		g2d.setClip(margin, margin, 500 - margin * 2, 500 - margin * 2);
		g2d.setColor(Color.black);
		g2d.drawLine(x, y, prevMouseX, prevMouseY);
		g2d.dispose();
		prevMouseX = x;
		prevMouseY = y;
	}

	// Creates images of sine and cosine functions

	public BufferedImage[] calculateFunctions() {
		
		// Create new image for both sine and cosine
		
		BufferedImage sine = new BufferedImage(500, 500,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage cosine = new BufferedImage(500, 500,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D gs = sine.createGraphics();
		Graphics2D gc = cosine.createGraphics();
		gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gs.setStroke(flatStroke);
		gs.setColor(Color.black);
		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gc.setStroke(flatStroke);
		gc.setColor(Color.black);
		gs.setClip(margin, margin, 500 - margin * 2, 250 - margin * 2);
		gc.setClip(margin, margin, 500 - margin * 2, 250 - margin * 2);

		// Loop through angles
		
		double step = -Math.PI / (250 - margin);
		for (int i = 0; i < 500 - margin * 2; i++) {
			double theta = step * i;
			double x = 0;
			double y = 0;
			int original = image.getRGB(250, 250);
			boolean intersects = true;
			do {
				x += Math.cos(theta); // Increment x
				y += Math.sin(theta); // Increment y
				if (x < -240 || x > 240 || y < -240 || y > 240) {
					intersects = false; // No point exists at this angle
					break;
				}
			} while (image.getRGB((int) x + 250, (int) y + 250) == original);

			// Draw point if color is different than at center
			
			if (intersects) {
				gs.drawRect(i + margin, (int) (y * 100 / defaultRadius) + 125,
						1, 1);
				gc.drawRect(i + margin, (int) (-x * 100 / defaultRadius) + 125,
						1, 1);
			}
		}
		
		// Finish
		
		gs.dispose();
		gc.dispose();
		return new BufferedImage[] { sine, cosine };
	}

	// Draws current image

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(regularStroke);

		// Draw unit circle in light gray for reference

		g2d.setColor(Color.lightGray);
		g2d.drawOval(250 - defaultRadius, 250 - defaultRadius,
				defaultRadius * 2, defaultRadius * 2);

		// Draw grid

		if (showGrid) {
			g2d.drawImage(grid_image, 0, 0, null);
		}

		// Draw image

		g2d.setColor(Color.black);
		g2d.setClip(margin, margin, 500 - margin * 2, 500 - margin * 2);
		g2d.drawImage(image, 0, 0, null);

		// Draw axes

		g2d.setClip(0, 0, 500, 500);
		g2d.drawLine(margin, 250, 500 - margin, 250);
		g2d.drawLine(250, margin, 250, 500 - margin);
	}

}
