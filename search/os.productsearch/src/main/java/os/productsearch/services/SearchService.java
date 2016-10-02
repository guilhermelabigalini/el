/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.productsearch.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;

/**
 *
 * @author guilhermelm
 */
@Component
public class SearchService implements ISearchService {

    private static class AggregationInfo {

        public final String FieldName;
        public final String Field;
        private final HashMap<String, String> Descriptions;

        public AggregationInfo(String FieldName, String Field, HashMap<String, String> Descriptions) {
            this.FieldName = FieldName;
            this.Field = Field;
            this.Descriptions = Descriptions;
        }

        public AggregationInfo(String FieldName, String Field) {
            this.FieldName = FieldName;
            this.Field = Field;
            this.Descriptions = null;
        }

        public String getDescription(String value) {
            if (Descriptions == null) {
                return value;
            }

            return Descriptions.get(value);
        }
    }

    private static final HashMap<String, String> ItemConditionDescription = new HashMap<String, String>() {
        {
            put("1", "New");
            put("2", "Used");
            put("3", "Refurbished");
        }
    };

    private static final HashMap<String, AggregationInfo> DefaultAggregation = new HashMap<String, AggregationInfo>() {
        {
            put("condition", new AggregationInfo("condition", "condition", ItemConditionDescription));
            put("brand", new AggregationInfo("brand", "Brand"));
            put("category", new AggregationInfo("category", "Category"));
        }
    };

    public static final ObjectMapper jsonReader;

    static {
        // init object mapper 
        jsonReader = new ObjectMapper();
        jsonReader.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        jsonReader.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private Client getClient() throws UnknownHostException {
        return TransportClient.builder()
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    private SearchResult internalDoSearch(SearchRequest request) throws SearchAPIException {
        try (Client client = this.getClient()) {

            QueryBuilder qbSearchQuery = QueryBuilders.matchQuery("_all", request.getQuery());

            if (!request.getFilters().isEmpty()) {
                BoolQueryBuilder filters = QueryBuilders.boolQuery();

                for (Filter f : request.getFilters()) {
                    filters = filters.filter(QueryBuilders.termQuery(f.getField(), f.getValue()));
                }

                filters.must(qbSearchQuery);

                qbSearchQuery = filters;
            }

            SearchRequestBuilder srb = client.prepareSearch("product");

            srb.setQuery(qbSearchQuery);

            for (Map.Entry<String, AggregationInfo> ai : DefaultAggregation.entrySet()) {
                if (!Iterables.any(request.getFilters(), fi -> fi.getField().equalsIgnoreCase(ai.getKey()))) {
                    srb.addAggregation(AggregationBuilders.terms(ai.getKey()).field(ai.getKey()));
                }
            }

            //srb.addAggregation(AggregationBuilders.terms("StatusID").field("StatusID"));
            //srb.addAggregation(AggregationBuilders.terms("ItemClassId").field("ItemClassId"));
            // max recors in the result
            srb.setSize(10);

            SearchResponse response = srb.execute().get();

            SearchResult result = new SearchResult();

            // header
            result.getHeader().setTook(response.getTookInMillis());
            result.getHeader().setTotal(response.getHits().getTotalHits());

            // aggregates
            if (response.getAggregations() != null) {
                for (Map.Entry<String, Aggregation> e : response.getAggregations().asMap().entrySet()) {
                    if (e.getValue() instanceof Terms) {
                        AggregationInfo ai = DefaultAggregation.get(e.getKey());

                        Agg agg = new Agg();
                        agg.setField(ai.Field);
                        agg.setFieldName(ai.FieldName);

                        agg.setKeys(((Terms) e.getValue()).getBuckets().stream()
                                .map(b -> new AggItem(b.getKeyAsString(), ai.getDescription(b.getKeyAsString()), b.getDocCount()))
                                .collect(Collectors.toList()));
                        result.getAggregations().add(agg);
                    }
                }
            }

            // details 
            result.setItems(
                    Stream.of(response.getHits().getHits())
                    .map((hit) -> {
                        Product p = null;
                        try {
                            p = jsonReader.readValue(hit.getSourceAsString(), Product.class);
                            p.id = hit.getId();
                        } catch (IOException ex) {
                            Logger.getLogger(SearchService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return p;                             
                    }).collect(Collectors.toList())
            );

            return result;
        } catch (IOException | InterruptedException | ExecutionException ex) {
            throw new SearchAPIException(ex);
        }
    }
    
    @Override
    public String[] getSuggestions(String query) throws SearchAPIException {
        try (Client client = this.getClient()) {
             String suggestName = "product-suggest";
             
             CompletionSuggestionBuilder csb = SuggestBuilders.completionSuggestion(suggestName)
                     .field("productName_suggest")
                     .text(query);
             
             SuggestRequestBuilder suggestRequestBuilder = client.prepareSuggest("product").addSuggestion(csb);
             SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();
             String[] result = suggestResponse.getSuggest().getSuggestion(suggestName)
                     .iterator().next().getOptions()
                     .stream()
                     .map(e -> e.getText().string()).toArray(size -> new String[size]);
             
             return result;
        } catch (IOException ex) {
            throw new SearchAPIException(ex);
        }
    }
    
    @Override
    public SearchResult doSearch(SearchRequest request) throws SearchAPIException {
        return this.internalDoSearch(request);
    }
}
