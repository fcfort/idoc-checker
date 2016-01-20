package uo.idoc;

public class ByteArrayDifference {
  private final byte[] oldData;
  private final byte[] newData;
  
  public ByteArrayDifference(byte[] oldData, byte[] newData) {
    this.oldData = oldData;
    this.newData = newData;
  }
  
  public byte[] getOldData() {
    return oldData;
  }
  
  public byte[] getNewData() {
    return newData;  
  }
}
