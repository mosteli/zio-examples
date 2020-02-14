package com.zio.examples.http4s_doobie
package db

import cats.effect.Blocker
import com.zio.examples.http4s_doobie.configuration.DbConfig
import doobie._
import doobie.{Query0, Transactor, Update0}
import zio._
import doobie.implicits._
import scala.concurrent.ExecutionContext
import zio.interop.catz._
import doobie.postgres.free.largeobjectmanager.LargeObjectManagerOp.Create

/**
  * Persistence Service
  */
trait Persistence extends Serializable {
  val userPersistence: Persistence.Service[Any]
  val notePersistence: Persistence.Service[Any]
}

object Persistence {
  trait Service[R] {
    def mkTable: RIO[R, Unit]
    def get(id: Int): RIO[R, User]
    def create(user: User): RIO[R, User]
    def delete(id: Int): RIO[R, Boolean]
  }

  /**
    * Persistence Module for production using Doobie
    */
  trait Live extends Persistence {
    protected def tnx: Transactor[Task]
    val userPersistence: Service[Any] = new UserService.Live
    val notePersistence: Service[Any] = new NoteService.Live
  }

  def mkTransactor[F[_]](
      conf: DbConfig,
      connectEC: ExecutionContext,
      transactEC: ExecutionContext
  ): Transactor[Task] = {
    import zio.interop.catz._
    implicit val cs = zio.interop.catz.zioContextShift
    implicit val async = zio.interop.catz.IOConcurrentEffectOps(IO)

    Transactor.fromDriverManager[Task](
      "org.postgresql.Driver", // driver classname
      "jdbc:postgresql:emost", // connect URL (driver-specific)
      conf.user, // user
      conf.password, // password
      Blocker.liftExecutionContext(transactEC) // just for testing
    )
  }
}
