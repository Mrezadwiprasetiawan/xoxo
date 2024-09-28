package id.pras.xoxo.ui;

import android.content.Context;
import android.view.MotionEvent;
import id.pras.xoxo.logic.AI;
import id.pras.xoxo.logic.BaseAI;
import id.pras.xoxo.logic.Evaluator;

public class SinglePlayerBoard extends SimpleBoard {
  private AI ai;

  public SinglePlayerBoard(Context context) {
    this(context, 7, 5, Board.O);
  }

  public SinglePlayerBoard(Context context, int sideSize, int winSize, byte role) {
    super(context, sideSize, winSize);
    setCurrentPlayer(role);
    initAI(role == Board.O ? Board.X : Board.O);
  }

  private void initAI(int role) {
    ai = new BaseAI(getBoard(), getWinSize(), role);
    ai.setTimeout(1000_000_000);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // Tangani event sentuh hanya jika action-nya adalah ACTION_DOWN
    if (event.getAction() == 0) {
      boolean changePlayer = false;
      int boardX = (int) (event.getX() - getOffsetX()) / getCellSize();
      int boardY = (int) (event.getY() - getOffsetY()) / getCellSize();
      if (boardX >= 0 && boardX < getBoard().length && boardY >= 0 && boardY < getBoard().length) {
        changePlayer = setValue(boardX, boardY);
      }

      if (changePlayer) {
        if (Evaluator.isWin(getBoard(), getWinSize(), getCurrentPlayer())) {
          setWinState(true);
        } else {
          setCurrentPlayer(getCurrentPlayer() == Board.X ? Board.O : Board.X);
        }
        return true;
      } else {
        return false;
      }
    }
    return super.onTouchEvent(event);
  }

  public AI getAi() {
    return this.ai;
  }

  public void setAi(AI ai) {
    this.ai = ai;
  }
}
