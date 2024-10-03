package id.pras.xoxo.logic;

import java.util.ArrayList;

/*
 * Static class for evaluating game logic.
 *
 * Public methods that can be used:
 *   - {@link #calcScore(byte[][], int)}:
 *     Method to calculate the total score.
 *   - {@link #isWin(byte[][], int, byte)}:
 *     Method to check if a specific role has won.
 *
 * This class is declared as final to prevent inheritance.
 *
 * @author [M Reza Dwi Prasetiawan]
 * @version 1.2
 */
public final class Evaluator {
  /** @value #NULL_HANDLE */
  public static final byte NULL_HANDLE = 0;
  /** @value #O */
  public static final byte O = 1;
  /** @value #X */
  public static final byte X = -1;

  // kelas sepenuhnya statis
  private Evaluator() {}

  /**
   * Calculates the total score for the given game board.
   *
   * <p>This method checks if player O or X has won the game. If player O has won, it returns {@link
   * Integer#MAX_VALUE}. If player X has won, it returns {@link Integer#MIN_VALUE}. If neither
   * player has won, it calculates the score based on vertical, horizontal, and diagonal alignments
   * of symbols on the board.
   *
   * @param board a 2D byte array representing the game board, where 0 represents an empty cell, 1
   *     represents player O, and -1 represents player X.
   * @param winSize the size required to win (i.e., the number of symbols in a row needed for
   *     victory).
   * @return the calculated score based on the current state of the game board.
   * @throws IllegalArgumentException if the board is not square.
   */
  public static int calcScore(byte[][] board, int winSize) {
    if (isWin(board, winSize, O)) {
      return Integer.MAX_VALUE;
    } else if (isWin(board, winSize, X)) {
      return Integer.MIN_VALUE;
    }
    return calcVerticalScore(board, winSize)
        + calcHorizontalScore(board, winSize)
        + calcDiagonalScore(board, winSize);
  }

