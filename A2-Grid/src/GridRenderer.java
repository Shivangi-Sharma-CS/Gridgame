import javax.swing.*;
import java.awt.*;

//class to draw the Grid onto the Screen
//DO NOT MAKE CHANGES IN THIS FILE
public class GridRenderer extends JPanel implements GridController.Updater {

    JLabel scoreField;
    JLabel scoreLabel;

    JPanel northPanel;
    JPanel southPanel;

    GridGUI gameBoard;

    GridController controller;

    public GridRenderer() {
        super();

        setFocusable(true);

        //setBackground(new Color(252, 246, 212, 80));
        setLayout(new BorderLayout());

        scoreField = new JLabel("0");
        scoreLabel = new JLabel("Score: ");

        northPanel = new JPanel();
        northPanel.setBackground(new Color(252, 246, 212, 80));
        northPanel.add(scoreLabel);
        northPanel.add(scoreField);
        add(northPanel, BorderLayout.NORTH);

        setGrid();
        setInstructions();
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    private void setGrid() {

        gameBoard = new GridGUI(NestedGrid.MAX_SIZE ,NestedGrid.MAX_SIZE);
        controller = new GridController(this);
        addKeyListener(controller);
        //gameBoard.add(midPan);
        add(gameBoard, BorderLayout.CENTER);
    }

    private void setInstructions() {

        String targetColor = "Score is calculated as total length of outside edges matching: ";

        JLabel scoreInstructions = new JLabel(targetColor);

        String spaces = "    ";
        JLabel blue = new JLabel(spaces);

        blue.setOpaque(true);
        blue.setBackground(ColorTemplate.SERENITY_NOW[0]);



        String up = "\u2191 mv up";
        String left = "\u2190 mv CCW";
        String right = "\u2192 mv CW";
        String down = "\u2193 mv down";

        String smash = "S smash";
        String rotateCW = "C rotate CW";
        String rotateCCW = "W rotate CCW";
        String swapH = "H swap horizonal";
        String swapV = "V swap vertical";

        JLabel smashLabel = new JLabel(smash);
        JLabel rotCLabel = new JLabel(rotateCW);
        JLabel rotCCLabel = new JLabel(rotateCCW);
        JLabel swapHLabel = new JLabel(swapH);
        JLabel swapVLabel = new JLabel(swapV);
        smashLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rotCLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rotCCLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        swapHLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        swapVLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel upLabel = new JLabel(up);
        upLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel dnLabel = new JLabel(down);
        dnLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel rightLabel = new JLabel(right);
        rightLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel leftLabel = new JLabel(left);
        leftLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel southSouth = new JPanel();
        southSouth.add(upLabel);
        southSouth.add(dnLabel);
        southSouth.add(rightLabel);
        southSouth.add(leftLabel);

        JPanel southSouthSouth = new JPanel();
        southSouthSouth.add(smashLabel);
        southSouthSouth.add(rotCCLabel);
        southSouthSouth.add(rotCLabel);
        southSouthSouth.add(swapHLabel);
        southSouthSouth.add(swapVLabel);

        JPanel northSouth = new JPanel();

        northSouth.add(scoreInstructions);
        northSouth.add(blue);


        southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(northSouth);
        southPanel.add(southSouth);
        southPanel.add(southSouthSouth);

        southPanel.setBackground(new Color(252, 246, 212, 80));

        add(southPanel, BorderLayout.SOUTH);
    }

    public void updateDrawing(Rectangle[] rectangles) {
        gameBoard.updateDrawing(rectangles);
    }

    public void updateScore(int score) {
        scoreField.setText(String.valueOf(score));
    }

}
