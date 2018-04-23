package compiler.frames;

import compiler.abstr.*;
import compiler.abstr.tree.*;
import compiler.seman.SymbDesc;
import compiler.seman.type.SemFunType;

import java.util.Stack;

public class FrmEvaluator implements Visitor {

    private static int level = 1;
    private static Stack<FrmFrame> frames = new Stack<>();

    @Override
    public void visit(AbsArrType acceptor) {}

    @Override
    public void visit(AbsAtomConst acceptor) {}

    @Override
    public void visit(AbsAtomType acceptor) {}

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
        acceptor.lo.accept(this);
        acceptor.hi.accept(this);
        acceptor.step.accept(this);
        acceptor.body.accept(this);
    }

    @Override
    public void visit(AbsFunCall acceptor) {
        FrmFrame frame = frames.peek();
        int size = 4;

        for (int i = 0; i < acceptor.numArgs(); i++) {
            size += SymbDesc.getType(acceptor.arg(i)).size();
        }
        int r = ((SemFunType) SymbDesc.getType(SymbDesc.getNameDef(acceptor))).resultType.size();

        size = r > size ? r : size;
        frame.sizeArgs = size > frame.sizeArgs ? size : frame.sizeArgs;
    }

    @Override
    public void visit(AbsFunDef acceptor) {
        frames.push(new FrmFrame(acceptor, level));
        level++;

        for (int i = 0; i < acceptor.numPars(); i++) {
            acceptor.par(i).accept(this);
        }
        acceptor.expr.accept(this);

        FrmDesc.setFrame(acceptor, frames.peek());

        level--;
        frames.pop();
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
        FrmDesc.setAccess(acceptor, new FrmParAccess(acceptor, frames.peek()));
    }

    @Override
    public void visit(AbsTypeDef acceptor) {}

    @Override
    public void visit(AbsTypeName acceptor) {}

    @Override
    public void visit(AbsUnExpr acceptor) {
        acceptor.expr.accept(this);
    }

    @Override
    public void visit(AbsVarDef acceptor) {
        if (level == 1) {
            FrmDesc.setAccess(acceptor, new FrmVarAccess(acceptor));
        } else {
            FrmDesc.setAccess(acceptor, new FrmLocAccess(acceptor, frames.peek()));
            frames.peek().locVars.add((FrmLocAccess) FrmDesc.getAccess(acceptor));
        }
    }

    @Override
    public void visit(AbsVarName acceptor) {}

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