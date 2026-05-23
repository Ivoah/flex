package net.ivoah.flex

import com.typesafe.config.ConfigFactory

import java.util.Base64

object Config {
  private val config = ConfigFactory.load()

  // object database {
  //   val user: String = config.getString("database.user")
  //   val password: String = config.getString("database.password")
  //   val connectionString: String = config.getString("database.connectionString")
  // }

  object tmdb {
    val token: String = config.getString("tmdb.token")
  }
}
