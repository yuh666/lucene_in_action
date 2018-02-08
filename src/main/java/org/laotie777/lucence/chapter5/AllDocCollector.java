package org.laotie777.lucence.chapter5;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author yuh
 * @Date Created in 上午10:06 2018/2/8
 * @Description 每个索引段里面的文档号都是单独计数的
 */
public class AllDocCollector extends Collector {

    private List<ScoreDoc> list = new LinkedList<>();
    private Scorer scorer;
    private int docBase;


    @Override
    public void setScorer(Scorer scorer) throws IOException {
        this.scorer = scorer;
    }

    @Override
    public void collect(int doc) throws IOException {
        list.add(new ScoreDoc(docBase+doc,scorer.score()));
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase) throws IOException {
        this.docBase = docBase;
    }

    public List<ScoreDoc> getHits(){
        return list;
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
        return true;
    }
}
