package org.laotie777.lucence.chapter4;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 下午2:21 2018/2/2
 * @Description 同音词过滤器
 */
public class MetaphoneTokenFilter extends TokenFilter {
    private static final String METAPHONE = "metaphone";
    private Metaphone metaphoner = new Metaphone();
    private TermAttribute termAttribute;
    private TypeAttribute typeAttribute;

    public MetaphoneTokenFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        //没有输入
        if (!input.incrementToken()) {
            return false;
        }
        termAttribute.setTermBuffer(metaphoner.encode(termAttribute.term()));
        typeAttribute.setType(METAPHONE);
        return true;
    }
}
