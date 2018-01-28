package org.laotie777.lucence.chapter1;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * 搜索索引库
 * @author yuh
 * 2018/1/28.
 */
public class IndexSearcherDemo {

    public static void main(String[] args) throws IOException, ParseException {
        FSDirectory open = FSDirectory.open(new File("./index"));
        IndexSearcher searcher = new IndexSearcher(open);
        QueryParser parser = new QueryParser(Version.LUCENE_30,"contents", new StandardAnalyzer(Version.LUCENE_30));
        Query q = parser.parse("package");
        TopDocs docs = searcher.search(q, 1000);
        int totalHits = docs.totalHits;
        System.out.println(String.format("查询到%d条",totalHits));
        for (ScoreDoc scoreDoc:docs.scoreDocs){
            org.apache.lucene.document.Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("fileName") + " => " + scoreDoc.score);
        }
    }
}
