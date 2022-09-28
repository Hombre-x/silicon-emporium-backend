package dev.firework.utils

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._

object DebugUtils:
  
  extension [F[_] : Sync, A] (fa: F[A])
    def debug: F[A] =
      for
        a <- fa
        t = Thread.currentThread().getName
        _ = println(s"[$t] $a")
      yield a

end DebugUtils
