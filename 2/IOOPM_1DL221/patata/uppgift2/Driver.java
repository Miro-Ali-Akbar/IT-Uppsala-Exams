public class Driver {
  private static Statement decl(String id) { return new VariableDeclaration(id); }
  private static Statement assign(String id, Expression e) { return new Assignment(id, e); }
  private static Statement print(Expression e) { return new Print(e); }
  private static Statement seq(Statement... ss) { return new Sequence(ss); }

  private static Expression var(String id) { return new Variable(id); }
  private static Expression add(Expression e1, Expression e2) { return new Addition(e1, e2); }
  private static Expression sub(Expression e1, Expression e2) { return new Subtraction(e1, e2); }
  private static Expression quote(Expression e) { return new Quote(e); }
  private static Expression unquote(Expression e) { return new Unquote(e); }

  private static Expression i(int i) { return new Integer(i); }
  private static Expression f(double f) { return new Float(f); } 
  private static Expression b() { return new Bottom(); }
  private static Expression s(String s) { return new StringLiteral(s); }

  private static void execute(String msg, Statement s) {
    try {
      System.out.print(msg + ": ");
      System.out.print(s + " = ");
      System.out.println(s.eval());
    } catch (Exception e) {
      System.out.println("A/An " + e.getClass().getName() + " was thrown (" + e.getMessage() + ")");
    }
  }
    
  public static void test1() {
    Statement s = quote(i(3));
    execute("test1", s);
  }
  public static void test2() {
    Statement s = quote(add(i(3), var("x")));
    execute("test2", s);
  }
  public static void test3() {
    Statement s = quote(sub(s("Hej"), i(4)));
    execute("test3", s);
  }
  public static void test4() {
    Statement s = quote(quote(var("x")));
    execute("test4", s);
  }
  public static void test5() {
    Statement s = quote(unquote(add(i(3), var("x"))));
    execute("test5", s);
  }
  public static void test6() {
    Statement s = quote(add(i(3), unquote(var("x"))));
    execute("test6", s);
  }
  public static void test7() {
    Statement s = quote(unquote(quote(var("x"))));
    execute("test7", s);
  }
  public static void test8() {
    Statement s = quote(quote(unquote(var("x"))));
    execute("test8", s);
  }
  public static void test9() {
    Statement s = quote(unquote(unquote(var("x"))));
    execute("test9", s);
  }

  public static void test10() {
    Statement s = unquote(var("x"));
    execute("test10", s);
  }

  public static void main(String[] args) {
    Environment.fresh();
    seq(decl("x"), assign("x", i(5))).eval();

    /// Make sure toString() is properly implemented before we test!
    System.out.println("Quote(var(\"x\"))::toString(): " + quote(var("x")));
    System.out.println("Unquote(var(\"x\"))::toString(): " + unquote(var("x")));

    test1();
    test2();
    test3();
    test4();
    test5();
    test6();
    test7();
    test8();
    test9();
    test10();
  }
}
