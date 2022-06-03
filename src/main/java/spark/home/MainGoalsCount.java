package spark.home;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MainGoalsCount {

    public static void main(String ... args) throws AnalysisException {
        SparkSession sparkSession = SparkSession.builder()
                .appName("Count awards")
                .master("local")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();

        Dataset<Row> dataset = sparkSession.read().option("header","true").csv(args[0]);
        dataset.createOrReplaceTempView("scoring");
       // dataset.show(8);
        Dataset<Row> countStint = sparkSession.sql("select count('stint'),playerID from scoring group by playerID");
        countStint.show();

    }
}
