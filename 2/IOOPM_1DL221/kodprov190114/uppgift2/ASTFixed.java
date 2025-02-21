////////////////////////////////////////////////////////////////////////////////
/// HELP CLASSES ///////////////////////////////////////////////////////////////

/// Holds all the logic for + and - and is used by Addition and Subtraction
class Calculation {
    public static Constant add(Constant a, Constant b) {
        if (a.isInteger() && b.isInteger()) {
            return new Integer(a.intValue() + b.intValue());
        } else if ((a.isString() && b.isString()) || (a.isString() && b.isInteger()) || (b.isString() && a.isFloat())) {
            return new StringLiteral(a.stringValue() + b.stringValue());
        } else if ((a.isFloat() && b.isFloat()) || (a.isFloat() && b.isInteger()) || (b.isFloat() && a.isInteger())) {
            return new Float(a.floatValue() + b.floatValue());
        } else {
            System.err.println(a.getClass());
            System.err.println(b.getClass());
            throw new EvaluationException("Bottom used as a value!");
        }
    }

    public static Constant sub(Constant a, Constant b) {
        if (a.isInteger() && b.isInteger()) {
            return new Integer(a.intValue() - b.intValue());
        } else if (a.isFloat() && b.isFloat() || (a.isFloat() && b.isInteger()) || (b.isFloat() && a.isInteger())) {
            return new Float(a.floatValue() - b.floatValue());
        } else if (a.isString() || b.isString()) {
            throw new EvaluationException("Strings do not support subtraction!");
        } else {
            throw new EvaluationException("Bottom used as a value!");
        }
    }
}

class EvaluationException extends RuntimeException {
    public EvaluationException(String s) {
        super(s);
    }
}

////////////////////////////////////////////////////////////////////////////////
/// STATEMENTS /////////////////////////////////////////////////////////////////

/// A statement does not return a value, e.g. x = 42; or print(42);
/// This is modelled by all statements returning Bottom
abstract class Statement {
    public Constant eval(Environment e) {
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

    public Constant eval(Environment e) {
        if (e.hasVariable(this.id)) {
            e.updateVariable(this.id, this.source.eval(e));
            return super.eval(e);
        } else {
            throw new EvaluationException("Attempt to assign undeclared variable");
        }
    }
}

// Makes the variable available for updating 
// var x; 
class VariableDeclaration extends Statement {
    final String id;

    public VariableDeclaration(String id) {
        this.id = id; 
    }

    public Constant eval(Environment e) {
        e.declareVariable(this.id, new Bottom());
        return super.eval(e);
    }
}

// A sequence of statements, e.g., var x; x = 1; x = x + 1;
class Sequence extends Statement {
    public final Statement[] statements;

    // new Sequence expects a variable number of arguments
    public Sequence(Statement... statements) {
        this.statements = statements;
    }

    public Constant eval(Environment e) {
        for (Statement s : this.statements) {
            s.eval(e);
        }
        return super.eval(e);
    }
}

// Print the result of an expression, e.g.,
// print(3 + 52)
class Print extends Statement {
    final Expression source;

    public Print(Expression source) {
        this.source = source;
    }

    public Constant eval(Environment e) {
        System.out.println(this.source.eval(e));
        return super.eval(e);
    }
}
// Print the result of an expression, e.g.,
// print(3 + 52)
class DumpScopes extends Statement {
    public Constant eval(Environment e) {
        System.out.println(e.dumpScopes());
        return new Bottom();
    }
}

////////////////////////////////////////////////////////////////////////////////
/// EXPRESSIONS ////////////////////////////////////////////////////////////////

/// An expression returns a value, e.g. 3 or x + 5
abstract class Expression extends Statement {}

class Variable extends Expression {
    final String id;

    public Variable(String id) {
        this.id = id; 
    }

    public Constant eval(Environment e) {
        Constant result = e.readVariable(this.id);
        if (result == null) {
            throw new EvaluationException("Access to undeclared variable '" + this.id + "'!");
        } else {
            return result;
        }
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

    public Constant eval(Environment e) {
        Constant left = this.left.eval(e);
        Constant right = this.right.eval(e);
        
        switch (this.operand()) {
        case Binary.ADDITION:
            return Calculation.add(left, right);
        case Binary.SUBTRACTION:
            return Calculation.sub(left, right);
        default:
            throw new EvaluationException("Unknown operand in binary expression!");
        }
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

    public Constant(Number value) {
        this.value = value;
    }

    public Constant(String value) {
        this.value = value;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBottom() {
        return false;
    }

    public Constant eval(Environment e) {
        return this;
    }

    public double floatValue() {
        throw new EvaluationException("floatValue called on non-float"); 
    }
    
    public String stringValue() {
        throw new EvaluationException("stringValue called on non-string"); 
    }
    
    public int intValue() {
        throw new EvaluationException("inValue called on non-int"); 
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
        super("<bottom>");
    }
    
    public boolean isBottom() {
        return true;
    }

    public Number value() {
        throw new EvaluationException("_|_ used as a value");
    }

    public String toString() {
        return "<bottom>";
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

    public String stringValue() {
        return this.value().toString();
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

class StringLiteral extends Constant {
    public StringLiteral(String s) {
        super(s);
    }

    public boolean isString() {
        return true;
    }

    public String toString() {
        return "'" + super.toString() + "'"; 
    }

    public String stringValue() {
        return (String) this.value();
    }
}


////////////////////////////////////////////////////////////////////////////////
/// IGNORE /////////////////////////////////////////////////////////////////////

// Required by Java so we can name the file ASTFixed.java 
public class ASTFixed {}

