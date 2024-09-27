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

    MultiPlayerBoard multiBoard = new MultiPlayerBoard(getBaseContext(), 8, 5, Board.O);
    SinglePlayerBoard singleboard = new SinglePlayerBoard(getBaseContext(), 7, 5, Board.O);
    Board board = singleboard;
    BaseAI ai=new BaseAI(board.getBoard(),Board.O);
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
                String boardArr="";
                boardArr+="[\n";
                for(int x=0;x<singleboard.getBoard().length;x++){
                  for (int y=0;y<singleboard.getBoard().length;y++){
                    boardArr+=" "+singleboard.getBoard()[x][y]+",";
                  }
                  boardArr+="\n";
                }
                boardArr+="]";
                binding.debugOutput.setText("\n Board = "+boardArr);
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
                            android.widget.Toast.LENGTH_LONG)
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
