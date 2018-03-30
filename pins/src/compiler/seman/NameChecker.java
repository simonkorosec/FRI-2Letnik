package compiler.seman;

import compiler.Report;
import compiler.abstr.*;
import compiler.abstr.tree.*;

/**
 * Preverjanje in razresevanje imen (razen imen komponent).
 * 
 * @author sliva
 */
public class NameChecker implements Visitor {

    @Override
    public void visit(AbsArrType acceptor) {
        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsAtomConst acceptor) {/* Pass */}

    @Override
    public void visit(AbsAtomType acceptor) { /* Pass */}

    @Override
    public void visit(AbsBinExpr acceptor) {
        acceptor.expr1.accept(this);
        acceptor.expr2.accept(this);
    }

    @Override
    public void visit(AbsDefs acceptor) {
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);

            if (def instanceof AbsVarDef) {
                AbsVarDef var = (AbsVarDef) def;
                try {
                    SymbTable.ins(var.name, var);
                } catch (SemIllegalInsertException e) {
                    Report.error(var.position, String.format("Variable '%s' alredy definded.", var.name));
                }
            } else if (def instanceof AbsFunDef) {
                AbsFunDef fun = (AbsFunDef) def;
                try {
                    SymbTable.ins(fun.name, fun);
                } catch (SemIllegalInsertException e) {
                    Report.error(fun.position, String.format("Function '%s' alredy definded.", fun.name));
                }
            } else if (def instanceof AbsTypeDef) {
                AbsTypeDef typ = (AbsTypeDef) def;
                try {
                    SymbTable.ins(typ.name, typ);
                } catch (SemIllegalInsertException e) {
                    Report.error(typ.position, String.format("Type '%s' alredy definded.", typ.name));
                }
            }
        }

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
        AbsDef funName = SymbTable.fnd(acceptor.name);

        if (!(funName instanceof AbsFunDef)) {
            Report.error(acceptor.position, String.format("Function '%s' not definded.", funName));
        }

        SymbDesc.setNameDef(acceptor, funName);

        for (int i = 0; i < acceptor.numArgs(); i++) {
            acceptor.arg(i).accept(this);
        }
    }

    @Override
    public void visit(AbsFunDef acceptor) {
        SymbTable.newScope();

        for (int i = 0; i < acceptor.numPars(); i++) {
            acceptor.par(i).accept(this);
        }

        acceptor.type.accept(this);
        acceptor.expr.accept(this);

        SymbTable.oldScope();
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
        try {
            SymbTable.ins(acceptor.name, acceptor);
            acceptor.type.accept(this);
        } catch (SemIllegalInsertException e) {
            Report.error(acceptor.position, String.format("Parameter '%s' redifined.", acceptor.name));
        }
    }

    @Override
    public void visit(AbsTypeDef acceptor) {
        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsTypeName acceptor) {
        AbsDef typDef = SymbTable.fnd(acceptor.name);

        if (!(typDef instanceof AbsTypeDef)) {
            Report.error(acceptor.position, String.format("Type '%s' undefined.", acceptor.name));
        }

        SymbDesc.setNameDef(acceptor, typDef);

    }

    @Override
    public void visit(AbsUnExpr acceptor) {
        acceptor.expr.accept(this);
    }

    @Override
    public void visit(AbsVarDef acceptor) {
        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsVarName acceptor) {
        AbsDef varDef = SymbTable.fnd(acceptor.name);

        if (!(varDef instanceof AbsVarDef || varDef instanceof AbsPar)) {
            Report.error(acceptor.position, String.format("Variable '%s' undefined.", acceptor.name));
        }

        SymbDesc.setNameDef(acceptor, varDef);
    }

    @Override
    public void visit(AbsWhere acceptor) {
        SymbTable.newScope();

        acceptor.defs.accept(this);
        acceptor.expr.accept(this);

        SymbTable.oldScope();
    }

    @Override
    public void visit(AbsWhile acceptor) {
        acceptor.cond.accept(this);
        acceptor.body.accept(this);
    }

}
