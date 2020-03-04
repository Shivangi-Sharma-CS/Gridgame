import javax.swing.*;
import java.awt.*;

/**
 * Main program entry point
 */
public class DriverGUI {

    public static void main(String [] args) {

        JFrame frame = new JFrame("Grid Game");

        GridRenderer renderer = new GridRenderer();

        frame.add(renderer);

        frame.setSize(new Dimension(NestedGrid.MAX_SIZE, NestedGrid.MAX_SIZE+132));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}


/**
 * this creates the frame for the program. It initializes the GridRenderer class and adds that object to the frame
 *
 * the frame size has a dimension that is taken from the maximum size of the Nested Grid and the height is maximum size of the nested grid + 132
 */