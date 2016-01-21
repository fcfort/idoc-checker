package uo.idoc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

public class HouseParser {

  private static final Pattern HOUSE_LINE = Pattern.compile("^\\+[a-z]");
  private static final Pattern HOUSE_TOKENS = Pattern.compile( "^\\+([a-z ]+): (\\d+) (\\d+) 1$");
  
  
  public List<House> parse(String houseListString) {
    List<String> houseList = stringToList(houseListString);
    
    Iterable<String> houseLines = Iterables.filter(houseList, Predicates.contains(HOUSE_LINE));
    
    List<House> houses = new LinkedList<>();
    
    for(String houseLine : houseLines) {
      Matcher matcher = HOUSE_TOKENS.matcher(houseLine);
      if(matcher.matches()) {
        String houseType = matcher.group(1);
        int latitude = Integer.valueOf(matcher.group(2));
        int longitude = Integer.valueOf(matcher.group(3));
        houses.add(new House(houseType, latitude, longitude));        
      }
    }
    
    return houses;
  }
  
  private static List<String> stringToList(String s) {
    return Arrays.asList(s.split("\\r?\\n"));
  }
}
