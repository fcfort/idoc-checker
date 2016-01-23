package uo.idoc.difference;

import uo.idoc.model.House;

public class HouseDifference extends AbstractDifference<House> implements Difference<House> {

  public HouseDifference(Iterable<House> oldData, Iterable<House> newData) {
    super(oldData, newData);
  }

}
