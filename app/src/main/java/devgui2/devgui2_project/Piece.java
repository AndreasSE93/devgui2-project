package devgui2.devgui2_project;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by redhotsmasher on 2015-05-29.
 */
public class Piece {

    Color color;
    int pattern;
    Boolean[][] shape;
    Bitmap bitmap;
    int x;
    int y;
    float rot;

    public Piece(Color color, Boolean[][] shape) {
        this.color = color;
        this.shape = shape;
        // Redner bitmap here plz
    }

}
