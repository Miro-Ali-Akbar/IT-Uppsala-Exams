////////////////////////////////////////////////////////////////////////////////
/// HELP CLASSES ///////////////////////////////////////////////////////////////

class Environment extends java.util.TreeMap<String, Constant> {
    private Environment() {}
    private static Environment instance = new Environment();

    public static Environment instance() {
        return Environment.instance;
    }
    public static void fresh() {
        Environment.instance = new Environment();
    }
    
    public boolean hasVariable(String variableName) {
        return this.get(variableName) != null;
    }
    public void updateVariable(String variableName, Constant value) {
        this.put(variableName, value);
    }
    public Constant readVariable(String variableName) {
        return this.get(variableName);
    }
}

////////////////////////////////////////////////////////////////////////////////
/// STATEMENTS /////////////////////////////////////////////////////////////////

/// A statement does not return a value, e.g. x = 42; or print(42);
/// This is modelled by all statements returning Bottom
abstract class Statement {
    public Constant eval() {
        return new Bottom();
    }
}

/// id = source;
class Assignment extends Statement {
    final Expression source;
    final String id;

    public Assignment(String id, Expression source) {
        this.id = id; 
        this.source = source;
    }

    public Constant eval() {
        Environment e = Environment.instance();
        
        if (e.hasVariable(this.id)) {
            e.updateVariable(this.id, this.source.eval());
            return super.eval();
        } else {
            throw new RuntimeException("Attempt to assign undeclared variable");
        }
    }

    public String toString() {
        return this.id + " = " + this.source;
    }
}

// Makes the variable available for updating 
// var x; 
class VariableDeclaration extends Statement {
    final String id;

    public VariableDeclaration(String id) {
        this.id = id; 
    }

    public Constant eval() {
        Environment e = Environment.instance();        
        
        e.updateVariable(this.id, new Bottom());
        return super.eval();
    }

    public String toString() {
        return "var " + this.id;
    }
}

// A sequence of statements, e.g., var x; x = 1; x = x + 1;
class Sequence extends Statement {
    public final Statement[] statements;

    // new Sequence expects a variable number of arguments
    public Sequence(Statement... statements) {
        this.statements = statements;
    }

    public Constant eval() {
        for (Statement s : this.statements) {
            s.eval();
        }
        return super.eval();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Statement s : this.statements) {
            sb.append(s);
            sb.append("; ");
        }
        return sb.toString();
    }
}

// Print the result of an expression, e.g.,
// print(3 + 52)
class Print extends Statement {
    final Expression source;

    public Print(Expression source) {
        this.source = source;
    }

    public Constant eval() {
        System.out.println(this.source.eval());
        return super.eval();
    }

    public String toString() {
        return "print(" + this.source + ")";
    }
}

////////////////////////////////////////////////////////////////////////////////
/// EXPRESSIONS ////////////////////////////////////////////////////////////////

/// An expression returns a value, e.g. 3 or x + 5
abstract class Expression extends Statement {
    public String asString() {
        return this.toString();
    }
}

class Variable extends Expression {
    final String id;

    public Variable(String id) {
        this.id = id; 
    }

    public Constant eval() {
        Environment e = Environment.instance();
        
        Constant result = e.readVariable(this.id);
        if (result == null) {
            throw new RuntimeException("Access to undeclared variable!");
        } else {
            return result;
        }
    }

    public String toString() {
        return this.id;
    }

    public String asString() {
        return this.id.toString();
    }
}

abstract class Binary extends Expression {
    public static final int ADDITION = 0;
    public static final int SUBTRACTION = 1;
    
    final Expression left;
    final Expression right;

