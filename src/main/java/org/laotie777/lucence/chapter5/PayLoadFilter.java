package org.laotie777.lucence.chapter5;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.Payload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

/**
 * @Author yuh
 * @Date Created in 下午2:01 2018/2/8
 * @Description
 */
public class PayLoadFilter extends TokenFilter {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    private TermAttribute termAttribute;
    private PayloadAttribute payloadAttribute;
    private Payload boost;


    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    protected PayLoadFilter(TokenStream input, float warningBoost) throws IOException {
        super(input);
        termAttribute = input.addAttribute(TermAttribute.class);
        payloadAttribute = input.addAttribute(PayloadAttribute.class);
        int i = Float.floatToIntBits(warningBoost);
        objectOutputStream.write(i);
        boost = new Payload(byteArrayOutputStream.toByteArray());
    }


    @Override
    public boolean incrementToken() throws IOException {
        if (incrementToken()) {
            if("warnning".equals(termAttribute.term())){
                payloadAttribute.setPayload(boost);
            }else{
                payloadAttribute.setPayload(null);
            }
            return true;
        } else {
            return false;
        }


    }
}
