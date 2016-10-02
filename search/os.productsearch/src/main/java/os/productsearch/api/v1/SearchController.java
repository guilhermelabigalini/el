/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.productsearch.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import os.productsearch.services.ISearchService;
import os.productsearch.services.SearchAPIException;
import os.productsearch.services.SearchRequest;
import os.productsearch.services.SearchResult;

/**
 *
 * @author guilhermelm
 */
@RestController()
public class SearchController {

    @Autowired
    private ISearchService searchService;

    /**
     * POST http://localhost:9000/api/v1/search
     * Content-Type: application/json
     * {"query":"123",
     *  "filters":[
     *  {"field":"StatusID", "value": 32},
     *  {"field":"StatusID", "value": 32}]
     * }   
     * @param g
     * @return
     * @throws os.productsearch.services.SearchAPIException
     */
    @RequestMapping(path = "/api/v1/search", method = POST)
    @ResponseStatus(HttpStatus.OK)
    public SearchResult doSearch(
            @RequestBody SearchRequest g) throws SearchAPIException {
        return searchService.doSearch(g);
    }

        @RequestMapping(path = "/api/v1/search", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public String[] getSuggestions(
           String query) throws SearchAPIException {
        return searchService.getSuggestions(query);
    }

}
