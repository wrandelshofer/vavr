package io.vavr.jmh;


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
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.Map;
import scala.collection.immutable.Vector;
import scala.collection.immutable.VectorMap;
import scala.collection.mutable.Builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * implementation 'org.scala-lang:scala-library:2.13.11-M1'
 *
 * Benchmark                            (mask)    (size)  Mode  Cnt      _     Score   Error  Units
 * ScalaVectorMapJmh.mContainsFound        -65        10  avgt    2      _     6.529          ns/op
 * ScalaVectorMapJmh.mContainsFound        -65       100  avgt    2      _    11.084          ns/op
 * ScalaVectorMapJmh.mContainsFound        -65      1000  avgt    2      _    20.634          ns/op
 * ScalaVectorMapJmh.mContainsFound        -65     10000  avgt    2      _    36.600          ns/op
 * ScalaVectorMapJmh.mContainsFound        -65    100000  avgt    2      _   100.555          ns/op
 * ScalaVectorMapJmh.mContainsFound        -65   1000000  avgt    2      _   269.156          ns/op
 * ScalaVectorMapJmh.mContainsFound        -65  10000000  avgt    2      _   430.601          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65        10  avgt    2      _     7.007          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65       100  avgt    2      _    11.134          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65      1000  avgt    2      _    20.664          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65     10000  avgt    2      _    35.706          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65    100000  avgt    2      _   104.273          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65   1000000  avgt    2      _   254.167          ns/op
 * ScalaVectorMapJmh.mContainsNotFound     -65  10000000  avgt    2      _   419.120          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65        10  avgt    2      _   847.698          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65       100  avgt    2      _  8552.204          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65      1000  avgt    2      _145905.646          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65     10000  avgt    2     1_495972.812          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65    100000  avgt    2    25_365742.926          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65   1000000  avgt    2   469_077151.250          ns/op
 * ScalaVectorMapJmh.mCopyOf               -65  10000000  avgt    2  8203_027493.500          ns/op
 * ScalaVectorMapJmh.mHead                 -65        10  avgt    2      _     7.234          ns/op
 * ScalaVectorMapJmh.mHead                 -65       100  avgt    2      _    21.065          ns/op
 * ScalaVectorMapJmh.mHead                 -65      1000  avgt    2      _    25.789          ns/op
 * ScalaVectorMapJmh.mHead                 -65     10000  avgt    2      _    25.612          ns/op
 * ScalaVectorMapJmh.mHead                 -65    100000  avgt    2      _    26.300          ns/op
 * ScalaVectorMapJmh.mHead                 -65   1000000  avgt    2      _    35.969          ns/op
 * ScalaVectorMapJmh.mHead                 -65  10000000  avgt    2      _    36.041          ns/op
 * ScalaVectorMapJmh.mIterate              -65        10  avgt    2      _    90.502          ns/op
 * ScalaVectorMapJmh.mIterate              -65       100  avgt    2      _  1609.942          ns/op
 * ScalaVectorMapJmh.mIterate              -65      1000  avgt    2      _ 24484.242          ns/op
 * ScalaVectorMapJmh.mIterate              -65     10000  avgt    2      _487208.667          ns/op
 * ScalaVectorMapJmh.mIterate              -65    100000  avgt    2     9_449481.765          ns/op
 * ScalaVectorMapJmh.mIterate              -65   1000000  avgt    2   330_425467.570          ns/op
 * ScalaVectorMapJmh.mIterate              -65  10000000  avgt    2  5154_931742.500          ns/op
 * ScalaVectorMapJmh.mPut                  -65        10  avgt    2      _    32.974          ns/op
 * ScalaVectorMapJmh.mPut                  -65       100  avgt    2      _    60.249          ns/op
 * ScalaVectorMapJmh.mPut                  -65      1000  avgt    2      _    92.300          ns/op
 * ScalaVectorMapJmh.mPut                  -65     10000  avgt    2      _   133.474          ns/op
 * ScalaVectorMapJmh.mPut                  -65    100000  avgt    2      _   230.001          ns/op
 * ScalaVectorMapJmh.mPut                  -65   1000000  avgt    2      _   501.866          ns/op
 * ScalaVectorMapJmh.mPut                  -65  10000000  avgt    2      _   861.718          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65        10  avgt    2      _   929.769          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65       100  avgt    2      _ 18665.378          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65      1000  avgt    2      _334045.424          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65     10000  avgt    2     5_846002.308          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65    100000  avgt    2   116_539751.645          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65   1000000  avgt    2  1703_989948.417          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne       -65  10000000  avgt    2 17861_188233.500          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65        10  avgt    2      _   153.322          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65       100  avgt    2      _   228.369          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65      1000  avgt    2      _   392.195          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65     10000  avgt    2      _   454.382          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65    100000  avgt    2      _   676.448          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65   1000000  avgt    2      _  1229.155          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd        -65  10000000  avgt    2      _  1677.770          ns/op
 * ScalaVectorMapJmh.mTail                 -65        10  avgt    2      _    58.791          ns/op
 * ScalaVectorMapJmh.mTail                 -65       100  avgt    2      _   101.469          ns/op
 * ScalaVectorMapJmh.mTail                 -65      1000  avgt    2      _   133.833          ns/op
 * ScalaVectorMapJmh.mTail                 -65     10000  avgt    2      _   117.610          ns/op
 * ScalaVectorMapJmh.mTail                 -65    100000  avgt    2      _   155.144          ns/op
 * ScalaVectorMapJmh.mTail                 -65   1000000  avgt    2      _   220.500          ns/op
 * ScalaVectorMapJmh.mTail                 -65  10000000  avgt    2      _   254.799          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaVectorMapJmh {
    @Param({/*"10","1000",*/"100000"/*,"10000000"*/})
    private int size;

    @Param({"-65"})
    private int mask;


    private BenchmarkData data;
    private VectorMap<Key, Boolean> mapA;
    private Vector<Tuple2<Key, Boolean>> listA;
    private Vector<Key> listAKeys;
    private Method appended;


    @SuppressWarnings("unchecked")
    @Setup
    public void setup() throws InvocationTargetException, IllegalAccessException {
        try {
            appended = Vector.class.getDeclaredMethod("appended",  Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, VectorMap<Key, Boolean>> b = VectorMap.newBuilder();
        for (Key key : data.setA) {
            Tuple2<Key, Boolean> elem = new Tuple2<>(key, Boolean.TRUE);
            b.addOne(elem);
        }
        listA=Vector.<Tuple2<Key,Boolean>>newBuilder().result();
        listAKeys=Vector.<Key>newBuilder().result();
        for (Key key : data.listA) {
            Tuple2<Key, Boolean> elem = new Tuple2<>(key, Boolean.TRUE);
            listA= (Vector<Tuple2<Key, Boolean>>) appended.invoke(listA,elem);
            listAKeys= (Vector<Key>) appended.invoke(listAKeys,key);
        }
        mapA = b.result();
    }

    @Benchmark
    public VectorMap<Key,Boolean> mAddAll() {
        return VectorMap.from(listA);
    }

    @Benchmark
    public VectorMap<Key,Boolean> mAddOneByOne() {
        VectorMap<Key,Boolean> set =  VectorMap.<Key,Boolean>newBuilder().result();
        for (Key key : data.listA) {
            set=set.updated(key,Boolean.TRUE);
        }
        return set;
    }

    @Benchmark
    public VectorMap<Key,Boolean> mRemoveOneByOne() {
        VectorMap<Key,Boolean> set = mapA;
        for (Key key : data.listA) {
            set=set.removed(key);
        }
        return set;
    }

    @Benchmark
    public Object mRemoveAll() {
        VectorMap<Key,Boolean> set = mapA;
        return set.removedAll(listAKeys);
    }    


    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Iterator<Key> i = mapA.keysIterator(); i.hasNext(); ) {
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public VectorMap<Key, Boolean> mRemoveThenAdd() {
        Key key = data.nextKeyInA();
        return (VectorMap<Key, Boolean>) mapA.$minus(key).$plus(new Tuple2<>(key, Boolean.TRUE));

    }

    @Benchmark
    public VectorMap<Key, Boolean> mPut() {
        Key key = data.nextKeyInA();
        return (VectorMap<Key, Boolean>) mapA.$plus(new Tuple2<>(key, Boolean.FALSE));
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapA.contains(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapA.contains(key);
    }

    @Benchmark
    public Key mHead() {
        return mapA.head()._1;
    }

}
