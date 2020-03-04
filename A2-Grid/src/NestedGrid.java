import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The back-end for the Grid Game
 * Each Square in the Grid Game can have either 0 or 4
 * Children - it's a quad tree
 *
 **/
public class NestedGrid {

    private int mxLevels;
    private Color[] palette;

    private Node root;
    private Node currentlySelected;

    /**
     * Class that handles the nodes of the quad tree
     */
    class Node {
        private Node parent;
        private Node ur;
        private Node ul;
        private Node ll;
        private Node lr;
        private Rectangle rect;

        /**
         * Constructor for the node class which takes in a Rectangle object and its parent
         * @param x the Rectangle object
         * @param parent the parent of the object rectangle
         */
        public Node(Rectangle x, Node parent) {
            this.parent = parent;
            rect = x;
        }

        /**
         * this method can change the selection of the node
         * @param selected true if selected, false otherwise
         */
        public void setSelected(boolean selected) {
            rect = new Rectangle(rect.getX(), rect.getY(), rect.getSize(), rect.getColor(), rect.isVisible(), selected);
        }

        /**
         * this method is used with the method call smash() and changes the visibility of the rectangle object
         */
        public void makeInvisible() {
            rect = new Rectangle(rect.getX(), rect.getY(), rect.getSize(), rect.getColor(), false, rect.isSelected());
        }

        /**
         * this method sets the X and Y coordinate of the rectangle object and if that rectangle has children they get the coordinates changed too
         * @param X the x coordinate
         * @param Y the y coordinate
         */
        public void setXY(int X, int Y) {
            rect = new Rectangle(X, Y, rect.getSize(), rect.getColor(), rect.isVisible(), rect.isSelected());
            if(ul != null) {
                ul.setXY(X,Y);
                ur.setXY(X+rect.getSize()/2,Y);
                ll.setXY(X,Y+rect.getSize()/2);
                lr.setXY(X+rect.getSize()/2,Y+rect.getSize()/2);
            }
        }

    }
    /**
     *    |-----|-----|
     *    |  UL |  UR |
     *    |-----+-----|
     *    |  LL |  LR |
     *    |_____|_____|
     */
    public static final int MAX_SIZE = 512;

    /**
     * Create a NestedGrid w/ 5 random colored squares to start
     * a root and its 4 children the root is at level 1 and children at 2
     * the selected square (denoted as yellow highlight)
     * is the root square (the owner of the 4 child squares)
     * @param mxLevels the max depth of the game board
     * @param palette the color palette to use
     */
    public NestedGrid(int mxLevels, Color[] palette) {

        this.mxLevels = mxLevels;
        this.palette = palette;
        Rectangle rootRect = new Rectangle(0, 0, MAX_SIZE, generateRandomColor(), false, true);
        root = new Node(rootRect, null);
        makeChildren(root);
        currentlySelected = root;
    }

    /**
     * The selected square moves up to be its parent (if possible)
     */
    public void moveUp() {
        if(root == currentlySelected) {
            return;
        }
        else {
            currentlySelected.setSelected(false);
            currentlySelected = currentlySelected.parent;
            currentlySelected.setSelected(true);
        }
    }

    /**
     * the selected square moves into the upper right child (if possible)
     * of the currently selected square
     */
    public void moveDown() {
        if(currentlySelected.ur == null) {
            return;
        }
        else {
            currentlySelected.ur.setSelected(true);
            currentlySelected.setSelected(false);
            currentlySelected = currentlySelected.ur;
        }
    }

    /**
     * the selected square moves counter clockwise to a sibling
     */
    public void moveLeft() {
        if(currentlySelected.parent == null) {
            return;
        } else {
            if(currentlySelected.parent.ur.rect.isSelected()) {
                currentlySelected.parent.ul.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.ul;

            }
            else if(currentlySelected.parent.ul.rect.isSelected()) {
                currentlySelected.parent.ll.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.ll;
            }
            else if(currentlySelected.parent.ll.rect.isSelected()) {
                currentlySelected.parent.lr.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.lr;
            }
            else if(currentlySelected.parent.lr.rect.isSelected()) {
                currentlySelected.parent.ur.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.ur;
            }
        }
    }

    /**
     * Move the selected square to the next sibling clockwise
     */
    public void moveRight() {
        if(currentlySelected.parent == null) {
            return;
        }
        else{
            if(currentlySelected.parent.ur.rect.isSelected()) {
                currentlySelected.parent.lr.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.lr;
            }
            else if(currentlySelected.parent.lr.rect.isSelected()) {
                currentlySelected.parent.ll.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.ll;
            }
            else if(currentlySelected.parent.ll.rect.isSelected()) {
                currentlySelected.parent.ul.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.ul;
            }
            else if(currentlySelected.parent.ul.rect.isSelected()) {
                currentlySelected.parent.ur.setSelected(true);
                currentlySelected.setSelected(false);
                currentlySelected = currentlySelected.parent.ur;
            }
        }
    }

