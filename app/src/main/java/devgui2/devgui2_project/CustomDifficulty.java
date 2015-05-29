package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		Intent intent = new Intent(this, GridGame.class);
		intent.putExtra("gridWidth",    gridWidthSpinner   .getValue());
		intent.putExtra("gridHeight",   gridHeightSpinner  .getValue());
		intent.putExtra("blockMinSize", blockMinSizeSpinner.getValue());
		intent.putExtra("blockMaxSize", blockMaxSizeSpinner.getValue());
		startActivity(intent);
	}
}
