package compiler.seman.type;

import java.util.*;

/**
 * Opis funkcijskega tipa.
 *
 * @author sliva
 */
public class SemFunType extends SemType {

    /**
     * Tipi parametrov.
     */
    private final SemType parTypes[];

    /**
     * Tipa rezultata.
     */
    public final SemType resultType;

    /**
     * Ustvari nov opis funkcijskega tipa.
     *
     * @param parTypes   Tipi parametrov.
     * @param resultType Tip rezultata.
     */
    public SemFunType(Vector<SemType> parTypes, SemType resultType) {
        this.parTypes = new SemType[parTypes.size()];
        for (int par = 0; par < parTypes.size(); par++)
            this.parTypes[par] = parTypes.elementAt(par);
        this.resultType = resultType;
    }

    /**
     * Vrne stevilo parametrov.
     *
     * @return Stevilo parametrov.
     */
    public int getNumPars() {
        return parTypes.length;
    }

    /**
     * Vrne tip zahtevanega parametra.
     *
     * @param index Indeks zahtevanega parametra.
     * @return Tip zahtevanega parametra.
     */
    public SemType getParType(int index) {
        return parTypes[index];
    }

    @Override
    public boolean sameStructureAs(SemType type) {
        if (type.actualType() instanceof SemFunType) {
            SemFunType funType = (SemFunType) (type.actualType());
            if (this.getNumPars() != funType.getNumPars())
                return false;
            for (int par = 0; par < getNumPars(); par++) {
                if (!this.getParType(par).sameStructureAs(
                        funType.getParType(par)))
                    return false;
            }
            return this.resultType.sameStructureAs(funType.resultType);
        } else
            return false;
    }

    /**
     * Vrne velikost podatkovnega tipa v bytih.
     *
     * @return Velikost podatkovnega tipa v bytih.
     */
    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("FUN(");
        for (int par = 0; par < parTypes.length; par++) {
            str.append(par > 0 ? "," : "").append(parTypes[par].toString());
        }
        str.append(":").append(resultType.toString());
        str.append(")");
        return str.toString();
    }

}
