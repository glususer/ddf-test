package ddf.test

import io.ddf.etl.ASqlHandler
import io.ddf.{DDFManager, DDF}
import io.ddf.datasource.{DataSourceURI, JDBCDataSourceCredentials, JDBCDataSourceDescriptor}
import io.ddf.misc.Config
import org.scalatest.Matchers
import io.ddf.DDFManager.EngineType

trait BaseBehaviors extends Matchers

object EngineDescriptor {
  def apply(engine: String) = {
    val user = Config.getValue(engine, "jdbcUser")
    val password = Config.getValue(engine, "jdbcPassword")
    val jdbcUrl = Config.getValue(engine, "jdbcUrl")
    val dataSourceURI = new DataSourceURI(jdbcUrl)
    val credentials = new JDBCDataSourceCredentials(user, password)
    new JDBCDataSourceDescriptor(dataSourceURI, credentials, null)
  }
}


trait Loader {

  def engine: EngineType

  def ddfManager: DDFManager

  val baseSchema = Config.getValue(engine.name(), "workspaceSchema")

  def dropTableIfExists(tableName: String) = {
    ddfManager.sql("drop table if exists " + tableName)
  }

  def create(command: String):DDF= {
    val sqlHandler = ddfManager.newDDF(ddfManager).getSqlHandler.asInstanceOf[ASqlHandler]
    sqlHandler.sql2ddf(command)
  }
  def load(command: String) = {
    val sqlHandler = ddfManager.newDDF(ddfManager).getSqlHandler.asInstanceOf[ASqlHandler]
    sqlHandler.sql2ddf(command)
  }


  def IRIS_CREATE = "create table iris (flower double, petal double, septal double)"



  def loadIrisTrain(): DDF = {
    try {
      ddfManager.getDDFByName("iris")
    } catch {
      case e: Exception =>
        dropTableIfExists("iris")
        create(IRIS_CREATE)
        val filePath = getClass.getResource("/fisheriris.csv").getPath
        ddfManager.sql("load '" + filePath + "' into iris")
        ddfManager.getDDFByName("iris")
    }
  }

  def loadIrisTest(): DDF = {
    val train = ddfManager.getDDFByName("iris")
    //train.sql2ddf("SELECT petal, septal FROM iris WHERE flower = 1.0000")
    train.VIEWS.project("petal", "septal")
  }

  def AIRLINE_CREATE = "create table airline (Year int,Month int,DayofMonth int," + "DayOfWeek int,DepTime int,CRSDepTime int,ArrTime int," + "CRSArrTime int,UniqueCarrier varchar, FlightNum int, " + "TailNum varchar, ActualElapsedTime int, CRSElapsedTime int, " + "AirTime int, ArrDelay int, DepDelay int, Origin varchar, " + "Dest varchar, Distance int, TaxiIn int, TaxiOut int, Cancelled int, " + "CancellationCode varchar, Diverted varchar, CarrierDelay int, " + "WeatherDelay int, NASDelay int, SecurityDelay int, LateAircraftDelay int )"

  def loadAirlineDDF(): DDF = {
    var ddf: DDF = null
    try {
      ddf = ddfManager.getDDFByName("airline")
    } catch {
      case e: Exception =>
        dropTableIfExists("airline")
        create(AIRLINE_CREATE)
        val filePath = getClass.getResource("/airline.csv").getPath
        load("load '" + filePath + "' into airline")
        ddf = ddfManager.getDDFByName("airline")
    }
    ddf
  }

  def AIRLINE_NA_CREATE = "create table airlineWithNA (Year int,Month int,DayofMonth int," + "DayOfWeek int,DepTime int,CRSDepTime int,ArrTime int," + "CRSArrTime int,UniqueCarrier varchar, FlightNum int, " + "TailNum varchar, ActualElapsedTime int, CRSElapsedTime int, " + "AirTime int, ArrDelay int, DepDelay int, Origin varchar, " + "Dest varchar, Distance int, TaxiIn int, TaxiOut int, Cancelled int, " + "CancellationCode varchar, Diverted varchar, CarrierDelay int, " + "WeatherDelay int, NASDelay int, SecurityDelay int, LateAircraftDelay int )"


  def loadAirlineNADDF(): DDF = {
    var ddf: DDF = null
    try {
      ddf = ddfManager.getDDFByName("airlineWithNA")
    } catch {
      case e: Exception =>
        dropTableIfExists("airlineWithNA")
        create(AIRLINE_NA_CREATE)
        val filePath = getClass.getResource("/airlineWithNA.csv").getPath
        load("load '" + filePath + "' WITH NULL '' NO DEFAULTS into airlineWithNA")
        ddf = ddfManager.getDDFByName("airlineWithNA")
    }
    ddf
  }

  def YEAR_NAMES_CREATE = "create table year_names (Year_num int,Name varchar)"

  def loadYearNamesDDF(): DDF = {
    var ddf: DDF = null
    try {
      ddf = ddfManager.getDDFByName("year_names")
    } catch {
      case e: Exception =>
        dropTableIfExists("year_names")
        create(YEAR_NAMES_CREATE)
        val filePath = getClass.getResource("/year_names.csv").getPath
        load("load '" + filePath + "' into year_names")
        ddf = ddfManager.getDDFByName("year_names")
    }
    ddf
  }

  def MT_CARS_CREATE = "CREATE TABLE mtcars (mpg double,cyl int, disp double, hp int, drat double, wt double, qsec double, vs int, am int, gear int, carb int)"

  def loadMtCarsDDF(): DDF = {
    var ddf: DDF = null
    try {
      ddf = ddfManager.getDDFByName("mtcars")
    } catch {
      case e: Exception =>
        dropTableIfExists("mtcars")
        create(MT_CARS_CREATE)
        val filePath = getClass.getResource("/mtcars").getPath
        load("load '" + filePath + "'  delimited by ' '  into mtcars")
        ddf = ddfManager.getDDFByName("mtcars")
    }
    ddf
  }


}

import scala.collection.JavaConverters._

object BetterList {
  def asBetterList[T](list: java.util.List[T]) = new BetterList[T](list)
}

class BetterList[T](list: java.util.List[T]) {
  def containsIgnoreCase(s: String) = list.asScala.filter(i => s.equalsIgnoreCase(i.toString)).size > 0
}