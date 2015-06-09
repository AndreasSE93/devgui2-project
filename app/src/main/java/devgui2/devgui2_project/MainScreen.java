package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;


public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);

        String s1 = getString(R.string.scores_button);
        String s2 = getString(R.string.not_yet_implemented);
        int n = s1.length();
        int m = s2.length();

        Spannable span = new SpannableString(s1 + "\n" +  s2);
        span.setSpan(new RelativeSizeSpan(0.6f), n, (n+m+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((Button)findViewById(R.id.scoresButton)).setText(span);
	}

	public void openSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void play(View view) {
		Intent intent = new Intent(this, DifficultySelect.class);
		startActivity(intent);
	}
}
