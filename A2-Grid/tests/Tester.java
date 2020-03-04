import org.junit.Test;

import static org.junit.Assert.*;

public class Tester {

    @Test
    public void creationSize() {
        NestedGrid ng = new NestedGrid(2, ColorTemplate.SERENITY_NOW);
        //should give 5 rectangles and root should be selected
        Rectangle[] rects = ng.rectanglesToDraw();
        assert(rects.length == 5);
    }

    @Test
    public void creationRootSelected() {
        NestedGrid ng = new NestedGrid(2, ColorTemplate.SERENITY_NOW);
        //should give 5 rectangles and root should be selected
        Rectangle[] rects = ng.rectanglesToDraw();

        int rootsFound = 0;
        for(Rectangle r : rects) {
            if (r.getX() == 0 &&
                r.getY() == 0 && r.getSize() == 512) {
                rootsFound++;
                assertTrue(r.isSelected());
            }
            else {
                assertFalse(r.isSelected());
            }
        }
        assertEquals(1, rootsFound);
    }

    @Test
    public void creationDnThenUp() {
        NestedGrid ng = new NestedGrid(2, ColorTemplate.SERENITY_NOW);

        //should give 5 rectangles and root should be selected
        Rectangle[] rects = ng.rectanglesToDraw();
        ng.moveDown();
        ng.moveUp();
        int rootsFound = 0;
        for(Rectangle r : rects) {
            if (r.getX() == 0 &&
                    r.getY() == 0 &&
                    r.getSize() == 512) {
                rootsFound++;
                assertTrue(r.isSelected());
            }
            else {
                assertFalse(r.isSelected());
            }
        }
        assertEquals(1, rootsFound);
    }
}
