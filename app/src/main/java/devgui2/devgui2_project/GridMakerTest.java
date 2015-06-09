package devgui2.devgui2_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class GridMakerTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_maker_test);
	}

	public void run(View view) {
		TextView textView = (TextView)findViewById(R.id.text);
		int gridWidth    = Integer.parseInt(((EditText)findViewById(R.id.gridWidth   )).getText().toString());
		int gridHeight   = Integer.parseInt(((EditText)findViewById(R.id.gridHeight  )).getText().toString());
		int blockMinSize = Integer.parseInt(((EditText)findViewById(R.id.blockMinSize)).getText().toString());
		int blockMaxSize = Integer.parseInt(((EditText)findViewById(R.id.blockMaxSize)).getText().toString());
		Piece[] pieces = GridMaker.makeGrid(gridWidth, gridHeight, blockMinSize, blockMaxSize);
		textView.append("Got " + Integer.toString(pieces.length) + " Pieces." + "\n");
	}
}
