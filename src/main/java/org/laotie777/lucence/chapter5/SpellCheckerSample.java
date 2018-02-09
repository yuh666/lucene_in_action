package org.laotie777.lucence.chapter5;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.laotie777.lucence.util.TestUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author yuh
 * @Date Created in 上午10:00 2018/2/9
 * @Description
 */
public class SpellCheckerSample {

    public static void main(String[] args) throws IOException {
        /*
        Directory indexDirectory = TestUtil.getIndexDirectory();
        FSDirectory spellDir = FSDirectory.open(new File("./spell"));
        String field = "author";
        SpellChecker spellChecker = new SpellChecker(spellDir);
        IndexReader open = IndexReader.open(indexDirectory);
        spellChecker.indexDictionary(new LuceneDictionary(open,field));
        indexDirectory.close();
        open.close();
        spellChecker.close();
        */
        FSDirectory spellDir = FSDirectory.open(new File("./spell"));
        SpellChecker spellChecker = new SpellChecker(spellDir);
        spellChecker.setStringDistance(new LevensteinDistance());
        String[] abcs = spellChecker.suggestSimilar("a", 5);
        System.out.println(Arrays.toString(abcs));

    }
}
