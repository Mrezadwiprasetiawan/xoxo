package id.pras.xoxo;

public final class Evaluator {
  private static final int NULL_HANDLE = 0;
  private static final int X = -1;
  private static final int O = 1;

  @Deprecated
  public Evaluator() {}

  public static int CalcScore(int[][] board, int winSize) {
    if (isWin(board, winSize, O)) {
      return Integer.MAX_VALUE;
    } else if (isWin(board, winSize, X)) {
      return Integer.MIN_VALUE;
    }
    return CalcVerticalScore(board, winSize)
        + CalcHorizontalScore(board, winSize)
        + CalcVerticalScore(board, winSize);
  }

  private static int CalcVerticalScore(int[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) {
      xLen = board[0].length;
    }
    if (xLen != yLen) {
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    }
    for (int x = 0; x < xLen; x++) result += CalcStraightScore(board[x]);
    return result;
  }

  private static int CalcHorizontalScore(int[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) {
      xLen = board[0].length;
    }
    if (xLen != yLen) {
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    }

    for (int y = 0; y < yLen; y++) {
      int[] straight = new int[board.length];
      for (int x = 0; x < board.length; x++) straight[x] = board[x][y];
      CalcStraightScore(straight);
    }

    return result;
  }

  private static int CalcDiagonalScore(int[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) {
      xLen = board[0].length;
    }
    if (xLen != yLen) {
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    }

    // Diagonal dari kiri atas ke kanan bawah=
    for (int startY = 0; startY <= yLen - winSize; startY++) {
      IntArr updown = new IntArr(yLen - winSize);
      for (int startX = 0; startX <= xLen - winSize; startX++) {}
    }

    // Diagonal dari kanan atas ke kiri bawah
    for (int startY = 0; startY <= yLen - winSize; startY++) {
      for (int startX = winSize - 1; startX < xLen; startX++) {}
    }

    return result;
  }

  /* menghitung score berdasarkan jumlah simbol pada array
   * jika simbol diblokir di salah satu ujung(awal atau akhir) maka nilainya dikurangi 1
   * jika simbol diblokir di keduanya maka nilai sementara simbol itu menjadi 0
   */
  private static int CalcStraightScore(int[] side) {
    int result = 0;
    boolean blockedO = false;
    boolean blockedX = false;
    int prevVal = 0;
    int tmpScoreO =0;
    int tmpScoreX = 0;
    int ScoreO = 0;
    int ScoreX = 0;
    for (int i = 0; i < side.length; i++) {
      int currVal = side[i];
      if (i == 0) {
        blockedO = currVal == O ? false : true;
        blockedX = currVal == X ? false : true;
      } else if(i!=side.length){
        if (blockedX&&!blockedO) {
          if (currVal == O) tmpScoreO++;
          
          //tidak menambahkan nilai ke O karena diblokir
          if (currVal == X) blockedO=true;
        }
        else if(blockedO&&!blockedX){
          if (currVal == X) tmpScoreO++;
          
          //tidak menambahkan nilai ke X karena diblokir
          if (currVal == O) blockedX=true;
        }
        else if(blockedO&&blockedX){
          if(currVal==NULL_HANDLE){
            //menambahkan score O dan score X serta mereset tmpScoreO dan tmpScoreX
            blockedO=blockedX=false;
            ScoreO+=tmpScoreO;
            ScoreX+=tmpScoreX;
            tmpScoreO=tmpScoreX=0;
          }
          else if(prevVal==O&&currVal==O){
            tmpScoreO++;
          }
          else if(prevVal==X&&currVal==X){
            tmpScoreX--;
          }
          else{
            tmpScoreO=tmpScoreX=0;
          }
        }
        else{
          if(currVal==O) tmpScoreO++;
          if(currVal==X) tmpScoreX--;
        }
      } else{
        if(blockedO&&currVal==X) tmpScoreO=0;
      }
      prevVal = currVal;
    }
    return result;
  }

  public static boolean isWin(int[][] board, int winSize, int role) {
    if (role == NULL_HANDLE) {
      throw new IllegalArgumentException("role cant be NULL_HANDLE or 0 :" + role);
    }
    int yLen = board.length;
    if (yLen == 0) {
      throw new IllegalArgumentException("Board size cant be 0");
    }
    int xLen = board[0].length;
    if (xLen != yLen) {
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    }
    if (evaluateVertical(board, winSize, role)
        || evaluateHorizontal(board, winSize, role)
        || evaluateDiagonal(board, winSize, role)) {
      return true;
    }
    return false;
  }

  // mengevaluasi kemenangan pada arah vertikal
  private static boolean evaluateVertical(int[][] board, int winSize, int role) {
    int sideSize = board.length;
    IntArr checker = new IntArr(winSize);
    for (int x = 0; x < sideSize; x++) {
      for (int y = 0; y < sideSize; y++) {
        if (checker.currIndex() == winSize) {
          return true;
        }
        if (board[x][y] == 0 || !(board[x][y] == role)) {
          checker.resetAllToZero();
        } else {
          checker.add(role);
        }
      }
    }
    return false;
  }

  // mengevaluasi kemenangan pada arah horizontal
  private static boolean evaluateHorizontal(int[][] board, int winSize, int role) {
    int sideSize = board.length;
    IntArr checker = new IntArr(winSize);
    for (int y = 0; y < sideSize; y++) {
      for (int x = 0; x < sideSize; x++) {
        if (checker.currIndex() == winSize) {
          return true;
        }
        if (board[x][y] == 0 || !(board[x][y] == role)) {
          checker.resetAllToZero();
        } else {
          checker.add(role);
        }
      }
    }
    return false;
  }

  // mengevaluasi kemenangan pada arah diagonal
  private static boolean evaluateDiagonal(int[][] board, int winSize, int role) {
    int sideSize = board.length;
    IntArr checkerDiagonalUp = new IntArr(winSize);
    IntArr checkerDiagonalDown = new IntArr(winSize);
    for (int i = 0; i < sideSize; i--) {
      if (checkerDiagonalUp.currIndex() == winSize) {
        return true;
      }
      if (board[i][i] == 0 || !(board[i][i] == role)) {
        checkerDiagonalUp.resetAllToZero();
      } else {
        checkerDiagonalUp.add(role);
      }
      if (checkerDiagonalDown.currIndex() == winSize) {
        return true;
      }
      if (board[sideSize - i][sideSize - i] == 0 || !(board[sideSize - i][sideSize - i] == role)) {
        checkerDiagonalDown.resetAllToZero();
      } else {
        checkerDiagonalDown.add(role);
      }
    }
    return false;
  }
}
