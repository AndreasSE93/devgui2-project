package devgui2.devgui2_project;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;


public class GridMakerTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_maker_test);
		run(null);
	}

	public void run(View view) {
		TextView textView = (TextView)findViewById(R.id.text);
		Piece[] pieces = GridMaker.makeGrid(5, 5, 3, 3);
		textView.append(Arrays.toString(pieces) + "\n");
		android.util.Log.d("GridMakerTest", Arrays.toString(pieces));
	}
}
