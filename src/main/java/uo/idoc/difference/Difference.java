package uo.idoc.difference;

import java.util.Set;

public interface Difference<T> {
  Set<T> getOld();
  Set<T> getNew();
}
