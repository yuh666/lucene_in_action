package org.laotie777.lucence.chapter4;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * 同义词分词器
 */
public class SynonymAnalyzer extends Analyzer {
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new SynonymFilter(
                new SynonymEngineImpl(),
                    new StopFilter(true,
                        new LowerCaseFilter(
                                new StandardFilter(
                                        new StandardTokenizer(Version.LUCENE_30, reader))),
                                            StopAnalyzer.ENGLISH_STOP_WORDS_SET));
    }
}
