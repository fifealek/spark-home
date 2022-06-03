package spark.home;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class MainPlayerTeam {

    private static final String TABLE_SCORING = "scoring";
    private static final String TABLE_TEAMS = "teams";
    private final SparkSession sparkSession;


    public MainPlayerTeam(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    public static void main(String... args) {
        SparkSession sparkSession = SparkSession.builder()
                .appName("Count awards")
                .master("local")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();
        MainPlayerTeam playerTeam = new MainPlayerTeam(sparkSession);
        playerTeam.createVeiw(args[0], TABLE_SCORING);
        playerTeam.createVeiw(args[1], TABLE_TEAMS);
        playerTeam.definedPlayerTeam(args[2]);
    }
    private void createVeiw(String path, String nameView) {
        Dataset<Row> dataset = sparkSession.read().option("header", "true").csv(path);
        dataset.createOrReplaceTempView(nameView);
    }

    private void definedPlayerTeam(String savePath) {
      Dataset<Row> datasetPlayrTeam = sparkSession.sql("select  distinct  s.playerID, t.name   from "+TABLE_SCORING+" s, "+TABLE_TEAMS+ " t where s.tmID=t.tmID");

      //datasetPlayrTeam.show();

        Dataset<Row> datasetPlayrTeam2 = sparkSession.sql("select    s.playerID, t.name,t.tmID  from "+TABLE_SCORING+" s "+"left join "+TABLE_TEAMS+" t on s.tmID=t.tmID group by s.playerID,t.name,t.tmID order by s.playerID asc ");
        //datasetPlayrTeam2.explain();

        datasetPlayrTeam2.repartition(1).write().mode(SaveMode.Overwrite).csv(savePath);

        //datasetPlayrTeam2.show(false);
    }
}
