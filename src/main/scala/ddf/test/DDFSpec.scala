package ddf.test

import ddf.test.analytics.AnalyticsBehaviors
import ddf.test.content.ContentBehaviors
import ddf.test.etl.ETLBehaviors
import io.ddf.DDFManager
import io.ddf.DDFManager.EngineType
import org.scalatest.FlatSpec

class DDFSpec(engineType: EngineType) extends FlatSpec with AnalyticsBehaviors with ContentBehaviors with ETLBehaviors {

  implicit val loader =
    engineType match {
      case EngineType.JDBC => H2Loader
      case EngineType.POSTGRES => PostgresLoader
      case EngineType.AWS => AWSLoader
    }

  it should behave like ddfWithAddressing
  it should behave like ddfWithAggregationHandler
  it should behave like ddfWithStatisticsHandler
  it should behave like ddfWithBinningHandler

  it should behave like ddfWithMetaDataHandler
  it should behave like ddfWithPersistenceHandler
  it should behave like ddfWithSchemaHandler
  it should behave like ddfWithViewHandler

   it should behave like ddfWithMissingDataFillSupport
   it should behave like ddfWithMissingDataDropSupport
   it should behave like ddfWithSqlHandler
   it should behave like ddfWithBasicTransformSupport
}

object Main extends App() {
  val engineName: String = "jdbc"
  val test = new DDFSpec(EngineType.fromString(engineName))
}


object H2Loader extends Loader {

  val engineDescriptor = EngineDescriptor(engine.name())

  override def engine: EngineType = EngineType.JDBC

  override def ddfManager: DDFManager = DDFManager.get(engine, engineDescriptor)

  override def MT_CARS_CREATE = "CREATE TABLE mtcars (mpg decimal,cyl int, disp decimal, hp int, drat decimal, wt decimal, qsec decimal, vs int, am int, gear int, carb int)"

}

object PostgresLoader extends Loader {

  val engineDescriptor = EngineDescriptor(engine.name())

  override def engine: EngineType = EngineType.POSTGRES

  override def ddfManager: DDFManager = DDFManager.get(engine, engineDescriptor)

  override def MT_CARS_CREATE = "CREATE TABLE mtcars (mpg decimal,cyl int, disp decimal, hp int, drat decimal, wt decimal, qsec decimal, vs int, am int, gear int, carb int)"
}

object AWSLoader extends Loader {
  val engineDescriptor = EngineDescriptor(engine.name())

  override def engine: EngineType = DDFManager.EngineType.AWS

  override def ddfManager: DDFManager = DDFManager.get(engine, engineDescriptor)

  override def MT_CARS_CREATE = "CREATE TABLE mtcars (mpg decimal,cyl int, disp decimal, hp int, drat decimal, wt decimal, qsec decimal, vs int, am int, gear int, carb int)"
}