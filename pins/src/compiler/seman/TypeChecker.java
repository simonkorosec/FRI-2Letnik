package compiler.seman;

import compiler.abstr.Visitor;
import compiler.abstr.tree.*;
import compiler.seman.type.SemArrType;
import compiler.seman.type.SemAtomType;
import compiler.seman.type.SemType;
import compiler.seman.type.SemTypeName;


/**
 * Preverjanje tipov.
 *
 * @author sliva
 */
public class TypeChecker implements Visitor {
    @Override
    public void visit(AbsArrType acceptor) {
        acceptor.type.accept(this);
        SymbDesc.setType(acceptor, new SemArrType(acceptor.length, SymbDesc.getType(acceptor.type)));
    }

    @Override
    public void visit(AbsAtomConst acceptor) {
        SymbDesc.setType(acceptor, new SemAtomType(acceptor.type));
    }

    @Override
    public void visit(AbsAtomType acceptor) {
        SymbDesc.setType(acceptor, new SemAtomType(acceptor.type));
    }

    @Override
    public void visit(AbsBinExpr acceptor) {
        acceptor.expr1.accept(this);
        acceptor.expr2.accept(this);

        SemType typLeft = SymbDesc.getType(acceptor.expr1);
        SemType typRight = SymbDesc.getType(acceptor.expr2);

//        switch (acceptor.)
    }

    @Override
    public void visit(AbsDefs acceptor) {
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsTypeDef) {
                SymbDesc.setType(def, new SemTypeName(((AbsTypeDef) def).name));
            }
        }
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsTypeDef) {
                ((AbsTypeDef) def).type.accept(this);
                SymbDesc.setType(def, SymbDesc.getType(((AbsTypeDef) def).type));
            }
        }
    }

    @Override
    public void visit(AbsExprs acceptor) {

    }

    @Override
    public void visit(AbsFor acceptor) {

    }

    @Override
    public void visit(AbsFunCall acceptor) {

    }

    @Override
    public void visit(AbsFunDef acceptor) {

    }

    @Override
    public void visit(AbsIfThen accpetor) {

    }

    @Override
    public void visit(AbsIfThenElse accpetor) {

    }

    @Override
    public void visit(AbsPar acceptor) {

    }

    @Override
    public void visit(AbsTypeDef acceptor) {
        acceptor.type.accept(this);
        SymbDesc.setType(acceptor, SymbDesc.getType(acceptor.type));
    }

    @Override
    public void visit(AbsTypeName acceptor) {
        AbsDef nameDef = SymbDesc.getNameDef(acceptor);
        SymbDesc.setType(acceptor, SymbDesc.getType(nameDef));
    }

    @Override
    public void visit(AbsUnExpr acceptor) {

    }

    @Override
    public void visit(AbsVarDef acceptor) {
        acceptor.type.accept(this);
        SymbDesc.setType(acceptor, SymbDesc.getType(acceptor.type));
    }

    @Override
    public void visit(AbsVarName acceptor) {
        SymbDesc.setType(acceptor, SymbDesc.getType(SymbDesc.getNameDef(acceptor)));
    }

    @Override
    public void visit(AbsWhere acceptor) {

    }

    @Override
    public void visit(AbsWhile acceptor) {

    }


}

