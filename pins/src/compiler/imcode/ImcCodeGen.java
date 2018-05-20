package compiler.imcode;

import java.util.*;

import compiler.Report;
import compiler.abstr.*;
import compiler.abstr.tree.*;
import compiler.frames.*;
import compiler.seman.SymbDesc;
import compiler.seman.SymbTable;
import compiler.seman.type.SemArrType;

public class ImcCodeGen implements Visitor {

    private static Stack<FrmFrame> frames;
    private static HashMap<AbsTree, ImcCode> imcCodeDesc;
    public LinkedList<ImcChunk> chunks;


    public ImcCodeGen() {
        chunks = new LinkedList<>();
        frames = new Stack<>();
        imcCodeDesc = new HashMap<>();

        frames.push(null);
    }

    @Override
    public void visit(AbsArrType acceptor) {
        acceptor.type.accept(this);
    }

    @Override
    public void visit(AbsAtomConst acceptor) {
        if (acceptor.type == AbsAtomConst.INT) {
            imcCodeDesc.put(acceptor, new ImcCONST(Integer.parseInt(acceptor.value)));
        } else if (acceptor.type == AbsAtomConst.LOG) {
            imcCodeDesc.put(acceptor, new ImcCONST(acceptor.value.equals("true") ? 1 : 0));
        } else if (acceptor.type == AbsAtomConst.STR) {
            //FrmLabel label = FrmLabel.newLabel();
            imcCodeDesc.put(acceptor, new ImcCONST(acceptor.value));
        }
    }

    @Override
    public void visit(AbsAtomType acceptor) {
    }

    @Override
    public void visit(AbsBinExpr acceptor) {
        acceptor.expr1.accept(this);
        acceptor.expr2.accept(this);

        ImcExpr expr1 = (ImcExpr) imcCodeDesc.get(acceptor.expr1);
        ImcExpr expr2 = (ImcExpr) imcCodeDesc.get(acceptor.expr2);

        if (acceptor.oper == AbsBinExpr.ASSIGN) {
            ImcSEQ imcSEQ = new ImcSEQ();
            ImcTEMP temp1 = new ImcTEMP(new FrmTemp());
            ImcTEMP temp2 = new ImcTEMP(new FrmTemp());
            ImcMOVE addr = new ImcMOVE(temp1, ((ImcMEM)expr1).expr);
            ImcMOVE val = new ImcMOVE(temp2, expr2);
            ImcMOVE assign = new ImcMOVE(new ImcMEM(temp1), temp2);
            imcSEQ.stmts.add(addr);
            imcSEQ.stmts.add(val);
            imcSEQ.stmts.add(assign);
            imcCodeDesc.put(acceptor, new ImcESEQ(imcSEQ, temp2));
        } else if (acceptor.oper == AbsBinExpr.MOD) {
            ImcTEMP a = new ImcTEMP(new FrmTemp());
            ImcTEMP n = new ImcTEMP(new FrmTemp());

            ImcSEQ imcSEQ = new ImcSEQ();
            imcSEQ.stmts.add(new ImcMOVE(a, expr1));
            imcSEQ.stmts.add(new ImcMOVE(n, expr2));

            imcCodeDesc.put(acceptor, new ImcESEQ(imcSEQ, new ImcBINOP(ImcBINOP.SUB, a, new ImcBINOP(ImcBINOP.MUL, n, new ImcBINOP(ImcBINOP.DIV, a, n)))));
        } else if (acceptor.oper == AbsBinExpr.ARR) {
            imcCodeDesc.put(acceptor, new ImcMEM(new ImcBINOP(ImcBINOP.ADD, ((ImcMEM) expr1).expr, new ImcBINOP(ImcBINOP.MUL, expr2, new ImcCONST(((SemArrType) SymbDesc.getType(acceptor.expr1).actualType()).type.size())))));
        } else {
            int opr = 0;
            switch (acceptor.oper) {
                case AbsBinExpr.IOR:
                    opr = ImcBINOP.OR;
                    break;
                case AbsBinExpr.AND:
                    opr = ImcBINOP.AND;
                    break;
                case AbsBinExpr.EQU:
                    opr = ImcBINOP.EQU;
                    break;
                case AbsBinExpr.NEQ:
                    opr = ImcBINOP.NEQ;
                    break;
                case AbsBinExpr.LEQ:
                    opr = ImcBINOP.LEQ;
                    break;
                case AbsBinExpr.GEQ:
                    opr = ImcBINOP.GEQ;
                    break;
                case AbsBinExpr.LTH:
                    opr = ImcBINOP.LTH;
                    break;
                case AbsBinExpr.GTH:
                    opr = ImcBINOP.GTH;
                    break;
                case AbsBinExpr.ADD:
                    opr = ImcBINOP.ADD;
                    break;
                case AbsBinExpr.SUB:
                    opr = ImcBINOP.SUB;
                    break;
                case AbsBinExpr.MUL:
                    opr = ImcBINOP.MUL;
                    break;
                case AbsBinExpr.DIV:
                    opr = ImcBINOP.DIV;
                    break;
            }

            imcCodeDesc.put(acceptor, new ImcBINOP(opr, expr1, expr2));
        }
    }

