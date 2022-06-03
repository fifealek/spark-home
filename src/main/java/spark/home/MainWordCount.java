package spark.home;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

public class MainWordCount {

    public static void main(String ...args) {
//        SparkSession sparkSession = SparkSession.builder()
//                .appName("Read txt and put data to database")
//                .master("local")
//                .getOrCreate();
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Word Count with Spark");

        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession sparkSession = SparkSession.builder().sparkContext(sc.sc()).getOrCreate();
        JavaRDD<String> javaRDD =sc.textFile(args[0]);


        int partitions_size=javaRDD.rdd().partitions().length;
        javaRDD= javaRDD.repartition(4);
        System.out.println("partitions_size="+partitions_size);
        partitions_size=javaRDD.rdd().partitions().length;
        System.out.println("partitions_size="+partitions_size);

        //RDD<String> row =javaRDD.rdd();

       // Dataset<String> dataset= sparkSession.createDataset(javaRDD.rdd(), Encoders.bean(String.class));
        System.out.println("partitions_size="+javaRDD.rdd());
        @SuppressWarnings("resource")
        JavaPairRDD<String, Integer> counts = javaRDD.flatMap(line ->
                {
                    return Arrays.asList(line.split(" ")).listIterator();
                })
                .mapToPair(word -> new Tuple2<String, Integer>(word, 1))
                .reduceByKey((x, y) -> x + y);

        counts.saveAsTextFile(args[1]);
    }
}
