package io.vavr.jmh;

import io.vavr.collection.HashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                           (mask)    (size)  Mode  Cnt           Score   Error  Units
 * VavrHashMapJmh.mContainsFound          -65        10  avgt    2           7.771          ns/op
 * VavrHashMapJmh.mContainsFound          -65      1000  avgt    2          19.575          ns/op
 * VavrHashMapJmh.mContainsFound          -65    100000  avgt    2          58.397          ns/op
 * VavrHashMapJmh.mContainsFound          -65  10000000  avgt    2         296.760          ns/op
 * VavrHashMapJmh.mContainsNotFound       -65        10  avgt    2           7.448          ns/op
 * VavrHashMapJmh.mContainsNotFound       -65      1000  avgt    2          19.435          ns/op
 * VavrHashMapJmh.mContainsNotFound       -65    100000  avgt    2          58.575          ns/op
 * VavrHashMapJmh.mContainsNotFound       -65  10000000  avgt    2         297.124          ns/op
 * VavrHashMapJmh.mFilter50Percent        -65        10  avgt    2          71.815          ns/op
 * VavrHashMapJmh.mFilter50Percent        -65      1000  avgt    2       16813.424          ns/op
 * VavrHashMapJmh.mFilter50Percent        -65    100000  avgt    2     1984981.814          ns/op
 * VavrHashMapJmh.mFilter50Percent        -65  10000000  avgt    2   689822640.733          ns/op
 * VavrHashMapJmh.mHead                   -65        10  avgt    2           3.793          ns/op
 * VavrHashMapJmh.mHead                   -65      1000  avgt    2           6.659          ns/op
 * VavrHashMapJmh.mHead                   -65    100000  avgt    2          11.393          ns/op
 * VavrHashMapJmh.mHead                   -65  10000000  avgt    2          15.695          ns/op
 * VavrHashMapJmh.mIterate                -65        10  avgt    2          73.284          ns/op
 * VavrHashMapJmh.mIterate                -65      1000  avgt    2       10075.395          ns/op
 * VavrHashMapJmh.mIterate                -65    100000  avgt    2     1988853.759          ns/op
 * VavrHashMapJmh.mIterate                -65  10000000  avgt    2   722459258.607          ns/op
 * VavrHashMapJmh.mMerge                  -65        10  avgt    2         100.815          ns/op
 * VavrHashMapJmh.mMerge                  -65      1000  avgt    2       22431.701          ns/op
 * VavrHashMapJmh.mMerge                  -65    100000  avgt    2     2459999.411          ns/op
 * VavrHashMapJmh.mMerge                  -65  10000000  avgt    2   284182503.011          ns/op
 * VavrHashMapJmh.mOfAll                  -65        10  avgt    2         393.681          ns/op
 * VavrHashMapJmh.mOfAll                  -65      1000  avgt    2       77993.184          ns/op
 * VavrHashMapJmh.mOfAll                  -65    100000  avgt    2    15487239.618          ns/op
 * VavrHashMapJmh.mOfAll                  -65  10000000  avgt    2  3994008834.167          ns/op
 * VavrHashMapJmh.mPut                    -65        10  avgt    2          17.316          ns/op
 * VavrHashMapJmh.mPut                    -65      1000  avgt    2          65.840          ns/op
 * VavrHashMapJmh.mPut                    -65    100000  avgt    2         140.905          ns/op
 * VavrHashMapJmh.mPut                    -65  10000000  avgt    2         469.792          ns/op
 * VavrHashMapJmh.mRemoveThenAdd          -65        10  avgt    2          49.363          ns/op
 * VavrHashMapJmh.mRemoveThenAdd          -65      1000  avgt    2         168.258          ns/op
 * VavrHashMapJmh.mRemoveThenAdd          -65    100000  avgt    2         288.521          ns/op
 * VavrHashMapJmh.mRemoveThenAdd          -65  10000000  avgt    2         681.511          ns/op
 * VavrHashMapJmh.mReplaceAll             -65        10  avgt    2         445.336          ns/op
 * VavrHashMapJmh.mReplaceAll             -65      1000  avgt    2       97855.377          ns/op
 * VavrHashMapJmh.mReplaceAll             -65    100000  avgt    2    13984023.160          ns/op
 * VavrHashMapJmh.mReplaceAll             -65  10000000  avgt    2  2659926436.375          ns/op
 * VavrHashMapJmh.mRetainAll              -65        10  avgt    2         107.130          ns/op
 * VavrHashMapJmh.mRetainAll              -65      1000  avgt    2       13606.111          ns/op
 * VavrHashMapJmh.mRetainAll              -65    100000  avgt    2     1808710.906          ns/op
 * VavrHashMapJmh.mRetainAll              -65  10000000  avgt    2   409088464.240          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrHashMapJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private HashMap<Key, Boolean> mapATrue;
    private HashMap<Key, Boolean> mapAFalse;
    private HashMap<Key, Boolean> mapB;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapATrue = HashMap.empty();
        mapAFalse = HashMap.empty();
        mapB = HashMap.empty();
        for (Key key : data.setA) {
            mapATrue = mapATrue.put(key, Boolean.TRUE);
            mapAFalse = mapAFalse.put(key, Boolean.FALSE);
        }
        for (Key key : data.listB) {
            mapB = mapB.put(key, Boolean.TRUE);
        }
    }

    @Benchmark
    public HashMap<Key, Boolean> mOfAll() {
        return HashMap.<Key, Boolean>ofAll(data.mapA);
    }

    @Benchmark
    public HashMap<Key, Boolean> mMerge() {
        return mapATrue.merge(mapAFalse);
    }

    @Benchmark
    public HashMap<Key, Boolean> mReplaceAll() {
        return mapATrue.replaceAll((k, v) -> !v);
    }

    @Benchmark
    public HashMap<Key, Boolean> mRetainAll() {
        return mapATrue.retainAll(mapB);
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : mapATrue.keysIterator()) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key = data.nextKeyInA();
        mapATrue.remove(key).put(key, Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key = data.nextKeyInA();
        mapATrue.put(key, Boolean.FALSE);
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapATrue.containsKey(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapATrue.containsKey(key);
    }

    @Benchmark
    public Key mHead() {
        return mapATrue.head()._1;
    }

    @Benchmark
    public HashMap<Key, Boolean> mFilter50Percent() {
        HashMap<Key, Boolean> map = mapATrue;
        return map.filter(e -> (e._1.value & 1) == 0);
    }
}
