package org.laotie777.lucence.chapter2;

import junit.framework.TestCase;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import java.io.IOException;

/**
 * 近实时搜索
 *
 * @author yuh
 * 2018/1/29.
 */
public class NearRealTimeTest extends TestCase {

    /**
     * 测试近实时搜索 IndexReader只对被创建之前的索引有作用
     */
    public void testNearRealTimeSearch() throws IOException {
        Directory directory = new RAMDirectory();
        IndexWriter writer = new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);

        for (int i = 1; i <= 10; i++) {
            Document document = new Document();
            document.add(new Field("id", i + "", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
            document.add(new Field("text", "aaa", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
            writer.addDocument(document);
        }
        writer.commit();
        IndexReader reader = writer.getReader();
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs docs = searcher.search(new TermQuery(new Term("text", "aaa")), 10);
        assertEquals(docs.totalHits, 10);

        writer.deleteDocuments(new Term("id", "8"));
        Document document = new Document();
        document.add(new Field("id", "11", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        document.add(new Field("text", "bbb", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        writer.addDocument(document);

        writer.commit();

        IndexReader reopen = reader.reopen();
        assertFalse(reopen == reader);
        reader = reopen;
        searcher = new IndexSearcher(reader);


        TopDocs docs1 = searcher.search(new TermQuery(new Term("text", "aaa")), 10);
        assertEquals(docs1.totalHits, 9);


        TopDocs docs2 = searcher.search(new TermQuery(new Term("text", "bbb")), 10);
        assertEquals(docs2.totalHits, 1);

        searcher.close();
        writer.close();
    }
}
