public class Driver {
    private static Statement decl(String id) { return new VariableDeclaration(id); }
    private static Statement assign(String id, Expression e) { return new Assignment(id, e); }
    private static Statement print(Expression e) { return new Print(e); }
    private static Statement seq(Statement... ss) { return new Sequence(ss); }
    private static Statement ifThenElse(Expression guard, Statement thenBody, Statement elseBody) {
        return new IfThenElse(guard, thenBody, elseBody);
    }
    private static Statement dump() { return new DumpScopes(); }

    private static Expression var(String id) { return new Variable(id); }
    private static Expression add(Expression e1, Expression e2) { return new Addition(e1, e2); }
    private static Expression sub(Expression e1, Expression e2) { return new Subtraction(e1, e2); }

    private static Constant i(int i) { return new Integer(i); }
    private static Constant f(double i) { return new Float(i); }
    private static Constant b() { return new Bottom(); }
    private static Constant s(String s) { return new StringLiteral(s); }

    private static void execute(String msg, Statement s) {
        System.out.print(msg + "\t");
        try {
            s.eval(new Environment());
        } catch (EvaluationException e) {
            System.out.println("A/An " + e.getClass().getName() + " was thrown (" + e.getMessage() + ")");
            e.printStackTrace(System.err);
        }
    }

    public static void test1() {
        Environment env = new Environment();
        env.declareVariable("x", i(1));
        dump().eval(env);
    }
    
    public static void test2() {
        Environment inner = new Environment();
        inner.declareVariable("x", i(1));
        inner.declareVariable("y", i(1));
        Environment outer = new Environment(inner);
        outer.declareVariable("x", i(2));
        dump().eval(outer);
    }
    
    public static void test3() {
        Environment inner = new Environment();
        inner.declareVariable("x", i(1));
        inner.declareVariable("y", i(1));
        Environment outer = new Environment(inner);
        outer.declareVariable("x", i(2));
        print(var("x")).eval(outer);
    }

    public static void test4() {
        Environment inner = new Environment();
        inner.declareVariable("x", i(1));
        inner.declareVariable("y", i(1));
        Environment outer = new Environment(inner);
        outer.declareVariable("x", i(2));
        print(var("y")).eval(outer);
    }

    public static void test5() {
        Environment inner = new Environment();
        inner.declareVariable("x", i(1));
        inner.declareVariable("y", i(1));
        Environment outer = new Environment(inner);
        outer.declareVariable("x", i(2));
        assign("x", i(3)).eval(outer);
        assign("y", i(3)).eval(outer);
        dump().eval(outer);
    }

    public static void test6() {
        Environment env = new Environment();
        env.declareVariable("x", i(1));
        ifThenElse(i(1), assign("x", s("then")), assign("x", s("else"))).eval(env);
        print(var("x")).eval(env);
    }

    public static void test7() {
        Environment env = new Environment();
        env.declareVariable("x", i(1));
        ifThenElse(i(0), assign("x", s("then")), assign("x", s("else"))).eval(env);
        print(var("x")).eval(env);
    }

    public static void test8() {
        Environment env = new Environment();
        env.declareVariable("x", i(1));
        ifThenElse(i(1),
                   seq(decl("x"), assign("x", s("then"))),
                   seq(decl("x"), assign("x", s("else")))).eval(env);
        print(var("x")).eval(env);
    }
    
    public static void test9() {
        Environment env = new Environment();
        env.declareVariable("x", i(1));
        ifThenElse(i(0),
                   seq(decl("x"), assign("x", s("then"))),
                   seq(decl("x"), assign("x", s("else")))).eval(env);
        print(var("x")).eval(env);
    }
    
    public static void test10() {
        Statement s = seq(print(s("prints follow")),
                          decl("x"),                                 /// var x;    
                          decl("y"),                                 /// var y;    
                          assign("x", i(42)),                        /// x = 42;    
                          assign("y", i(0)),                         /// y = 0;    
                          ifThenElse(var("y"),                       /// if (y) {
                                     seq(assign("x",                 ///   x = x + 1
                                                add(var("x"), i(1))),
                                         decl("x"),                  ///   var x;
                                         assign("x", i(1)),          ///   x = 1;    
                                         assign("x",                 ///   x = x + 1
                                                add(var("x"), i(1))),
                                         print(add(s("thenBody: "), var("x"))),
                                         dump()),
                                                                     /// } else {
                                     seq(assign("x",                 ///   x = x + 2
                                                add(var("x"), i(2))),
                                         print(add(s("elseBody: "), var("x"))),
                                         dump())),
                                                                     /// } 
                          dump());
        execute("Test 10", s);
    }

    public static void test11() {
        Statement s = seq(print(s("prints follow")),
                          decl("x"),                                 /// var x;    
                          decl("y"),                                 /// var y;    
                          assign("x", i(42)),                        /// x = 42;    
                          assign("y", i(1)),                         /// y = 1;
                          ifThenElse(var("y"),                       /// if (y) {
                                     seq(assign("x",                 ///   x = x + 1
                                                add(var("x"), i(1))),
                                         decl("x"),                  ///   var x;
                                         assign("x", i(1)),          ///   x = 1;    
                                         assign("x",                 ///   x = x + 1
                                                add(var("x"), i(1))),
                                         print(add(s("thenBody: "), var("x"))),
                                         dump()),
                                                                     /// } else {
                                     seq(assign("x",                 ///   x = x + 2
                                                add(var("x"), i(2))),
                                         print(add(s("elseBody: "), var("x"))),
                                         dump())),
                                                                     /// } 
                          dump());
        
        execute("Test 11", s);
    }

    public static void test12() {
        Statement printAndIncrementX1 = seq(decl("x"),
                                            assign("x", i(10)),
                                            assign("x", add(var("x"), i(10))));
        Statement printAndIncrementX2 = seq(decl("x"),
                                            assign("x", s("shadowed ")),
                                            assign("x", add(var("x"), s("from inner"))));
        Statement innerIf = ifThenElse(i(1),
                                       seq(print(s("entering inner if")),
                                           dump(),
                                           printAndIncrementX1,
                                           dump(),
                                           assign("y", s("assign from innermost scope")),
                                           print(s("leaving inner if"))),
                                       seq());
        Statement outerIf = ifThenElse(i(1),
                                       seq(print(s("entering outer if")),
                                           dump(),
                                           printAndIncrementX2,
                                           innerIf,
                                           dump(),
                                           print(s("entering outer if"))),
                                       seq());
        
        Statement s = seq(print(s("top-level scope")),
                          decl("x"), 
                          decl("y"), 
                          assign("x", i(1)), 
                          assign("y", i(1)),
                          dump(),
                          outerIf,
                          dump());
        
        execute("Test 12", s);
    }

    public static void test13() {
        Constant c = ifThenElse(i(1), seq(), seq()).eval(new Environment());

        System.out.println("Test 13\tIfThenElse evaluates to " + c);
    }
    
    public static void main(String[] args) {
        System.out.print("Test 1\t");
        test1();
        System.out.print("Test 2\t");
        test2();
        System.out.print("Test 3\t"); 
        test3();
        System.out.print("Test 4\t");
        test4();
        System.out.print("Test 5\t"); 
        test5();
        System.out.print("Test 6\t"); 
        test6();
        System.out.print("Test 7\t"); 
        test7();
        System.out.print("Test 8\t"); 
        test8();
        System.out.print("Test 9\t");
        test9();
        System.out.println("Integration tests follow -- hard to use these to debug, sorry");
        test10();
        test11();
        test12();
        test13();
    }
}
