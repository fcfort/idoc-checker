package uo.idoc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import uo.idoc.model.House;
import uo.idoc.model.HouseParser;

public class HouseParserTest {

  private final HouseParser hp = new HouseParser();

  @Test
  public void testParseSimple() {
    String s = "+villa: 710 784 1\n+marble shop: 4424 1346 1\n+tower: 2829 644 1\n\n+EOF";
    List<House> fromFile = hp.parse(s);
    List<House> expected = ImmutableList.of(new House("villa", 710, 784), new House("marble shop", 4424, 1346),
        new House("tower", 2829, 644));

    assertEquals(expected, fromFile);
  }

  @Test
  public void testParseFromFile() throws IOException {
    InputStream in = this.getClass().getClassLoader().getResourceAsStream("hp");

    String content = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
    Closeables.closeQuietly(in);

    List<House> houses = hp.parse(content);

    assertEquals(2059, houses.size());
  }

}
