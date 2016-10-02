/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.productsearch.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author guilhermelm
 */
public class SearchRequest {
    
    private String query;
    private List<Filter> filters = new ArrayList<>();

    public SearchRequest() {

    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }    
}
