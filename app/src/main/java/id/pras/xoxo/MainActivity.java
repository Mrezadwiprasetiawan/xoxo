package id.pras.xoxo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import id.pras.xoxo.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

  private ActivityMainBinding binding;
  private Thread winState;
  private boolean running = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    Board board = new MultiPlayerBoard(getBaseContext(),25, 5);
    board.setLayoutParams(
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    binding.getRoot().addView(board);
    winState =
        new Thread(
            () -> {
              while (!board.getWinState()) {
                this.running = true;
              }
              this.running = false;
              runOnUiThread(
                  () -> {
                    binding.getRoot().removeView(board);
                  });
            });
	  winState.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.binding = null;
    if (this.winState != null && this.winState.isAlive()) {
      winState.interrupt();
    }
    winState = null;
  }
  
}
