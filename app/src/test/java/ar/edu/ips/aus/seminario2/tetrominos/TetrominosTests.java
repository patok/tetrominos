package ar.edu.ips.aus.seminario2.tetrominos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.graphics.Point;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import ar.edu.ips.aus.seminario2.tetrominos.domain.PlayField;
import ar.edu.ips.aus.seminario2.tetrominos.domain.Tetromino;

@RunWith(RobolectricTestRunner.class)
public class TetrominosTests {

    @Test
    public void simple_tetromino_test() {
        PlayField field = new PlayField(1, 1);

        Tetromino block = new Tetromino(Tetromino.Shape.T);
        System.out.println(block);
        System.out.println("\n\n");

        assertFalse(field.place(block, new Point(0,0)));
        System.out.println(field.toString());
        System.out.println("\n\n");

        field = new PlayField(5,5);

        assertTrue(field.place(block, new Point(2,2)));
        System.out.println(field.toString());
        System.out.println("\n\n");

        assertTrue(field.place(block, new Point(0,1)));
        System.out.println(field.toString());
        System.out.println("\n\n");
    }

    @Test
    public void test_tetromino_clone() {
        Tetromino block = new Tetromino(Tetromino.Shape.T);

        Tetromino block2 = new Tetromino(block);

        assertEquals(block, block2);

        block2.rotateClockwise();

        assertNotEquals(block, block2);
    }

    @Test
    public void test_tetromino_rotate() {
        for (Tetromino.Shape shape: Tetromino.Shape.values()) {
            if (shape != Tetromino.Shape.O)
                test_tetromino_rotate_by_shape(shape);
        }
    }

    void test_tetromino_rotate_by_shape(Tetromino.Shape shape) {
        Tetromino block = new Tetromino(shape);

        Tetromino block2 = new Tetromino(block);

        block.rotateClockwise();
        System.out.println(block);
        System.out.println("\n");
        assertNotEquals(block, block2);

        block.rotateCounterClockwise();
        System.out.println(block);
        System.out.println("\n");
        assertEquals(block, block2);

        block.rotateClockwise();
        System.out.println(block);
        System.out.println("\n");
        assertNotEquals(block, block2);

        block.rotateClockwise();
        System.out.println(block);
        System.out.println("\n");
        assertNotEquals(block, block2);

        block.rotateClockwise();
        System.out.println(block);
        System.out.println("\n");
        assertNotEquals(block, block2);

        block.rotateClockwise();
        System.out.println(block);
        System.out.println("\n");
        assertEquals(block, block2);
    }

    @Test
    public void test_tetrominos_arrangement_4x10() {
        PlayField field = new PlayField(10, 4);

        Tetromino t1 = new Tetromino(Tetromino.Shape.O);
        System.out.println(t1);
        System.out.println("\n");
        assertTrue(field.place(t1, new Point(0,2)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t2 = new Tetromino(Tetromino.Shape.S);
        System.out.println(t2);
        System.out.println("\n");
        assertTrue(field.place(t2, new Point(2,1)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t3 = new Tetromino(Tetromino.Shape.T);
        t3.rotateClockwise();
        t3.rotateClockwise();
        System.out.println(t3);
        System.out.println("\n");
        assertTrue(field.place(t3, new Point(4,2)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t4 = new Tetromino(Tetromino.Shape.J);
        t4.rotateClockwise();
        System.out.println(t4);
        System.out.println("\n");
        assertTrue(field.place(t4, new Point(7,1)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t5 = new Tetromino(Tetromino.Shape.I);
        t5.rotateCounterClockwise();
        System.out.println(t5);
        System.out.println("\n");
        assertTrue(field.place(t5, new Point(8,0)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t6 = new Tetromino(Tetromino.Shape.T);
        System.out.println(t6);
        System.out.println("\n");
        assertTrue(field.place(t6, new Point(1,0)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t7 = new Tetromino(Tetromino.Shape.O);
        System.out.println(t7);
        System.out.println("\n");
        assertTrue(field.place(t7, new Point(6,1)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t8 = new Tetromino(Tetromino.Shape.L);
        System.out.println(t8);
        System.out.println("\n");
        assertTrue(field.place(t8, new Point(0,-1)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t9 = new Tetromino(Tetromino.Shape.Z);
        System.out.println(t9);
        System.out.println("\n");
        assertTrue(field.place(t9, new Point(3,-1)));
        System.out.println(field);
        System.out.println("\n");

        Tetromino t10 = new Tetromino(Tetromino.Shape.I);
        System.out.println(t10);
        System.out.println("\n");
        assertTrue(field.place(t10, new Point(5,-1)));
        System.out.println(field);
        System.out.println("\n");

    }
}
