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
public class SearchResponseHeader {
    private long total;
    private long took;

    public SearchResponseHeader() {
        
    }
    
    public SearchResponseHeader(long total, long took) {
        this.total = total;
        this.took = took;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTook() {
        return took;
    }

    public void setTook(long took) {
        this.took = took;
    }
    
    
}
