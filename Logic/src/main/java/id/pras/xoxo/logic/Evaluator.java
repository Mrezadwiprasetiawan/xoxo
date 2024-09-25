package id.pras.xoxo.logic;

import java.util.ArrayList;

/*
 * Kelas statis untuk mengevaluasi logika permainan
 * metode publik yang bisa dipakai adalah :
 *   calcScore(int[][] board, int winSize) :
 *     metode untuk menghitung Skor total
 *   isWin(int[][] board, int winSize, int role) :
 *     metode untuk memeriksa apakah role tertentu menang
 * kelas dibuat final agar tidak bisa diturunkan
 */
public final class Evaluator {
  private static final int NULL_HANDLE = 0;
  private static final int X = -1;
  private static final int O = 1;

  // kelas sepenuhnya statis
  private Evaluator() {}

  // metode untuk menghitung skor
  public static int calcScore(int[][] board, int winSize) {
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
  private static int calcVerticalScore(int[][] board, int winSize) {
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
  private static int calcHorizontalScore(int[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);

    for (int y = 0; y < yLen; y++) {
      int[] straight = new int[board.length];
      for (int x = 0; x < board.length; x++) straight[x] = board[x][y];
      calcStraightScore(straight, winSize);
    }

    return result;
  }

  // sementara dibuat private
  private static int calcDiagonalScore(int[][] board, int winSize) {
    int yLen = board.length;
    int xLen = 0;
    int result = 0;

    if (yLen != 0) xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);

    int sideSize = board.length;

    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      int[] diagonal1 = new int[sideSize - startX];
      int[] diagonal2 = new int[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] = board[startX + i][i]; // Diagonal dari kiri atas ke kanan bawah
        diagonal2[i] = board[i][startX + i]; // Diagonal dari kanan atas ke kiri bawah
      }
      result += calcStraightScore(diagonal1, winSize);
      result += calcStraightScore(diagonal2, winSize);
    }

    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      int[] diagonal1 = new int[sideSize - startX];
      int[] diagonal2 = new int[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] =
            board[i][sideSize - 1 - (startX + i)]; // Diagonal dari kiri bawah ke kanan atas
        diagonal2[i] =
            board[startX + i][sideSize - 1 - i]; // Diagonal dari kanan bawah ke kiri atas
      }
      result += calcStraightScore(diagonal1, winSize);
      result += calcStraightScore(diagonal2, winSize);
    }

    // Diagonal dari kanan atas ke kiri bawah
    for (int startY = 0; startY <= yLen - winSize; startY++) {
      for (int startX = winSize - 1; startX < xLen; startX++) {}
    }

    return result;
  }

  /* menghitung score berdasarkan jumlah simbol pada array
   * jika simbol diblokir di salah satu ujung(awal atau akhir) maka nilai sementara urutan simbol dikurangi 1
   * jika simbol diblokir di keduanya maka nilai sementara urutan simbol itu menjadi 0
   */
  // sementara dibuat private
  private static int calcStraightScore(int[] side, int winSize) {
    int result = 0;
    int prevVal = NULL_HANDLE;
    ArrayList<Sequence> elements = new ArrayList<>();
    Sequence elementO = new Sequence(O, winSize);
    Sequence elementX = new Sequence(X, winSize);

    for (int i = 0; i < side.length; i++) {
      int currVal = side[i];

      // Penanganan elemen pertama
      if (i == 0) {
        // elemen pertama ditandai diblokir karena tidak ada ruang kosong sebelum elemen pertama
        if (currVal == O) {
          elementO.add();
          elementO.setBlockedStart();
        } else if (currVal == X) {
          elementX.add();
          elementX.setBlockedStart();
        }
      }
      // Penanganan elemen di tengah-tengah array
      else if (i != side.length - 1) {
        if (prevVal == currVal || prevVal == NULL_HANDLE) {
          if (currVal == O) {
            elementO.add();
          } else if (currVal == X) {
            elementX.add();
          }
        } else {
          // Menangani transisi antar elemen
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
            // jika element saat ini kosong
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
        // elemen terakhir ditandai diblokir karena tidak ada ruang kosong setelahnya
        if (currVal != NULL_HANDLE) {
          if (currVal == O) {
            elementO.add();
            if (prevVal == X) {
              elementO.setBlockedStart();
              elementO.setBlockedEnd();
            }
            if (prevVal == O) {
              elementO.setBlockedEnd();
            }
            elements.add(elementO);
          } else if (currVal == X) {
            elementX.add();
            if (prevVal == O) {
              elementX.setBlockedStart();
              elementX.setBlockedEnd();
            }
            if (prevVal == X) {
              elementX.setBlockedEnd();
            }
            elements.add(elementX);
          }
        } else if (prevVal == O) {
          elements.add(elementO);
        } else if (prevVal == X) {
          elements.add(elementX);
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

  // kelas tambahan untuk dukungan penghitungan skor
  private static class Sequence {
    private int type;
    private boolean blockedStart;
    private boolean blockedEnd;
    private int score;
    private int winSize;

    public Sequence(int type, int winSize) {
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

    public void setBlockedEnd() {
      this.blockedEnd = true;
    }

    public int score() {
      if (blockedStart ^ blockedEnd) {
        return score - type; // Jika diblokir di satu sisi
      } else if (blockedStart && blockedEnd) {
        return 0; // Jika diblokir di kedua sisi
      } else {
        return score >= winSize ? score * 2 : score; // Jika tidak diblokir
      }
    }
  }

  // metode publik untuk memeriksa apakah role tertentu memang
  /*
   * metode ini memeriksa apakah ada role tertentu menang dalam arah vertikal, horizontal, maupun diagonal
   * dengan bantuan kelas IntArr
   */
  public static boolean isWin(int[][] board, int winSize, int role) {
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
  private static boolean evaluateVertical(int[][] board, int winSize, int role) {
    int sideSize = board.length;
    IntArr checker = new IntArr(winSize);
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
  private static boolean evaluateHorizontal(int[][] board, int winSize, int role) {
    int sideSize = board.length;
    IntArr checker = new IntArr(winSize);
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
  private static boolean evaluateDiagonal(int[][] board, int winSize, int role) {
    int sideSize = board.length;

    // Evaluasi diagonal dari kiri atas ke kanan bawah
    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      int[] diagonal1 = new int[sideSize - startX];
      int[] diagonal2 = new int[sideSize - startX];
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
      int[] diagonal1 = new int[sideSize - startX];
      int[] diagonal2 = new int[sideSize - startX];
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
  private static boolean evaluateSingleArray(int[] arr, int winSize, int role) {
    IntArr checker = new IntArr(winSize);
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
