package org.laotie777.lucence.chapter5;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 下午2:30 2018/2/7
 * @Description
 */
public class DistanceComparatorSource extends FieldComparatorSource {

    private int x;
    private int y;

    public DistanceComparatorSource(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public FieldComparator newComparator(String fieldname, int numHits, int sortPos, boolean reversed) throws IOException {
        return new DistanceScoreComparator(fieldname, numHits);
    }


    //排序的执行顺序 获得自定sorter里面的comparator copy方法输入匹配的文档号码充实分数数组 然后调用集合的sort方法 返回每个文档对应的comparable对象
    private class DistanceScoreComparator extends FieldComparator {
        private int[] xDoc, yDoc;
        private float[] values;
        private float bottom;
        private String fieldName;

        public DistanceScoreComparator(String fieldname, int numHits) {
            values = new float[numHits];
            this.fieldName = fieldname;
        }

        @Override
        public int compare(int slot1, int slot2) {
            if (values[slot1] - values[slot2] < 0) return -1;
            else if (values[slot1] - values[slot2] > 0) return 1;
            else return 0;
        }

        @Override
        public void setBottom(int slot) {
            bottom = values[slot];
        }

        @Override
        public int compareBottom(int doc) throws IOException {
            if (getDistance(doc) - bottom > 0) return -1;
            else if (getDistance(doc) - bottom < 0) return 1;
            else return 0;
        }

        @Override
        public void copy(int slot, int doc) throws IOException {
            values[slot] = getDistance(doc);
        }

        @Override
        public void setNextReader(IndexReader reader, int docBase) throws IOException {
            xDoc = FieldCache.DEFAULT.getInts(reader, "x");
            yDoc = FieldCache.DEFAULT.getInts(reader, "y");
        }

        private float getDistance(int slot) {
            float f = (float) Math.sqrt((xDoc[slot] - x) * (xDoc[slot] - x) + (yDoc[slot] - y) * (yDoc[slot] - y));
            return f;
        }


        @Override
        public Comparable value(int slot) {
            return new Float(values[slot]);
        }
    }
}