    public Binary(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public abstract int operand();

    public Constant eval() {
        Constant left = this.left.eval();
        Constant right = this.right.eval();
        
        switch (this.operand()) {
        case Binary.ADDITION:
            return Calculation.add(left, right);
        case Binary.SUBTRACTION:
            return Calculation.sub(left, right);
        default:
            throw new RuntimeException("Unknown operand in binary expression!");
        }
    }

    public String toString() {
        return this.left + (operand() == ADDITION ? " + " : " - ") + this.right;
    }

    public String asString() {
        return this.left.asString() + (operand() == ADDITION ? " + " : " - ") + this.right.asString();
    }
}

class Addition extends Binary  {
    public Addition(Expression left, Expression right) {
        super(left, right);
    }
    
    public int operand() {
        return Binary.ADDITION;
    }
}

class Subtraction extends Binary  {
    public Subtraction(Expression left, Expression right) {
        super(left, right);
    }
    
    public int operand() {
        return Binary.SUBTRACTION;
    }
}

abstract class Constant extends Expression {
    private final Object value;  

    public Constant(Object value) {
        this.value = value;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isBottom() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isStringLiteral() {
        return false;
    }

    public double floatValue() {
        throw new RuntimeException("floatValue called on non-float"); 
    }
    
    public String stringValue() {
        return this.value().toString();
    }

    public int intValue() {
        throw new RuntimeException("intValue called on non-int"); 
    }
    
    public Constant eval() {
        return this;
    }

    public Object value() {
        return this.value; 
    }
    
    public String toString() {
        return this.value().toString(); 
    }
}

class Bottom extends Constant {
    public Bottom() {
        super(null);
    }
    
    public boolean isBottom() {
        return true;
    }

    public Number value() {
        throw new RuntimeException("_|_ used as a value");
    }

    public String toString() {
        return "<bottom>";
    }
}

class StringLiteral extends Constant {
    public StringLiteral(String s) {
        super(s.replaceAll("'", "\\\\'"));
    }

    public boolean isStringLiteral() {
        return true;
    }

    public String toString() {
        return "'" + super.toString() + "'"; 
    }
}

class NumberConstant extends Constant {
    public NumberConstant(Number value) {
        super(value);
    }

    public Number value() {
        return (Number) super.value();
    }
    
    public int intValue() {
        return this.value().intValue(); 
    }

    public double floatValue() {
        return this.value().doubleValue(); 
    }
}

class Integer extends NumberConstant {
    public Integer(int value) {
        super(value);
    }

    public boolean isInteger() {
        return true;
    }
}

class Float extends NumberConstant {
    public Float(double value) {
        super(value);
    }

    public boolean isFloat() {
        return true;
    }
}

////////////////////////////////////////////////////////////////////////////////
/// IGNORE /////////////////////////////////////////////////////////////////////

class Calculation {
    public static Constant add(Constant a, Constant b) {
        if (a.isInteger() && b.isInteger()) {
            return new Integer(a.intValue() + b.intValue());
        } else if ((a.isStringLiteral() && b.isStringLiteral()) ||
                   (a.isStringLiteral() && b.isInteger()) ||
                   (a.isStringLiteral() && b.isFloat())) {
            return new StringLiteral(a.stringValue() + b.stringValue());
        } else if ((a.isFloat() && b.isFloat()) ||
                   (a.isFloat() && b.isInteger()) ||
                   (b.isFloat() && a.isInteger())) {
            return new Float(a.floatValue() + b.floatValue());
        } else {
            throw new RuntimeException("Bottom used as a value!");
        }
    }

    public static Constant sub(Constant a, Constant b) {
        if (a.isInteger() && b.isInteger()) {
            return new Integer(a.intValue() - b.intValue());
        } else if ((a.isFloat() && b.isFloat()) ||
                   (a.isFloat() && b.isInteger()) ||
                   (b.isFloat() && a.isInteger())) {
            return new Float(a.floatValue() - b.floatValue());
        } else if (a.isStringLiteral() || b.isStringLiteral()) {
            throw new RuntimeException("Strings do not support subtraction!");
        } else {
            throw new RuntimeException("Bottom used as a value!");
        }
    }
}

// Required by Java so we can name the file ASTFixed.java 
public class ASTFixed {}

