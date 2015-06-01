package devgui2.devgui2_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by redhotsmasher on 2015-05-29.
 */
public class Piece {

    private int color;
    private int pattern;
    private Boolean[][] shape;
    private Bitmap bitmap;
    private int x;
    private int y;
    private float rot;

    public Piece(int color, int pattern, Boolean[][] shape) {
        this.color = color;
        this.pattern = pattern;
        this.shape = shape;
    }

    public void initBitmap(float blockLength) {
        int xSize = this.shape.length;
        int ySize = this.shape[0].length;
        android.util.Log.i("PieceXSize: ", ((Integer) xSize).toString());
        android.util.Log.i("PieceYSize: ", ((Integer) ySize).toString());
        android.util.Log.i("BlockLength: ", ((Float) blockLength).toString());
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
                if(shape[i][j] == true) {
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

    public Boolean[][] getShape() {
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
}
