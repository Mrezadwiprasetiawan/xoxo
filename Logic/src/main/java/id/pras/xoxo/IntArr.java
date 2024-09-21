package id.pras.xoxo;

public class IntArr {
  private int[] values;
  private int size;
  private int index;

  public IntArr(int size) {
    this.size = size;
    this.index = 0;
    this.values = new int[size];
  }

  public boolean add(int value) {
    if (index + 1 == size) {
      return false;
    }
    this.values[index] = value;
    index++;
    return true;
  }

  public void addAll(int[] values) {
    if (index == 0 && values.length <= size) {
      for (int i = 0; i < values.length; i++) {
        this.values[i] = values[i];
      }
    } else if (values.length <= (size - index)) {
      for (int i = index; i < values.length; i++) {
        this.values[i] = values[i];
      }
    } else {
      throw new IllegalArgumentException(
          "not enough space! Empty space :" + (size - index) + "\tValues size" + values.length);
    }
  }

  public int get(int index) {
    if (!(index <= size - 1)) {
      throw new IndexOutOfBoundsException(
          "Cant get Integer on the Index>=Size! index" + index + " size:" + size);
    }
    return this.values[index];
  }

  public void reset(int index) {
    this.values[index] = 0;
  }

  public void resetAllToZero() {
    for (int i = 0; i < size; i++) {
      this.values[i] = 0;
    }
  }

  public int[] values() {
    return values;
  }

  public int sum() {
    int result = 0;
    for (int i = 0; i < size; i++) {
      result += this.values[i];
    }
    return result;
  }
  
  public int currIndex(){
		return index;
	}
}
