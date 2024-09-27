package id.pras.xoxo.logic;

import java.util.Scanner;

public class SinglePlayerTest {
  private static final int O = 1;
  private static final int X = -1;
  private static final int EMPTY = 0;

  private AI ai;
  private int[][] board;
  private int currentPlayer;
  private boolean isProcessingMove = false;
  private int winSize;
  private int sideSize;
  private long currTimeout;

  public SinglePlayerTest(int sideSize, int winSize, int role) {
    this.sideSize = sideSize;
    this.winSize = winSize;
    this.board = new int[sideSize][sideSize];
    this.currentPlayer = role;
    initAI(role == O ? X : O);
  }

  private void initAI(int role) {
    ai = new BaseAI(board, winSize, role);
    ai.setTimeout(1000_000);
  }

  public void setAItimeout(int second) {
    currTimeout=second * 1000_000_000;
    ai.setTimeout(currTimeout);
  }

  public void setAItimeout(long nanoSec) {
    currTimeout=nanoSec;
    ai.setTimeout(nanoSec);
  }

  public void startGame() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      printBoard();
      System.out.println("Skor saat ini :");
      for(int x=0;x<board.length;x++){
        for(int y=0;y<board.length;y++) System.out.print(board[x][y]);
        System.out.println();
      }
      printScore();
      if (isWin(currentPlayer)) {
        System.out.println("Player " + (currentPlayer == X ? "X" : "O") + " wins!");
        break;
      }

      if (currentPlayer == ai.getRole()) {
        System.out.println("AI is thinking...");
        isProcessingMove = true;
        ai.Start();
        while (ai.IsRunning()) {
          System.out.println("AI is running");
        }
        
        System.out.println("AI selesai berfikir");
        int[] result = ai.getResult();
        int bestScore=0;
        if(ai instanceof BaseAI){
          BaseAI revert=(BaseAI) ai;
          bestScore=revert.getBestScore();
        }
        else{
          System.out.println("error, AI is not BaseAI");
        }
        System.out.println(bestScore);
        if (!setValue(result[0], result[1], ai.getRole())) {
          System.out.println("AI memilih posisi yang sudah diisi");
          System.out.println("menambah timeout 1 detik");
          setAItimeout(currTimeout+1000_000);
          continue;
        }
        currentPlayer = (currentPlayer == X ? O : X);
        isProcessingMove = false;
      } else {
        System.out.println(
            "Player "
                + (currentPlayer == X ? "X" : "O")
                + ", masukkan langkahmu (baris dan kolom): ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();

        if (!setValue(row - 1, col - 1, currentPlayer)) {
          System.out.println("Posisi tidak valid, coba lagi.");
          continue;
        }

        currentPlayer = (currentPlayer == X ? O : X);
      }
    }

    scanner.close();
  }

  private boolean setValue(int row, int col, int player) {
    if (row < 0 || row >= sideSize || col < 0 || col >= sideSize || board[row][col] != EMPTY) {
      return false;
    }
    board[row][col] = player;
    return true;
  }

  private void printBoard() {
    for (int i = 0; i < sideSize; i++) {
      for (int j = 0; j < sideSize; j++) {
        char symbol = (board[i][j] == X) ? 'X' : (board[i][j] == O) ? 'O' : '.';
        System.out.print(symbol + " ");
      }
      System.out.println();
    }
  }
  
  private void printScore(){
    System.out.println(Evaluator.calcScore(board,winSize));
  }

  private boolean isWin(int player) {
    return Evaluator.isWin(board, winSize, player);
  }

  public static void main(String[] args) {
    // Ukuran papan dan ukuran kemenangan bisa disesuaikan
    int sideSize = 7;
    int winSize = 5;
    int playerRole = O; // Player bisa memilih menjadi X atau O

    SinglePlayerTest game = new SinglePlayerTest(sideSize, winSize, playerRole);
    if (args.length != 0) {
      boolean flagN = false;
      boolean flagS = false;
      boolean lastFlagN = false;
      for (String arg : args) {
        if (flagN) {
          game.setAItimeout(Long.valueOf(args[1]));
          flagN = false;
        }
        if(flagS){
          game.setAItimeout(Integer.valueOf(arg));
          flagS=false;
        }
        if (arg.equals("-n")){
          flagN = true;
          if(flagS) lastFlagN = true;
        }
        if (arg.equals("-s")){
          flagS = true;
          if(flagN) lastFlagN=false;
        }
        
        if(flagN&&flagS){
          if(lastFlagN){
            game.setAItimeout(Integer.valueOf(arg));
            flagN=false;
          } else{
            game.setAItimeout(Long.valueOf(arg).longValue());
            flagS=false;
          }
        }
      }
    }
    game.startGame();
  }
}
