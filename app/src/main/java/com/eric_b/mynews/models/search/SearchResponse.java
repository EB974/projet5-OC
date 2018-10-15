
package com.eric_b.mynews.models.search;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("docs")
    @Expose
    private List<SearchDoc> docs = null;
    @SerializedName("meta")
    @Expose
    private SearchMeta meta;

    public List<SearchDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<SearchDoc> docs) {
        this.docs = docs;
    }

    public SearchMeta getMeta() {
        return meta;
    }

    public void setMeta(SearchMeta meta) {
        this.meta = meta;
    }

}
