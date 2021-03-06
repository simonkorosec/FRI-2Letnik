package compiler.interpreter;

import java.util.*;

import compiler.*;
import compiler.frames.*;
import compiler.imcode.*;

public class Interpreter {

    public static boolean debug = false;
    private static boolean setGlobal = true;    // ali so globalne spr nastavlene
    private static FrmFrame oldFrame = null;

    /*--- staticni del navideznega stroja ---*/

    /**
     * Preslikava globalnih spremenljivk v njihove naslove
     * */
    private static final HashMap<String, Integer> globSpr = new HashMap<>();

    /**
     * Pomnilnik navideznega stroja.
     */
    private static final HashMap<Integer, Object> mems = new HashMap<>();

    private static void stM(Integer address, Object value) {
        if (debug) System.out.println(" [" + address + "] <= " + value);
        mems.put(address, value);
    }

    private static Object ldM(Integer address) {
        Object value = mems.get(address);
        if (debug) System.out.println(" [" + address + "] => " + value);
        return value;
    }

    /**
     * Kazalec na vrh klicnega zapisa.
     */
    private static int fp = 1000;
    /**
     * Kazalec na dno klicnega zapisa.
     */
    private static int sp = 1000;

    /*--- dinamicni del navideznega stroja ---*/

    /**
     * Zacasne spremenljivke (`registri') navideznega stroja.
     */
    private final HashMap<FrmTemp, Object> temps = new HashMap<>();

    private void stT(FrmTemp temp, Object value) {
        if (debug) System.out.println(" " + temp.name() + " <= " + value);
        temps.put(temp, value);
    }

    private Object ldT(FrmTemp temp) {
        Object value = temps.get(temp);
        if (debug) System.out.println(" " + temp.name() + " => " + value);
        return value;
    }

    /*--- Izvajanje navideznega stroja. ---*/

    public Interpreter(){
        this(CodeGenerator.framesByLabel.get(CodeGenerator.getMain()), (ImcSEQ) CodeGenerator.codesByLabel.get(CodeGenerator.getMain()));
    }

    private Interpreter(FrmFrame frame, ImcSEQ code) {
        if (debug) {
            System.out.println("[START OF " + frame.label.name() + "]");
        }

        if (setGlobal){
            setGlobalVar();
            stM(1004, 0);   // začetni argument funkcije main()
        }
        int offset;
        //if (frame.level == 1){
        if (oldFrame == null){
            offset = 0;
            oldFrame = frame;
        } else {
            offset = oldFrame.size();
            oldFrame = frame;
        }

        stM(sp + offset, fp);
        //stM(sp + frame.oldFPoffset, fp);
        fp = sp;
        sp = sp - frame.size();
        stT(frame.FP, fp);
        stT(frame.RV, sp);
        if (debug) {
            System.out.println("[FP=" + fp + "]");
            System.out.println("[SP=" + sp + "]");
        }

        int pc = 0;
        Object result = null;
        while (pc < code.stmts.size()) {
            if (debug) System.out.println("pc=" + pc);
            ImcCode instruction = code.stmts.get(pc);
            result = execute(instruction);
            if (result instanceof FrmLabel) {
                for (pc = 0; pc < code.stmts.size(); pc++) {
                    instruction = code.stmts.get(pc);
                    if ((instruction instanceof ImcLABEL) && (((ImcLABEL) instruction).label.name().equals(((FrmLabel) result).name())))
                        break;
                }
            } else {
                pc++;
            }
        }

        fp = (Integer) ldM(fp + offset);
        sp = sp + frame.size();
        if (debug) {
            System.out.println("[FP=" + fp + "]");
            System.out.println("[SP=" + sp + "]");
        }

        stM(sp, result);
        if (debug) {
            System.out.println("[RV=" + result + "]");
        }

        if (debug) {
            System.out.println("[END OF " + frame.label.name() + "]");
        }
    }

