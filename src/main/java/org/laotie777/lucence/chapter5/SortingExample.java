package org.laotie777.lucence.chapter5;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * @Author yuh
 * @Date Created in 上午9:12 2018/2/5
 * @Description 排序Demo
 */
public class SortingExample {

    protected Directory directory;

    public SortingExample(Directory directory) {
        this.directory = directory;
    }

    /**
     * 按照排序展示结果 排序是依靠索引进行排序
     *
     * @param query
     * @param sort
     */
    public void displaySortResults(Query query, Sort sort) throws IOException {
        IndexSearcher searcher = new IndexSearcher(directory);
        searcher.setDefaultFieldSortScoring(true, false);
        TopDocs results = searcher.search(query, null, 20, sort);
        System.out.println("\nResults for: " +                      // #4
                query.toString() + " sorted by " + sort);

        System.out.println(StringUtils.rightPad("Title", 30) +
                StringUtils.rightPad("pubmonth", 10) +
                StringUtils.center("id", 4) +
                StringUtils.center("score", 15));

        PrintStream out = new PrintStream(System.out, true, "UTF-8");    // #5

        DecimalFormat scoreFormatter = new DecimalFormat("0.######");
        for (ScoreDoc sd : results.scoreDocs) {
            int docID = sd.doc;
            float score = sd.score;
            Document doc = searcher.doc(docID);
            out.println(
                    StringUtils.rightPad(                                                  // #6
                            StringUtils.abbreviate(doc.get("title"), 29), 30) +                // #6
                            StringUtils.rightPad(doc.get("pubmonth"), 10) +                        // #6
                            StringUtils.center("" + docID, 4) +                                    // #6
                            StringUtils.leftPad(                                                   // #6
                                    scoreFormatter.format(score), 12));                                 // #6
            out.println("   " + doc.get("category"));
            //out.println(searcher.explain(query, docID));   // #7
        }

        searcher.close();
    }


    public static void main(String[] args) throws ParseException, IOException {
        MatchAllDocsQuery ma = new MatchAllDocsQuery();
        BooleanQuery bq = new BooleanQuery();
        QueryParser parser = new QueryParser(Version.LUCENE_30,"contents",new StandardAnalyzer(Version.LUCENE_30));
        Query pq = parser.parse("java OR action");
        bq.add(ma, BooleanClause.Occur.SHOULD);
        bq.add(pq, BooleanClause.Occur.SHOULD);
        SortingExample example = new SortingExample(TestUtil.getIndexDirectory());
        //example.displaySortResults(ma,Sort.RELEVANCE);
        //example.displaySortResults(ma,Sort.INDEXORDER);
        //example.displaySortResults(bq,new Sort(new SortField("category",SortField.STRING)));
        //example.displaySortResults(pq,new Sort(new SortField("pubmonth",SortField.INT,true)));
        example.displaySortResults(pq,new Sort(new SortField("category",SortField.STRING), SortField.FIELD_SCORE,new SortField("pubmonth",SortField.INT,true)));
    }
}
