package id.pras.xoxo.logic;

import java.util.ArrayList;

/*
 * Kelas statis untuk mengevaluasi logika permainan
 * metode publik yang bisa dipakai adalah :
 *   calcScore(byte[][] board, int winSize) :
 *     metode untuk menghitung Skor total
 *   isWin(byte[][] board, int winSize, byte role) :
 *     metode untuk memeriksa apakah role tertentu menang
 * kelas dibuat final agar tidak bisa diturunkan
 */
public final class Evaluator {
  private static final byte NULL_HANDLE=0;
  private static final byte O=1;
  private static final byte X=-1;
  // kelas sepenuhnya statis
  private Evaluator() {}

  // metode untuk menghitung skor
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
  /*
  Sequence[] existingSequences(byte[][] board, int winSize){
    ArrayList<Sequence> result=new ArrayList<>();
    //vertical array
    Sequence tmpO;
    Sequence tmpX;
    for(int x=0;x<board.length;x++)
      for(int y=0;y<board.length;y++){
        board[x][y]=O;
      }
    return result.toArray(new Sequence[]{});
  }
  
  Sequence[] existingSequence(){
    ArrayList<Sequence> result=new ArrayList<>();
    return result.toArray(new Sequence[]{});
  }
  */

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

  // metode publik untuk memeriksa apakah role tertentu memang
  /*
   * metode ini memeriksa apakah ada role tertentu menang dalam arah vertikal, horizontal, maupun diagonal
   * dengan bantuan kelas IntArr
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
