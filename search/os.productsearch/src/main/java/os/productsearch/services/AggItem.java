/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.productsearch.services;

/**
 *
 * @author guilherme
 */
public class AggItem {
    
    public String key;
    public String keyDescription;
    public long total;

    public AggItem(String key, String keyDescription, long total) {
        this.key = key;
        this.keyDescription = keyDescription;
        this.total = total;
    }

    public AggItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyDescription() {
        return keyDescription;
    }

    public void setKeyDescription(String keyDescription) {
        this.keyDescription = keyDescription;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
    
}
