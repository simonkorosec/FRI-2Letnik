package compiler;

import compiler.interpreter.Interpreter;
import compiler.lexan.*;
import compiler.interpreter.CodeGenerator;
import compiler.synan.*;
import compiler.abstr.*;
import compiler.abstr.tree.*;
import compiler.seman.*;
import compiler.frames.*;
import compiler.imcode.*;

/**
 * Osnovni razred prevajalnika, ki vodi izvajanje celotnega procesa prevajanja.
 *
 * @author sliva
 */
class Main {

    /**
     * Ime izvorne datoteke.
     */
    private static String sourceFileName;

    /**
     * Seznam vseh faz prevajalnika.
     */
    private static final String allPhases = "(lexan|synan|ast|seman|frames|imcode|exec)";

    /**
     * Doloca zadnjo fazo prevajanja, ki se bo se izvedla.
     */
    private static String execPhase = "exec";

    /**
     * Doloca faze, v katerih se bodo izpisali vmesni rezultati.
     */
    private static String dumpPhases = "exec";

    /**
     * Metoda, ki izvede celotni proces prevajanja.
     *
     * @param args Parametri ukazne vrstice.
     */
    public static void main(String[] args) {
        System.out.print("This is PINS compiler, v0.3:\n");

        // Pregled ukazne vrstice.
        for (String arg : args) {
            if (arg.startsWith("--")) {
                // Stikalo v ukazni vrstici.
                if (arg.startsWith("--phase=")) {
                    String phase = arg.substring("--phase=".length());
                    if (phase.matches(allPhases))
                        execPhase = phase;
                    else
                        Report.warning("Unknown exec phase '" + phase + "' ignored.");
                    continue;
                }
                if (arg.startsWith("--dump=")) {
                    String phases = arg.substring("--dump=".length());
                    if (phases.matches(allPhases + "(," + allPhases + ")*"))
                        dumpPhases = phases;
                    else
                        Report.warning("Illegal dump phases '" + phases + "' ignored.");
                    continue;
                }
                // Neznano stikalo.
                Report.warning("Unrecognized switch in the command line.");
            } else {
                // Ime izvorne datoteke.
                if (sourceFileName == null)
                    sourceFileName = arg;
                else
                    Report.warning("Source file name '" + sourceFileName + "' ignored.");
            }
        }
        if (sourceFileName == null)
            Report.error("Source file name not specified.");

        // Odpiranje datoteke z vmesnimi rezultati.
        if (dumpPhases != null) Report.openDumpFile(sourceFileName);

        // Izvajanje faz prevajanja.
        while (true) {
            // Leksikalna analiza.
            LexAn lexAn = new LexAn(sourceFileName, dumpPhases.contains("lexan"));
            if (execPhase.equals("lexan")) {
                while (lexAn.lexAn().token != Token.EOF) {
                }
                break;
            }

            // Sintaksna analiza.
            SynAn synAn = new SynAn(lexAn, dumpPhases.contains("synan"));
            AbsTree source = synAn.parse();
            if (execPhase.equals("synan")) break;

            // Abstraktna sintaksa.
            Abstr ast = new Abstr(dumpPhases.contains("ast"));
            ast.dump(source);
            if (execPhase.equals("ast")) break;

            // Semanticna analiza.
            SemAn semAn = new SemAn(dumpPhases.contains("seman"));
            source.accept(new NameChecker());
            source.accept(new TypeChecker());
            semAn.dump(source);
            if (execPhase.equals("seman")) break;

            // Klicni zapisi.
            Frames frames = new Frames(dumpPhases.contains("frames"));
            source.accept(new FrmEvaluator());
            frames.dump(source);
            if (execPhase.equals("frames")) break;

            // Vmesna koda.
            ImCode imcode = new ImCode(dumpPhases.contains("imcode"));
            ImcCodeGen imcodegen = new ImcCodeGen();
            source.accept(imcodegen);
            imcode.dump(imcodegen.chunks);
            if (execPhase.equals("imcode")) break;

            // Izvajanje vmesne kode
            CodeGenerator codegen = new CodeGenerator();
            codegen.generate(imcodegen.chunks);
            new ImCode(dumpPhases.contains("exec")).dump(imcodegen.chunks);
            Interpreter.debug = false;
            new Interpreter();
            if (execPhase.equals("exec")) break;
            // Neznana faza prevajanja.
            if (!execPhase.equals(""))
                Report.warning("Unknown compiler phase specified.");
        }

        // Zapiranje datoteke z vmesnimi rezultati.
        if (dumpPhases != null) Report.closeDumpFile();

        System.out.print(":-) Done.\n");
        System.exit(0);
    }
}
