package org.laotie777.lucence.chapter4;

import junit.framework.TestCase;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.AnalyzerUtils;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;
import java.io.StringReader;

/**
 * 多值域分析器测试
 */
public class PerfiledAnalyzerTest extends TestCase {


    protected IndexSearcher searcher;
    protected IndexWriter writer;
    protected Directory directory;


    @Override
    protected void setUp() throws Exception {
        directory = new RAMDirectory();
        writer = new IndexWriter(directory, new SimpleAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field("description", "A SPACE B", Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("partnum","Q36", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        writer.addDocument(document);
        writer.optimize();
        writer.commit();
        writer.close();
        searcher = new IndexSearcher(directory);
    }

    public void testTermQuery() throws IOException, ParseException {
        TermQuery query = new TermQuery(new Term("partnum","Q36"));
        TopDocs docs = searcher.search(query,10);
        System.out.println(docs.totalHits);

        QueryParser parser = new QueryParser(Version.LUCENE_30,"description",new SimpleAnalyzer());
        Query query1 = parser.parse("partnum:Q36 AND SPACE");
        System.out.println(query1.toString("description"));
        TopDocs docs1 = searcher.search(query1,10);
        System.out.println(docs1.totalHits);

        PerFieldAnalyzerWrapper perFieldAnalyzerWrapper = new PerFieldAnalyzerWrapper(new SimpleAnalyzer());
        perFieldAnalyzerWrapper.addAnalyzer("partnum",new KeywordAnalyzer());
        QueryParser parser1 = new QueryParser(Version.LUCENE_30,"description",perFieldAnalyzerWrapper);
        Query query2 = parser1.parse("partnum:Q36 AND SPACE");
        System.out.println(query2.toString("description"));
        TopDocs docs2 = searcher.search(query2,10);
        System.out.println(docs2.totalHits);






















    }

    @Override
    protected void tearDown() throws Exception {
        searcher.close();
    }
}
