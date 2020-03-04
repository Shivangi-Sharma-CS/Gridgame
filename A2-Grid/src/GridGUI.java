import javax.swing.*;
import java.awt.*;

/**
 * The game board
 *
 * Do not make changes in this file
 */
public class GridGUI extends JComponent {

    private Rectangle[] rectangles;

    public GridGUI(int width, int height) {
        super();
        setSize(width, height);
    }

    //update the squares and redraw
    public void updateDrawing(Rectangle[] rects) {
        rectangles = rects;
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(rectangles != null) {

            Rectangle selected = rectangles[0];
            for(Rectangle r : rectangles) {

                Color clr = r.getColor();

                if(r.isVisible()) {
                    g.setColor(clr);

                    int x = r.getX();
                    int y = r.getY();
                    g.fillRect(x, y, r.getSize(), r.getSize());
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, r.getSize(), r.getSize());
                }
                if(r.isSelected()) {
                    selected = r;
                }
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.YELLOW);
            g2.drawRect(selected.getX(), selected.getY(), selected.getSize(), selected.getSize());

        }
    }
}
