package id.pras.xoxo;

import android.content.Context;

public class DebugBoard extends Board {
  public DebugBoard(Context context, int sideSize, int winSize) {
    super(context, sideSize, winSize);
  }

  public void setCurrentRole(int role) {
    setCurrentPlayer(role);
  }

  public int getScore() {
    return Evaluator.calcScore(getBoard(), getWinSize());
  }
}
