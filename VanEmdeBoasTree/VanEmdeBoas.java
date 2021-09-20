// -1 Means Null in this Implementation of Van-Emde-Boas since I am assuming the universe contains NONnegative integers less than the total number of items u

public class VanEmdeBoas {
  private int size, min, max;

  private VanEmdeBoas[] clusters;

  private VanEmdeBoas summary;

  public VanEmdeBoas(int u) {
    if (u <= 0) throw new IllegalArgumentException("Size of Van Emde Boas tree must be greater than 0");

    size = u;

    // -1 means initially "null" (no value) since universe is between 0 and u - 1
    min = -1;
    
    max = -1;

    if (u > 2) {
      clusters = new VanEmdeBoas[(int) Math.ceil(Math.sqrt(u))];

      for (int i = 0; i < clusters.length; i++) {
        clusters[i] = new VanEmdeBoas((int) Math.floor(Math.sqrt(u)));
      }

      summary = new VanEmdeBoas((int) Math.ceil(Math.sqrt(u)));
    } 
    
  }

  private void vEBEmptyInsert(VanEmdeBoas V, int x) {
    V.min = x;
    V.max = x;
  }

  public void vEBInsert(int x) {
    if (x < 0 || x >= size) throw new IllegalArgumentException("x must be between 0 and size of Van Emde Boas tree minus 1");
    vEBInsert(this, x);
  }

  private void vEBInsert(VanEmdeBoas V, int x) {
    // V.min is only -1 if the tree is empty
    if (V.min == -1) {
      vEBEmptyInsert(V, x);
    }

    // x is the new smaller min in this Van-Emde-Boas structure so swap and pass down the original V.min, which is the larger item
    else { 
      if (x < V.min) {
        int temp = x;
        x = V.min;
        V.min = temp;
      }

      if (V.size > 2) {

        // this checks if the cluster x is going into is currently empty
        if (vEBMinimum(V.clusters[high(x)]) == -1) {
          vEBInsert(V.summary, high(x));
          vEBEmptyInsert(V.clusters[high(x)], low(x));
        } else {
          vEBInsert(V.clusters[high(x)], low(x));
        }

      }

      if (x > V.max) {
        V.max = x;
      }
    }
  }

  // public int vEBTreeSuccessor(int x) {
  //   System.out.println("Entered Public Method!");
  //   return vEBTreeSuccessor(this, x);
  // }

  // // return -1 if successor not found
  // private int vEBTreeSuccessor(VanEmdeBoas V, int x) {
  //   if (V.size == 2) {
  //     System.out.println("Entered Base Case");
  //     if (x == 0 && V.max == 1) {
  //       return 1;
  //     } else {
  //       return -1;
  //     }
  //   }

  //   else if (V.min != -1 && x < V.min) {
  //     System.out.println("Second Case");
  //     return V.min;
  //   } 
    
  //   else {
  //     System.out.println("Last Case");
  //     // index into V.clusters where x is to find if low(x) < maximum in this cluster
  //     int maxLow = vEBMaximum(V.clusters[high(x)]);

  //     System.out.println("maxLow: " + maxLow + " high(x): " + high(x) + " low(x): " + low(x));

  //     // if x's cluster contains some element that is greater than x, we know x has a successor in its own cluster
  //     if (maxLow != -1 && low(x) < maxLow) {
  //       System.out.println("Last Case If");
  //       int offset = vEBTreeSuccessor(V.clusters[high(x)], low(x));
  //       System.out.println("high(x): " + high(x));
  //       System.out.println("offset: " + offset);
  //       return index(high(x), offset);
  //     } 
      
  //     // we get here if x either has no successor in its cluster or it is greater than the maximum element in its cluster
  //     else {
  //       System.out.println("Last Case Else");
  //       int succeedingClusterIndex = vEBTreeSuccessor(V.summary, high(x));
  //       System.out.println(V.summary);
  //       System.out.println("succeedingClusterIndex: " + succeedingClusterIndex);
  //       if (succeedingClusterIndex == -1) {
  //         return -1;
  //       } else {
  //         int offset = vEBMinimum(V.clusters[succeedingClusterIndex]);
  //         return index(succeedingClusterIndex, offset);
  //       }
  //     }
  //   }
  // }

  public int vEBTreePredecessor(int x) {
    return vEBTreePredecessor(this, x);
  }

