public class Driver {

    static void check(boolean flag, String message) {
	if (!flag) {
	    System.out.println("ERROR: " + message);
	    assert false;
	}
    }
	
    public static void main(String[] args) {
        String[] strings = new String[128];
        for (int i = 0; i < strings.length; ++i) strings[i] = "String " + i;

        Multiset<Integer> multiset1 = new Multiset<Integer>(4);
        try {
            for (int i = 0; i < 5; ++i) multiset1.add(i);
            check(false,"Seems able to put 5 different elements in a multiset of size 4");
        } catch (MultisetFullException e) {
            // do nothing
        }

        boolean result = multiset1.size() == 4;
        check(result, "Size or add seem broken");
	
	try {
            multiset1.add(2);
        } catch (MultisetFullException e) {
            check(false, "Not able to add an existing element to a multiset at maximum capacity.");
        }

        result = multiset1.size() == 5;
        check(result, "Size or add seem broken");
	
        Multiset<String> multiset2 = new Multiset<String>(40);
        for (int i = 10; i < 50; ++i) multiset2.add(strings[i]);
	multiset2.add(strings[42]);
        for (int i = 10; i < 50; ++i) {
            result = multiset2.contains("String " + i) > 0;
            check(result, "Either contains or add is broken");
        }
        result = multiset2.contains("NoSuchString") == 0;
        check(result, "Contains seems broken");

        result = multiset2.contains(strings[42]) == 2;
	      check(result, "Contains seems broken");

        result = multiset2.size() == 41;
	      check(result, "Size or add seem broken");

        Object o1 = multiset2;
        //result = o1.equals(o1);
	      check(result, "Equals broken -- an object is not equal to itself!"); 
        
        
        Multiset<String> multiset3 = new Multiset<String>(40);
        Object o2 = multiset3;
        //result = o1.equals(o2);
        assert result== false: "Equals broken -- non-empty multiset equal to empty multiset!"; 

        for (int i = 49; i >= 10; --i) multiset3.add(strings[i]);

        result = o1.equals(o2);
        check(!result, "Equals broken -- two multisets with same elements but different multiplicities are equal");

	
	multiset3.add(strings[42]);
	
        result = o1.equals(o2);
	check(result, "Equals broken -- two multisets with same elements and multiplicities are not equal");

        System.err.println("If you can read this, and there are no error messages above, then all tests pass!");
    }
}
