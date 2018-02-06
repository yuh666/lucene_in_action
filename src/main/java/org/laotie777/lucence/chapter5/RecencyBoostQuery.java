package org.laotie777.lucence.chapter5;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;
import java.util.Date;

/**
 * @Author yuh
 * @Date Created in 下午12:57 2018/2/6
 * @Description 自定义评分后置处理类
 */
public class RecencyBoostQuery extends CustomScoreQuery {

    private String dayField;
    private int maxDay;
    private double multiplier;
    private int today ;

    public RecencyBoostQuery(Query subQuery, String dayField, int maxDay, double multiplier) {
        super(subQuery);
        this.dayField = dayField;
        this.maxDay = maxDay;
        this.multiplier = multiplier;
        today = (int) (System.currentTimeMillis()/(1000*3600*24));
    }

    private class RecencyBooster extends CustomScoreProvider {
        private int[] days;

        public RecencyBooster(IndexReader reader) throws IOException {
            super(reader);
            days = FieldCache.DEFAULT.getInts(reader, dayField);
        }

        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
            int day1 = days[doc];
            int day = today - day1;
            if (day < maxDay) {
                float boost = (float) (multiplier*(maxDay - day) / maxDay);
                subQueryScore *= (1 + boost);
            }
            return subQueryScore;
        }
    }

    @Override
    protected CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
        return new RecencyBooster(reader);
    }

}
