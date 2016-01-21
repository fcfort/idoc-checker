package uo.idoc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class House {

  public House(String houseType, int latitude, int longitude) {
    this.houseType = houseType;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  private final String houseType;
  private final int latitude;
  private final int longitude;

  public String getHouseType() {
    return houseType;
  }

  public int getLatitude() {
    return latitude;
  }

  public int getLongitude() {
    return longitude;
  }

  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o, false);
  }

  public int hashcode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }
}
