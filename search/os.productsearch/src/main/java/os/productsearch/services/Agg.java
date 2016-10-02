/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.productsearch.services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guilherme
 */
public class Agg {

    private String field;
    private String fieldName;
    private List<AggItem> keys = new ArrayList<>();

    public Agg() {

    }

    public Agg(String field, String fieldName, List<AggItem> aggs) {
        this.field = field;
        this.keys = aggs;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<AggItem> getKeys() {
        return keys;
    }

    public void setKeys(List<AggItem> keys) {
        this.keys = keys;
    }
}
