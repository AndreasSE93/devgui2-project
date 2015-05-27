package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
//import android.widget.NumberPicker;


public class CustomDifficulty extends Activity {
	NumberPicker gridHeightSpinner, gridWidthSpinner, blockMinSizeSpinner, blockMaxSizeSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_difficulty);

		gridWidthSpinner = (NumberPicker)findViewById(R.id.gridWidthSpinner);
		gridWidthSpinner.setMinValue(2);
		gridWidthSpinner.setMaxValue(20);
		gridWidthSpinner.setValue(7);

		gridHeightSpinner = (NumberPicker)findViewById(R.id.gridHeightSpinner);
		gridHeightSpinner.setMinValue(2);
		gridHeightSpinner.setMaxValue(20);
		gridHeightSpinner.setValue(7);

		blockMinSizeSpinner = (NumberPicker)findViewById(R.id.blockMinSize);
		blockMinSizeSpinner.setMinValue(1);
		blockMinSizeSpinner.setMaxValue(20);
		blockMinSizeSpinner.setValue(2);

		blockMaxSizeSpinner = (NumberPicker)findViewById(R.id.blockMaxSize);
		blockMaxSizeSpinner.setMinValue(2);
		blockMaxSizeSpinner.setMaxValue(20);
		blockMaxSizeSpinner.setValue(5);
	}

	public void startGame(View view) {
		Intent intent = new Intent(this, gridgame.class);
		intent.putExtra("gridWidth",    gridWidthSpinner   .getValue());
		intent.putExtra("gridHeight",   gridHeightSpinner  .getValue());
		intent.putExtra("blockMinSize", blockMinSizeSpinner.getValue());
		intent.putExtra("blockMaxSize", blockMaxSizeSpinner.getValue());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_custom_difficulty, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
