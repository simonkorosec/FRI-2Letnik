package compiler.imcode;

import compiler.*;

/**
 * Konstanta.
 *
 * @author sliva
 */
public class ImcCONST extends ImcExpr {

    /**
     * Vrednost.
     */
    public Integer value;
    public String strValue;

    /**
     * Ustvari novo konstanto.
     *
     * @param value Vrednost konstante.
     */
    public ImcCONST(Integer value) {
        this.value = value;
    }

    public ImcCONST(String value) {
        this.strValue = value;
    }

    @Override
    public void dump(int indent) {
        if (value != null) {
            Report.dump(indent, "CONST value=" + value.toString());
        } else {
            Report.dump(indent, "CONST value=" + strValue.toString());
        }
    }

    @Override
    public ImcESEQ linear() {
        return new ImcESEQ(new ImcSEQ(), this);
    }

}