    @Override
    public void visit(AbsDefs acceptor) {
        for (int i = 0; i < acceptor.numDefs(); i++) {
            acceptor.def(i).accept(this);
        }
    }

    @Override
    public void visit(AbsExprs acceptor) {
        ImcSEQ imcSEQ = new ImcSEQ();
        for (int i = 0; i < acceptor.numExprs(); i++) {
            acceptor.expr(i).accept(this);
        }

        for (int i = 0; i < acceptor.numExprs() - 1; i++) {
            ImcCode code = imcCodeDesc.get(acceptor.expr(i));
            if (code instanceof ImcStmt) {
                imcSEQ.stmts.add((ImcStmt) code);
            } else {
                imcSEQ.stmts.add(new ImcEXP((ImcExpr) code));
            }
        }

        if (imcCodeDesc.get(acceptor.expr(acceptor.numExprs() - 1)) instanceof ImcExpr) {
            ImcExpr exp = (ImcExpr) imcCodeDesc.get(acceptor.expr(acceptor.numExprs() - 1));
            imcCodeDesc.put(acceptor, new ImcESEQ(imcSEQ, exp));
        } else {
            imcSEQ.stmts.add((ImcStmt) imcCodeDesc.get(acceptor.expr(acceptor.numExprs() - 1)));
            imcCodeDesc.put(acceptor, imcSEQ);
        }
    }

    @Override
    public void visit(AbsFor acceptor) {
        acceptor.count.accept(this);
        acceptor.lo.accept(this);
        acceptor.hi.accept(this);
        acceptor.step.accept(this);
        acceptor.body.accept(this);

        ImcSEQ imcSEQ = new ImcSEQ();
        FrmLabel condLabel = FrmLabel.newLabel();
        FrmLabel trueLabel = FrmLabel.newLabel();
        FrmLabel falseLabel = FrmLabel.newLabel();

        imcSEQ.stmts.add(new ImcMOVE((ImcExpr) imcCodeDesc.get(acceptor.count), (ImcExpr) imcCodeDesc.get(acceptor.lo)));
        imcSEQ.stmts.add(new ImcLABEL(condLabel));

        ImcBINOP cond = new ImcBINOP(ImcBINOP.LTH, (ImcExpr) imcCodeDesc.get(acceptor.count), (ImcExpr) imcCodeDesc.get(acceptor.hi));
        imcSEQ.stmts.add(new ImcCJUMP(cond, trueLabel, falseLabel));

        imcSEQ.stmts.add(new ImcLABEL(trueLabel));
        ImcCode code = imcCodeDesc.get(acceptor.body);
        imcSEQ.stmts.add(code instanceof ImcStmt ? (ImcStmt) code : new ImcEXP((ImcExpr) code));

        ImcMOVE inc = new ImcMOVE((ImcExpr) imcCodeDesc.get(acceptor.count), new ImcBINOP(ImcBINOP.ADD, (ImcExpr) imcCodeDesc.get(acceptor.count), (ImcExpr) imcCodeDesc.get(acceptor.step)));
        imcSEQ.stmts.add(inc);
        imcSEQ.stmts.add(new ImcJUMP(condLabel));
        imcSEQ.stmts.add(new ImcLABEL(falseLabel));

        imcCodeDesc.put(acceptor, imcSEQ);
    }

