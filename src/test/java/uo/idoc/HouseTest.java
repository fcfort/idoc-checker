package uo.idoc;

import static org.junit.Assert.*;

import org.junit.Test;

import uo.idoc.model.House;

public class HouseTest {

  @Test
  public void testHouseEquals() {
    House h = new House("a", 1, 2);
    House hp = new House("a", 1, 2);

    assertEquals(h, hp);
  }

  @Test
  public void testHouseHashCodes() {
    House h = new House("a", 1, 2);
    House hp = new House("a", 1, 2);

    assertEquals(h.hashCode(), hp.hashCode());
  }
}
