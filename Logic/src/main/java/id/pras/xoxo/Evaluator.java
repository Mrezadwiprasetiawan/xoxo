package id.pras.xoxo;

public final class Evaluator {
  private static final int NULL_HANDLE = 0;
  private static final int X = -1;
  private static final int O = 1;

  @Deprecated
  public Evaluator() {}

  public static int CalcScore(int[][] board, int winSize){
		return CalcVerticalScore(board,winSize)+CalcHorizontalScore(board,winSize)+CalcVerticalScore(board,winSize);
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

    for (int x = 0; x < xLen; x++) {
      for (int startY = 0; startY <= yLen - winSize; startY++) {
        int tmpScore = 0;
        boolean blockedStart = false;
        boolean blockedEnd = false;

        for (int i = 0; i < winSize; i++) {
          int currValue = board[startY + i][x];

          if (i == 0 && (startY == 0 || board[startY - 1][x] != 0)) {
            blockedStart = true;
          }

          if (currValue == O) {
            tmpScore++;
          } else if (currValue == X) {
            tmpScore--;
          }

          if (i == winSize - 1 && (startY + winSize == yLen || board[startY + winSize][x] != 0)) {
            blockedEnd = true;
          }
        }

        if (blockedStart && blockedEnd) {
          tmpScore = 0;
        } else if (blockedStart || blockedEnd) {
          tmpScore -= (tmpScore > 0) ? 1 : -1;
        }

        result += tmpScore;
      }
    }

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
      for (int startX = 0; startX <= xLen - winSize; startX++) {
        int tmpScore = 0;
        boolean blockedStart = false;
        boolean blockedEnd = false;

        for (int i = 0; i < winSize; i++) {
          int currValue = board[y][startX + i];

          if (i == 0 && (startX == 0 || board[y][startX - 1] != 0)) {
            blockedStart = true;
          }

          if (currValue == O) {
            tmpScore++;
          } else if (currValue == X) {
            tmpScore--;
          }

          if (i == winSize - 1 && (startX + winSize == xLen || board[y][startX + winSize] != 0)) {
            blockedEnd = true;
          }
        }

        if (blockedStart && blockedEnd) {
          tmpScore = 0;
        } else if (blockedStart || blockedEnd) {
          tmpScore -= (tmpScore > 0) ? 1 : -1;
        }

        result += tmpScore;
      }
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

    // Diagonal dari kiri atas ke kanan bawah
    for (int startY = 0; startY <= yLen - winSize; startY++) {
      for (int startX = 0; startX <= xLen - winSize; startX++) {
        int tmpScore = 0;
        boolean blockedStart = false;
        boolean blockedEnd = false;

        for (int i = 0; i < winSize; i++) {
          int currValue = board[startY + i][startX + i];

          if (i == 0 && (startX == 0 || startY == 0 || board[startY - 1][startX - 1] != 0)) {
            blockedStart = true;
          }

          if (currValue == O) {
            tmpScore++;
          } else if (currValue == X) {
            tmpScore--;
          }

          if (i == winSize - 1 && (startX + winSize == xLen || startY + winSize == yLen || board[startY + winSize][startX + winSize] != 0)) {
            blockedEnd = true;
          }
        }

        if (blockedStart && blockedEnd) {
          tmpScore = 0;
        } else if (blockedStart || blockedEnd) {
          tmpScore -= (tmpScore > 0) ? 1 : -1;
        }

        result += tmpScore;
      }
    }

    // Diagonal dari kanan atas ke kiri bawah
    for (int startY = 0; startY <= yLen - winSize; startY++) {
      for (int startX = winSize - 1; startX < xLen; startX++) {
        int tmpScore = 0;
        boolean blockedStart = false;
        boolean blockedEnd = false;

        for (int i = 0; i < winSize; i++) {
          int currValue = board[startY + i][startX - i];

          if (i == 0 && (startX == xLen - 1 || startY == 0 || board[startY - 1][startX + 1] != 0)) {
            blockedStart = true;
          }

          if (currValue == O) {
            tmpScore++;
          } else if (currValue == X) {
            tmpScore--;
          }

          if (i == winSize - 1 && (startX == winSize - 1 || startY + winSize == yLen || board[startY + winSize][startX - winSize] != 0)) {
            blockedEnd = true;
          }
        }

        if (blockedStart && blockedEnd) {
          tmpScore = 0;
        } else if (blockedStart || blockedEnd) {
          tmpScore -= (tmpScore > 0) ? 1 : -1;
        }

        result += tmpScore;
      }
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
