package spark.home;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MainAwardsCount {

    public static void main(String... args) {
        SparkSession sparkSession = SparkSession.builder()
                .appName("Count awards")
                .master("local")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();

        Dataset<Row> dataset = sparkSession.read().option("header", "true").format("csv").load(args[0]);
      //  dataset.printSchema();
        //dataset.show(8);

        dataset.createOrReplaceTempView("awards");
       Dataset<Row> dfCount= sparkSession.sql("select count('award'),playerID from awards  group by playerID");
        dfCount.show();

    }
}