  // return -1 if predecessor not found
  private int vEBTreePredecessor(VanEmdeBoas V, int x) {
    if (V.size == 2) {
      if (x == 1 && V.min == 0) {
        return 0;
      } else {
        return -1;
      }
    }

    else if (V.max != -1 && x > V.max) {
      return V.max;
    } else {
      // index into V.clusters where x is to find if low(x) > minimum in this cluster
      int min = vEBMinimum(V.clusters[high(x)]);
      if (min != -1 && low(x) > min) {
        // find predecessor in x's cluster
        int offset = vEBTreePredecessor(V.clusters[high(x)], low(x));

        // piece the value of the predecessor back together 
        return index(high(x), offset);
      } else {
        int predecessorClusterIndex = vEBTreePredecessor(V.summary, high(x));
        if (predecessorClusterIndex == -1) {
          if (V.min != -1 && x > V.min) {
            return V.min;
          } else {
            return -1;
          }
        } else {
          int offset = vEBMaximum(V.clusters[predecessorClusterIndex]);
          return index(predecessorClusterIndex, offset);
        }
      }
    }
  }

  public void vEBDelete(int x) {
    if (x < 0 || x >= size) throw new IllegalArgumentException("x must be between 0 and size of Van Emde Boas tree minus 1");
    vEBDelete(this, x);
  }

  public int deleteMin() {
    int currMin = this.min;
    vEBDelete(this, this.min);
    return currMin;
  }

  private void vEBDelete(VanEmdeBoas V, int x) {
    if (V.max == V.min) {
      V.min = -1;
      V.max = -1;
    } else if (V.size == 2) {
      if (x == 0) {
        V.min = 1;
      } else {
        V.min = 0;
      }
      V.max = V.min;
    } else {
      if (x == V.min) {
        int firstMinimumCluster = vEBMinimum(V.summary);
        x = index(firstMinimumCluster, vEBMinimum(V.clusters[firstMinimumCluster]));
        V.min = x;
      }

      vEBDelete(V.clusters[high(x)], low(x));

      // x's cluster is now empty since it was the only element
      if (vEBMinimum(V.clusters[high(x)]) == -1) {

        // x's cluster is empty so remove from V.summary
        vEBDelete(V.summary, high(x));

        // it is possible we are deleting the max element in V (since we could have skipped the x == V.min if clause) so we have to handle this case
        if (x == V.max) {
          int maxInSummary = vEBMaximum(V.summary);
          if (maxInSummary == -1) {
            V.max = V.min;
          }
          else {
            V.max = index(maxInSummary, vEBMaximum(V.clusters[maxInSummary]));
          }
        }
      }

      // x was not the only element in its cluster, so we need to update V.max
      else if (x == V.max) {
        V.max = index(high(x), vEBMaximum(V.clusters[high(x)]));
      } 
    }
  }

  public int vEBMinimum(VanEmdeBoas V) {
    return V.min;
  }

  public int vEBMaximum(VanEmdeBoas V) {
    return V.max;
  }

  public boolean vEBTreeMember(int x) {
    if (x < 0 || x >= size) throw new IllegalArgumentException("x must be between 0 and size of Van Emde Boas tree minus 1");
    return vEBTreeMember(this, x);
  }

  private boolean vEBTreeMember(VanEmdeBoas V, int x) {
    if (x == V.min || x == V.max) return true;

    // in base case but x != V.min and x != V.max
    else if (V.size == 2) return false;

    else {
      return vEBTreeMember(V.clusters[high(x)], low(x));
    }
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append(Integer.toString(size));
    str.append("\n");
    for (VanEmdeBoas V : clusters) {
      str.append("size: " + V.size + " min: " + V.min + " max: " + V.max);
      str.append(" ");
    }
    return str.toString();
  }

	private int high(int x)
	{
		return (int)Math.floor(x / lowerSquareRoot());
	}
	
	private int low(int x)
	{
		return x % (int)lowerSquareRoot();
	}
	
	
	/*
	 * Returns the value of the least significant bits of x.
	 */
	private double lowerSquareRoot()
	{
		/* In a more mathetmatical statement: of 2 ^ floor((log2size) / 2) 
       This gives us the lower square root bits value 
    */
		return Math.pow(2, Math.floor((Math.log10(size) / Math.log10(2)) / 2.0));
	}
	
	
	private int index(int x, int y)
	{
		return (int)(x * lowerSquareRoot() + y);
	}
  
}