    @Override
    public void visit(AbsFunCall acceptor) {
        FrmFrame fun = FrmDesc.getFrame(SymbDesc.getNameDef(acceptor));
        if (isLibCall(fun.label.name())) {
            ImcCALL call = (ImcCALL) libCall(fun.label.name(), acceptor);
            ImcESEQ eseq = new ImcESEQ(new ImcEXP(call), new ImcCONST(0));
            imcCodeDesc.put(acceptor, eseq);
            return;
        }

        ImcCALL imcCALL = new ImcCALL(fun.label);
        ImcExpr staticLink = new ImcTEMP(frames.peek().FP);
        for (int i = 0; i <= frames.peek().level - fun.level; i++) {
            staticLink = new ImcMEM(staticLink);
        }

        imcCALL.args.add(staticLink);

        for (int i = 0; i < acceptor.numArgs(); i++) {
            acceptor.arg(i).accept(this);
            imcCALL.args.add((ImcExpr) imcCodeDesc.get(acceptor.arg(i)));
        }

        imcCodeDesc.put(acceptor, imcCALL);
    }


    @Override
    public void visit(AbsFunDef acceptor) {
        frames.push(FrmDesc.getFrame(acceptor));
        acceptor.expr.accept(this);
        ImcMOVE move = new ImcMOVE(new ImcTEMP(frames.peek().RV), (ImcExpr) imcCodeDesc.get(acceptor.expr));
        chunks.add(new ImcCodeChunk(frames.peek(), move));
        frames.pop();
    }

    @Override
    public void visit(AbsIfThen accpetor) {
        accpetor.cond.accept(this);
        accpetor.thenBody.accept(this);

        ImcSEQ imcSEQ = new ImcSEQ();
        FrmLabel trueLabel = FrmLabel.newLabel();
        FrmLabel falseLabel = FrmLabel.newLabel();

        ImcCJUMP cjump = new ImcCJUMP((ImcExpr) imcCodeDesc.get(accpetor.cond), trueLabel, falseLabel);

        imcSEQ.stmts.add(cjump);
        imcSEQ.stmts.add(new ImcLABEL(trueLabel));

        ImcCode code = imcCodeDesc.get(accpetor.thenBody);
        imcSEQ.stmts.add(code instanceof ImcStmt ? (ImcStmt) code : new ImcEXP((ImcExpr) code));

        imcSEQ.stmts.add(new ImcLABEL(falseLabel));

        imcCodeDesc.put(accpetor, imcSEQ);
    }

    @Override
    public void visit(AbsIfThenElse accpetor) {
        accpetor.cond.accept(this);
        accpetor.thenBody.accept(this);
        accpetor.elseBody.accept(this);

        ImcSEQ imcSEQ = new ImcSEQ();
        FrmLabel trueLabel = FrmLabel.newLabel();
        FrmLabel falseLabel = FrmLabel.newLabel();
        FrmLabel endLabel = FrmLabel.newLabel();

        ImcCJUMP cjump = new ImcCJUMP((ImcExpr) imcCodeDesc.get(accpetor.cond), trueLabel, falseLabel);

        imcSEQ.stmts.add(cjump);
        imcSEQ.stmts.add(new ImcLABEL(trueLabel));

        ImcCode code = imcCodeDesc.get(accpetor.thenBody);
        imcSEQ.stmts.add(code instanceof ImcStmt ? (ImcStmt) code : new ImcEXP((ImcExpr) code));
        imcSEQ.stmts.add(new ImcJUMP(endLabel));

        imcSEQ.stmts.add(new ImcLABEL(falseLabel));
        code = imcCodeDesc.get(accpetor.elseBody);
        imcSEQ.stmts.add(code instanceof ImcStmt ? (ImcStmt) code : new ImcEXP((ImcExpr) code));

        imcSEQ.stmts.add(new ImcLABEL(endLabel));

        imcCodeDesc.put(accpetor, imcSEQ);
    }

