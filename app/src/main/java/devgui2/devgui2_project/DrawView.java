package devgui2.devgui2_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * DrawView handles all drawing during the game, including the grid and all the {@link Piece}s.
 */
public class DrawView extends View
{
    private Paint paint = new Paint();
    private int gridWidth, gridHeight; // Grid size in number of cells
	private float gridX1, gridY1; // Coordinates for the grid's top-left     corner
    private float gridX2, gridY2; // Coordinates for the grid's bottom-right corner
	private float blockLength;    // Size in pixels for each grid cell
    private Piece[] pieces;

    public DrawView(Context context) {
        super(context);
        paint.setColor(Color.GRAY);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        // Draw vertical  lines
        for (int i = 0; i <= gridWidth; i++) {
            float offset = i * blockLength;
            canvas.drawLine(gridX1 + offset, gridY1, gridX1 + offset, gridY2, paint);
        }

        //Draw horizontal lines
        for (int i = 0; i <= gridHeight; i++) {
            float offset = i * blockLength;
            canvas.drawLine(gridX1, gridY1 + offset, gridX2, gridY1 + offset, paint);
        }

        // Draw Pieces
        for (Piece piece : pieces) {
	        float rot = piece.getRot();
	        int x = piece.getX();
	        int y = piece.getY();
	        Bitmap bitmap = piece.getBitmap();
	        canvas.save();
	        canvas.rotate(rot, x + (bitmap.getWidth() / 2), y + (bitmap.getHeight() / 2));
	        canvas.drawBitmap(bitmap, x, y, null);
	        canvas.restore();
        }

	    this.invalidate();
    }

	public void init(float gridX1, float gridY1, float gridX2, float gridY2, int gridWidth, int gridHeight, float blockLength) {
		this.gridX1 = gridX1;
		this.gridY1 = gridY1;
		this.gridX2 = gridX2;
		this.gridY2 = gridY2;
		this.gridWidth  = gridWidth;
		this.gridHeight = gridHeight;
		this.blockLength = blockLength;
	}

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }
}
