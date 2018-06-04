package compiler.imcode;

import java.util.*;

import compiler.*;

/**
 * Izracun fragmentov vmesne kode.
 * 
 * @author sliva
 */
public class ImCode {

	/** Ali se izpisujejo vmesni rezultati. */
	private final boolean dump;

	/**
	 * Izracun fragmentov vmesne kode.
	 * 
	 * @param dump
	 *            Ali se izpisujejo vmesni rezultati.
	 */
	public ImCode(boolean dump) {
		this.dump = dump;
	}
	
	/**
	 * Izpise fragmente vmesne kode na datoteko vmesnih rezultatov.
	 * 
	 * @param chunks
	 *            Seznam fragmentov vmesne kode.
	 */
	public void dump(LinkedList<ImcChunk> chunks) {
		if (! dump) return;
		if (Report.dumpFile() == null) return;
		for (ImcChunk chunk1 : chunks) {
			chunk1.dump();
		}
	}

}