    /**
     * Return an array of the squares (as class Rectangle) to draw on the screen
     * @return
     */
    public Rectangle[] rectanglesToDraw () {
        ArrayList<Rectangle> listing = new ArrayList<>();
        traversal(root, listing);

        for(Rectangle w : listing) {
            checkScore(w);
        }

        return listing.toArray(new Rectangle[listing.size()]);
    }

    /**
     * smash a square into 4 smaller squares (if possible)
     * a square at max depth level is not allowed to be smashed
     * leave the selected square as the square that was just
     * smashed (it's just not visible anymore)
     */
    public void smash() {
        if (currentlySelected == root) {
            return;
        }
        else {
            if( (currentlySelected.ur == null) && (currentlySelected.rect.getSize() >= finalLevelSize())) {
                currentlySelected.makeInvisible();
                makeChildren(currentlySelected);
            }
        }
    }

    /**
     * Rotate the descendants of the currently selected square
     * @param clockwise if true rotate clockwise, else counterclockwise
     */
    public void rotate(boolean clockwise) {
        if (currentlySelected.ur == null) {
            return;
        }
        rotate(currentlySelected, clockwise);

    }

    /**
     * flip the descendants of the currently selected square
     * the descendants will become the mirror image
     * @param horizontally if true then flip over the x-axis,
     *                     else flip over the y-axis
     */
    public void swap (boolean horizontally) {
        if (currentlySelected.ur != null) {
            swap(currentlySelected, horizontally);
        }
    }

    /*******************************************************************************************************************
     * HELPER CODE
     ******************************************************************************************************************/


    /**
     * generates random color
     * @return the color generated randomly
     */
    private Color generateRandomColor() {
        Random random = new Random();
        return palette[random.nextInt(palette.length)];
    }


    /**
     * this method divides a given size in half
     * @param size the size of the rectangle
     * @return the half size of the rectangle
     */
    private int mapSize(int size) {
        return size/2;
    }

    /**
     * Creates a rectangle object and also sets its border size
     * @param x the x coordinate of the rectangle
     * @param y the y coordinate of the rectangle
     * @param size the size of the rectangle
     * @return Rectangle object that is created
     */
    private Rectangle createRectangle(int x, int y , int size) {
        Rectangle w = new Rectangle(x, y, size, generateRandomColor(), true, false);
        int borderSize = 0;
        if(x+size >= MAX_SIZE || x == 0) {
            borderSize += size;
        }
        if(y+size >= MAX_SIZE || y == 0) {
            borderSize += size;
        }
        w.setBorderSize(borderSize);
        return w;
    }

    /**
     * this method creates four children nodes given a parent node
     * @param parent the parent node
     */
    private void makeChildren(Node parent) {
        int parentSize= parent.rect.getSize();
        int parentX = parent.rect.getX();
        int parentY = parent.rect.getY();
        Rectangle ul = createRectangle(parentX, parentY, mapSize(parentSize));
        Rectangle ur = createRectangle(parentX+mapSize(parentSize), parentY, mapSize(parentSize));
        Rectangle ll = createRectangle(parentX, parentY+ mapSize(parentSize), mapSize(parentSize));
        Rectangle lr = createRectangle(parentX+mapSize(parentSize),parentY+ mapSize(parentSize), mapSize(parentSize));
        parent.ul = new Node(ul, parent);
        parent.ur = new Node(ur, parent);
        parent.ll = new Node(ll, parent);
        parent.lr = new Node(lr, parent);
    }


    /**
     * this method recursively traverses through the quad tree
     * @param node the node from where to traverse the tree that is the root
     * @param list array list to store the rectangle objects if they are selected and visible
     */
    private void traversal(Node node, ArrayList<Rectangle> list) {
        if(node == null) {
            return;
        }
        if(node.rect.isVisible() || node.rect.isSelected()) {
            list.add(node.rect);
        }
        traversal(node.ul, list);
        traversal(node.ur, list);
        traversal(node.ll, list);
        traversal(node.lr, list);
    }


    /**
     * checks the score of the rectangle objetc to see of the x coordinate or the y coordinate touches the border edge to reset the bordersize of the rectangle
     * @param w the Rectangle object
     */
    private void checkScore(Rectangle w) {
        int borderSize = 0;
        if(w.getX()+ w.getSize() >= MAX_SIZE || w.getX() == 0) {
            borderSize += w.getSize();
        }
        if(w.getY()+w.getSize() >= MAX_SIZE || w.getY() == 0) {
            borderSize += w.getSize();
        }
        w.setBorderSize(borderSize);
    }

