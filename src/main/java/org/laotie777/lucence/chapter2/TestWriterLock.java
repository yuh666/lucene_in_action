package org.laotie777.lucence.chapter2;

import junit.framework.TestCase;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * 测试Lucene的锁机制
 * @author yuh
 * 2018/1/29.
 */
public class TestWriterLock extends TestCase{

    protected Directory directory;
    @Override
    protected void setUp() throws Exception {
        directory = FSDirectory.open(new File("./index"));
    }

    /**
     * 测试锁机制
     */
    public void testLock() throws IOException {
        IndexWriter writer1 = new IndexWriter(directory,new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
        writer1.setInfoStream(System.out);
        IndexWriter writer2 = null;

        try{
            writer2 = new IndexWriter(directory,new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
            //fail("LockObtainFailedException");
        }catch (LockObtainFailedException e){
            e.printStackTrace();
        }finally {
            assertNull(writer2);
            writer1.close();
        }
    }
}
