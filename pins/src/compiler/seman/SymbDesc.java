package compiler.seman;

import java.util.*;

import compiler.abstr.tree.*;

/**
 * Opisi posameznih definicij.
 * 
 * @author sliva
 */
public class SymbDesc {

	/** Nivo vidnosti. */
	private static HashMap<AbsTree, Integer> scope = new HashMap<>();

	/**
	 * Doloci globino nivoja vidnosti za dano definicijo imena.
	 * 
	 * @param node
	 *            Vozlisce drevesa.
	 * @param nodeScope
	 *            Globina nivoja vidnosti.
	 */
	public static void setScope(AbsTree node, int nodeScope) {
		scope.put(node, new Integer(nodeScope));
	}

	/**
	 * Vrne globino nivoja vidnosti za dano definicijo imena.
	 * 
	 * @param node
	 *            Vozlisce drevesa.
	 * @return Globina nivoja vidnosti.
	 */
	public static Integer getScope(AbsTree node) {
		return scope.get(node);
	}

	/** Definicija imena. */
	private static HashMap<AbsTree, AbsDef> nameDef = new HashMap<>();

	/**
	 * Poveze vozlisce drevesa z definicijo imena.
	 * 
	 * @param node
	 *            Vozlisce drevesa.
	 * @param def
	 *            Definicija imena.
	 */
	public static void setNameDef(AbsTree node, AbsDef def) {
		nameDef.put(node, def);
	}

	/**
	 * Vrne definicijo imena, ki je povezano z vozliscem.
	 * 
	 * @param node
	 *            Vozlisce drevesa.
	 * @return Definicija imena.
	 */
	public static AbsDef getNameDef(AbsTree node) {
		return nameDef.get(node);
	}

}