    /**
     * this helper method recursively rotates the x and y coordinates and the node references of the selected part of the quad tree along with the children if any 
     * @param parent the node from where the rotation starts, that is the currently selected node
     * @param clockwise true is clockwise, false otherwise
     */
    private void rotate(Node parent, boolean clockwise) {
        if(parent.ur == null) {
            return;
        }
        else {
            int Xtemp1 = parent.ul.rect.getX();
            int Ytemp1 = parent.ul.rect.getY();
            int Xtemp2 = parent.ur.rect.getX();
            int Ytemp2 = parent.ur.rect.getY();
            int Xtemp3 = parent.ll.rect.getX();
            int Ytemp3 = parent.ll.rect.getY();
            int Xtemp4 = parent.lr.rect.getX();
            int Ytemp4 = parent.lr.rect.getY();
            if(!clockwise) {
                parent.ul.setXY(Xtemp3, Ytemp3);
                parent.ur.setXY(Xtemp1, Ytemp1);
                parent.ll.setXY(Xtemp4, Ytemp4);
                parent.lr.setXY(Xtemp2, Ytemp2);
                Node temp1 = parent.ll;  parent.ll = parent.ul;  parent.ul = temp1;
                Node temp2 = parent.ur;  parent.ur = parent.ul;  parent.ul = temp2;
                Node temp3 = parent.lr;  parent.lr = parent.ur;  parent.ur = temp3;
            }
            else {
                parent.ul.setXY(Xtemp2, Ytemp2);
                parent.ur.setXY(Xtemp4, Ytemp4);
                parent.ll.setXY(Xtemp1, Ytemp1);
                parent.lr.setXY(Xtemp3, Ytemp3);

                Node temp1 = parent.lr;  parent.lr = parent.ur;  parent.ur = temp1;
                Node temp2 = parent.ur;  parent.ur = parent.ul;  parent.ul = temp2;
                Node temp3 = parent.ll;  parent.ll = parent.ul;  parent.ul = temp3;
            }

            rotate(parent.ur, clockwise);
            rotate(parent.ul, clockwise);
            rotate(parent.ll, clockwise);
            rotate(parent.lr, clockwise);

        }
    }

    /**
     * this helper method recursively swaps the quad tree for the currently selected node along with children nodes if any
     * @param parent the node that is currently selected
     * @param horizontally true is swap is horizontal, vertical otherwise.
     */
    private void swap(Node parent, boolean horizontally) {
        if(parent.ur == null) {
            return;
        }
        else {
            int Xtemp1 = parent.ul.rect.getX();
            int Ytemp1 = parent.ul.rect.getY();
            int Xtemp2 = parent.ur.rect.getX();
            int Ytemp2 = parent.ur.rect.getY();
            int Xtemp3 = parent.ll.rect.getX();
            int Ytemp3 = parent.ll.rect.getY();
            int Xtemp4 = parent.lr.rect.getX();
            int Ytemp4 = parent.lr.rect.getY();
            if(horizontally) {
                parent.ul.setXY(Xtemp3, Ytemp3);
                parent.ur.setXY(Xtemp4, Ytemp4);
                parent.ll.setXY(Xtemp1, Ytemp1);
                parent.lr.setXY(Xtemp2, Ytemp2);

                Node temp1 = parent.ll;  parent.ll = parent.ul;  parent.ul = temp1;
                Node temp2 = parent.lr;  parent.lr = parent.ur;  parent.ur = temp2;

            }
            else {
                parent.ul.setXY(Xtemp2, Ytemp2);
                parent.ur.setXY(Xtemp1, Ytemp1);
                parent.ll.setXY(Xtemp4, Ytemp4);
                parent.lr.setXY(Xtemp3, Ytemp3);

                Node temp1 = parent.ur;  parent.ur = parent.ul;  parent.ul = temp1;
                Node temp2 = parent.lr;  parent.lr = parent.ll;  parent.ll = temp2;
            }

            swap(parent.ur, horizontally);
            swap(parent.ul, horizontally);
            swap(parent.ll, horizontally);
            swap(parent.lr, horizontally);

        }
    }

    /**
     * this is a helper method to calculate the size of the rectangle object in the last level
     * @return the size of the rectangle in the last level of the quad tree
     */
    private int finalLevelSize() {
        int finalLevelSize = MAX_SIZE;
        for (int i = 2; i < mxLevels; i++) {
            finalLevelSize = finalLevelSize/2;
        }
        return finalLevelSize;
    }

}
