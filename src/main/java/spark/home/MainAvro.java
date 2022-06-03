package spark.home;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;



public class MainAvro {

    public static void main(String... args) {
        SparkSession sparkSession = SparkSession.builder()
                .appName("Read csv and put data to database")
                .master("local")
                .getOrCreate();
        Dataset<Row> df = sparkSession.read().format("avro").option("header", "true")
                .load("/home/fife/Documents/books/big_data/expedia/part-00000-ef2b800c-0702-462d-b37f-5f2fb3a093d0-c000.avro");

       // df = df.withColumn("channel", df.col("channel"));
//        Map<String, String> map = new HashMap<>();
//        map.put("Latitude", "min");
//        df = df.agg(map);
        df.write().mode(SaveMode.Overwrite).csv("/home/fife/app/spark-home/src/main/resources/2.csv");
    }
}
