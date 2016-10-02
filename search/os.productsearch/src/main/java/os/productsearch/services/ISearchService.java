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
public interface ISearchService {

    String[] getSuggestions(String query) throws SearchAPIException;
    
    SearchResult doSearch(SearchRequest request) throws SearchAPIException;
    
}
