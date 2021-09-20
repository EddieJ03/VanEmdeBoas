class Main {
  public static void main(String[] args) {
    VanEmdeBoas myTree = new VanEmdeBoas(32);
    myTree.vEBInsert(1);
    myTree.vEBInsert(2);
    myTree.vEBInsert(3);
    myTree.vEBInsert(4);
    myTree.vEBInsert(13);
    myTree.vEBInsert(15);
    System.out.println(myTree.vEBTreeMember(3));

    // Fix Successor
    // System.out.println(myTree.vEBTreeSuccessor(3));

    myTree.vEBDelete(15);
    System.out.println(myTree.vEBTreeMember(15));

    
  }
}