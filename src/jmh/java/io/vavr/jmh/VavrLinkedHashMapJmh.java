package io.vavr.jmh;

import io.vavr.collection.LinkedHashMap;
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
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                                 (mask)    (size)  Mode  Cnt           Score   Error  Units
 * VavrLinkedHashMapJmh.mContainsFound          -65        10  avgt                7.699          ns/op
 * VavrLinkedHashMapJmh.mContainsFound          -65      1000  avgt               19.772          ns/op
 * VavrLinkedHashMapJmh.mContainsFound          -65    100000  avgt               56.371          ns/op
 * VavrLinkedHashMapJmh.mContainsFound          -65  10000000  avgt              289.436          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound       -65        10  avgt                8.035          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound       -65      1000  avgt               19.726          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound       -65    100000  avgt               54.549          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound       -65  10000000  avgt              295.545          ns/op
 * VavrLinkedHashMapJmh.mHead                   -65        10  avgt                4.958          ns/op
 * VavrLinkedHashMapJmh.mHead                   -65      1000  avgt                6.143          ns/op
 * VavrLinkedHashMapJmh.mHead                   -65    100000  avgt                8.287          ns/op
 * VavrLinkedHashMapJmh.mHead                   -65  10000000  avgt               10.929          ns/op
 * VavrLinkedHashMapJmh.mIterate                -65        10  avgt               93.746          ns/op
 * VavrLinkedHashMapJmh.mIterate                -65      1000  avgt             9904.360          ns/op
 * VavrLinkedHashMapJmh.mIterate                -65    100000  avgt          1139262.869          ns/op
 * VavrLinkedHashMapJmh.mIterate                -65  10000000  avgt        540481786.053          ns/op
 * VavrLinkedHashMapJmh.mPut                    -65        10  avgt               49.617          ns/op
 * VavrLinkedHashMapJmh.mPut                    -65      1000  avgt              147.118          ns/op
 * VavrLinkedHashMapJmh.mPut                    -65    100000  avgt              289.709          ns/op
 * VavrLinkedHashMapJmh.mPut                    -65  10000000  avgt              819.841          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd          -65        10  avgt              165.895          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd          -65      1000  avgt              372.596          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd          -65    100000  avgt              596.014          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd          -65  10000000  avgt             1314.441          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65        10  avgt              177.127          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65      1000  avgt            72620.018          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65    100000  avgt         17765560.654          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65  10000000  avgt       3357144320.667          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65        10  avgt             1243.260          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65      1000  avgt           207090.639          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65    100000  avgt         30707195.758          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65  10000000  avgt       6677428515.000          ns/op
 *</pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashMapJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private LinkedHashMap<Key, Boolean> mapA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapA =  LinkedHashMap.empty();
        for (Key key : data.setA) {
            mapA=mapA.put(key,Boolean.TRUE);
        }
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : mapA.keysIterator()) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        mapA.remove(key).put(key,Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key =data.nextKeyInA();
        mapA.put(key,Boolean.FALSE);
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapA.containsKey(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapA.containsKey(key);
    }

    @Benchmark
    public Key mHead() {
        return mapA.head()._1;
    }
}
