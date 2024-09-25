package id.pras.xoxo.ui;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.widget.Toast;
import id.pras.xoxo.logic.AI;
import id.pras.xoxo.logic.BaseAI;
import id.pras.xoxo.logic.Evaluator;
import id.pras.xoxo.ui.Board;

public class SinglePlayerBoard extends Board {
  private AI ai;
  private boolean isProcessingMove = false; // Penanda apakah ada langkah yang sedang diproses

  public SinglePlayerBoard(Context context) {
    this(context,7,5,Board.O);
  }
  
  public SinglePlayerBoard(Context context, int sideSize, int winSize, int role) {
    super(context, sideSize,winSize);
    setCurrentPlayer(role);
    initAI(role==Board.O?Board.X:Board.O);
  }

  private void initAI(int role) {
    ai = new BaseAI(getBoard(), getSideSize(), getWinSize(), role);
    ai.setTimeout(1000_000_000);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // Tangani event sentuh hanya jika action-nya adalah ACTION_DOWN
    if (event.getAction() == MotionEvent.ACTION_DOWN && !isProcessingMove) {
      int score = Evaluator.calcScore(getBoard(), getWinSize());
      android.widget.Toast.makeText(getContext(), "Score = " + score, 1).show();
      if (ai.IsRunning()) {
        android.widget.Toast.makeText(getContext(), "AI sedang berfikir", 1).show();
        return false;
      }

      // Mulai memproses langkah
      isProcessingMove = true;
      boolean parentChangePlayer = super.onTouchEvent(event);

      if (parentChangePlayer) {
        int role = getCurrentPlayer();
        // Jika giliran AI
        if (role == ai.getRole() && !ai.IsRunning()) {
          // Jalankan AI
          ai.Start();

          // Buat thread untuk AI dan tunggu sampai selesai
          new Thread(
                  () -> {
                    while (ai.IsRunning()) {
                      // Tunggu hingga AI selesai
                    }

                    // Setelah AI selesai berpikir, dapatkan langkah AI
                    int[] result = ai.getResult();

                    // Pastikan nilai bisa diset di papan
                    boolean success = setValue(result[0], result[1], ai.getRole());
                    if (!success) {
                      android.widget.Toast.makeText(
                              getContext(), "AI memilih posisi yang sudah diisi", 1)
                          .show();
                    }

                    // Cek kemenangan
                    if (Evaluator.isWin(getBoard(), getWinSize(), getCurrentPlayer())) {
                      setWinState(true);
                    } else {
                      setCurrentPlayer(role == X ? O : X);
                    }

                    // Selesai memproses langkah
                    isProcessingMove = false;
                  })
              .start();

          return true; // Proses berhasil
        }
      }
      isProcessingMove = false;
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
