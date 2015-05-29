package devgui2.devgui2_project;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by redhotsmasher on 2015-05-29.
 */
public class Piece {

    int color;
    int pattern;
    boolean[][] shape;
    Bitmap bitmap;
    int x;
    int y;
    float rot;

    public Piece(int color, boolean[][] shape) {
        this.color = color;
        this.shape = shape;
        // Redner bitmap here plz
    }

}
