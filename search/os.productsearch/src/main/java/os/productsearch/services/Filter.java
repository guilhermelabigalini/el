/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.productsearch.services;

/**
 *
 * @author guilhermelm
 */
public class Filter {

    private String field;
    private String value;

    public Filter(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public Filter() {

    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Filter{" + "field=" + field + ", value=" + value + '}';
    }

}
