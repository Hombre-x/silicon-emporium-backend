package dev.firework.utils

import cats.effect.Sync
import cats.syntax.flatMap.*
import cats.syntax.functor.*

extension [F[_] : Sync, A] (fa: F[A])
  def debug: F[A] =
    for
      a <- fa
      t <- Sync[F].delay(Thread.currentThread().getName)
      _ <- Sync[F].delay(println(s"[$t] $a"))
    yield a

