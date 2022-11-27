import com.typesafe.config.ConfigFactory
package object cian911 {
  val settings = new Settings(ConfigFactory.load())
}