  // sementara dibuat private
  private static int calcVerticalScore(byte[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    for (int x = 0; x < xLen; x++) result += calcStraightScore(board[x], winSize);
    return result;
  }

  // sementara dibuat private
  private static int calcHorizontalScore(byte[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);

    for (int y = 0; y < yLen; y++) {
      byte[] straight = new byte[board.length];
      for (int x = 0; x < board.length; x++) straight[x] = board[x][y];
      result += calcStraightScore(straight, winSize);
    }

    return result;
  }

  // sementara dibuat private
  private static int calcDiagonalScore(byte[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);

    int sideSize = board.length;

    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      byte[] diagonal1 = new byte[sideSize - startX];
      byte[] diagonal2 = new byte[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] = board[startX + i][i]; // Diagonal dari kiri atas ke kanan bawah
        diagonal2[i] = board[i][startX + i]; // Diagonal dari kanan atas ke kiri bawah
      }
      result += calcStraightScore(diagonal1, winSize);
      result += calcStraightScore(diagonal2, winSize);
    }

    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      byte[] diagonal1 = new byte[sideSize - startX];
      byte[] diagonal2 = new byte[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] =
            board[i][sideSize - 1 - (startX + i)]; // Diagonal dari kiri bawah ke kanan atas
        diagonal2[i] =
            board[startX + i][sideSize - 1 - i]; // Diagonal dari kanan bawah ke kiri atas
      }
      result += calcStraightScore(diagonal1, winSize);
      result += calcStraightScore(diagonal2, winSize);
    }

    return result;
  }

  /* menghitung score berdasarkan jumlah simbol pada array
   * jika simbol diblokir di salah satu ujung(awal atau akhir) maka nilai sementara urutan simbol dikurangi 1
   * jika simbol diblokir di keduanya maka nilai sementara urutan simbol itu menjadi 0
   */
  // dibuat publik untuk debugging atau kegunaan lain
  public static int calcStraightScore(byte[] straight, int winSize) {
    int result = 0;
    byte prevVal = NULL_HANDLE;
    ArrayList<Sequence> elements = new ArrayList<>();
    Sequence elementO = new Sequence(O, winSize);
    Sequence elementX = new Sequence(X, winSize);

    for (int i = 0; i < straight.length; i++) {
      byte currVal = straight[i];

      // Penanganan elemen pertama
      if (i == 0) {
        if (currVal == O) {
          elementO.add();
          elementO.setBlockedStart();
        } else if (currVal == X) {
          elementX.add();
          elementX.setBlockedStart();
        }
      }
      // Penanganan elemen di tengah-tengah array
      else if (i != straight.length - 1) {
        if (prevVal == currVal || prevVal == NULL_HANDLE) {
          if (currVal == O) {
            elementO.add();
          } else if (currVal == X) {
            elementX.add();
          }
        } else {
          if (currVal == O) {
            elementX.setBlockedEnd();
            elements.add(elementX);
            elementX = new Sequence(X, winSize); // Reset sequence X
            elementO.setBlockedStart();
            elementO.add();
          } else if (currVal == X) {
            elementO.setBlockedEnd();
            elements.add(elementO);
            elementO = new Sequence(O, winSize); // Reset sequence O
            elementX.setBlockedStart();
            elementX.add();
          } else {
            if (prevVal == O) {
              elements.add(elementO);
              elementO = new Sequence(O, winSize); // Reset sequence O
            } else if (prevVal == X) {
              elements.add(elementX);
              elementX = new Sequence(X, winSize); // Reset sequence X
            }
          }
        }
      }
      // Penanganan elemen terakhir
      else {
        if (currVal != NULL_HANDLE) {
          if (currVal == O) {
            elementO.add();
            if (prevVal == X) {
              elementO.setBlockedStart();
              elementO.setBlockedEnd();
            } else {
              elementO.setBlockedEnd();
            }
            elements.add(elementO);
          } else if (currVal == X) {
            elementX.add();
            if (prevVal == O) {
              elementX.setBlockedStart();
              elementX.setBlockedEnd();
            } else {
              elementX.setBlockedEnd();
            }
            elements.add(elementX);
          }
        } else {
          if (prevVal == O) {
            elements.add(elementO);
          } else if (prevVal == X) {
            elements.add(elementX);
          }
        }
      }

      prevVal = currVal;
    }

    // Menghitung total nilai berdasarkan elemen dalam ArrayList
    for (Sequence seq : elements) {
      result += seq.score();
    }

    return result;
  }

  // Metode untuk menemukan urutan yang sudah ada
  Sequence[] existingSequences(byte[][] board, int winSize) {
    ArrayList<Sequence> result = new ArrayList<>();

    // Mengecek urutan secara vertikal, horizontal, dan diagonal
    // Vertical
    for (int x = 0; x < board.length; x++) {
      for (int y = 0; y <= board.length - winSize; y++) {
        checkSequence(board, result, x, y, 0, 1, winSize); // 0, 1 untuk vertikal
      }
    }

    // Horizontal
    for (int x = 0; x <= board.length - winSize; x++) {
      for (int y = 0; y < board.length; y++) {
        checkSequence(board, result, x, y, 1, 0, winSize); // 1, 0 untuk horizontal
      }
    }

    // Diagonal kanan bawah
    for (int x = 0; x <= board.length - winSize; x++) {
      for (int y = 0; y <= board.length - winSize; y++) {
        checkSequence(board, result, x, y, 1, 1, winSize); // 1, 1 untuk diagonal
      }
    }

    // Diagonal kiri bawah
    for (int x = 0; x <= board.length - winSize; x++) {
      for (int y = winSize - 1; y < board.length; y++) {
        checkSequence(board, result, x, y, 1, -1, winSize); // 1, -1 untuk diagonal
      }
    }

    return result.toArray(new Sequence[0]);
  }

  // Metode untuk mengecek dan menambahkan urutan
  private void checkSequence(
      byte[][] board,
      ArrayList<Sequence> result,
      int startX,
      int startY,
      int deltaX,
      int deltaY,
      int winSize) {
    byte currentType = board[startX][startY];
    if (currentType == 0) return; // Tidak ada urutan jika tipe adalah 0 (kosong)

    Sequence sequence = new Sequence(currentType, winSize);
    int count = 1;
    boolean blockedStart = false;
    boolean blockedEnd = false;

    // Mengecek arah positif
    for (int i = 1; i < winSize; i++) {
      int newX = startX + i * deltaX;
      int newY = startY + i * deltaY;
      if (newX >= board.length
          || newY >= board.length
          || newY < 0
          || board[newX][newY] != currentType) {
        blockedEnd = true; // Jika terblokir di akhir
        break;
      }
      count++;
      sequence.add();
    }

    // Mengecek arah negatif
    for (int i = 1; i < winSize; i++) {
      int newX = startX - i * deltaX;
      int newY = startY - i * deltaY;
      if (newX < 0 || newY >= board.length || newY < 0 || board[newX][newY] != currentType) {
        blockedStart = true; // Jika terblokir di awal
        break;
      }
      count++;
      sequence.add();
    }

    // Jika ada urutan valid, tambahkan ke hasil
    if (count >= winSize) {
      if (blockedStart) sequence.setBlockedStart();
      if (blockedEnd) sequence.setBlockedEnd();
      result.add(sequence);
    }
  }

  // kelas tambahan untuk dukungan penghitungan skor
  private static class Sequence {
    private byte type;
    private boolean blockedStart;
    private boolean blockedEnd;
    private int score;
    private int winSize;

    public Sequence(byte type, int winSize) {
      this.type = type;
      blockedStart = false;
      blockedEnd = false;
      score = 0;
      this.winSize = winSize;
    }

    public void add() {
      score += type; // Tambah skor berdasarkan tipe
    }

    public void setBlockedStart() {
      this.blockedStart = true;
    }

    // debugging
    public boolean getBlockedStart() {
      return this.blockedStart;
    }

    // debugging
    public boolean getBlockedEnd() {
      return this.blockedEnd;
    }

    public void setBlockedEnd() {
      this.blockedEnd = true;
    }

    // debugging
    public int realScore() {
      return score;
    }

    public int score() {
      if (blockedStart ^ blockedEnd) {
        return score - type; // Jika diblokir di satu sisi
      } else if (blockedStart && blockedEnd) {
        return 0; // Jika diblokir di kedua sisi
      } else {
        return score >= winSize - 2 ? score * 2 : score; // Jika tidak diblokir
      }
    }
  }

  /**
   * Checks if the specified player has won the game on the given board.
   *
   * <p>This method verifies if the player represented by the specified symbol has achieved the
   * required alignment (winSize) in any direction: vertically, horizontally, or diagonally.
   *
   * @param board a 2D byte array representing the game board, where 0 represents an empty cell, 1
   *     represents player O, and -1 represents player X.
   * @param winSize the size required to win (i.e., the number of symbols in a row needed for
   *     victory).
   * @param playerSymbol the symbol of the player to check for a win (O or X).
   * @return true if the specified player has won, false otherwise.
   * @throws IllegalArgumentException if the board is not square.
   */
  public static boolean isWin(byte[][] board, int winSize, byte role) {
    if (role == NULL_HANDLE)
      throw new IllegalArgumentException("role cant be NULL_HANDLE or 0 :" + role);
    if (!(role == O ^ role == X))
      throw new IllegalArgumentException("undefined role! Role = " + role);
    int yLen = board.length;
    if (yLen == 0) throw new IllegalArgumentException("Board size cant be 0");
    int xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    if (evaluateVertical(board, winSize, role)
        || evaluateHorizontal(board, winSize, role)
        || evaluateDiagonal(board, winSize, role)) return true;
    return false;
  }

  // mengevaluasi kemenangan pada arah vertikal
  private static boolean evaluateVertical(byte[][] board, int winSize, byte role) {
    int sideSize = board.length;
    ByteArr checker = new ByteArr(winSize);
    for (int x = 0; x < sideSize; x++) {
      checker.resetAllToZero(); // Reset checker untuk setiap kolom
      for (int y = 0; y < sideSize; y++) {
        if (board[x][y] == role) {
          if (!checker.add(role)) return true;
        } else {
          checker.resetAllToZero(); // Reset jika tidak sama
        }
      }
    }
    return false;
  }

  // mengevaluasi kemenangan pada arah horizontal
  private static boolean evaluateHorizontal(byte[][] board, int winSize, byte role) {
    int sideSize = board.length;
    ByteArr checker = new ByteArr(winSize);
    for (int y = 0; y < sideSize; y++) {
      checker.resetAllToZero(); // Reset checker untuk setiap baris
      for (int x = 0; x < sideSize; x++) {
        if (board[x][y] == role) {
          if (!checker.add(role)) return true;
        } else {
          checker.resetAllToZero(); // Reset jika tidak sama
        }
      }
    }
    return false;
  }

  // Mengevaluasi kemenangan pada arah diagonal
  private static boolean evaluateDiagonal(byte[][] board, int winSize, byte role) {
    int sideSize = board.length;

    // Evaluasi diagonal dari kiri atas ke kanan bawah
    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      byte[] diagonal1 = new byte[sideSize - startX];
      byte[] diagonal2 = new byte[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] = board[startX + i][i]; // Diagonal dari kiri atas ke kanan bawah
        diagonal2[i] = board[i][startX + i]; // Diagonal dari kanan atas ke kiri bawah
      }
      if (evaluateSingleArray(diagonal1, winSize, role)
          || evaluateSingleArray(diagonal2, winSize, role)) {
        return true;
      }
    }

    // Evaluasi diagonal dari kanan atas ke kiri bawah
    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      byte[] diagonal1 = new byte[sideSize - startX];
      byte[] diagonal2 = new byte[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] =
            board[i][sideSize - 1 - (startX + i)]; // Diagonal dari kiri bawah ke kanan atas
        diagonal2[i] =
            board[startX + i][sideSize - 1 - i]; // Diagonal dari kanan bawah ke kiri atas
      }
      if (evaluateSingleArray(diagonal1, winSize, role)
          || evaluateSingleArray(diagonal2, winSize, role)) {
        return true;
      }
    }

    return false;
  }

  // Mengevaluasi kemenangan untuk array 1 dimensi
  private static boolean evaluateSingleArray(byte[] arr, int winSize, byte role) {
    ByteArr checker = new ByteArr(winSize);
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == role) {
        if (!checker.add(role)) return true;
      } else {
        checker.resetAllToZero(); // Reset jika tidak sama
      }
    }
    return false;
  }
}