    @Override
    public void visit(AbsPar acceptor) {
    }

    @Override
    public void visit(AbsTypeDef acceptor) {
    }

    @Override
    public void visit(AbsTypeName acceptor) {
    }

    @Override
    public void visit(AbsUnExpr acceptor) {
        acceptor.expr.accept(this);

        if (acceptor.oper == AbsUnExpr.ADD) {
            imcCodeDesc.put(acceptor, new ImcBINOP(ImcBINOP.ADD, new ImcCONST(0), (ImcExpr) imcCodeDesc.get(acceptor.expr)));
        } else if (acceptor.oper == AbsUnExpr.SUB) {
            imcCodeDesc.put(acceptor, new ImcBINOP(ImcBINOP.SUB, new ImcCONST(0), (ImcExpr) imcCodeDesc.get(acceptor.expr)));
        } else if (acceptor.oper == AbsUnExpr.NOT) {
            imcCodeDesc.put(acceptor, new ImcBINOP(ImcBINOP.EQU, new ImcCONST(0), (ImcExpr) imcCodeDesc.get(acceptor.expr)));
        }
    }

    @Override
    public void visit(AbsVarDef acceptor) {
        if (frames.peek() == null) {
            chunks.add(new ImcDataChunk(((FrmVarAccess) FrmDesc.getAccess(acceptor)).label, SymbDesc.getType(acceptor).size()));
        }
    }

    @Override
    public void visit(AbsVarName acceptor) {
        FrmAccess frmAccess = FrmDesc.getAccess(SymbDesc.getNameDef(acceptor));

        if (frmAccess instanceof FrmVarAccess) {
            imcCodeDesc.put(acceptor, new ImcMEM(new ImcNAME(((FrmVarAccess) frmAccess).label)));
        } else if (frmAccess instanceof FrmLocAccess) {
            FrmFrame currFrm = frames.peek();
            ImcExpr tmp = new ImcTEMP(currFrm.FP);
            for (int i = 0; i < currFrm.level - ((FrmLocAccess) frmAccess).frame.level; i++) {
                tmp = new ImcMEM(tmp);
            }
            ImcCONST offset = new ImcCONST(((FrmLocAccess) frmAccess).offset);
            ImcMEM mem = new ImcMEM(new ImcBINOP(ImcBINOP.ADD, tmp, offset));
            imcCodeDesc.put(acceptor, mem);

        } else if (frmAccess instanceof FrmParAccess) {
            FrmFrame currFrm = frames.peek();
            ImcExpr tmp = new ImcTEMP(currFrm.FP);
            for (int i = 0; i < currFrm.level - ((FrmParAccess) frmAccess).frame.level; i++) {
                tmp = new ImcMEM(tmp);
            }
            ImcCONST offset = new ImcCONST(((FrmParAccess) frmAccess).offset);
            ImcMEM mem = new ImcMEM(new ImcBINOP(ImcBINOP.ADD, tmp, offset));
            imcCodeDesc.put(acceptor, mem);
        }
    }

    @Override
    public void visit(AbsWhere acceptor) {
        acceptor.defs.accept(this);
        acceptor.expr.accept(this);

        imcCodeDesc.put(acceptor, imcCodeDesc.get(acceptor.expr));
    }

