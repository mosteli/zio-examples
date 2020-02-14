package com.zio.examples.http4s_doobie

import zio.RIO

/**
  * Helper that will access to the Persistence Service
  */
package object db extends Persistence.Service[Persistence] {
  object User {
    def mkTable(): RIO[Persistence, Unit] =
      RIO.accessM(_.userPersistence.mkTable)
    def get(id: Int): RIO[Persistence, User] =
      RIO.accessM(_.userPersistence.get(id))
    def create(user: User): RIO[Persistence, User] =
      RIO.accessM(_.userPersistence.create(user))
    def delete(id: Int): RIO[Persistence, Boolean] =
      RIO.accessM(_.userPersistence.delete(id))
  }

  object Note {
    def mkTable(): RIO[Persistence, Unit] =
      RIO.accessM(_.notePersistence.mkTable)
    def get(id: Int): RIO[Persistence, User] =
      RIO.accessM(_.notePersistence.get(id))
    def create(user: User): RIO[Persistence, User] =
      RIO.accessM(_.notePersistence.create(user))
    def delete(id: Int): RIO[Persistence, Boolean] =
      RIO.accessM(_.notePersistence.delete(id))
  }
}
