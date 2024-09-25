package id.pras.xoxo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import id.pras.xoxo.databinding.ActivityMainBinding;
import id.pras.xoxo.ui.Board;
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
    
    int score = debugBoard.getScore();
    SinglePlayerBoard singleboard = new SinglePlayerBoard(getBaseContext(), 7, 5,Board.O);
    Board board = singleboard;
    board.setLayoutParams(
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    binding.getRoot().addView(board);
    winState =
        new Thread(
            () -> {
              while (!board.getWinState()) {
                this.running = true;
                if (singleboard.getAi().IsRunning()) {
                  runOnUiThread(
                      () -> {
                        binding.debugOutput.setText("AI sedang berfikir");
                      });
                  First = true;
                } else if (First) {
                  runOnUiThread(
                      () -> {
                        int[] result = singleboard.getAi().getResult();
                        binding.debugOutput.setText("Result =" + result[0] + "," + result[1]);
                      });
                  First = false;
                }
              }
              this.running = false;
              runOnUiThread(
                  () -> {
                    binding.getRoot().removeView(board);
                    android.widget.Toast.makeText(
                            getBaseContext(),
                            "Role " + (board.getCurrentPlayer() == 1 ? "O" : "X") + " is Win",
                            1)
                        .show();
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
