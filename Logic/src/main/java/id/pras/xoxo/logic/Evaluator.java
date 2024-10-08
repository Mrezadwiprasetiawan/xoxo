package id.pras.xoxo.logic;

import java.util.ArrayList;

/**
 * Static class for evaluating game logic.
 *
 * Public methods that can be used:
 *   - {@link #calcScore(byte[][], int)}:
 *     Method to calculate the total score.
 *   - {@link #isWin(byte[][], int, byte)}:
 *     Method to check if a specific role has won.
 *
 * This is the main Logic of the game where everything calculated precisely.
 *
 * This class is declared as final to prevent inheritance.
 *
 * @author [M Reza Dwi Prasetiawan]
 * @version 1.3
 */
public final class Evaluator {
  public static final byte NULL_HANDLE = 0;
  public static final byte O = 1;
  public static final byte X = -1;
  
  //debugging
  public enum straightType{
    VERTICAL,
    HORIZONTAL,
    DIAGONAL1,
    DIAGONAL2,
    DIAGONAL3,
    DIAGONAL4;
    public void print(){
      switch(this){
        case VERTICAL:System.out.print("vertical"); break;
        case HORIZONTAL:System.out.print("horizontal"); break;
        case DIAGONAL1:System.out.print("diagonal1"); break;
        case DIAGONAL2:System.out.print("diagonal2"); break;
        case DIAGONAL3:System.out.print("diagonal3"); break;
        case DIAGONAL4:System.out.print("diagonal4"); break;
      }
    }
  }

  private Evaluator() {}

  public static int calcScore(byte[][] board, int winSize) {
    if (isWin(board, winSize, O)) {
      return Integer.MAX_VALUE;
    } else if (isWin(board, winSize, X)) {
      return Integer.MIN_VALUE;
    }
    int result=0;
    for(Sequence seq:AllDirectionSequences(board,winSize)) result+=seq.score();
    return result;
  }
  
  public static Sequence[] AllDirectionSequences(byte[][] board, int winSize){
    ArrayList<Sequence> result=new ArrayList<>();
    int yLen = board.length;
    int xLen = 0;

    if (yLen != 0) xLen = board[0].length;
    if (xLen != yLen)
      throw new IllegalArgumentException("Board must be square! boardSize :" + xLen + "×" + yLen);
    int sideSize = board.length;
    //vertical
    for (int x = 0; x < sideSize; x++)
    for(Sequence seq:straightSequences(board[x],winSize,straightType.VERTICAL)) result.add(seq);
    
    //horizontal
    for (int y = 0; y < yLen; y++) {
      byte[] straight = new byte[sideSize];
      for (int x = 0; x < sideSize; x++) straight[x] = board[x][y];
      for(Sequence seq:straightSequences(straight,winSize,straightType.HORIZONTAL)) result.add(seq);
    }
    
    //diagonal
    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      byte[] diagonal1 = new byte[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal1[i] = board[startX + i][i];
      }
      for(Sequence seq:straightSequences(diagonal1,winSize,straightType.DIAGONAL1)) result.add(seq);
    }

    for (int startX = 0; startX <= sideSize - winSize; startX++) {
      byte[] diagonal2 = new byte[sideSize - startX];
      for (int i = 0; i < sideSize - startX; i++) {
        diagonal2[i] = board[startX + i][sideSize - 1 - i];
      }
      for(Sequence seq:straightSequences(diagonal2, winSize,straightType.DIAGONAL4)) result.add(seq);
    }

    result.trimToSize();
    return result.toArray(new Sequence[]{});
  }

  public static ArrayList<Sequence> straightSequences(byte[] straight, int winSize, straightType type) {
    int result = 0;
    byte prevVal = NULL_HANDLE;
    ArrayList<Sequence> elements = new ArrayList<>();
    Sequence elementO = new Sequence(O, winSize,type);
    Sequence elementX = new Sequence(X, winSize, type);

    for (int i = 0; i < straight.length; i++) {
      byte currVal = straight[i];

      if (i == 0) {
        if (currVal == O) {
          elementO.add();
          elementO.setBlockedStart();
        } else if (currVal == X) {
          elementX.add();
          elementX.setBlockedStart();
        }
      }
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
            elementX = new Sequence(X, winSize, type);
            elementO.setBlockedStart();
            elementO.add();
          } else if (currVal == X) {
            elementO.setBlockedEnd();
            elements.add(elementO);
            elementO = new Sequence(O, winSize, type);
            elementX.setBlockedStart();
            elementX.add();
          } else {
            if (prevVal == O) {
              elements.add(elementO);
              elementO = new Sequence(O, winSize, type);
            } else if (prevVal == X) {
              elements.add(elementX);
              elementX = new Sequence(X, winSize, type);
            }
          }
        }
      }
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
    return elements;
  }

  public static class Sequence {
    private byte type;
    private boolean blockedStart;
    private boolean blockedEnd;
    private int score;
    private int winSize;
    private straightType directType;

    public Sequence(byte type, int winSize, straightType directType) {
      this.type = type;
      blockedStart = false;
      blockedEnd = false;
      score = 0;
      this.winSize = winSize;
      this.directType=directType;
    }

    public void add() {
      score += type;
    }

    public straightType getDirectType() {
      return this.directType;
    }

    public void setBlockedStart() {
      this.blockedStart = true;
    }

    public boolean getBlockedStart() {
      return this.blockedStart;
    }

    public boolean getBlockedEnd() {
      return this.blockedEnd;
    }

    public void setBlockedEnd() {
      this.blockedEnd = true;
    }

    public int realScore() {
      return score;
    }

    public int score() {
      if (blockedStart ^ blockedEnd) {
        return score - type;
      } else if (blockedStart && blockedEnd) {
        return 0;
      } else {
        return score;
      }
    }
  }

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

  private static boolean evaluateVertical(byte[][] board, int winSize, byte role) {
    int sideSize = board.length;
    ByteArr checker = new ByteArr(winSize);
    for (int x = 0; x < sideSize; x++) {
      checker.resetAllToZero();
      for (int y = 0; y < sideSize; y++) {
        if (board[x][y] == role) {
          if (!checker.add(role)) return true;
        } else {
          checker.resetAllToZero();
        }
      }
    }
    return false;
  }

  private static boolean evaluateHorizontal(byte[][] board, int winSize, byte role) {
    int sideSize = board.length;
    ByteArr checker = new ByteArr(winSize);
    for (int y = 0; y < sideSize; y++) {
      checker.resetAllToZero();
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
