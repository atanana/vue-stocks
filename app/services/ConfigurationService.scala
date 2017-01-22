package services

import javax.inject.{Inject, Singleton}

import com.google.inject.ImplementedBy
import play.api.Configuration

@ImplementedBy(classOf[ConfigurationServiceImpl])
trait ConfigurationService {
  def login: String

  def password: String

  def salt: String
}

@Singleton
class ConfigurationServiceImpl @Inject()(val config: Configuration) extends ConfigurationService {
  override def login: String = config.getString("stocks.login").get

  override def password: String = config.getString("stocks.password").get

  override def salt: String = config.getString("stocks.salt").get
}
