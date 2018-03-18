package compiler.synan;

import compiler.Report;
import compiler.lexan.*;

/**
 * Sintaksni analizator.
 *
 * @author sliva
 */
public class SynAn {

    /**
     * Leksikalni analizator.
     */
    private LexAn lexAn;
    private Symbol currSybol;

    /**
     * Ali se izpisujejo vmesni rezultati.
     */
    private boolean dump;

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
     */
    public void parse() {
        currSybol = lexAn.lexAn();

        source();
    }

    private void source() {
        dump("source -> defnitions .");
        defnitions();
    }

    private void defnitions() {
        dump("defnitions -> defnition defnitions' .");
        defnition();
        defnitions1();
    }

    private void defnitions1() {
        if (currSybol.token == Token.SEMIC) {
            dump("defnitions' -> ; defnition defnitions' .");
            nextSymbol();
            defnition();
            defnitions1();
        } else {
            dump("defnitions' -> .");
        }
    }

    private void defnition() {
        switch (currSybol.token) {
            case Token.KW_TYP:
                dump("defnition -> type_defnition .");
                nextSymbol();
                typeDef();
                break;
            case Token.KW_FUN:
                dump("defnition -> function_defnition .");
                nextSymbol();
                funDef();
                break;
            case Token.KW_VAR:
                dump("defnition -> variable_defnition .");
                nextSymbol();
                varDef();
                break;
            default:
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
                break;
        }
    }

    private void varDef() {
        dump("variable_defnition -> var identifer : type .");
        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        nextSymbol();
        type();
    }

