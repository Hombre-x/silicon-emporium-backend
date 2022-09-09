package dev.firework.utils

import cats.effect.IO

object IOUtils:

  extension[A] (io: IO[A])
    def debug: IO[A] =
      for
        a <- io
        t = Thread.currentThread().getName
        _ = println(s"[$t] $a")
      yield a

end IOUtils