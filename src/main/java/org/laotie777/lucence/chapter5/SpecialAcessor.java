package org.laotie777.lucence.chapter5;

/**
 * @Author yuh
 * @Date Created in 上午11:20 2018/2/8
 * @Description
 */
public interface SpecialAcessor {
    String[] isbn();
    class TestSpecialsAccessor implements SpecialAcessor{

        public TestSpecialsAccessor(String[] isbn){
            this.isbn = isbn;
        }

        private String[] isbn;
        @Override
        public String[] isbn() {
            return isbn;
        }
    }
}
