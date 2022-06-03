package spark.home;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class MainTopPlayers {

    private static final String TABLE_SCORING = "scoring";
    private static final String TABLE_TEAMS = "teams";
    private final SparkSession sparkSession;

    public MainTopPlayers(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    public static void main(String... args) {
        SparkSession sparkSession = SparkSession.builder()
                .appName("Read txt and put data to database")
                .master("local")
                .getOrCreate();
        MainTopPlayers mainTopPlayers = new MainTopPlayers(sparkSession);
        mainTopPlayers.createVeiw(args[0],TABLE_SCORING);
        mainTopPlayers.buildSqlRequest(args[1]);
    }

    private void createVeiw(String path, String nameView) {
        Dataset<Row> dataset = sparkSession.read().option("header", "true").csv(path);
        dataset.createOrReplaceTempView(nameView);
    }

    private void buildSqlRequest(String saveFile){
        Dataset<Row> dataset = sparkSession.sql("select playerID,count(stint) as countGoals from "+TABLE_SCORING +" group by playerID order by count(stint) desc limit 10");
        dataset.show();
        dataset.repartition(1).write().mode(SaveMode.Overwrite).csv(saveFile);
        dataset.repartition(1).write().format("csv").option("path","~/outputtable").partitionBy("playerID").bucketBy(1000,"countGoals").saveAsTable("top_players");
    }


}
