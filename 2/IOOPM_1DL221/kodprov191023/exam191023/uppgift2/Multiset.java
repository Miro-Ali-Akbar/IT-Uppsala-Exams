public class Multiset<T extends Comparable <T>> {

  private T elements[];
  private int count[];
  private int maxCapacity;
  private int inArray = 0;

  public Multiset() {
    this(16);
  }
  
  public Multiset(int maxCapacity) {
    elements = (T[]) new Comparable [maxCapacity];
    count = new int[maxCapacity];

    for (T e : elements) {
      e = null;
    }

    for (int i : count) {
      i = 0;
    }

    this.maxCapacity = maxCapacity;
  }

  public void add(T elem) {
    
    if (elem == null) {
      throw new IllegalArgumentException();
    }

    int index = indexOf(elem);

    if (index < 0) {
      throw new MultisetFullException();
    }

    if (elements[index] == null) {
      elements[index] = elem;
      count[index]++;
      inArray++;
    } else if (elements[index].compareTo(elem) == 0) {
      count[index]++;
    } else {
      T tempElem = elements[index];
      int tempCount = count[index];
      elements[index] = elem;
      count[index] = 1;
      insertElem(tempElem, tempCount);
      inArray++;
    }    
  }

  private void insertElem(T elem, int count) {
    
    
    for (int i = 0; i < maxCapacity; i++) {      
      if (elements[i] == null) {
        elements[i] = elem;
        this.count[i] = count;
      }
      if (elem.compareTo(elements[i]) < 0) {
        T tempElem = elements[i];
        int tempCount = this.count[i];
        elements[i] = elem;
        this.count[i] = count;
        insertElem(tempElem, tempCount);
        break;
      }      
    }
  }
  
  private int indexOf(T elem) {

    int index = 0;

    while(index < maxCapacity) {
      if (elements[index] == null) {
        return index;
      }
      if (elem.compareTo(elements[index]) == 0) {
        return index;
      }
      if (elem.compareTo(elements[index]) < 0) {
        break;
      }
      index++;
    }

    return inArray == maxCapacity ? -(++index) : index;
  }

  public int contains(T elem) {
    int index = indexOf(elem);
    if (index < 0) {
      return 0;
    }
    return count[index];
  }

  public int size() {
    int returnValue = 0;
    for (int i : count) {
      returnValue += i;
    }
    return returnValue;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Multiset) {
      return equals((Multiset) o);
    }
    return false;
  }

    
  
  public boolean equals(Multiset m) {

    for (int i = 0; i < maxCapacity; i++) {
      if (m.count[i] != count[i]) {
        return false;
      }
      if (!equalElements((T) m.elements[i], elements[i])) {
        return false;        
      }
    }

    return true;
  }

  private boolean equalElements(T a, T b) {

    if (a == null) {      
      if (b == null) {
        return true;
      } else
        return false;
    }
    return a.equals(b);
  }
}

class MultisetFullException extends IllegalArgumentException {
  
}
  
