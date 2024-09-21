package id.pras.xoxo;

public final class Data {
  private int score;
  private int[] sequenceData;

  public Data(int[] sequenceData) {
    this.sequenceData = sequenceData;
    this.score = 0;
  }

  public Data(int score, int[] sequenceData) {
    this.score = score;
    this.sequenceData = sequenceData;
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
