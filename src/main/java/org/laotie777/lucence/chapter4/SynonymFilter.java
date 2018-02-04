package org.laotie777.lucence.chapter4;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.Stack;

/**
 * 同义词过滤器
 */
public class SynonymFilter extends TokenFilter {

    private static final String SYNONYM = "synonym";
    private Stack<String> synonymStrs = new Stack<>();
    private TermAttribute termAttribute;
    private PositionIncrementAttribute positionIncrementAttribute;
    private TypeAttribute typeAttribute;
    private SynonymEngine engine;
    private AttributeSource.State current;

    public SynonymFilter(SynonymEngine engine, TokenStream stream) {
        super(stream);
        this.engine = engine;
        this.termAttribute = stream.addAttribute(TermAttribute.class);
        this.positionIncrementAttribute = stream.addAttribute(PositionIncrementAttribute.class);
        this.typeAttribute = stream.addAttribute(TypeAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if(!synonymStrs.isEmpty()){
            String pop = synonymStrs.pop();
            restoreState(current);
            termAttribute.setTermBuffer(pop);
            typeAttribute.setType(SYNONYM);
            positionIncrementAttribute.setPositionIncrement(0);
            return true;
        }

        //利用引用推动上一级别的词汇流 整个是个递归的过程 filter可以進行處理 也可以不處理 位置增量是相對上一個詞彙說的 set实际上就是累加
        if(!input.incrementToken()){
            return false;
        }

        if(addAliasesToStatck()){
            current = captureState();
        }

        return true;
    }

    /**
     * 将上一级的词汇单元压入栈中
     * @return
     */
    private boolean addAliasesToStatck() {
        String[] synonyms = engine.getSynonyms(termAttribute.term());
        if (synonyms == null) {
            return false;
        }else{
            for (String synonym : synonyms) {
                synonymStrs.push(synonym);
            }
            return true;
        }
    }

}
