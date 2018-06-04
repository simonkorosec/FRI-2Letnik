package compiler.seman;

import java.util.*;

import compiler.*;
import compiler.abstr.tree.*;
import compiler.seman.type.SemAtomType;
import compiler.seman.type.SemFunType;
import compiler.seman.type.SemType;

public class SymbTable {

    /**
     * Simbolna tabela.
     */
    private static final HashMap<String, LinkedList<AbsDef>> mapping = new HashMap<>();

    /**
     * Trenutna globina nivoja gnezdenja.
     */
    private static int scope = 0;

    /**
     * Preide na naslednji nivo gnezdenja.
     */
    public static void newScope() {
        if (scope == 0) {
            libFunctions();
        }
        scope++;
    }

    /**
     * Odstrani vse definicije na trenutnem nivoju gnezdenja in preide na
     * predhodni nivo gnezdenja.
     */
    public static void oldScope() {
        LinkedList<String> allNames = new LinkedList<>();
        allNames.addAll(mapping.keySet());
        for (String name : allNames) {
            try {
                SymbTable.del(name);
            } catch (SemIllegalDeleteException ignored) {
            }
        }
        scope--;
    }

    /**
     * Vstavi novo definicijo imena na trenutni nivo gnezdenja.
     *
     * @param name   Ime.
     * @param newDef Nova definicija.
     * @throws SemIllegalInsertException Ce definicija imena na trenutnem nivoju gnezdenja ze obstaja.
     */
    static void ins(String name, AbsDef newDef)
            throws SemIllegalInsertException {
        LinkedList<AbsDef> allNameDefs = mapping.get(name);
        if (allNameDefs == null) {
            allNameDefs = new LinkedList<>();
            allNameDefs.addFirst(newDef);
            SymbDesc.setScope(newDef, scope);
            mapping.put(name, allNameDefs);
            return;
        }
        if ((allNameDefs.size() == 0)
                || (SymbDesc.getScope(allNameDefs.getFirst()) == null)) {
            Thread.dumpStack();
            Report.error("Internal error.");
            return;
        }
        if (SymbDesc.getScope(allNameDefs.getFirst()) == scope)
            throw new SemIllegalInsertException();
        allNameDefs.addFirst(newDef);
        SymbDesc.setScope(newDef, scope);
    }

    /**
     * Odstrani definicijo imena s trenutnega nivoja gnezdenja.
     *
     * @param name Ime.
     * @throws SemIllegalDeleteException Ce definicije imena na trenutnem nivoju gnezdenja ni.
     */
    private static void del(String name) throws SemIllegalDeleteException {
        LinkedList<AbsDef> allNameDefs = mapping.get(name);
        if (allNameDefs == null)
            throw new SemIllegalDeleteException();
        if ((allNameDefs.size() == 0)
                || (SymbDesc.getScope(allNameDefs.getFirst()) == null)) {
            Thread.dumpStack();
            Report.error("Internal error.");
            return;
        }
        if (SymbDesc.getScope(allNameDefs.getFirst()) < scope)
            throw new SemIllegalDeleteException();
        allNameDefs.removeFirst();
        if (allNameDefs.size() == 0)
            mapping.remove(name);
    }

    /**
     * Vrne definicijo imena.
     *
     * @param name Ime.
     * @return Definicija imena ali null, ce definicija imena ne obstaja.
     */
    public static AbsDef fnd(String name) {
        LinkedList<AbsDef> allNameDefs = mapping.get(name);
        if (allNameDefs == null)
            return null;
        if (allNameDefs.size() == 0)
            return null;
        return allNameDefs.getFirst();
    }

    private static void libFunctions() {
        try {
            if (fnd("getInt") == null) {
                Vector<AbsPar> params = new Vector<>();
                params.add(new AbsPar(new Position(0, 0), "i", new AbsAtomType(new Position(0, 0), AbsAtomType.INT)));

                AbsFunDef def = new AbsFunDef(new Position(0, 0),
                        "getInt", params,
                        new AbsAtomType(new Position(0, 0), AbsAtomType.INT),
                        null);

                Vector<SemType> parTypes = new Vector<>();
                parTypes.add(new SemAtomType(SemAtomType.INT));
                SymbDesc.setType(def, new SemFunType(parTypes, new SemAtomType(SemAtomType.INT)));
                SymbDesc.setType(params.get(0), new SemAtomType(SemAtomType.INT));
                ins("getInt", def);
            }
            if (fnd("putInt") == null) {
                Vector<AbsPar> params = new Vector<>();
                params.add(new AbsPar(new Position(0, 0), "i", new AbsAtomType(new Position(0, 0), AbsAtomType.INT)));

                AbsFunDef def = new AbsFunDef(new Position(0, 0),
                        "putInt",
                        params,
                        new AbsAtomType(new Position(0, 0), AbsAtomType.INT),
                        null);

                Vector<SemType> parTypes = new Vector<>();
                parTypes.add(new SemAtomType(SemAtomType.INT));
                SymbDesc.setType(def, new SemFunType(parTypes, new SemAtomType(SemAtomType.INT)));
                SymbDesc.setType(params.get(0), new SemAtomType(SemAtomType.INT));
                ins("putInt", def);
            }
            if (fnd("putString") == null) {
                Vector<AbsPar> params = new Vector<>();
                params.add(new AbsPar(new Position(0, 0), "s", new AbsAtomType(new Position(0, 0), AbsAtomType.STR)));

                AbsFunDef def = new AbsFunDef(new Position(0, 0),
                        "putString", params,
                        new AbsAtomType(new Position(0, 0),
                                AbsAtomType.INT),
                        null);

                Vector<SemType> parTypes = new Vector<>();
                parTypes.add(new SemAtomType(SemAtomType.STR));
                SymbDesc.setType(def, new SemFunType(parTypes, new SemAtomType(SemAtomType.INT)));
                SymbDesc.setType(params.get(0), new SemAtomType(SemAtomType.STR));
                ins("putString", def);
            }
            if (fnd("getString") == null) {
                Vector<AbsPar> params = new Vector<>();
                params.add(new AbsPar(new Position(0, 0), "s", new AbsAtomType(new Position(0, 0), AbsAtomType.STR)));

                AbsFunDef def = new AbsFunDef(new Position(0, 0),
                        "getString", params,
                        new AbsAtomType(new Position(0, 0),
                                AbsAtomType.STR),
                        null);

                Vector<SemType> parTypes = new Vector<>();
                parTypes.add(new SemAtomType(SemAtomType.STR));
                SymbDesc.setType(def, new SemFunType(parTypes, new SemAtomType(SemAtomType.STR)));
                SymbDesc.setType(params.get(0), new SemAtomType(SemAtomType.STR));
                ins("getString", def);
            }
        } catch (Exception ex) {
            Report.error("[SymbTable] While inserting library functions, failed.");
        }
    }
}
