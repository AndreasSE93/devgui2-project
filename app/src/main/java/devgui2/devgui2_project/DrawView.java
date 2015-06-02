package devgui2.devgui2_project;

/**
 * Created by redhotsmasher on 2015-05-27.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View
{
    Bundle bundle;
    Paint paint = new Paint();
    int gridWidth, gridHeight;
	float gridX1, gridY1, gridX2, gridY2; // Grid coordinates
	float blockLength;
    Piece[] pieces;

	/*
	float pointX[] = new float[100];
	float pointY[] = new float[100];
	int pointColor[] = new int[100];
	*/

    public DrawView(Context context, Bundle bundle)
    {
        super(context);
        paint.setColor(Color.GRAY);
        this.bundle = bundle;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        for (int i = 0; i <= gridWidth; i++) { //Draw longitudinal lines
            float offset = (float)(i) * blockLength;
            canvas.drawLine(gridX1 + offset, gridY1, gridX1 + offset, gridY2, paint);
        }

        for (int i = 0; i <= gridHeight; i++) { //Draw latitudinal lines
            float offset = (float)(i) * blockLength;
            canvas.drawLine(gridX1, gridY1 + offset, gridX2, gridY1 + offset, paint);
        }

        for (Piece piece : pieces) {
	        float rot = piece.getRot();
	        int x = piece.getX();
	        int y = piece.getY();
	        Bitmap bitmap = piece.getBitmap();
	        canvas.save();
	        canvas.rotate(rot, x + (bitmap.getWidth() / 2), y + (bitmap.getHeight() / 2));
	        canvas.drawBitmap(bitmap, x, y, null);
	        canvas.restore();
	        //android.util.Log.i("X: ", ((Integer) x).toString());
	        //android.util.Log.i("Y: ", ((Integer) y).toString());
	        //android.util.Log.i("Rot: ", ((Float) rot).toString());
	        //android.util.Log.i("BitmapW: ", ((Integer) bitmap.getWidth()).toString());
	        //android.util.Log.i("BitmapH: ", ((Integer) bitmap.getHeight()).toString());
	        //int[] pixels = new int[100];
	        //bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, 9, 9);
	        //android.util.Log.i("Bitmap: ", pixels.toString());
        }

	    /*
	    Paint paint = new Paint();
	    for (int i = 0; i < pointX.length; i++) {
		    paint.setColor(pointColor[i]);
		    canvas.drawCircle(pointX[i], pointY[i], 10.0f, paint);
	    }
	    */

        //canvas.drawLine(startX, startY, endX, endY, paint);
        //canvas.drawRect(xMargin, yMargin, drawWidth-xMargin, drawHeight-yMargin, paint);
        /*
        android.util.Log.i("drawWidth", ((Integer) drawWidth).toString());
        android.util.Log.i("drawHeight", ((Integer)drawHeight).toString());
        android.util.Log.i("aspectRatio", ((Float)aspectRatio).toString());
        android.util.Log.i("xMargin", ((Float)xMargin).toString());
        android.util.Log.i("yMargin", ((Float)yMargin).toString());
        */

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
	};

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }
}
