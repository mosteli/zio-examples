package com.zio.examples.http4s_doobie

trait Entity
final case class User(id: Long, name: String) extends Entity
final case class Note(id: Long, title: String, contents: String) extends Entity

final case class UserNotFound(id: Int) extends Exception
final case class TableCreationFailed() extends Exception
