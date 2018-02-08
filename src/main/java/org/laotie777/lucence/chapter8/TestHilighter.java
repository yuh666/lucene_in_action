package org.laotie777.lucence.chapter8;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 * 测试高亮
 */
public class TestHilighter {

    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException {
        IndexSearcher searcher = new IndexSearcher(TestUtil.getIndexDirectory());
        TermQuery termQuery = new TermQuery(new Term("title","action"));
        TopDocs topDocs = searcher.search(termQuery, 100);
        QueryScorer scorer = new QueryScorer(termQuery,"title");
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font style=\"color:red\">","</font>");
        Highlighter highlighter = new Highlighter(formatter,scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));

        for (ScoreDoc doc : topDocs.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            String title = document.get("title");
            TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), doc.doc, "title", document, new StandardAnalyzer(Version.LUCENE_30));
            String bestFragment = highlighter.getBestFragment(stream, title);
            System.out.println(bestFragment);
        }
    }
}
