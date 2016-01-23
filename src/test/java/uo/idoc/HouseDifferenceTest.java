package uo.idoc;

import static org.junit.Assert.*;

import org.junit.Test;

import uo.idoc.difference.HouseDifference;
import uo.idoc.model.HouseParser;

public class HouseDifferenceTest {

  @Test
  public void testDayOverDay() {
    HouseParser hp = new HouseParser();

    HouseDifference diff = new HouseDifference(hp.parse(TestUtils.readResourceAsString("hp")),
        hp.parse(TestUtils.readResourceAsString("hp_day2")));
    System.out.println(String.format("Got old %d new %d", diff.getOld().size(), diff.getNew().size()));
    
    System.out.println(String.format("Got old %d new %d", diff.getOnlyInOld().size(), diff.getOnlyInNew().size()));
    assertEquals(27, diff.getOnlyInOld().size());
    assertEquals(30, diff.getOnlyInNew().size());
  }

}
