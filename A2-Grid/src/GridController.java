import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.ref.WeakReference;

//class to handle the commands for the Game
public class GridController implements KeyListener {

    /**
     * keyboard commands
     */
    public final int UP = 38;
    public final int LEFT = 37;
    public final int RIGHT = 39;
    public final int DOWN = 40;

    public final int SMASH = 83;
    public final int ROTATE_CW = 67;
    public final int ROTATE_CCW = 87;
    public final int SWAP_H = 72;
    public final int SWAP_V = 86;

    //keep track of a high score

    //listen for keyboard presses and force redraws

    private NestedGrid grid;

    private WeakReference<Updater> delegate;

    public interface Updater {

        void updateDrawing(Rectangle[] gameBoard);
        void updateScore(int score);

    }

    public GridController(Updater u) {

        delegate = new WeakReference<Updater>(u);
        grid = new NestedGrid(7, ColorTemplate.SERENITY_NOW);

        delegate.get().updateDrawing(grid.rectanglesToDraw());
        Rectangle[] rects = grid.rectanglesToDraw();
        int score = 0;
        Color target = ColorTemplate.SERENITY_NOW[0];
        for(Rectangle r : rects) {
            if(r.getColor().equals(target) && r.isVisible())
                score += r.getBorderSize();
        }
        delegate.get().updateScore(score);
    }


    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        //System.out.println(e.getKeyChar());

        switch(e.getKeyCode()) {

            case UP: grid.moveUp();
                     System.out.println("up");
                     break;

            case DOWN:  grid.moveDown();
                        System.out.println("dn");
                        break;

            case LEFT:  grid.moveLeft();
                        System.out.println("cw");
                        break;

            case RIGHT: grid.moveRight();
                        System.out.println("ccw");
                        break;

            case SMASH: grid.smash();
                        System.out.println("smash");
                        break;

            case ROTATE_CCW: grid.rotate(false);
                             System.out.println("rotate ccw");
                             break;

            case ROTATE_CW: grid.rotate(true);
                            System.out.println("rotate cw");
                            break;

            case SWAP_H: grid.swap(true);
                         System.out.println("flip h");
                         break;

            case SWAP_V: grid.swap(false);
                         System.out.println("flip v");
                         break;

            default: System.out.println("Invalid Key");
        }

        delegate.get().updateDrawing(grid.rectanglesToDraw());

        Rectangle[] rects = grid.rectanglesToDraw();
        int score = 0;
        Color target = ColorTemplate.SERENITY_NOW[0];
        for(Rectangle r : rects) {
            if(r.getColor().equals(target) && r.isVisible())
                score += r.getBorderSize();
        }

        delegate.get().updateScore(score);
    }
}
