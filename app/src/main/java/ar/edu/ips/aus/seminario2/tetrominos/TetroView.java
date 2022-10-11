package ar.edu.ips.aus.seminario2.tetrominos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;

public class TetroView extends View {

    private final PlayFieldViewModel model;

    public TetroView(Context context, PlayFieldViewModel model) {
        super(context);
        this.model = model;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (model.game.nextBlock != null) {
            int left = 0, top = 0;
            boolean[][] cells = model.game.nextBlock.cells;
            Tetromino.Shape shape = model.game.nextBlock.getShape();

            int blockSize = Math.min(getWidth() / model.game.nextBlock.width, getHeight() / model.game.nextBlock.height);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(blockSize * model.game.nextBlock.width,
                    blockSize * model.game.nextBlock.height);
            setLayoutParams(params);

            for (int y = 0; y < cells.length; y++) {
                for (int x = 0; x < cells[0].length; x++) {
                    if (cells[y][x] &&
                            shape != null) {
                        Bitmap bitmap = model.shapeResources.get(shape);
                        Rect drawArea = new Rect(left, top, left + blockSize, top + blockSize);
                        canvas.drawBitmap(bitmap, null, drawArea, null);
                    }
                    left += blockSize;
                }
                top += blockSize;
                left = 0;
            }
        }
    }
}
