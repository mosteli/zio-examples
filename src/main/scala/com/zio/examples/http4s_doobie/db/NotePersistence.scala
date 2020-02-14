package com.zio.examples.http4s_doobie.db

trait NoteService {
  object Live extends Persistence.Service {
    def get(id: Int): Task[User] =
      SQL
        .get(id)
        .option
        .transact(tnx)
        .foldM(
          err => Task.fail(err),
          maybeUser => Task.require(UserNotFound(id))(Task.succeed(maybeUser))
        )

    def create(user: User): Task[User] =
      SQL
        .create(user)
        .run
        .transact(tnx)
        .foldM(err => Task.fail(err), _ => Task.succeed(user))

    def delete(id: Int): Task[Boolean] =
      SQL
        .delete(id)
        .run
        .transact(tnx)
        .fold(_ => false, _ => true)

    def mkTable(): Task[Unit] =
      SQL
        .mkTable()
        .run
        .transact(tnx)
        .foldM(err => Task.fail(err), _ => Task.unit)

    object SQL {
      def get(id: Int): Query0[User] =
        sql"""SELECT * FROM USERS WHERE ID = $id """.query[User]

      def create(user: User): Update0 =
        sql"""INSERT INTO USERS (id, name) VALUES (${user.id}, ${user.name})""".update

      def delete(id: Int): Update0 =
        sql"""DELETE FROM USERS WHERE id = $id""".update

      def mkTable(): Update0 =
        sql"""CREATE TABLE IF NOT EXISTS Users (
          id integer PRIMARY KEY,
          name varchar(255) NOT NULL
        );""".update
    }

  }

  object Test extends Persistence.Service {
    // Implement test service.
  }
}
