package org.laotie777.lucence.chapter4;

import org.apache.lucene.analysis.*;

import java.io.Reader;
import java.util.Set;

/**
 * @Author yuh
 * @Date Created in 下午1:22 2018/2/2
 * @Description
 */
public class StopAnalyzer2 extends Analyzer {
    private Set stopWords;

    public StopAnalyzer2() {
        this.stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
    }

    public StopAnalyzer2(String[] stopWords) {
        this.stopWords = StopFilter.makeStopSet(stopWords);
    }

    /**
     * tokenNizer和filter同时继承自tokenStream 先执行nizer在执行filterchain 越靠近nizer的越先被执行
     *
     * @param s
     * @param reader
     * @return
     */
    @Override
    public TokenStream tokenStream(String s, Reader reader) {
        //return new StopFilter(true, new LowerCaseFilter(new LetterTokenizer(reader)), stopWords);
        return new LowerCaseFilter(new StopFilter(true, new LetterTokenizer(reader), stopWords));
    }


}
