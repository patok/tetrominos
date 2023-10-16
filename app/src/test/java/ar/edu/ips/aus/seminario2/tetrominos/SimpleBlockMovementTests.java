package ar.edu.ips.aus.seminario2.tetrominos;

import org.junit.Test;
import org.junit.runner.RunWith;
/*
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
*/
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
/*
import static org.mockito.Mockito.*;
*/

import android.graphics.Point;

import ar.edu.ips.aus.seminario2.tetrominos.domain.Block;
import ar.edu.ips.aus.seminario2.tetrominos.domain.PlayField;
import static ar.edu.ips.aus.seminario2.tetrominos.infra.ui.console.ConsoleHelper.output;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class SimpleBlockMovementTests {

    @Test
    public void empty_block_in_1_by_1_field() {
        PlayField field = new PlayField(1, 1);
        field.place(new Block(), new Point(0,0));

        Block emptyBlock = field.getBlock(new Point(0, 0), 1, 1);

        assertFalse(emptyBlock.isFilled(0, 0));
    }

    @Test
    public void check_empty_1x1_field_can_place_only_one_block() {
        PlayField field = new PlayField(1, 1);
        Block newBlock = new Block();
        newBlock.fill(0,0);

        assertTrue(field.place(newBlock, new Point(0,0)));

        assertFalse(field.place(newBlock, new Point(0,0)));
    }

    @Test
    public void check_moving_blocks() {
        PlayField field = new PlayField(3, 3);
        Block block1 = new Block(2,2);
        block1.fill(0,0);
        block1.fill(1,1);
        System.out.println(output(block1));
        System.out.println("\n\n");

        Block block2 = new Block(2, 2);
        block2.fill(0,0);
        block2.fill(1,1);
        System.out.println(output(block2));
        System.out.println("\n\n");


        field.place(block1, new Point(0,1));
        System.out.println(output(field));
        System.out.println("\n\n");
        assertTrue(field.canBePositioned(block1,new Point(0,0)));

        field.place(block2, new Point(0,0));
        System.out.println(output(field));
        System.out.println("\n\n");
        assertFalse(field.canBePositioned(block2, new Point(0,0)));
    }

    @Test
    public void check_1x1() {
        PlayField field = new PlayField(1,1);
        Block newBlock = new Block();
        field.place(newBlock, new Point(0,0));

        assertTrue(field.canBePositioned(newBlock, new Point(0,0)));

        boolean[][] cells = new boolean[1][1];
        cells[0][0] = true;
        Block fullBlock = new Block(cells);
        assertFalse(field.canBePositioned(fullBlock, new Point(0,1)));
    }

    @Test
    public void check_1x1_block_move_inside_2x2_field() {
        PlayField field = new PlayField(2,2);

        boolean[][] cells = new boolean[1][1];
        cells[0][0] = true;

        Block newBlock = new Block(cells);
        //field.moveBlock(newBlock,0, 1);

        //simulate initial vertical move
        assertTrue(field.canBePositioned(newBlock, new Point(0,0)));

        //simulate 1 step vertical move
        assertTrue(field.canBePositioned(newBlock, new Point(0,0 + 1)));

        //simulate another 1 step vertical move
        assertFalse(field.canBePositioned(newBlock, new Point(0, 0 + 2)));

        //horizontal move
        assertTrue(field.canBePositioned(newBlock, new Point(0 + 1,0)));

        //horizontal move
        assertFalse(field.canBePositioned(newBlock, new Point(0 + 2,0)));
    }

    @Test
    public void test_two_complete_levels() {
        PlayField field = new PlayField(4, 2);

        Block block1 = new Block(4, 1);
        block1.fill(0,0);
        block1.fill(1,0);
        block1.fill(2,0);
        block1.fill(3,0);
        System.out.println(output(block1));
        System.out.println("\n\n");

        for (int i = 0; i < 2; i++) {
            System.out.println(output(field));
            System.out.println("\n\n");
            field.place(block1, new Point(0, i));
        }

        System.out.println(output(field));
        System.out.println("\n\n");

        assertEquals(field.removeCompletedLevels(), 2);

        System.out.println(output(field));
        System.out.println("\n\n");
    }

    @Test
    public void test_three_oof_five_complete_levels() {
        PlayField field = new PlayField(4, 5);

        Block block1 = new Block(4, 1);
        block1.fill(0,0);
        block1.fill(1,0);
        block1.fill(2,0);
        block1.fill(3,0);
        System.out.println(output(block1));
        System.out.println("\n\n");

        field.place(block1, new Point(0, 0));
        field.place(block1, new Point(0, 2));
        field.place(block1, new Point(0, 4));

        System.out.println(output(field));
        System.out.println("\n\n");

        assertEquals(field.removeCompletedLevels(), 3);

        System.out.println(output(field));
        System.out.println("\n\n");
    }

    @Test
    public void test_complete_w_uncomplete_levels() {
        PlayField field = new PlayField(4, 5);

        Block block1 = new Block(4, 1);
        block1.fill(0,0);
        block1.fill(1,0);
        block1.fill(2,0);
        block1.fill(3,0);
        System.out.println(output(block1));
        System.out.println("\n\n");

        field.place(block1, new Point(0, 0));
        field.place(block1, new Point(0, 2));
        field.place(block1, new Point(0, 4));

        Block block2 = new Block(4, 1);
        block2.fill(0,0);
        block2.fill(3,0);
        field.place(block2, new Point(0, 1));
        field.place(block2, new Point(0, 3));

        System.out.println(output(field));
        System.out.println("\n\n");

        assertEquals(field.removeCompletedLevels(), 3);

        System.out.println(output(field));
        System.out.println("\n\n");

        Block block3 = new Block(4, 1);

        assertEquals(field.getBlock(new Point(0,4), 4,1), block2);
        assertEquals(field.getBlock(new Point(0,3), 4,1), block2);
        assertEquals(field.getBlock(new Point(0,2), 4,1), block3);
        assertEquals(field.getBlock(new Point(0,1), 4,1), block3);
        assertEquals(field.getBlock(new Point(0,0), 4,1), block3);
    }

    @Test
    public void test_block_clone() throws Exception {
        Block block = new Block(4, 1);
        block.fill(0,0);
        block.fill(1,0);
        block.fill(2,0);

        Block newBlock = new Block(block);
        assertTrue(newBlock.equals(block));

        block.fill(3,0);
        assertFalse(newBlock.equals(block));
    }

}