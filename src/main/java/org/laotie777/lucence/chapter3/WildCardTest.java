package org.laotie777.lucence.chapter3;

import junit.framework.TestCase;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 上午9:57 2018/1/30
 * @Description 通配符测试
 */
public class WildCardTest extends TestCase {
    protected IndexSearcher searcher;
    protected Directory directory;
    protected IndexWriter indexWriter;

    @Override
    public void setUp() throws IOException {
        directory = new RAMDirectory();
        indexWriter = new IndexWriter(directory, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
        indexingDoc(new String[]{"fuzzy","wuzza"});
        searcher = new IndexSearcher(directory);
    }

    /**
     * 加索引
     * @param fields
     * @throws IOException
     */
    protected void indexingDoc(String[] fields) throws IOException {
        for (String field : fields) {
            Document document = new Document();
            document.add(new Field("contents", field, Field.Store.YES, Field.Index.NOT_ANALYZED));
            indexWriter.addDocument(document);
        }
        indexWriter.optimize();
        indexWriter.close();
    }

    @Override
    protected void tearDown() throws Exception {
        searcher.close();
        directory.close();
    }

    /**
     * 通配符测试
     * @throws IOException
     */
    public void testWildCard() throws IOException {
        WildcardQuery query = new WildcardQuery(new Term("contents", "*ild*"));
        TopDocs docs = searcher.search(query, 10);
        System.out.println(docs.totalHits);
    }

    /**
     * 相似度测试
     * @throws IOException
     */
    public void testFuzzy() throws IOException {
        FuzzyQuery query = new FuzzyQuery(new Term("contents", "wuzzy"));
        TopDocs docs = searcher.search(query, 10);
        System.out.println(docs.totalHits);
    }

    /**
     * 全部匹配
     * @throws IOException
     */
    public void testMatchAll() throws IOException {
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 10);
        System.out.println(docs.totalHits);
    }


}
