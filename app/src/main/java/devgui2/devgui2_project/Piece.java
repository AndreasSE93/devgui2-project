package devgui2.devgui2_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by redhotsmasher on 2015-05-29.
 */
public class Piece implements Serializable {

    private int color;
    private int pattern;
    private boolean[][] shape;
    private transient Bitmap bitmap;
    private int x;
    private int y;
    private float rot;
	private boolean snapped  = false;
	private boolean snapping = false;

    public Piece(int color, int pattern, boolean[][] shape) {
        this.color = color;
        this.pattern = pattern;
        this.shape = shape;
    }

    public void initBitmap(float blockLength) {
        int xSize = this.shape.length;
        int ySize = this.shape[0].length;
        bitmap = Bitmap.createBitmap((int)(xSize*blockLength)+1, (int)(ySize*blockLength)+1, Bitmap.Config.ARGB_8888);
        int color2 = Color.argb(255, Color.red(color)/2, Color.green(color)/2,Color.blue(color)/2);
        Canvas temp = new Canvas(bitmap);
        temp.drawARGB (0, 0, 0, 0);
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        paint.setColor(color);
        paint2.setColor(color2);
        for (int j = 0; j < ySize; j++) {
            for (int i = 0; i < xSize; i++) {
                if(shape[i][j]) {
                    temp.drawRect(i*blockLength, j*blockLength, (i+1)*blockLength+1, (j+1)*blockLength+1, paint2);
                    temp.drawRect(i*blockLength+1, j*blockLength+1, (i+1)*blockLength, (j+1)*blockLength, paint);
                }
            }
        }
    }

    public int getColor() {
        return color;
    }

    public int getPattern() {
        return pattern;
    }

    public boolean[][] getShape() {
        return shape;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getRot() {
        return rot;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

	public void setRot(float rot) {
		this.rot = rot;
	}

	public void setSnapped(boolean snapped) {
		this.snapped = snapped;
	}

	public boolean isSnapped() {
		return this.snapped;
	}

	public void setSnapping(boolean snapping) {
		this.snapping = snapping;
	}

	public boolean isSnapping() {
		return this.snapping;
	}
}
