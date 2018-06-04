package compiler.synan;

import compiler.Position;
import compiler.Report;
import compiler.abstr.tree.*;
import compiler.lexan.*;

import java.util.Vector;

/**
 * Sintaksni analizator.
 *
 * @author sliva
 */
public class SynAn {

    /**
     * Leksikalni analizator.
     */
    private final LexAn lexAn;
    private Symbol currSybol;       // Trenutni gledan simbol
    private Symbol prevSybol;       // Prejšnji simbol

    /**
     * Ali se izpisujejo vmesni rezultati.
     */
    private final boolean dump;

    /**
     * Ustvari nov sintaksni analizator.
     *
     * @param lexAn Leksikalni analizator.
     * @param dump  Ali se izpisujejo vmesni rezultati.
     */
    public SynAn(LexAn lexAn, boolean dump) {
        this.lexAn = lexAn;
        this.dump = dump;
    }

    /**
     * Opravi sintaksno analizo.
     * Pravila analize v datoteki "pins-gram.txt"
     */
    public AbsTree parse() {
        currSybol = lexAn.lexAn();

        return source();
    }

    private AbsTree source() {
        dump("source -> defnitions");
        AbsTree t = defnitions();

        if (currSybol.token != Token.EOF) {
            Report.error(currSybol.position, "Expected ; found " + currSybol.lexeme);
        }
        return t;
    }

    private AbsDefs defnitions() {
        dump("defnitions -> defnition defnitions'");
        Position pos = currSybol.position;
        Vector<AbsDef> defs = new Vector<>();
        defs.add(defnition());
        defnitions1(defs);

        return new AbsDefs(new Position(pos, prevSybol.position), defs);
    }

    private void defnitions1(Vector<AbsDef> defs) {
        if (currSybol.token == Token.SEMIC) {
            dump("defnitions' -> ; defnition defnitions'");
            nextSymbol();
            defs.add(defnition());
            defnitions1(defs);
        } else {
            dump("defnitions' -> .");
        }
    }

    private AbsDef defnition() {
        AbsDef t = null;

        switch (currSybol.token) {
            case Token.KW_TYP:
                dump("defnition -> type_defnition");
                nextSymbol();
                t = typeDef();
                break;
            case Token.KW_FUN:
                dump("defnition -> function_defnition");
                nextSymbol();
                t = funDef();
                break;
            case Token.KW_VAR:
                dump("defnition -> variable_defnition");
                nextSymbol();
                t = varDef();
                break;
            default:
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
                break;
        }

        return t;
    }

    private AbsVarDef varDef() {
        dump("variable_defnition -> var identifer : type");
        AbsVarDef t;
        Position pos = prevSybol.position;

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        String name = currSybol.lexeme;

        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        nextSymbol();
        AbsType type = type();
        t = new AbsVarDef(new Position(pos, prevSybol.position), name, type);

        return t;
    }

    private AbsFunDef funDef() {
        dump("function_defnition -> fun identifer ( parameters ) : type = expression");
        Position pos = prevSybol.position;

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected IDENTIFIER");
        }
        String name = currSybol.lexeme;