    private Object execute(ImcCode instruction) {
        if (instruction instanceof ImcBINOP) {
            ImcBINOP instr = (ImcBINOP) instruction;
            Object fstSubValue = execute(instr.limc);
            Object sndSubValue = execute(instr.rimc);
            switch (instr.op) {
                case ImcBINOP.OR:
                    return (((Integer) fstSubValue != 0) || ((Integer) sndSubValue != 0) ? 1 : 0);
                case ImcBINOP.AND:
                    return (((Integer) fstSubValue != 0) && ((Integer) sndSubValue != 0) ? 1 : 0);
                case ImcBINOP.EQU:
                    if (fstSubValue instanceof Integer) {
                        return (((Integer) fstSubValue).intValue() == ((Integer) sndSubValue).intValue() ? 1 : 0);
                    } else if (fstSubValue instanceof String) {
                        return (((String) fstSubValue).compareTo((String) sndSubValue)) == 0 ? 1 : 0;
                    }
                case ImcBINOP.NEQ:
                    if (fstSubValue instanceof Integer) {
                        return (((Integer) fstSubValue).intValue() != ((Integer) sndSubValue).intValue() ? 1 : 0);
                    } else if (fstSubValue instanceof String) {
                        return (((String) fstSubValue).compareTo((String) sndSubValue)) != 0 ? 1 : 0;
                    }
                case ImcBINOP.LTH:
                    if (fstSubValue instanceof Integer) {
                        return ((Integer) fstSubValue < (Integer) sndSubValue ? 1 : 0);
                    } else if (fstSubValue instanceof String) {
                        return (((String) fstSubValue).compareTo((String) sndSubValue)) < 0 ? 1 : 0;
                    }
                case ImcBINOP.GTH:
                    if (fstSubValue instanceof Integer) {
                        return ((Integer) fstSubValue > (Integer) sndSubValue ? 1 : 0);
                    } else if (fstSubValue instanceof String) {
                        return (((String) fstSubValue).compareTo((String) sndSubValue)) > 0 ? 1 : 0;
                    }
                case ImcBINOP.LEQ:
                    if (fstSubValue instanceof Integer) {
                        return ((Integer) fstSubValue <= (Integer) sndSubValue ? 1 : 0);
                    } else if (fstSubValue instanceof String) {
                        return (((String) fstSubValue).compareTo((String) sndSubValue)) <= 0 ? 1 : 0;
                    }
                case ImcBINOP.GEQ:
                    if (fstSubValue instanceof Integer) {
                        return ((Integer) fstSubValue >= (Integer) sndSubValue ? 1 : 0);
                    } else if (fstSubValue instanceof String) {
                        return (((String) fstSubValue).compareTo((String) sndSubValue)) >= 0 ? 1 : 0;
                    }
                case ImcBINOP.ADD:
                    return ((Integer) fstSubValue + (Integer) sndSubValue);
                case ImcBINOP.SUB:
                    return ((Integer) fstSubValue - (Integer) sndSubValue);
                case ImcBINOP.MUL:
                    return ((Integer) fstSubValue * (Integer) sndSubValue);
                case ImcBINOP.DIV:
                    return ((Integer) fstSubValue / (Integer) sndSubValue);
            }
            Report.error("Internal error.");
            return null;
        }

        if (instruction instanceof ImcCALL) {
            ImcCALL instr = (ImcCALL) instruction;
            int offset = 0;
            //stM(sp + offset, execute(instr.sl));
            //offset += 4;
            for (ImcCode arg : instr.args) {
                stM(sp + offset, execute(arg));
                offset += 4;
            }
            if (instr.label.name().equals("_putInt")) {
                System.out.println(ldM(sp));
                return null;
            }
            if (instr.label.name().equals("_getInt")) {
                Scanner scanner = new Scanner(System.in);
                stM((Integer) ldM(sp), scanner.nextInt());
                return null;
            }
            if (instr.label.name().equals("_putString")) {
                System.out.println((String) ldM(sp));
                return null;
            }
            if (instr.label.name().equals("_getString")) {
                Scanner scanner = new Scanner(System.in);
                stM((Integer) ldM(sp), scanner.next());
                return null;
            }

//            new Interpreter(compiler.compiler.interpreter.CodeGenerator.framesByLabel.get(instr.fun), (ImcSEQ) compiler.compiler.interpreter.CodeGenerator.codesByLabel.get(instr.fun));

            new Interpreter(CodeGenerator.framesByLabel.get(instr.label), (ImcSEQ) CodeGenerator.codesByLabel.get(instr.label));
            return ldM(sp);
        }

        if (instruction instanceof ImcCJUMP) {
            ImcCJUMP instr = (ImcCJUMP) instruction;
            Object cond = execute(instr.cond);
            if (cond instanceof Integer) {
                if ((Integer) cond != 0) {
                    return instr.trueLabel;
                } else {
                    return instr.falseLabel;
                }
            } else {
                Report.error("CJUMP: illegal condition type.");
            }
        }

        if (instruction instanceof ImcCONST) {
            ImcCONST instr = (ImcCONST) instruction;
            if (instr.value != null) {
                return instr.value;
            } else {
                return instr.strValue;
            }
        }

        if (instruction instanceof ImcJUMP) {
            ImcJUMP instr = (ImcJUMP) instruction;
            return instr.label;
        }

        if (instruction instanceof ImcLABEL) {
            return null;
        }

        if (instruction instanceof ImcMEM) {
            ImcMEM instr = (ImcMEM) instruction;
            return ldM((Integer) execute(instr.expr));
        }

        if (instruction instanceof ImcMOVE) {
            ImcMOVE instr = (ImcMOVE) instruction;
            if (instr.dst instanceof ImcTEMP) {
                FrmTemp temp = ((ImcTEMP) instr.dst).temp;
                Object srcValue = execute(instr.src);
                stT(temp, srcValue);
                return srcValue;
            }
            if (instr.dst instanceof ImcMEM) {
                Object dstValue = execute(((ImcMEM) instr.dst).expr);
                Object srcValue = execute(instr.src);
                stM((Integer) dstValue, srcValue);
                return srcValue;
            }
        }

        if (instruction instanceof ImcNAME) {
            ImcNAME instr = (ImcNAME) instruction;
            if (instr.label.name().equals("FP")) return fp;
            if (instr.label.name().equals("SP")) return sp;
            return globSpr.get(instr.label.name());
        }

        if (instruction instanceof ImcTEMP) {
            ImcTEMP instr = (ImcTEMP) instruction;
            return ldT(instr.temp);
        }


        return null;
    }


    /*--- Nastavitev globalnih spremenljivk ---*/

    private static void setGlobalVar(){
        int addr = 3000;
        for (String name : CodeGenerator.variableByLabel.keySet()) {
            globSpr.put(name, addr);
            mems.put(addr, 0);
            addr += CodeGenerator.variableByLabel.get(name);
        }
        setGlobal = false;
    }
}
