package compiler.frames;

import java.util.*;

import compiler.abstr.tree.*;

/**
 * Klicni zapis funkcije.
 * 
 * @author sliva
 */
public class FrmFrame {

	/** Opis funckije.  */
    private final AbsFunDef fun;

	/** Staticni nivo funkcije.  */
	public final int level;

	/** Vstopna labela.  */
	public final FrmLabel label;

	/** Stevilo parametrov.  */
	public int numPars;
	
	/** Velikost bloka parametrov.  */
	public int sizePars;

	/** Lokalne spremenljivke podprograma.  */
	final LinkedList<FrmLocAccess> locVars;

	/** Velikost bloka lokalnih spremenljivk.  */
	public int sizeLocs;

	/** Velikost bloka za oldFP in retAddr.  */
    private final int sizeFPRA;

	/** Velikost bloka zacasnih spremenljivk.  */
    private final int sizeTmps;

	/** Velikost bloka registrov.  */
    private final int sizeRegs;

	/** Velikost izhodnih argumentov.  */
	public int sizeArgs;

	/** Kazalec FP.  */
	public final FrmTemp FP;

	/** Spremenljivka z rezultatom funkcije.  */
	public final FrmTemp RV;

	/**
	 * Ustvari nov klicni zapis funkcije.
	 * 
	 * @param fun Funkcija.
	 * @param level Staticni nivo funkcije.
	 */
	public FrmFrame(AbsFunDef fun, int level) {
		this.fun = fun;
		this.level = level;
		this.label = (level == 1 ? FrmLabel.newLabel(fun.name) : FrmLabel.newLabel());
		this.numPars = 0;
		this.sizePars = 4;
		this.locVars = new LinkedList<>();
		this.sizeLocs = 0;
		this.sizeFPRA = 8;
		this.sizeTmps = 0;
		this.sizeRegs = 0;
		this.sizeArgs = 0;
		FP = new FrmTemp();
		RV = new FrmTemp();
	}

	/** Velikost klicnega zapisa.  */
	public int size() {
		return sizeLocs + sizeFPRA + sizeTmps + sizeRegs + sizeArgs;
	}

	@Override
	public String toString() {
		return ("FRAME(" + fun.name + ": " +
					"level=" + level + "," +
					"label=" + label.name() + "," +
					"sizeLocs=" + sizeLocs + "," +
					"sizeArgs=" + sizeArgs + "," +
					"size=" + size() + "," +
					"FP=" + FP.name() + "," +
					"RV=" + RV.name() + ")");
	}
	
}
