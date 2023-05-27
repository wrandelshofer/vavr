package io.vavr.jmh;

import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.LinkedHashSet;
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
 * Benchmark                                 (mask)    (size)  Mode  Cnt            Score   Error  Units
 * VavrHashSetJmh.mAddOneByOne                  -65        10  avgt               357.455          ns/op
 * VavrHashSetJmh.mAddOneByOne                  -65      1000  avgt             81663.862          ns/op
 * VavrHashSetJmh.mAddOneByOne                  -65    100000  avgt          20942609.404          ns/op
 * VavrHashSetJmh.mAddOneByOne                  -65  10000000  avgt        5900853148.000          ns/op
 * VavrHashSetJmh.mContainsFound                -65        10  avgt                 7.629          ns/op
 * VavrHashSetJmh.mContainsFound                -65      1000  avgt                22.110          ns/op
 * VavrHashSetJmh.mContainsFound                -65    100000  avgt               133.716          ns/op
 * VavrHashSetJmh.mContainsFound                -65  10000000  avgt               386.827          ns/op
 * VavrHashSetJmh.mContainsNotFound             -65        10  avgt                 7.606          ns/op
 * VavrHashSetJmh.mContainsNotFound             -65      1000  avgt                22.988          ns/op
 * VavrHashSetJmh.mContainsNotFound             -65    100000  avgt               131.390          ns/op
 * VavrHashSetJmh.mContainsNotFound             -65  10000000  avgt               334.513          ns/op
 * VavrHashSetJmh.mFilter50Percent              -65        10  avgt                63.274          ns/op
 * VavrHashSetJmh.mFilter50Percent              -65      1000  avgt             16191.909          ns/op
 * VavrHashSetJmh.mFilter50Percent              -65    100000  avgt           1531677.508          ns/op
 * VavrHashSetJmh.mFilter50Percent              -65  10000000  avgt        1601694938.143          ns/op
 * VavrHashSetJmh.mHead                         -65        10  avgt                 4.489          ns/op
 * VavrHashSetJmh.mHead                         -65      1000  avgt                 7.974          ns/op
 * VavrHashSetJmh.mHead                         -65    100000  avgt                12.952          ns/op
 * VavrHashSetJmh.mHead                         -65  10000000  avgt                14.732          ns/op
 * VavrHashSetJmh.mIterate                      -65        10  avgt                71.653          ns/op
 * VavrHashSetJmh.mIterate                      -65      1000  avgt              9432.046          ns/op
 * VavrHashSetJmh.mIterate                      -65    100000  avgt           1617621.396          ns/op
 * VavrHashSetJmh.mIterate                      -65  10000000  avgt         680535322.067          ns/op
 * VavrHashSetJmh.mOfAll                        -65        10  avgt               327.604          ns/op
 * VavrHashSetJmh.mOfAll                        -65      1000  avgt             60705.304          ns/op
 * VavrHashSetJmh.mOfAll                        -65    100000  avgt          12618105.650          ns/op
 * VavrHashSetJmh.mOfAll                        -65  10000000  avgt       10374521304.500          ns/op
 * VavrHashSetJmh.mPartition50Percent           -65        10  avgt               476.604          ns/op
 * VavrHashSetJmh.mPartition50Percent           -65      1000  avgt             67140.761          ns/op
 * VavrHashSetJmh.mPartition50Percent           -65    100000  avgt           8352181.228          ns/op
 * VavrHashSetJmh.mPartition50Percent           -65  10000000  avgt       14827854574.000          ns/op
 * VavrHashSetJmh.mRemoveAll                    -65        10  avgt               273.452          ns/op
 * VavrHashSetJmh.mRemoveAll                    -65      1000  avgt             80808.061          ns/op
 * VavrHashSetJmh.mRemoveAll                    -65    100000  avgt          21648143.561          ns/op
 * VavrHashSetJmh.mRemoveAll                    -65  10000000  avgt        5246291524.500          ns/op
 * VavrHashSetJmh.mRemoveOneByOne               -65        10  avgt               297.129          ns/op
 * VavrHashSetJmh.mRemoveOneByOne               -65      1000  avgt             83302.196          ns/op
 * VavrHashSetJmh.mRemoveOneByOne               -65    100000  avgt          22483739.278          ns/op
 * VavrHashSetJmh.mRemoveOneByOne               -65  10000000  avgt        5296471964.500          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                -65        10  avgt                51.090          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                -65      1000  avgt               163.612          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                -65    100000  avgt               279.554          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                -65  10000000  avgt               609.790          ns/op
 * VavrHashSetJmh.mTail                         -65        10  avgt                26.138          ns/op
 * VavrHashSetJmh.mTail                         -65      1000  avgt                53.853          ns/op
 * VavrHashSetJmh.mTail                         -65    100000  avgt                97.361          ns/op
 * VavrHashSetJmh.mTail                         -65  10000000  avgt               128.051          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrHashSetJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private HashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA = HashSet.ofAll(data.setA);
    }

    @Benchmark
    public HashSet<Key> mFilter50Percent() {
        HashSet<Key> set = setA;
        return set.filter(e->(e.value&1)==0);
    }
    @Benchmark
    public Tuple2<HashSet<Key>,HashSet<Key>> mPartition50Percent() {
        HashSet<Key> set = setA;
        return set.partition(e -> (e.value & 1) == 0);
    }

    @Benchmark
    public HashSet<Key> mOfAll() {
        return HashSet.ofAll(data.listA);
    }

    @Benchmark
    public HashSet<Key> mAddOneByOne() {
        HashSet<Key> set = HashSet.of();
        for (Key key : data.listA) {
            set = set.add(key);
        }
        return set;
    }

    @Benchmark
    public HashSet<Key> mRemoveOneByOne() {
        HashSet<Key> set = setA;
        for (Key key : data.listA) {
            set = set.remove(key);
        }
        return set;
    }

    @Benchmark
    public HashSet<Key> mRemoveAll() {
        HashSet<Key> set = setA;
        return set.removeAll(data.listA);
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : setA) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key = data.nextKeyInA();
        setA.remove(key).add(key);
    }

    @Benchmark
    public Key mHead() {
        return setA.head();
    }

    @Benchmark
    public HashSet<Key> mTail() {
        return setA.tail();
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return setA.contains(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return setA.contains(key);
    }

}
