package compiler.seman;

import compiler.Report;
import compiler.abstr.Visitor;
import compiler.abstr.tree.*;
import compiler.seman.type.*;

import java.util.Vector;


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

        SemType logical = new SemAtomType(SemAtomType.LOG);
        SemType integer = new SemAtomType(SemAtomType.INT);
        SemType string = new SemAtomType(SemAtomType.STR);

        switch (acceptor.oper) {
            case AbsBinExpr.AND:
            case AbsBinExpr.IOR: {
                if (typLeft.sameStructureAs(logical) && typRight.sameStructureAs(logical)) {
                    SymbDesc.setType(acceptor, logical);
                } else {
                    Report.error(acceptor.position, "Expected [LOGICAL, LOGICAL] found [" + typLeft.actualType() + ", " + typRight.actualType() + "].");
                }
                break;
            }

            case AbsBinExpr.ADD:
            case AbsBinExpr.SUB:
            case AbsBinExpr.MUL:
            case AbsBinExpr.DIV:
            case AbsBinExpr.MOD: {
                if (typLeft.sameStructureAs(integer) && typRight.sameStructureAs(integer)) {
                    SymbDesc.setType(acceptor, integer);
                } else {
                    Report.error(acceptor.position, "Expected [INTEGER, INTEGER] found [" + typLeft.actualType() + ", " + typRight.actualType() + "].");
                }
                break;
            }

            case AbsBinExpr.EQU:
            case AbsBinExpr.NEQ:
            case AbsBinExpr.LEQ:
            case AbsBinExpr.GEQ:
            case AbsBinExpr.LTH:
            case AbsBinExpr.GTH: {
                if ((typLeft.sameStructureAs(integer) && typRight.sameStructureAs(integer)) ||
                        typLeft.sameStructureAs(logical) && typRight.sameStructureAs(logical)) {
                    SymbDesc.setType(acceptor, logical);
                } else {
                    Report.error(acceptor.position, "Expected {INTEGER, LOGICAL} found [" + typLeft.actualType() + ", " + typRight.actualType() + "].");
                }
                break;
            }

            case AbsBinExpr.ARR: {
                if (typLeft.actualType() instanceof SemArrType && typRight.sameStructureAs(integer)) {
                    SymbDesc.setType(acceptor, ((SemArrType) typLeft.actualType()).type);
                } else {
                    Report.error(acceptor.position, "Expected [ARR, INTEGER] found [" + typLeft.actualType() + ", " + typRight.actualType() + "].");
                }
                break;
            }

            case AbsBinExpr.ASSIGN: {
                if ((typLeft.sameStructureAs(integer) && typRight.sameStructureAs(integer)) ||
                        (typLeft.sameStructureAs(logical) && typRight.sameStructureAs(logical)) ||
                        (typLeft.sameStructureAs(string) && typRight.sameStructureAs(string))) {
                    SymbDesc.setType(acceptor, typLeft);
                } else {
                    Report.error(acceptor.position, "Expected {INTEGER, LOGICAL, STRING} found [" + typLeft.actualType() + ", " + typRight.actualType() + "].");
                }
                break;
            }
        }
    }

    @Override
    public void visit(AbsDefs acceptor) {
        /* Pregled tipov */
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsTypeDef) {
                SymbDesc.setType(def, new SemTypeName(((AbsTypeDef) def).name));
            }
        }
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsTypeDef) {
                def.accept(this);
            }
        }

        /* Pregled spremenljivk */
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsVarDef) {
                def.accept(this);
            }
        }

        /* Pregled funkcij */
        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsFunDef) {
                AbsFunDef func = (AbsFunDef) def;
                Vector<SemType> pars = new Vector<>();

                for (int j = 0; j < func.numPars(); j++) {
                    func.par(j).accept(this);
                    pars.add(SymbDesc.getType(func.par(j)));
                }

                func.type.accept(this);
                SymbDesc.setType(def, new SemFunType(pars, SymbDesc.getType(func.type)));
            }
        }

        for (int i = 0; i < acceptor.numDefs(); i++) {
            AbsDef def = acceptor.def(i);
            if (def instanceof AbsFunDef) {
                def.accept(this);
            }
        }
    }

    @Override
    public void visit(AbsExprs acceptor) {
        for (int i = 0; i < acceptor.numExprs(); i++) {
            acceptor.expr(i).accept(this);
        }
        SemType lastTyp = SymbDesc.getType(acceptor.expr(acceptor.numExprs() - 1));
        SymbDesc.setType(acceptor, lastTyp);
    }

    @Override
    public void visit(AbsFor acceptor) {
        acceptor.count.accept(this);
        acceptor.lo.accept(this);
        acceptor.hi.accept(this);
        acceptor.step.accept(this);
        acceptor.body.accept(this);

        SemType integer = new SemAtomType(SemAtomType.INT);

        if (!SymbDesc.getType(acceptor.count).sameStructureAs(integer)) {
            Report.error(acceptor.position, "Expected INTEGER in identifier found " + SymbDesc.getType(acceptor.count).actualType() + ".");
        } else if (!SymbDesc.getType(acceptor.lo).sameStructureAs(integer)) {
            Report.error(acceptor.position, "Expected INTEGER in expr1 found " + SymbDesc.getType(acceptor.lo).actualType() + ".");
        } else if (!SymbDesc.getType(acceptor.hi).sameStructureAs(integer)) {
            Report.error(acceptor.position, "Expected INTEGER in expr2 found " + SymbDesc.getType(acceptor.hi).actualType() + ".");
        } else if (!SymbDesc.getType(acceptor.step).sameStructureAs(integer)) {
            Report.error(acceptor.position, "Expected INTEGER in expr3 found " + SymbDesc.getType(acceptor.step).actualType() + ".");
        }

        SymbDesc.setType(acceptor, new SemAtomType(SemAtomType.VOID));
    }

    @Override
    public void visit(AbsFunCall acceptor) {
        SemFunType func = (SemFunType) SymbDesc.getType(SymbDesc.getNameDef(acceptor));

        if (acceptor.numArgs() != func.getNumPars()) {
            Report.error(acceptor.position, "Expected " + func.getNumPars() + " parameters, found " + acceptor.numArgs() + ".");
        }

        for (int i = 0; i < acceptor.numArgs(); i++) {
            acceptor.arg(i).accept(this);

            if (!SymbDesc.getType(acceptor.arg(i)).sameStructureAs(func.getParType(i))) {
                Report.error(acceptor.arg(i).position, "Expected " + func.getParType(i).actualType() + ", found " + SymbDesc.getType(acceptor.arg(i)).actualType() + " in parameter " + i + ".");
            }
        }

        SymbDesc.setType(acceptor, func.resultType);
    }

    @Override
    public void visit(AbsFunDef acceptor) {
        acceptor.expr.accept(this);

        SemType expectedType = ((SemFunType) SymbDesc.getType(acceptor)).resultType;
        SemType actualType = SymbDesc.getType(acceptor.expr);

        //System.out.printf("Name: %s \nExpected: %s\nActual: %s\n", acceptor.name, expectedType.actualType(), actualType.actualType());

        if (!expectedType.sameStructureAs(actualType)) {
            Report.error(acceptor.expr.position, "Expected " + expectedType.actualType() + ", found " + actualType.actualType() + ".");
        }

        checkParamRetType(acceptor.type);
    }

    @Override
    public void visit(AbsIfThen accepetor) {
        accepetor.cond.accept(this);
        accepetor.thenBody.accept(this);

        if (SymbDesc.getType(accepetor.cond).sameStructureAs(new SemAtomType(SemAtomType.LOG))) {
            SymbDesc.setType(accepetor, new SemAtomType(SemAtomType.VOID));
        } else {
            Report.error(accepetor.position, "Expected [LOGICAL] found [" + SymbDesc.getType(accepetor.cond).actualType() + "].");
        }
    }

    @Override
    public void visit(AbsIfThenElse acceptor) {
        acceptor.cond.accept(this);
        acceptor.thenBody.accept(this);
        acceptor.elseBody.accept(this);

        if (SymbDesc.getType(acceptor.cond).sameStructureAs(new SemAtomType(SemAtomType.LOG))) {
            SymbDesc.setType(acceptor, new SemAtomType(SemAtomType.VOID));
        } else {
            Report.error(acceptor.position, "Expected [LOGICAL] found [" + SymbDesc.getType(acceptor.cond).actualType() + "].");
        }
    }

    @Override
    public void visit(AbsPar acceptor) {
        acceptor.type.accept(this);
        SymbDesc.setType(acceptor, SymbDesc.getType(acceptor.type));

        checkParamRetType(acceptor.type);
    }

    @Override
    public void visit(AbsTypeDef acceptor) {
        acceptor.type.accept(this);
        ((SemTypeName) SymbDesc.getType(acceptor)).setType(SymbDesc.getType(acceptor.type));
    }

    @Override
    public void visit(AbsTypeName acceptor) {
        AbsDef nameDef = SymbDesc.getNameDef(acceptor);
        SymbDesc.setType(acceptor, SymbDesc.getType(nameDef));
    }

    @Override
    public void visit(AbsUnExpr acceptor) {
        acceptor.expr.accept(this);

        SemType logical = new SemAtomType(SemAtomType.LOG);
        SemType integer = new SemAtomType(SemAtomType.INT);

        switch (acceptor.oper) {
            case AbsUnExpr.NOT: {
                if (SymbDesc.getType(acceptor.expr).sameStructureAs(logical)) {
                    SymbDesc.setType(acceptor, logical);
                } else {
                    Report.error(acceptor.position, "Expected LOGICAL, found " + SymbDesc.getType(acceptor.expr).actualType() + ".");
                }
                break;
            }
            case AbsUnExpr.ADD:
            case AbsUnExpr.SUB: {
                if (SymbDesc.getType(acceptor.expr).sameStructureAs(integer)) {
                    SymbDesc.setType(acceptor, integer);
                } else {
                    Report.error(acceptor.position, "Expected INTEGER, found " + SymbDesc.getType(acceptor.expr).actualType() + ".");
                }
                break;
            }
        }
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
        acceptor.defs.accept(this);
        acceptor.expr.accept(this);
        SymbDesc.setType(acceptor, SymbDesc.getType(acceptor.expr));
    }

    @Override
    public void visit(AbsWhile acceptor) {
        acceptor.cond.accept(this);
        acceptor.body.accept(this);

        if (SymbDesc.getType(acceptor.cond).sameStructureAs(new SemAtomType(SemAtomType.LOG))) {
            SymbDesc.setType(acceptor, new SemAtomType(SemAtomType.VOID));
        } else {
            Report.error(acceptor.position, "Expected [LOGICAL] found [" + SymbDesc.getType(acceptor.cond).actualType() + "].");
        }
    }


    private void checkParamRetType(AbsType type) {
        SemType actType = SymbDesc.getType(type);
        if (!(actType.sameStructureAs(new SemAtomType(SemAtomType.INT)) ||
                actType.sameStructureAs(new SemAtomType(SemAtomType.STR)) ||
                actType.sameStructureAs(new SemAtomType(SemAtomType.LOG))))
        {
            Report.error(type.position, "Invalid type, found [" + actType.actualType() + "] expected [LOGICAL, STRING, INTEGER].");
        }
    }
}