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
 * @author guilhermelm
 */
public class SearchResult {

    private SearchResponseHeader header =  new SearchResponseHeader();
    private List<Product> items = new ArrayList<>();
    private List<Agg> aggregations = new ArrayList<>();

    
    public SearchResult() {
        
    }

    public SearchResponseHeader getHeader() {
        return header;
    }

    public void setHeader(SearchResponseHeader header) {
        this.header = header;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public List<Agg> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<Agg> aggregations) {
        this.aggregations = aggregations;
    }

}
