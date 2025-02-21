/*
  NOTE!!
  This is the only file you are allowed to make changes too as it
  is the only file that is handed in by make handin!

  To help you, this file is divided into four parts:

  0. Additions -- a space for adding any new classes needed
  1. Dirty extensions -- classes whose methods you will likely have to change
  2. Clean extensions -- classes that you will likely have to add new methods to
  3. Ignore -- stuff you can likely safely ignore

*/

////////////////////////////////////////////////////////////////////////////////
/// ADDITIONS //////////////////////////////////////////////////////////////////

class IfThenElse extends Statement {
  /// TODO
  private Expression guard;
  private Statement thenBody;
  private Statement elseBody;
    
  public IfThenElse(Expression guard,
                    Statement thenBody,
                    Statement elseBody) {
    /// TODO
    this.guard = guard;
    this.thenBody = thenBody;
    this.elseBody = elseBody;
  }

  public Constant eval(Environment env) {
    Constant g = guard.eval(env);
    if ((int)g.value() != 0) {
      return thenBody.eval(new Environment(env));
    }
    else
      return elseBody.eval(new Environment(env));
  }

}

////////////////////////////////////////////////////////////////////////////////
/// DIRTY EXTENSIONS ///////////////////////////////////////////////////////////

/// You don't need to know anything about tree maps other than
/// the put and get methods which are used in the code below
class Environment extends java.util.TreeMap<String, Constant> {
  private Environment next = null;

  public Environment() {
    /// Keep this method -- no need to change it though
  }
    
  public Environment(Environment next) {
    /// TODO
    this.next = next;
  }
    
  public boolean hasVariable(String variableName) {
    if (hasVariableShallow(variableName)) {
      return true;
    } else {
      if (next.hasVariable(variableName)) {
        return true;
      }
      return false;
    }
  }

  public void updateVariable(String variableName, Constant value) {
    /// TODO: change this logic when environment changes
    if (!hasVariableShallow(variableName)) {
      next.updateVariable(variableName, value);
    } else 
      this.put(variableName, value);
  }
  public Constant readVariable(String variableName) {

    if (!hasVariableShallow(variableName)) {
      return next.readVariable(variableName);
    }
    return this.get(variableName);
    /// TODO: change this logic when environment changes
    
  }

  /////////////////////////////////////////////////////////////
  /// You do not need to change any of the methods below
    
  /// Has variable shallow just looks in the current scope
  private boolean hasVariableShallow(String variableName) {
    return this.get(variableName) != null;
  }
    
  /////////////////////////////////////////////////////////////
  /// You should not need to look at any of the metods below
    
  public void declareVariable(String variableName, Constant value) {
    this.put(variableName, value);
  }

  /// Needed for the dumping of scopes
  public String dumpScopes() {
    return this.dumpScopes("");
  }

  public String dumpScopes(String inner) {
    StringBuilder sb = new StringBuilder("Scope{ ");
    boolean skipComma = inner.equals("");

    sb.append(inner);

    for (String var : keySet()) {
      if (skipComma) {
        skipComma = false;
      } else {
        sb.append(", ");
      }
      sb.append(var);
      sb.append(" => ");
      sb.append(this.get(var));
      sb.append(" ");
    }

    sb.append("} ");

    if (this.next == null) {
      return sb.toString();
    } else {
      return this.next.dumpScopes(sb.toString());
    }
  }
    
}

////////////////////////////////////////////////////////////////////////////////
/// CLEAN EXTENSIONS ///////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////
/// IGNORE! ////////////////////////////////////////////////////////////////////

// Required by Java so we can name the file YourCode.java 
public class YourCode {}

