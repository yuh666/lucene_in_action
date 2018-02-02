package org.laotie777.lucence.chapter4;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;

/**
 * @Author yuh
 * @Date Created in 下午2:19 2018/2/2
 * @Description 同音词分析器
 */
public class MetaphoneAnalyzer extends Analyzer{
    @Override
    public TokenStream tokenStream(String s, Reader reader) {
        return new MetaphoneTokenFilter(new LetterTokenizer(reader));
    }
}
