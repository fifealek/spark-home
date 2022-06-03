package spark.home;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String... args) {
        SparkSession sparkSession = SparkSession.builder()
                .appName("Read csv and put data to database")
                .master("local")
                .getOrCreate();
        Dataset<Row> df = sparkSession.read().format("csv").option("header", "true")
                .load("/home/fife/Documents/books/big_data/hotels/part-00000-7b2b2c30-eb5e-4ab6-af89-28fae7bdb9e4-c000.csv");
        df = df.withColumn("Latitude", df.col("Latitude"));
        Map<String, String> map = new HashMap<>();
        map.put("Latitude", "min");
        df = df.agg(map);
        df.write().mode(SaveMode.Overwrite).csv("/home/fife/app/spark-home/src/main/resources/1.csv");
    }
}
