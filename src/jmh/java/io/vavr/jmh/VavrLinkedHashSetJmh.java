package io.vavr.jmh;

import io.vavr.Tuple2;
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
 * VavrLinkedHashSetJmh.mAddOneByOne            -65        10  avgt              1197.515          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne            -65      1000  avgt            239368.530          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne            -65    100000  avgt          47292876.745          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne            -65  10000000  avgt        9371661069.000          ns/op
 * VavrLinkedHashSetJmh.mContainsFound          -65        10  avgt                 9.297          ns/op
 * VavrLinkedHashSetJmh.mContainsFound          -65      1000  avgt                21.601          ns/op
 * VavrLinkedHashSetJmh.mContainsFound          -65    100000  avgt                63.274          ns/op
 * VavrLinkedHashSetJmh.mContainsFound          -65  10000000  avgt               354.553          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound       -65        10  avgt                 7.670          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound       -65      1000  avgt                22.801          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound       -65    100000  avgt                57.437          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound       -65  10000000  avgt               333.511          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65        10  avgt               191.747          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65      1000  avgt             75180.623          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65    100000  avgt          20837619.347          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent        -65  10000000  avgt        3606709635.333          ns/op
 * VavrLinkedHashSetJmh.mHead                   -65        10  avgt                 4.992          ns/op
 * VavrLinkedHashSetJmh.mHead                   -65      1000  avgt                 5.678          ns/op
 * VavrLinkedHashSetJmh.mHead                   -65    100000  avgt                 8.342          ns/op
 * VavrLinkedHashSetJmh.mHead                   -65  10000000  avgt                10.719          ns/op
 * VavrLinkedHashSetJmh.mIterate                -65        10  avgt                61.277          ns/op
 * VavrLinkedHashSetJmh.mIterate                -65      1000  avgt              4920.634          ns/op
 * VavrLinkedHashSetJmh.mIterate                -65    100000  avgt           3127994.358          ns/op
 * VavrLinkedHashSetJmh.mIterate                -65  10000000  avgt         309678736.485          ns/op
 * VavrLinkedHashSetJmh.mOfAll                  -65        10  avgt              1103.041          ns/op
 * VavrLinkedHashSetJmh.mOfAll                  -65      1000  avgt            195307.192          ns/op
 * VavrLinkedHashSetJmh.mOfAll                  -65    100000  avgt          34074874.136          ns/op
 * VavrLinkedHashSetJmh.mOfAll                  -65  10000000  avgt        7014418779.000          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65        10  avgt              1261.272          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65      1000  avgt            207291.182          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65    100000  avgt          29947520.772          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent     -65  10000000  avgt        6475540645.000          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll              -65        10  avgt               699.517          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll              -65      1000  avgt            369705.488          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll              -65    100000  avgt          80044945.167          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll              -65  10000000  avgt       16465937063.000          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne         -65        10  avgt               925.847          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne         -65      1000  avgt            387705.176          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne         -65    100000  avgt          86587531.638          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne         -65  10000000  avgt       17474214453.000          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd          -65        10  avgt               168.386          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd          -65      1000  avgt               371.339          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd          -65    100000  avgt               618.611          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd          -65  10000000  avgt              1324.575          ns/op
 * VavrLinkedHashSetJmh.mTail                   -65        10  avgt                42.908          ns/op
 * VavrLinkedHashSetJmh.mTail                   -65      1000  avgt                77.685          ns/op
 * VavrLinkedHashSetJmh.mTail                   -65    100000  avgt               109.233          ns/op
 * VavrLinkedHashSetJmh.mTail                   -65  10000000  avgt               149.438          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashSetJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private LinkedHashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA = LinkedHashSet.ofAll(data.setA);
    }

    @Benchmark
    public LinkedHashSet<Key> mFilter50Percent() {
        LinkedHashSet<Key> set = setA;
        return set.filter(e -> (e.value & 1) == 0);
    }

    @Benchmark
    public Tuple2<LinkedHashSet<Key>, LinkedHashSet<Key>> mPartition50Percent() {
        LinkedHashSet<Key> set = setA;
        return set.partition(e -> (e.value & 1) == 0);
    }

    @Benchmark
    public LinkedHashSet<Key> mOfAll() {
        return LinkedHashSet.ofAll(data.listA);
    }

    @Benchmark
    public LinkedHashSet<Key> mAddOneByOne() {
        LinkedHashSet<Key> set = LinkedHashSet.of();
        for (Key key : data.listA) {
            set = set.add(key);
        }
        return set;
    }

    @Benchmark
    public LinkedHashSet<Key> mRemoveOneByOne() {
        LinkedHashSet<Key> set = setA;
        for (Key key : data.listA) {
            set = set.remove(key);
        }
        return set;
    }

    @Benchmark
    public LinkedHashSet<Key> mRemoveAll() {
        LinkedHashSet<Key> set = setA;
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
    public LinkedHashSet<Key> mTail() {
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
