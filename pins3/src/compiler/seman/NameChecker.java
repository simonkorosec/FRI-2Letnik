package compiler.seman;

import compiler.Report;
import compiler.abstr.*;
import compiler.abstr.tree.*;

import java.util.*;

/**
 * Preverjanje in razresevanje imen (razen imen komponent).
 *
 * @author sliva
 */
public class NameChecker implements Visitor {

    private static final HashMap<String, AbsType> typeMap = new HashMap<>();


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
                    NameChecker.typeMap.put(typ.name, typ.type);
                } catch (SemIllegalInsertException e) {
                    Report.error(typ.position, String.format("Type '%s' alredy definded.", typ.name));
                }
            }
        }

        for (int i = 0; i < acceptor.numDefs(); i++) {
            acceptor.def(i).accept(this);
        }

        NameChecker.checkTypeCycle();
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
            Report.error(acceptor.position, String.format("Function '%s' not definded.", acceptor.name));
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

    private static void checkTypeCycle() {
        HashMap<String, String> graph = new HashMap<>();

        for (String key : typeMap.keySet()) {
            AbsType type = typeMap.get(key);
            addType(type, graph, key);
        }

        Set<String> keys = graph.keySet();
        LinkedList<String> visited = new LinkedList<>();
        LinkedList<String> goodType = new LinkedList<>();
        while (!keys.isEmpty()) {
            String key = keys.iterator().next();
            visited.add(key);
            while (true) {
                String type = graph.get(key);
                if (type.equals("log") || type.equals("int") || type.equals("str") || goodType.contains(type)) {
                    break;
                }
                if (visited.contains(type)) {
                    AbsDef d = SymbTable.fnd(key);
                    if (d != null) {
                        Report.error(d.position, "Detected cycle in type assignment.");
                    } else {
                        Report.error("Detected cycle in type assignment.");
                    }
                }
                key = type;
                visited.add(key);
            }
            goodType.addAll(visited);
            keys.removeAll(visited);
            visited.clear();
        }
    }

    private static void addType(AbsType type, HashMap<String, String> graph, String key) {
        if (type instanceof AbsAtomType) {
            if (((AbsAtomType) type).type == 0) {
                graph.put(key, "log");
            } else if (((AbsAtomType) type).type == 1) {
                graph.put(key, "int");
            } else if (((AbsAtomType) type).type == 2) {
                graph.put(key, "str");
            }
        } else if (type instanceof AbsArrType) {
            addType(((AbsArrType) type).type, graph, key);
        } else if (type instanceof AbsTypeName) {
            graph.put(key, ((AbsTypeName) type).name);
        }
    }

}