        nextSymbol();
        if (currSybol.token != Token.LPARENT) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected LPARENT");
        }

        nextSymbol();
        Vector<AbsPar> parameters = parameters();

        if (currSybol.token != Token.RPARENT) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected RPARENT");
        }
        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected COLON");
        }

        nextSymbol();
        AbsType type = type();

        if (currSybol.token != Token.ASSIGN) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected ASSIGN");
        }

        nextSymbol();
        AbsExpr expr = expression();

        return new AbsFunDef(new Position(pos, prevSybol.position), name, parameters, type, expr);
    }

    private AbsExpr expression() {
        dump("expression -> logical_ior_expression expression'");

        AbsExpr expr = logical_ior_expression();
        AbsDefs defs = expression1();

        if (defs != null) {
            expr = new AbsWhere(new Position(expr.position, prevSybol.position), expr, defs);
        }

        return expr;
    }

    private AbsExpr logical_ior_expression() {
        dump("logical_ior_expression -> logical_and_expression logical_ior_expression'");

        AbsExpr left = logical_and_expression();
        return logical_ior_expression1(left);
    }

    private AbsExpr logical_and_expression() {
        dump("logical_and_expression -> compare_expression logical_and_expression'");
        AbsExpr left = compare_expression();
        return logical_and_expression1(left);
    }

    private AbsExpr compare_expression() {
        dump("compare_expression -> additive_expression compare_expression'");
        AbsExpr left = additive_expression();
        return compare_expression1(left);
    }

    private AbsExpr compare_expression1(AbsExpr left) {

        AbsExpr right = null;
        int opr = 0;

        if (currSybol.token == Token.EQU) {
            dump("compare_expression' -> == additive_expression");
            opr = AbsBinExpr.EQU;
            nextSymbol();
            right = additive_expression();
        } else if (currSybol.token == Token.NEQ) {
            dump("compare_expression' -> != additive_expression");
            opr = AbsBinExpr.NEQ;
            nextSymbol();
            right = additive_expression();
        } else if (currSybol.token == Token.LEQ) {
            dump("compare_expression' -> <= additive_expression");
            opr = AbsBinExpr.LEQ;
            nextSymbol();
            right = additive_expression();
        } else if (currSybol.token == Token.GEQ) {
            dump("compare_expression' -> >= additive_expression");
            opr = AbsBinExpr.GEQ;
            nextSymbol();
            right = additive_expression();
        } else if (currSybol.token == Token.LTH) {
            dump("compare_expression' -> < additive_expression");
            opr = AbsBinExpr.LTH;
            nextSymbol();
            right = additive_expression();
        } else if (currSybol.token == Token.GTH) {
            dump("compare_expression' -> > additive_expression");
            opr = AbsBinExpr.GTH;
            nextSymbol();
            right = additive_expression();
        } else {
            dump("compare_expression' -> .");
        }

        if (right != null) {
            return new AbsBinExpr(new Position(left.position, right.position), opr, left, right);
        }
        return left;
    }

    private AbsExpr additive_expression() {
        dump("additive_expression -> multiplicative_expression additive_expression'");
        AbsExpr left = multiplicative_expression();
        return additive_expression1(left);
    }

    private AbsExpr multiplicative_expression() {
        dump("multiplicative_expression -> prefx_expression multiplicative_expression'");
        AbsExpr left = prefx_expression();
        return multiplicative_expression1(left);
    }

    private AbsExpr prefx_expression() {
        AbsExpr t;
        Position pos = currSybol.position;
        if (currSybol.token == Token.ADD) {
            dump("prefx_expression -> + prefx_expression");
            nextSymbol();
            AbsExpr expr = prefx_expression();
            t = new AbsUnExpr(new Position(pos, prevSybol.position), AbsUnExpr.ADD, expr);
        } else if (currSybol.token == Token.SUB) {
            dump("prefx_expression -> - prefx_expression");
            nextSymbol();
            AbsExpr expr = prefx_expression();
            t = new AbsUnExpr(new Position(pos, prevSybol.position), AbsUnExpr.SUB, expr);
        } else if (currSybol.token == Token.NOT) {
            dump("prefx_expression -> ! prefx_expression");
            nextSymbol();
            AbsExpr expr = prefx_expression();
            t = new AbsUnExpr(new Position(pos, prevSybol.position), AbsUnExpr.NOT, expr);
        } else {
            dump("prefx_expression -> postfx_expression");
            t = postfx_expression();
        }
        return t;
    }

    private AbsExpr postfx_expression() {
        dump("postfx_expression -> atom_expression postfx_expression'");
        AbsExpr left = atom_expression();
        return postfx_expression1(left);
    }

    private AbsExpr atom_expression() {
        AbsExpr t = null;
        Position pos = currSybol.position;

        switch (currSybol.token) {
            case Token.LOG_CONST:
                dump("atom_expression -> log_constant");
                t = new AbsAtomConst(pos, AbsAtomConst.LOG, currSybol.lexeme);
                nextSymbol();
                break;
            case Token.INT_CONST:
                dump("atom_expression -> int_constant");
                t = new AbsAtomConst(pos, AbsAtomConst.INT, currSybol.lexeme);
                nextSymbol();
                break;
            case Token.STR_CONST:
                dump("atom_expression -> str_constant");
                t = new AbsAtomConst(pos, AbsAtomConst.STR, currSybol.lexeme);
                nextSymbol();
                break;
            case Token.IDENTIFIER:
                dump("atom_expression -> identifer atom_expression'");
                String name = currSybol.lexeme;
                nextSymbol();
                Vector<AbsExpr> exprs = atom_expression1();
                if (exprs != null) {
                    t = new AbsFunCall(new Position(pos, prevSybol.position), name, exprs);
                } else {
                    t = new AbsVarName(pos, name);
                }
                break;
            case Token.LBRACE:
                dump("atom_expression -> { atom_expression''");
                nextSymbol();
                t = atom_expression2();
                break;
            case Token.LPARENT:
                dump("atom_expression -> ( expressions )");
                nextSymbol();
                Vector<AbsExpr> expr = expressions();
                if (currSybol.token != Token.RPARENT) {
                    Report.error(currSybol.position, "Unknow production1: " + currSybol.lexeme + " expected RPARENT");
                }
                nextSymbol();
                t = new AbsExprs(new Position(pos, prevSybol.position), expr);
                break;
            default:
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
                break;

        }
        return t;
    }

    private Vector<AbsExpr> atom_expression1() {
        Vector<AbsExpr> t = null;
        if (currSybol.token == Token.LPARENT) {
            dump("atom_expression' -> ( expressions )");
            nextSymbol();
            t = expressions();
            if (currSybol.token != Token.RPARENT) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected RPARENT");
            }
            nextSymbol();
        } else {
            dump("atom_expression' -> .");
        }
        return t;
    }

    private AbsExpr atom_expression2() {
        AbsExpr expr;
        Position pos = prevSybol.position;

        if (currSybol.token == Token.KW_IF) {
            dump("atom_expression'' -> if expression then expression atom_expression'''");
            nextSymbol();
            AbsExpr expr1 = expression();
            if (currSybol.token != Token.KW_THEN) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            AbsExpr expr2 = expression();
            AbsExpr expr3 = atom_expression3();

            if (expr3 == null) {
                expr = new AbsIfThen(new Position(pos, prevSybol.position), expr1, expr2);
            } else {
                expr = new AbsIfThenElse(new Position(pos, prevSybol.position), expr1, expr2, expr3);
            }


        } else if (currSybol.token == Token.KW_WHILE) {
            dump("atom_expression'' -> while expression : expression }");
            nextSymbol();
            AbsExpr expr1 = expression();
            if (currSybol.token != Token.COLON) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected COLON");
            }
            nextSymbol();
            AbsExpr expr2 = expression();

            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected RBRACE");
            }

            expr = new AbsWhile(new Position(pos, currSybol.position), expr1, expr2);
            nextSymbol();

        } else if (currSybol.token == Token.KW_FOR) {
            dump("atom_expression'' -> for identifer = expression , expression , expression : expression }");
            nextSymbol();
            if (currSybol.token != Token.IDENTIFIER) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            AbsVarName varName = new AbsVarName(currSybol.position, currSybol.lexeme);

            nextSymbol();
            if (currSybol.token != Token.ASSIGN) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            AbsExpr expr1 = expression();
            if (currSybol.token != Token.COMMA) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            AbsExpr expr2 = expression();
            if (currSybol.token != Token.COMMA) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            AbsExpr expr3 = expression();
            if (currSybol.token != Token.COLON) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            AbsExpr expr4 = expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();

            expr = new AbsFor(new Position(pos, prevSybol.position), varName, expr1, expr2, expr3, expr4);

        } else {
            dump("atom_expression'' -> expression = expression } .");
            AbsExpr expr1 = expression();
            if (currSybol.token != Token.ASSIGN) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            AbsExpr expr2 = expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();

            expr = new AbsBinExpr(new Position(pos, prevSybol.position), AbsBinExpr.ASSIGN, expr1, expr2);
        }
        return expr;
    }

    private AbsExpr atom_expression3() {
        AbsExpr expr = null;
        if (currSybol.token == Token.RBRACE) {
            dump("atom_expression''' -> } .");
            nextSymbol();
        } else if (currSybol.token == Token.KW_ELSE) {
            dump("atom_expression''' -> else expression }");
            nextSymbol();
            expr = expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
        }
        return expr;
    }

    private Vector<AbsExpr> expressions() {
        dump("expressions -> expression expressions'");
        Vector<AbsExpr> exprs = new Vector<>();
        exprs.add(expression());
        expressions1(exprs);
        return exprs;
    }

    private void expressions1(Vector<AbsExpr> exprs) {
        if (currSybol.token == Token.COMMA) {
            dump("expressions' -> , expression expressions'");
            nextSymbol();
            exprs.add(expression());
            expressions1(exprs);
        } else {
            dump("expressions' -> .");
        }
    }

    private AbsExpr postfx_expression1(AbsExpr left) {
        if (currSybol.token == Token.LBRACKET) {
            dump("postfx_expression' -> [ expression ] postfx_expression'");
            nextSymbol();
            AbsExpr expr = expression();
            if (currSybol.token != Token.RBRACKET) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected RBRACKET");
            }
            nextSymbol();

            left = new AbsBinExpr(new Position(left.position, prevSybol.position), AbsBinExpr.ARR, left, expr);
            left = postfx_expression1(left);
        } else {
            dump("postfx_expression' -> .");
        }
        return left;
    }

    private AbsExpr multiplicative_expression1(AbsExpr left) {
        if (currSybol.token == Token.MUL) {
            dump("multiplicative_expression' -> * prefx_expression multiplicative_expression'");
            nextSymbol();
            AbsExpr right = prefx_expression();
            left = multiplicative_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.MUL, left, right));
        } else if (currSybol.token == Token.DIV) {
            dump("multiplicative_expression' -> / prefx_expression multiplicative_expression'");
            nextSymbol();
            AbsExpr right = prefx_expression();
            left = multiplicative_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.DIV, left, right));
        } else if (currSybol.token == Token.MOD) {
            dump("multiplicative_expression' -> % prefx_expression multiplicative_expression'");
            nextSymbol();
            AbsExpr right = prefx_expression();
            left = multiplicative_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.MOD, left, right));
        } else {
            dump("multiplicative_expression' -> .");
        }
        return left;
    }

    private AbsExpr additive_expression1(AbsExpr left) {
        if (currSybol.token == Token.ADD) {
            dump("additive_expression' -> + multiplicative_expression additive_expression'");
            nextSymbol();
            AbsExpr right = multiplicative_expression();
            left = additive_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.ADD, left, right));
        } else if (currSybol.token == Token.SUB) {
            dump("additive_expression' -> - multiplicative_expression additive_expression'");
            nextSymbol();
            AbsExpr right = multiplicative_expression();
            left = additive_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.SUB, left, right));
        } else {
            dump("additive_expression' -> .");
        }
        return left;
    }

    private AbsExpr logical_and_expression1(AbsExpr left) {
        if (currSybol.token == Token.AND) {
            dump("logical_and_expression' -> & compare_expression logical_and_expression'");
            nextSymbol();
            AbsExpr right = compare_expression();
            left = logical_and_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.AND, left, right));
        } else {
            dump("logical_and_expression' -> .");
        }

        return left;
    }

    private AbsExpr logical_ior_expression1(AbsExpr left) {
        if (currSybol.token == Token.IOR) {
            dump("logical_ior_expression' -> | logical_and_expression logical_ior_expression'");
            nextSymbol();
            AbsExpr right = logical_and_expression();
            left = logical_ior_expression1(new AbsBinExpr(new Position(left.position, right.position), AbsBinExpr.IOR, left, right));
        } else {
            dump("logical_ior_expression' -> .");
        }
        return left;
    }

    private AbsDefs expression1() {
        AbsDefs t = null;
        if (currSybol.token == Token.LBRACE) {
            dump("expression' -> { WHERE defnitions }");
            nextSymbol();
            if (currSybol.token != Token.KW_WHERE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected KW_WHERE" + currSybol.token);
            }

            nextSymbol();
            t = defnitions();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme+ " expected RBRACE");
            }
            nextSymbol();

        } else {
            dump("expression' -> .");
        }
        return t;
    }

    private Vector<AbsPar> parameters() {
        dump("parameters -> parameter parameters'");
        Vector<AbsPar> pars = new Vector<>();
        pars.add(parameter());
        parameters1(pars);
        return pars;
    }

    private void parameters1(Vector<AbsPar> pars) {
        if (currSybol.token == Token.COMMA) {
            dump("parameters' -> , parameter parameters'");
            nextSymbol();
            pars.add(parameter());
            parameters1(pars);
        } else {
            dump("parameters' -> .");
        }
    }

    private AbsPar parameter() {
        dump("parameter -> identifer : type");
        Position pos = currSybol.position;

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected IDENTIFIER");
        }
        String name = currSybol.lexeme;

        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected COLON");
        }
        nextSymbol();
        AbsType type = type();

        return new AbsPar(new Position(pos, prevSybol.position), name, type);
    }

    private AbsTypeDef typeDef() {
        dump("type_defnition -> typ identifer : type");
        AbsTypeDef t;
        Position pos = prevSybol.position;

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        String name = currSybol.lexeme;

        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        nextSymbol();
        AbsType type = type();
        t = new AbsTypeDef(new Position(pos, prevSybol.position), name, type);

        return t;
    }

    private AbsType type() {
        AbsType t = null;

        switch (currSybol.token) {
            case Token.LOGICAL:
                dump("type -> logical");
                t = new AbsAtomType(currSybol.position, AbsAtomType.LOG);
                nextSymbol();
                break;
            case Token.INTEGER:
                dump("type -> integer");
                t = new AbsAtomType(currSybol.position, AbsAtomType.INT);
                nextSymbol();
                break;
            case Token.STRING:
                dump("type -> string");
                t = new AbsAtomType(currSybol.position, AbsAtomType.STR);
                nextSymbol();
                break;
            case Token.IDENTIFIER:
                dump("type -> identifer");
                t = new AbsTypeName(currSybol.position, currSybol.lexeme);
                nextSymbol();
                break;
            case Token.KW_ARR:
                dump("type -> arr [ int_constant ] type");
                Position pos = currSybol.position;

                nextSymbol();
                if (currSybol.token != Token.LBRACKET) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected LBRACKET");
                }

                nextSymbol();
                if (currSybol.token != Token.INT_CONST) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected INT_CONST");
                }
                String size = currSybol.lexeme;

                nextSymbol();
                if (currSybol.token != Token.RBRACKET) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected RBRACKET");
                }
                nextSymbol();
                AbsType type = type();
                t = new AbsArrType(new Position(pos, prevSybol.position), Integer.parseInt(size), type);
                break;
            default:
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected type");
                break;
        }

        return t;
    }

    /**
     * Izpise produkcijo v datoteko z vmesnimi rezultati.
     *
     * @param production Produkcija, ki naj bo izpisana.
     */
    private void dump(String production) {
        if (!dump)
            return;
        if (Report.dumpFile() == null)
            return;
        Report.dumpFile().println(production);
    }

    private void nextSymbol() {
        prevSybol = currSybol;
        currSybol = lexAn.lexAn();
    }

}
