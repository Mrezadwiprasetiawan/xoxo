package id.pras.xoxo.logic;

public final class Data {
  private int boardSideSize;
  private int winSize;
  private int score;
  private int[] sequenceData;

  public Data(int boardSideSize, int winSize, int[] sequenceData) {
    this(boardSideSize, winSize, 0, sequenceData);
  }

  public Data(int boardSideSize, int winSize, int score, int[] sequenceData) {
    this.boardSideSize = boardSideSize;
    this.winSize = winSize;
    this.score = score;
    this.sequenceData = sequenceData;
  }

  public int getBoardSideSize() {
    return this.boardSideSize;
  }

  public void setBoardSideSize(int boardSideSize) {
    this.boardSideSize = boardSideSize;
  }

  public int getWinSize() {
    return this.winSize;
  }

  public void setWinSize(int winSize) {
    this.winSize = winSize;
  }

  public int getScore() {
    return this.score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int[] getSequenceData() {
    return this.sequenceData;
  }

  public void setSequenceData(int[] sequenceData) {
    this.sequenceData = sequenceData;
  }
}
