package id.pras.xoxo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import id.pras.xoxo.databinding.ActivityMainBinding;
import id.pras.xoxo.ui.SimpleBoard;

public class MainActivity extends Activity {

  private ActivityMainBinding binding;
  private Thread winState;
  private boolean running = false;
  private boolean First = true;
  private String evScore = "";
  private String currBoard = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    
  }

  private void format(String input) {
    input += "\n";
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
