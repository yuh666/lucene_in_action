package org.laotie777.lucence.chapter4;

import junit.framework.TestCase;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
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
        //writer.add


        searcher = new IndexSearcher(directory);
    }

    /**
     * 测试自定义词分词器
     */
    public void testMetaAnalyzer() throws IOException {

    }

}
