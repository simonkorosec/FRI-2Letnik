package compiler.abstr;

import compiler.abstr.tree.*;

/**
 * @author sliva
 */
public interface Visitor {

    void visit(AbsArrType acceptor);

    void visit(AbsAtomConst acceptor);

    void visit(AbsAtomType acceptor);

    void visit(AbsBinExpr acceptor);

    //  public void visit(AbsDef        acceptor);
    void visit(AbsDefs acceptor);

    //  public void visit(AbsExpr       acceptor);
    void visit(AbsExprs acceptor);

    void visit(AbsFor acceptor);

    void visit(AbsFunCall acceptor);

    void visit(AbsFunDef acceptor);

    void visit(AbsIfThen accpetor);

    void visit(AbsIfThenElse accpetor);

    void visit(AbsPar acceptor);

    //  public void visit(AbsTree       acceptor);
//  public void visit(AbsType       acceptor);
    void visit(AbsTypeDef acceptor);

    void visit(AbsTypeName acceptor);

    void visit(AbsUnExpr acceptor);

    void visit(AbsVarDef acceptor);

    void visit(AbsVarName acceptor);

    void visit(AbsWhere acceptor);

    void visit(AbsWhile acceptor);


}
