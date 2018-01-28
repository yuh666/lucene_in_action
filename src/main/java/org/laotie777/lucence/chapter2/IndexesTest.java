package org.laotie777.lucence.chapter2;

import junit.framework.TestCase;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 索引的增删改
 */
public class IndexesTest extends TestCase {

    /**
     * id 存储不分析 不索引
     */
    protected String[] ids = {"1", "2"};
    /**
     * 名称 存储 不索引
     */
    protected String[] unindexed = {"Netherlands", "Italy"};
    /**
     * 介绍 不存储 索引
     */
    protected String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};
    /**
     * 关键词 存储 索引
     */
    protected String[] text = {"Amsterdam", "Venice"};

    /**
     * 索引目录
     */
    private Directory directory;


    /**
     * 每次写文档到内存中
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        /**内存缓存库*/
        directory = new RAMDirectory();
        IndexWriter indexWriter = getIndexWriter();
        for (int i = 0; i < ids.length; i++) {
            Document document = new Document();
            document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
            document.add(new Field("country", unindexed[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
            document.add(new Field("contents", unstored[i], Field.Store.NO, Field.Index.ANALYZED));
            document.add(new Field("city", text[i], Field.Store.YES, Field.Index.ANALYZED));
            indexWriter.addDocument(document);
        }
        indexWriter.setUseCompoundFile(false);
        indexWriter.close();
    }

    /**
     * 获得indexWriter
     *
     * @return
     */
    private IndexWriter getIndexWriter() throws IOException {
        return new IndexWriter(directory, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
    }

    /**
     * 查询返回的条数
     *
     * @param fieldName
     * @param searchingString
     * @return
     */
    protected int getHitCount(String fieldName, String searchingString) throws IOException {
        IndexSearcher searcher = new IndexSearcher(directory);
        TermQuery query = new TermQuery(new Term(fieldName, searchingString));
        int hitCount = TestUtil.hitCount(searcher, query);
        System.out.println(String.format("%s => 查询到%d条", searchingString,hitCount));
        return hitCount;
    }

    /**
     * 测试indexWriter写入数量
     */
    public void testIndexWriter() throws IOException {
        IndexWriter indexWriter = getIndexWriter();
        assertEquals(ids.length, indexWriter.numDocs());
        indexWriter.close();
    }

    /**
     * 侧对indexReader读取数量
     */
    public void testIndexReader() throws IOException {
        IndexReader indexReader = IndexReader.open(directory);
        assertEquals(ids.length, indexReader.maxDoc());
        assertEquals(ids.length, indexReader.numDocs());
        indexReader.close();
    }

    /**
     * 删除文档
     */
    public void testDeleteIndex() throws IOException {
        IndexWriter indexWriter = getIndexWriter();
        assertEquals(2,indexWriter.numDocs());
        indexWriter.deleteDocuments(new Term("id","1"));
        indexWriter.commit();
        assertTrue(indexWriter.hasDeletions());
        assertEquals(1,indexWriter.numDocs());
        assertEquals(2,indexWriter.maxDoc());
        indexWriter.close();
    }

    /**
     * 删除文档强制优化索引 索引会标记为删除 hasDeletions是查看已经标记将要被删除的
     * optimzie是强制优化 直接将索引删除并且合并 numDocs是除了标记的 maxDoc是全部的
     */
    public void testDeleteIndexOptimize() throws IOException {
        IndexWriter indexWriter = getIndexWriter();
        assertEquals(2,indexWriter.numDocs());
        indexWriter.deleteDocuments(new Term("id","1"));
        indexWriter.optimize();
        indexWriter.commit();
        //assertTrue(indexWriter.hasDeletions());
        assertEquals(1,indexWriter.numDocs());
        assertEquals(1,indexWriter.maxDoc());
        indexWriter.close();
    }

    /**
     * 更新文档
     */
    public void testUpdateDocument() throws IOException {
        assertEquals(1,getHitCount("city","Venice"));
        IndexWriter indexWriter = getIndexWriter();
        Document document = new Document();
        document.add(new Field("id", "2", Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("country", "China", Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("contents", "five thousands years", Field.Store.NO, Field.Index.ANALYZED));
        document.add(new Field("city", "Liaoning", Field.Store.YES, Field.Index.ANALYZED));
        indexWriter.updateDocument(new Term("id","2"),document);
        indexWriter.commit();
        indexWriter.close();
        assertEquals(0,getHitCount("city","Venice"));
        assertEquals(1,getHitCount("city","Liaoning"));
    }


}
