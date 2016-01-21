package uo.idoc.difference;

public class StringDifference {
  private final String oldData;
  private final String newData;
  
  public StringDifference(String oldData, String newData) {
    this.oldData = oldData;
    this.newData = newData;
  }
  
  public String getOldData() {
    return oldData;
  }
  
  public String getNewData() {
    return newData;  
  }
}
