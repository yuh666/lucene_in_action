package org.laotie777.lucence.chapter5;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yuh
 * @Date Created in 上午9:50 2018/2/8
 * @Description Lucene只能针对单个段处理
 */
public class BookLinkCollector extends Collector {
    private Map<String, String> documents = new HashMap<>();
    private String[] titles;
    private String[] urls;
    private Scorer scorer;

    @Override
    public void setScorer(Scorer scorer) throws IOException {
        this.scorer = scorer;
    }

    @Override
    public void collect(int doc) throws IOException {
        String title = titles[doc];
        String url = urls[doc];
        System.out.println(String.format("%s -> %s -> %f",titles,url,scorer.score()));
        documents.put(url,title);
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase) throws IOException {
        titles = FieldCache.DEFAULT.getStrings(reader, "title2");
        urls = FieldCache.DEFAULT.getStrings(reader, "url");
    }

    public Map<String,String> getLinks(){
        return documents;
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
        return true;
    }
}
