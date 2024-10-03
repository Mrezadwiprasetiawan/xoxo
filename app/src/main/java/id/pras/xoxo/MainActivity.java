package id.pras.xoxo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import id.pras.xoxo.databinding.ActivityMainBinding;
import id.pras.xoxo.logic.BaseAI;
import id.pras.xoxo.ui.Board;
import id.pras.xoxo.ui.MultiPlayerBoard;
import id.pras.xoxo.ui.SinglePlayerBoard;

public class MainActivity extends Activity {

  private ActivityMainBinding binding;
  private Thread winState;
  private boolean running = false;
  private boolean First = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
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
