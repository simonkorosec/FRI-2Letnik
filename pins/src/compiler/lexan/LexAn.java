package compiler.lexan;

import compiler.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Leksikalni analizator.
 *
 * @author sliva
 */
public class LexAn {

    /**
     * Ali se izpisujejo vmesni rezultati.
     */
    private boolean dump;
    private FileInputStream fis;
    private int currChar;           /* Trenutni znak za pregled */
    private int lineNum;            /* Trenutna vrstica */
    private int colNum;             /* Trenutna pozicija v vrstici */

    /**
     * Ustvari nov leksikalni analizator.
     *
     * @param sourceFileName Ime izvorne datoteke.
     * @param dump           Ali se izpisujejo vmesni rezultati.
     */
    public LexAn(String sourceFileName, boolean dump) {
        // TODO

        this.openFile(sourceFileName);
        this.dump = dump;

        this.lineNum = 1;
        this.colNum = 0;
        this.nextCharr();

    }

    /**
     * Vrne naslednji simbol iz izvorne datoteke. Preden vrne simbol, ga izpise
     * na datoteko z vmesnimi rezultati.
     *
     * @return Naslednji simbol iz izvorne datoteke.
     */
    public Symbol lexAn() {
        // TODO Izpis napak in col -1 pri besedah

        while (true) {
            StringBuilder string = new StringBuilder("");
            int col = this.colNum;
            int line = this.lineNum;

            // Pregled konca datoteke
            if (this.currChar == -1) {
                return new Symbol(Token.EOF, "", line, col, line, col);
            }

            // Pregled belega besedila
            if (this.currChar == 32 || this.currChar == 9 ) {
                nextCharr();
                continue;
            } else if (this.currChar == 10) {
                this.lineNum++;
                this.colNum = 0;
                nextCharr();
                continue;
            } else if (this.currChar == 13){
                this.colNum = 0;
                nextCharr();
                continue;
            }

            if (this.currChar == (int) '+') {
                nextCharr();
                return new Symbol(Token.ADD, "+", line, col, line, col);
            }
            if (this.currChar == (int) '-') {
                nextCharr();
                return new Symbol(Token.SUB, "-", line, col, line, col);
            }
            if (this.currChar == (int) '*') {
                nextCharr();
                return new Symbol(Token.MUL, "*", line, col, line, col);
            }
            if (this.currChar == (int) '/') {
                nextCharr();
                return new Symbol(Token.DIV, "/", line, col, line, col);
            }
            if (this.currChar == (int) '%') {
                nextCharr();
                return new Symbol(Token.MOD, "%", line, col, line, col);
            }
            if (this.currChar == (int) '&') {
                nextCharr();
                return new Symbol(Token.AND, "&", line, col, line, col);
            }
            if (this.currChar == (int) '|') {
                nextCharr();
                return new Symbol(Token.IOR, "|", line, col, line, col);
            }
            if (this.currChar == (int) '!') {
                nextCharr();
                if (this.currChar == (int) '=') {
                    return new Symbol(Token.NEQ, "!=", line, col, this.lineNum, this.colNum);
                } else {
                    return new Symbol(Token.NOT, "!", line, col, line, col);
                }
            }
            if (this.currChar == (int) '=') {
                nextCharr();
                if (this.currChar == (int) '=') {
                    return new Symbol(Token.EQU, "==", line, col, this.lineNum, this.colNum);
                } else {
                    return new Symbol(Token.ASSIGN, "=", line, col, line, col);
                }
            }
            if (this.currChar == (int) '<') {
                nextCharr();
                if (this.currChar == (int) '=') {
                    return new Symbol(Token.LEQ, "<=", line, col, this.lineNum, this.colNum);
                } else {
                    return new Symbol(Token.LTH, "<", line, col, line, col);
                }
            }
            if (this.currChar == (int) '>') {
                nextCharr();
                if (this.currChar == (int) '=') {
                    return new Symbol(Token.GEQ, ">=", line, col, this.lineNum, this.colNum);
                } else {
                    return new Symbol(Token.GTH, ">", line, col, line, col);
                }
            }
            if (this.currChar == (int) '(') {
                nextCharr();
                return new Symbol(Token.LPARENT, "(", line, col, line, col);
            }
            if (this.currChar == (int) ')') {
                nextCharr();
                return new Symbol(Token.RPARENT, ")", line, col, line, col);
            }
            if (this.currChar == (int) '[') {
                nextCharr();
                return new Symbol(Token.LBRACKET, "[", line, col, line, col);
            }
            if (this.currChar == (int) ']') {
                nextCharr();
                return new Symbol(Token.RBRACKET, "]", line, col, line, col);
            }
            if (this.currChar == (int) '{') {
                nextCharr();
                return new Symbol(Token.LBRACE, "{", line, col, line, col);
            }
            if (this.currChar == (int) '}') {
                nextCharr();
                return new Symbol(Token.RBRACE, "}", line, col, line, col);
            }
            if (this.currChar == (int) ':') {
                nextCharr();
                return new Symbol(Token.COLON, ":", line, col, line, col);
            }
            if (this.currChar == (int) ';') {
                nextCharr();
                return new Symbol(Token.SEMIC, ";", line, col, line, col);
            }
            if (this.currChar == (int) '.') {
                nextCharr();
                return new Symbol(Token.DOT, ".", line, col, line, col);
            }
            if (this.currChar == (int) ',') {
                nextCharr();
                return new Symbol(Token.COMMA, ",", line, col, line, col);
            }

            // Prebere komentarje
            if (this.currChar == (int) '#') {
                while (true) {
                    if (this.currChar == 10 || this.currChar == 13) {
                        nextCharr();
                        break;
                    }
                    nextCharr();
                }
                continue;
            }

            // Prebere stevilke
            if (this.currChar >= (int) '0' && this.currChar <= (int) '9') {
                while (true) {
                    if (this.currChar >= (int) '0' && this.currChar <= (int) '9') {
                        string.append((char) this.currChar);
                        nextCharr();
                    } else {
                        return new Symbol(Token.INT_CONST, string.toString(), line, col, this.lineNum, this.colNum);
                    }
                }
            }

            // Prebere imena, kljucne bsede, atomarne pod. tipe
            if ((this.currChar >= (int) 'A' && this.currChar <= (int) 'Z') ||
                    (this.currChar >= (int) 'a' && this.currChar <= (int) 'z') ||
                    this.currChar == (int) '_') {
                while (true) {
                    if ((this.currChar >= (int) 'A' && this.currChar <= (int) 'Z') ||
                            (this.currChar >= (int) 'a' && this.currChar <= (int) 'z') ||
                            (this.currChar >= (int) '0' && this.currChar <= (int) '9') ||
                            this.currChar == (int) '_') {
                        string.append((char) this.currChar);
                        nextCharr();
                    } else {
                        String s = string.toString();
                        switch (s) {
                            case "arr":
                                return new Symbol(Token.KW_ARR, s, line, col, this.lineNum, this.colNum);
                            case "else":
                                return new Symbol(Token.KW_ELSE, s, line, col, this.lineNum, this.colNum);
                            case "for":
                                return new Symbol(Token.KW_FOR, s, line, col, this.lineNum, this.colNum);
                            case "fun":
                                return new Symbol(Token.KW_FUN, s, line, col, this.lineNum, this.colNum);
                            case "if":
                                return new Symbol(Token.KW_IF, s, line, col, this.lineNum, this.colNum);
                            case "then":
                                return new Symbol(Token.KW_THEN, s, line, col, this.lineNum, this.colNum);
                            case "typ":
                                return new Symbol(Token.KW_TYP, s, line, col, this.lineNum, this.colNum);
                            case "var":
                                return new Symbol(Token.KW_VAR, s, line, col, this.lineNum, this.colNum);
                            case "where":
                                return new Symbol(Token.KW_WHERE, s, line, col, this.lineNum, this.colNum);
                            case "while":
                                return new Symbol(Token.KW_WHILE, s, line, col, this.lineNum, this.colNum);
                            case "logical":
                                return new Symbol(Token.LOGICAL, s, line, col, this.lineNum, this.colNum);
                            case "integer":
                                return new Symbol(Token.INTEGER, s, line, col, this.lineNum, this.colNum);
                            case "string":
                                return new Symbol(Token.STRING, s, line, col, this.lineNum, this.colNum);
                            case "true":
                                return new Symbol(Token.LOG_CONST, s, line, col, this.lineNum, this.colNum);
                            case "false":
                                return new Symbol(Token.LOG_CONST, s, line, col, this.lineNum, this.colNum);
                            default:
                                return new Symbol(Token.IDENTIFIER, s, line, col, this.lineNum, this.colNum);
                        }
                    }
                }
            }

            // Prebere string konstante
            if (this.currChar == (int) '\'') {
                while (true) {
                    nextCharr();
                    if (this.currChar == (int) '\'') {
                        nextCharr();
                        if (this.currChar == (int) '\'') {
                            string.append('\'');
                        } else {
                            return new Symbol(Token.STR_CONST, string.toString(), line, col, this.lineNum, this.colNum);
                        }
                    }

                    if (this.currChar >= 32 && this.currChar <= 126) {
                        string.append((char) this.currChar);
                    }
                }
            }

        }

    }

    /**
     * Izpise simbol v datoteko z vmesnimi rezultati.
     *
     * @param symb Simbol, ki naj bo izpisan.
     */
    private void dump(Symbol symb) {
        if (!dump) return;
        if (Report.dumpFile() == null) return;
        if (symb.token == Token.EOF)
            Report.dumpFile().println(symb.toString());
        else
            Report.dumpFile().println("[" + symb.position.toString() + "] " + symb.toString());
    }

    /**
     * Preberi naslednji znak v datoteki
     */
    private void nextCharr() {
        try {
            this.currChar = this.fis.read();
            this.colNum++;
//            if (this.currChar == 10 || this.currChar == 13) {
//                this.lineNum++;
//                this.colNum = 1;
//            }
        } catch (IOException ignored) {
        }
    }

    private void openFile(String sourceFileName) {
        try {
            this.fis = new FileInputStream(new File(sourceFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeFile() {
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
