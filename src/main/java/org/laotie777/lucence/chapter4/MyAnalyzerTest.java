package org.laotie777.lucence.chapter4;

import junit.framework.TestCase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.AnalyzerUtils;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 下午1:18 2018/2/2
 * @Description 自定义词分析器测试
 */
public class MyAnalyzerTest extends TestCase{

    protected IndexSearcher searcher;
    protected IndexWriter writer;
    protected Directory directory;


    @Override
    protected void setUp() throws Exception {
        directory = new RAMDirectory();
        writer = new IndexWriter(directory,new MetaphoneAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field("field","cat cool", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(document);
        writer.optimize();
        writer.commit();
        writer.close();
        searcher = new IndexSearcher(directory);
    }

    /**
     * 测试自定义词分词器
     */
    public void testMetaAnalyzer() throws IOException, ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_30,"field",new MetaphoneAnalyzer());
        TopDocs docs = searcher.search(parser.parse("kat kool"), 10);
        System.out.println(docs.totalHits);
    }

}
