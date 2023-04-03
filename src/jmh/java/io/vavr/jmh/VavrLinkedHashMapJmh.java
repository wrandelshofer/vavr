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
 * Benchmark         (size)  Mode  Cnt          Score        Error  Units
 * ContainsFound     1000000  avgt    4       203.768 ±      20.920  ns/op
 * ContainsNotFound  1000000  avgt    4       207.006 ±      22.474  ns/op
 * Iterate           1000000  avgt    4  61178364.610 ± 1591497.482  ns/op
 * Put               1000000  avgt    4  20852951.646 ± 4411897.843  ns/op
 * Head              1000000  avgt    4         3.219 ±       0.061  ns/op
 * RemoveThenAdd     1000000  avgt    4  54802086.451 ± 5489641.693  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashMapJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

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
