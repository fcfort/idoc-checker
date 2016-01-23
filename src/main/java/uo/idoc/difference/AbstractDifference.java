package uo.idoc.difference;

import java.util.Set;

import com.google.common.collect.Sets;

public abstract class AbstractDifference<T> implements Difference<T> {
  
  private final Set<T> oldData;
  private final Set<T> newData;
  
  public AbstractDifference(Iterable<T> oldData, Iterable<T> newData) {
    this.oldData = Sets.newHashSet(oldData);
    this.newData = Sets.newHashSet(newData);
  }
  
  public Set<T> getOld() {
    return oldData;
  }
  
  public Set<T> getNew() {
    return newData;
  }
  
  public Set<T> getOnlyInOld() {
    return Sets.difference(this.oldData, this.newData);
  }
  
  public Set<T> getOnlyInNew() {
    return Sets.difference(this.newData, this.oldData);
  }
}
