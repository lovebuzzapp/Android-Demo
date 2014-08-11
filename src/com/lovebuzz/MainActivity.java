package com.lovebuzz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lovebuzz.util.ReschedulableTimer;

public class MainActivity extends Activity {

  private static final long delayInMillis = 5000;
  private static final String LISTENING_MESSAGE_ON = "Listening for Potential Matches...";
  private static final String LISTENING_MESSAGE_OFF = "Listening turned OFF";

  private List<String> matchInterests = new ArrayList<String>();
  private List<Drawable> matchPhotos = new ArrayList<Drawable>();

  private ReschedulableTimer proximityAlertTimer = new ReschedulableTimer();

  private void init() {
    Button dismissButton = (Button) this.findViewById(R.id.dismissButton);
    dismissButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        clearAlert();
        proximityAlertTimer.scheduleTimer(proximityAlertRunnable, delayInMillis);
      }
    });
    dismissButton.setVisibility(Button.INVISIBLE);

    ToggleButton toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton);
    toggleButton.setChecked(true);
    toggleButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        toggleListeningState();
      }
    });

    matchInterests = new ArrayList<String>();
    matchInterests.add("Nature");
    matchInterests.add("Scuba");
    matchInterests.add("Zumba");
    matchInterests.add("Belly Dancing");
    matchInterests.add("Feminism");
    matchInterests.add("Knitting");
    matchInterests.add("Puppies");
    matchInterests.add("Whiskey");
    matchInterests.add("Bacon");
    matchInterests.add("Beer");
    matchInterests.add("Recycling");
    matchInterests.add("Karl Marx");

    Resources res = getResources();
    matchPhotos.add(res.getDrawable(R.drawable.photo0));
    matchPhotos.add(res.getDrawable(R.drawable.photo1));
    matchPhotos.add(res.getDrawable(R.drawable.photo2));
    matchPhotos.add(res.getDrawable(R.drawable.photo3));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    this.setTitle(R.string.app_name);
    proximityAlertTimer.scheduleTimer(proximityAlertRunnable, delayInMillis);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private int getRandomInterestIndex(int high) {
    Random r = new Random();
    return r.nextInt(high);
  }

  private void notifyAboutMatch() {

    ImageView iv = (ImageView) this.findViewById(R.id.imageView);
    iv.setImageDrawable(matchPhotos.get(getRandomInterestIndex(matchPhotos.size())));
    
    String notificationMessage = "Someone within 50 feet \n";
    notificationMessage += "also likes \n";
    notificationMessage += matchInterests.get(getRandomInterestIndex(matchInterests.size()));

    TextView noticeText = (TextView) this.findViewById(R.id.noticeText);
    noticeText.setText(notificationMessage);

    Button dismissButton = (Button) this.findViewById(R.id.dismissButton);
    dismissButton.setVisibility(Button.VISIBLE);

    buzz();
  }

  private void buzz() {
    Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    long[] pattern = { 0, 500, 200, 500, 500, 500, 200, 500 };
    v.vibrate(pattern, -1);
  }

  private void clearAlert() {
    proximityAlertTimer.cancel();

    ImageView iv = (ImageView) this.findViewById(R.id.imageView);
    iv.setImageDrawable(null);

    Button dismissButton = (Button) this.findViewById(R.id.dismissButton);
    dismissButton.setVisibility(Button.INVISIBLE);

    setListeningStateMessage();
  }

  // Sample proximity alert to be scheduled
  private Runnable proximityAlertRunnable = new Runnable() {
    @Override
    public void run() {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          notifyAboutMatch();
        }
      });
    }
  };

  private void toggleListeningState() {
    ToggleButton toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton);
    if (toggleButton.isChecked()) {
      proximityAlertTimer.scheduleTimer(proximityAlertRunnable, delayInMillis);
    } else {
      clearAlert();
    }
    setListeningStateMessage();
  }

  private void setListeningStateMessage() {
    TextView noticeText = (TextView) this.findViewById(R.id.noticeText);
    ToggleButton toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton);
    if (toggleButton.isChecked()) {
      noticeText.setText(LISTENING_MESSAGE_ON);
    } else {
      noticeText.setText(LISTENING_MESSAGE_OFF);
    }
  }

  private void manuallyExitApp() {
    proximityAlertTimer.cancel();
    onStop();
    finish();
  }
}
