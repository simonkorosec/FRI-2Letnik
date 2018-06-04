package compiler.interpreter;

import compiler.Report;
import compiler.frames.FrmFrame;
import compiler.frames.FrmLabel;
import compiler.imcode.ImcChunk;
import compiler.imcode.ImcCode;
import compiler.imcode.ImcCodeChunk;
import compiler.imcode.ImcDataChunk;

import java.util.HashMap;
import java.util.LinkedList;

public class CodeGenerator {
    static HashMap<FrmLabel, FrmFrame> framesByLabel;    // Labela in frame za funkcije
    static HashMap<FrmLabel, ImcCode> codesByLabel;      // Labela in koda za funkcije
    static HashMap<String, Integer> variableByLabel;     // Labela in velikost globalnih spr
    private static FrmLabel main;                        // Labela funkcije main()

    public CodeGenerator() {
        framesByLabel = new HashMap<>();
        codesByLabel = new HashMap<>();
        variableByLabel = new HashMap<>();
        main = null;
    }

    public static FrmLabel getMain() {
        if (main == null){
            Report.error("Program does not contain main()");
        }
        return main;
    }

    public void generate(LinkedList<ImcChunk> chunks){
        for (ImcChunk chunk : chunks) {
            if (chunk instanceof ImcCodeChunk){
                FrmFrame frame = ((ImcCodeChunk) chunk).frame;
                framesByLabel.put(frame.label, frame);
                codesByLabel.put(frame.label, ((ImcCodeChunk) chunk).imcode.linear());

                ((ImcCodeChunk) chunk).lincode = ((ImcCodeChunk) chunk).imcode.linear();

                if (frame.label.name().equals("_main")){
                    main = frame.label;
                }
            } else if (chunk instanceof ImcDataChunk){
                variableByLabel.put(((ImcDataChunk) chunk).label.name(), ((ImcDataChunk) chunk).size);
            }
        }
    }
}
