package org.nextime.ion.backoffice.bean;

import org.apache.commons.lang.StringUtils;

/**
 * Bean options utilisées  pour tous les formulaires
 */
public class Option implements Comparable {

    private String label = StringUtils.EMPTY;
    private String value = StringUtils.EMPTY;


    public Option(String p_Label, String p_Value) {
        label = p_Label;
        value = p_Value;
    }

    /**
     * @return Retourne l'attribut <b>label</b>.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label la valeur <b>label</b> &agrave; allouer.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int compareTo(Object o) {
        if (o == null)
            throw new NullPointerException("Cannot compare with null object");
        if (!(o instanceof Option))
            throw new ClassCastException("Cannot compare Option with " + o.getClass());

        Option opt = (Option) o;

        return value.compareTo(opt.getValue());
    }
}
