package compiler.frames;

import compiler.abstr.*;
import compiler.abstr.tree.*;
import compiler.seman.SymbDesc;
import compiler.seman.type.SemFunType;

public class FrmEvaluator implements Visitor {
    // TODO

    private static int level = 1;
    private static FrmFrame currFrame = null;

    @Override
    public void visit(AbsArrType acceptor) {
        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsAtomConst acceptor) {
        // Pass
    }

    @Override
    public void visit(AbsAtomType acceptor) {
        // Pass
    }

    @Override
    public void visit(AbsBinExpr acceptor) {
        acceptor.expr1.accept(this);
        acceptor.expr2.accept(this);
    }

    @Override
    public void visit(AbsDefs acceptor) {
        for (int i = 0; i < acceptor.numDefs(); i++) {
            acceptor.def(i).accept(this);
        }

    }

    @Override
    public void visit(AbsExprs acceptor) {
        for (int i = 0; i < acceptor.numExprs(); i++) {
            acceptor.expr(i).accept(this);
        }
    }

    @Override
    public void visit(AbsFor acceptor) {
        acceptor.count.accept(this);
        acceptor.lo.accept(this);
        acceptor.hi.accept(this);
        acceptor.step.accept(this);
        acceptor.body.accept(this);
    }

    @Override
    public void visit(AbsFunCall acceptor) {
        int size = 4;

        for (int i = 0; i < acceptor.numArgs(); i++) {
            acceptor.arg(i).accept(this);
            size += SymbDesc.getType(acceptor.arg(i)).size();
        }
        int r = ((SemFunType)SymbDesc.getType(SymbDesc.getNameDef(acceptor))).resultType.size();
        size = size > r ? size : r;
        currFrame.sizeArgs = size > currFrame.sizeArgs ? size : currFrame.sizeArgs;
    }

    @Override
    public void visit(AbsFunDef acceptor) {
        FrmFrame previusFrame = currFrame;
        currFrame = new FrmFrame(acceptor, level);
        level++;

        for (int i = 0; i < acceptor.numPars(); i++) {
            acceptor.par(i).accept(this);
        }
        acceptor.type.accept(this);
        acceptor.expr.accept(this);

        FrmDesc.setFrame(acceptor, currFrame);

        level--;
        currFrame = previusFrame;
    }

    @Override
    public void visit(AbsIfThen accpetor) {
        accpetor.cond.accept(this);
        accpetor.thenBody.accept(this);
    }

    @Override
    public void visit(AbsIfThenElse accpetor) {
        accpetor.cond.accept(this);
        accpetor.thenBody.accept(this);
        accpetor.elseBody.accept(this);
    }

    @Override
    public void visit(AbsPar acceptor) {
        acceptor.type.accept(this);
        FrmDesc.setAccess(acceptor, new FrmParAccess(acceptor, currFrame));
    }

    @Override
    public void visit(AbsTypeDef acceptor) {
        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsTypeName acceptor) {
        // Pass
    }

    @Override
    public void visit(AbsUnExpr acceptor) {
        acceptor.expr.accept(this);
    }

    @Override
    public void visit(AbsVarDef acceptor) {
        if (level == 1){
            FrmDesc.setAccess(acceptor, new FrmVarAccess(acceptor));
        } else {
            FrmDesc.setAccess(acceptor, new FrmLocAccess(acceptor, currFrame));
            currFrame.locVars.add((FrmLocAccess) FrmDesc.getAccess(acceptor));
        }

        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsVarName acceptor) {
        // Pass
    }

    @Override
    public void visit(AbsWhere acceptor) {
        acceptor.defs.accept(this);
        acceptor.expr.accept(this);
    }

    @Override
    public void visit(AbsWhile acceptor) {
        acceptor.cond.accept(this);
        acceptor.body.accept(this);
    }

}
