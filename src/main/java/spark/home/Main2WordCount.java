package spark.home;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

public class Main2WordCount {

    public static void main(String ...args) {
                SparkSession sparkSession = SparkSession.builder()
                .appName("Read txt and put data to database")
                .master("local")
                .getOrCreate();

        Dataset<Row> dataset=  sparkSession.read().format("text").load(args[0]);
//        String str=dataset.toString().replaceAll("\\!","").replaceAll("\\?","").replaceAll("\\.","").toLowerCase();

        String str=dataset.toString().toLowerCase();
        System.out.println("str="+dataset);

        Dataset<String> ds= sparkSession.createDataset(Arrays.asList(str.split(" ")), Encoders.STRING());
        //ds = ds.withColumnRenamed("w","words");
        //ds.show();
        System.out.println("count = "+ds.count());


    }
}