    private void funDef() {
        dump("function_defnition -> fun identifer ( parameters ) : type = expression .");

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected IDENTIFIER");
        }
        nextSymbol();
        if (currSybol.token != Token.LPARENT) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected LPARENT");
        }

        nextSymbol();
        parameters();

        if (currSybol.token != Token.RPARENT) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected RPARENT");
        }
        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected COLON");
        }

        nextSymbol();
        type();

        if (currSybol.token != Token.ASSIGN) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected ASSIGN");
        }

        nextSymbol();
        expression();

    }

    private void expression() {
        dump("expression -> logical_ior_expression expression' .");

        logical_ior_expression();
        expression1();
    }

    private void logical_ior_expression() {
        dump("logical_ior_expression -> logical_and_expression logical_ior_expression' .");

        logical_and_expression();
        logical_ior_expression1();
    }

    private void logical_and_expression() {
        dump("logical_and_expression -> compare_expression logical_and_expression' .");
        compare_expression();
        logical_and_expression1();
    }

    private void compare_expression() {
        dump("compare_expression -> additive_expression compare_expression' .");
        additive_expression();
        compare_expression1();
    }

    private void compare_expression1() {

        if (currSybol.token == Token.EQU) {
            dump("compare_expression' -> == additive_expression .");
            nextSymbol();
            additive_expression();
        } else if (currSybol.token == Token.NEQ) {
            dump("compare_expression' -> != additive_expression .");
            nextSymbol();
            additive_expression();
        } else if (currSybol.token == Token.LEQ) {
            dump("compare_expression' -> <= additive_expression .");
            nextSymbol();
            additive_expression();
        } else if (currSybol.token == Token.GEQ) {
            dump("compare_expression' -> >= additive_expression .");
            nextSymbol();
            additive_expression();
        } else if (currSybol.token == Token.LTH) {
            dump("compare_expression' -> < additive_expression .");
            nextSymbol();
            additive_expression();
        } else if (currSybol.token == Token.GTH) {
            dump("compare_expression' -> > additive_expression .");
            nextSymbol();
            additive_expression();
        } else {
            dump("compare_expression' -> .");
        }
    }

    private void additive_expression() {
        dump("additive_expression -> multiplicative_expression additive_expression' .");
        multiplicative_expression();
        additive_expression1();
    }

    private void multiplicative_expression() {
        dump("multiplicative_expression -> prefx_expression multiplicative_expression' .");
        prefx_expression();
        multiplicative_expression1();
    }

    private void prefx_expression() {
        if (currSybol.token == Token.ADD) {
            dump("prefx_expression -> + prefx_expression .");
            nextSymbol();
            prefx_expression();
        } else if (currSybol.token == Token.SUB) {
            dump("prefx_expression -> - prefx_expression .");
            nextSymbol();
            prefx_expression();
        } else if (currSybol.token == Token.NOT) {
            dump("prefx_expression -> ! prefx_expression .");
            nextSymbol();
            prefx_expression();
        } else {
            dump("prefx_expression -> postfx_expression .");
            postfx_expression();
        }
    }

    private void postfx_expression() {
        dump("postfx_expression -> atom_expression postfx_expression' .");
        atom_expression();
        postfx_expression1();

    }

    private void atom_expression() {
        switch (currSybol.token) {
            case Token.LOG_CONST:
                dump("atom_expression -> log_constant .");
                nextSymbol();
                break;
            case Token.INT_CONST:
                dump("atom_expression -> int_constant .");
                nextSymbol();
                break;
            case Token.STR_CONST:
                dump("atom_expression -> str_constant .");
                nextSymbol();
                break;
            case Token.IDENTIFIER:
                dump("atom_expression -> identifer atom_expression' .");
                nextSymbol();
                atom_expression1();
                break;
            case Token.LBRACE:
                dump("atom_expression -> { atom_expression'' .");
                nextSymbol();
                atom_expression2();
                break;
            case Token.LPARENT:
                dump("atom_expression -> ( expressions ) .");
                nextSymbol();
                expressions();
                if (currSybol.token != Token.RPARENT) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + " expected RPARENT");
                }
                nextSymbol();
                break;
            default:
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
                break;

        }
    }

    private void atom_expression1() {
        if (currSybol.token == Token.LPARENT) {
            dump("atom_expression' -> ( expressions ) .");
            nextSymbol();
            expressions();
            if (currSybol.token != Token.RPARENT) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected RPARENT");
            }
        } else {
            dump("atom_expression' -> .");
        }
    }

    private void atom_expression2() {
        if (currSybol.token == Token.KW_IF) {
            dump("atom_expression'' -> if expression then expression atom_expression''' .");
            nextSymbol();
            expression();
            if (currSybol.token != Token.KW_THEN) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            atom_expression3();

        } else if (currSybol.token == Token.KW_WHILE) {
            dump("atom_expression'' -> while expression : expression } .");
            nextSymbol();
            expression();
            if (currSybol.token != Token.COLON) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }

        } else if (currSybol.token == Token.KW_FOR) {
            dump("atom_expression'' -> for identifer = expression , expression , expression : expression } .");
            nextSymbol();
            if (currSybol.token != Token.IDENTIFIER) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            if (currSybol.token != Token.ASSIGN) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            if (currSybol.token != Token.COMMA) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            if (currSybol.token != Token.COMMA) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            if (currSybol.token != Token.COLON) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }

        } else {
            dump("atom_expression'' -> expression = expression } .");
            expression();
            if (currSybol.token != Token.ASSIGN) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
            expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }

        }
    }

    private void atom_expression3() {
        if (currSybol.token == Token.RBRACE) {
            dump("atom_expression''' -> } .");
            nextSymbol();
        } else if (currSybol.token == Token.KW_ELSE) {
            dump("atom_expression''' -> else expression } .");
            nextSymbol();
            expression();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();
        }
    }

    private void expressions() {
        dump("expressions -> expression expressions' .");
        expression();
        //nextSymbol();
        expressions1();
    }

    private void expressions1() {
        if (currSybol.token == Token.COMMA) {
            dump("expressions' -> , expression expressions' .");
            nextSymbol();
            expression();
            expressions1();
        } else {
            dump("expressions' -> .");
        }
    }

    private void postfx_expression1() {
        if (currSybol.token == Token.LBRACKET) {
            dump("postfx_expression' -> [ expression ] postfx_expression' .");
            nextSymbol();
            expression();
            if (currSybol.token != Token.RBRACKET) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected RBRACKET");
            }
            nextSymbol();
            postfx_expression1();
        } else {
            dump("postfx_expression' -> .");
        }
    }

    private void multiplicative_expression1() {
        if (currSybol.token == Token.MUL) {
            dump("multiplicative_expression' -> * prefx_expression multiplicative_expression' .");
            nextSymbol();
            prefx_expression();
            multiplicative_expression1();
        } else if (currSybol.token == Token.DIV) {
            dump("multiplicative_expression' -> / prefx_expression multiplicative_expression' .");
            nextSymbol();
            prefx_expression();
            multiplicative_expression1();
        } else if (currSybol.token == Token.MOD) {
            dump("multiplicative_expression' -> % prefx_expression multiplicative_expression' .");
            nextSymbol();
            prefx_expression();
            multiplicative_expression1();
        } else {
            dump("multiplicative_expression' -> .");
        }
    }

    private void additive_expression1() {
        if (currSybol.token == Token.ADD) {
            dump("additive_expression' -> + multiplicative_expression additive_expression' .");
            nextSymbol();
            multiplicative_expression();
            additive_expression1();
        } else if (currSybol.token == Token.SUB) {
            dump("additive_expression' -> - multiplicative_expression additive_expression' .");
            nextSymbol();
            multiplicative_expression();
            additive_expression1();
        } else {
            dump("additive_expression' -> .");
        }
    }

    private void logical_and_expression1() {
        if (currSybol.token == Token.AND) {
            dump("logical_and_expression' -> & compare_expression logical_and_expression' .");
            nextSymbol();
            compare_expression();
            logical_and_expression1();
        } else {
            dump("logical_and_expression' -> .");
        }

    }

    private void logical_ior_expression1() {
        if (currSybol.token == Token.IOR) {
            dump("logical_ior_expression' -> | logical_and_expression logical_ior_expression' .");
            nextSymbol();
            logical_and_expression();
            logical_ior_expression1();
        } else {
            dump("logical_ior_expression' -> .");
        }
    }

    private void expression1() {
        if (currSybol.token == Token.LBRACE) {
            dump("expression' -> { WHERE defnitions } .");
            nextSymbol();
            if (currSybol.token != Token.KW_WHERE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }

            nextSymbol();
            defnitions();
            if (currSybol.token != Token.RBRACE) {
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
            }
            nextSymbol();

        } else {
            dump("expression' -> .");
        }
    }

    private void parameters() {
        dump("parameters -> parameter parameters' .");
        parameter();
        parameters1();
    }

    private void parameters1() {
        if (currSybol.token == Token.COMMA) {
            dump("parameters' -> , parameter parameters' .");
            nextSymbol();
            parameter();
            parameters1();
        } else {
            dump("parameters' -> .");
        }
    }

    private void parameter() {
        dump("parameter -> identifer : type .");

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected IDENTIFIER");
        }
        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected COLON");
        }
        nextSymbol();
        type();

    }

    private void typeDef() {
        dump("type_defnition -> typ identifer : type .");

        if (currSybol.token != Token.IDENTIFIER) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        nextSymbol();
        if (currSybol.token != Token.COLON) {
            Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme);
        }
        nextSymbol();
        type();
    }

    private void type() {
        switch (currSybol.token) {
            case Token.LOGICAL:
                dump("type -> logical .");
                nextSymbol();
                break;
            case Token.INTEGER:
                dump("type -> integer .");
                nextSymbol();
                break;
            case Token.STRING:
                dump("type -> string .");
                nextSymbol();
                break;
            case Token.IDENTIFIER:
                dump("type -> identifer .");
                nextSymbol();
                break;
            case Token.KW_ARR:
                dump("type -> arr [ int_constant ] type .");
                nextSymbol();
                if (currSybol.token != Token.LBRACKET) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected LBRACKET");
                }
                nextSymbol();
                if (currSybol.token != Token.INT_CONST) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected INT_CONST");
                }
                nextSymbol();
                if (currSybol.token != Token.RBRACKET) {
                    Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected RBRACKET");
                }
                nextSymbol();
                type();
                break;
            default:
                Report.error(currSybol.position, "Unknow production: " + currSybol.lexeme + "expected type");
                break;
        }
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
        currSybol = lexAn.lexAn();
    }

}
