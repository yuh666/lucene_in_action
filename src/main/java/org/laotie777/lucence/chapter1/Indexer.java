package org.laotie777.lucence.chapter1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

;

/**
 * 生成磁盘索引
 *
 * @author yuh
 * 2018/1/28.
 */
public class Indexer{

    public static void main(String[] args) throws IOException {
        indexDirectory("/Users/yuh/Desktop/快学scala", "./index");
    }

    /**
     * 索引文件夹
     *
     * @param dataDir
     * @param indexDir
     */
    private static void indexDirectory(String dataDir, String indexDir) throws IOException {
        Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_30);
        FSDirectory dir = FSDirectory.open(new File(indexDir));
        IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
        indexFile(writer, new File(dataDir));
        writer.close();
    }

    /**
     * 递归加索引
     *
     * @param writer
     * @param dataDir
     */
    private static void indexFile(IndexWriter writer, File dataDir) throws IOException {
        File[] files = dataDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                indexFile(writer, file);
            } else {
                //加索引操作
                System.out.println("indexing " + file.getCanonicalPath());
                Document document = new Document();
                document.add(new Field("contents", new FileReader(file)));
                document.add(new Field("fileName", file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                writer.addDocument(document);
            }
        }
    }
}
