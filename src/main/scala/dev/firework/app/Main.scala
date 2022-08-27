package dev.firework.app

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp:
  
  override def run(args: List[String]): IO[ExitCode] =
    
    for
      _ <- IO.println("Hellow from the future server!")
    yield ExitCode.Success
    
  end run
  
end Main