    @Override
    public void visit(AbsWhile acceptor) {
        acceptor.cond.accept(this);
        acceptor.body.accept(this);

        ImcSEQ imcSEQ = new ImcSEQ();
        FrmLabel condLabel = FrmLabel.newLabel();
        FrmLabel trueLabel = FrmLabel.newLabel();
        FrmLabel falseLabel = FrmLabel.newLabel();

        imcSEQ.stmts.add(new ImcLABEL(condLabel));
        imcSEQ.stmts.add(new ImcCJUMP((ImcExpr) imcCodeDesc.get(acceptor.cond), trueLabel, falseLabel));

        imcSEQ.stmts.add(new ImcLABEL(trueLabel));
        ImcCode code = imcCodeDesc.get(acceptor.body);
        imcSEQ.stmts.add(code instanceof ImcStmt ? (ImcStmt) code : new ImcEXP((ImcExpr) code));
        imcSEQ.stmts.add(new ImcJUMP(condLabel));

        imcSEQ.stmts.add(new ImcLABEL(falseLabel));

        imcCodeDesc.put(acceptor, imcSEQ);
    }


    private boolean isLibCall(String name) {
        return name.equals("_putInt") || name.equals("_getInt") || name.equals("_putString") || name.equals("_getString");
    }

    private ImcCode libCall(String name, AbsFunCall acceptor) {
        if (name.equals("_putInt") || name.equals("_putString")) {
            return createPut(acceptor);
        } else {
            return createGet(acceptor);
        }
    }

    private ImcCode createPut(AbsFunCall acceptor) {
        FrmFrame fun = FrmDesc.getFrame(SymbDesc.getNameDef(acceptor));
        ImcCALL imcCALL = new ImcCALL(fun.label);
        for (int i = 0; i < acceptor.numArgs(); i++) {
            acceptor.arg(i).accept(this);
            imcCALL.args.add((ImcExpr) imcCodeDesc.get(acceptor.arg(i)));
        }
        return imcCALL;
    }

    private ImcCode createGet(AbsFunCall acceptor) {
        FrmFrame fun = FrmDesc.getFrame(SymbDesc.getNameDef(acceptor));
        ImcCALL imcCALL = new ImcCALL(fun.label);

        FrmAccess frmAccess = FrmDesc.getAccess(SymbDesc.getNameDef(acceptor.arg(0)));

        if (frmAccess instanceof FrmParAccess) {
            FrmFrame currFrm = frames.peek();
            ImcExpr tmp = new ImcTEMP(currFrm.FP);
            for (int i = 0; i < currFrm.level - ((FrmParAccess) frmAccess).frame.level; i++) {
                tmp = new ImcMEM(tmp);
            }
            ImcCONST offset = new ImcCONST(((FrmParAccess) frmAccess).offset);
            imcCALL.args.add(new ImcBINOP(ImcBINOP.ADD, tmp, offset));
        } else if (frmAccess instanceof FrmLocAccess) {
            FrmFrame currFrm = frames.peek();
            ImcExpr tmp = new ImcTEMP(currFrm.FP);
            for (int i = 0; i < currFrm.level - ((FrmLocAccess) frmAccess).frame.level; i++) {
                tmp = new ImcMEM(tmp);
            }
            ImcCONST offset = new ImcCONST(((FrmLocAccess) frmAccess).offset);
            imcCALL.args.add(new ImcBINOP(ImcBINOP.ADD, tmp, offset));
        } else if (frmAccess instanceof FrmVarAccess){
            imcCALL.args.add(new ImcNAME(((FrmVarAccess) frmAccess).label));
        } else if (acceptor.arg(0) instanceof AbsBinExpr){
            acceptor.arg(0).accept(this);
            imcCALL.args.add(((ImcMEM)imcCodeDesc.get(acceptor.arg(0))).expr);
        }

        return imcCALL;
    }

}
