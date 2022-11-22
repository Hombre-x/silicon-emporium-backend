package algebras.scrappers

import cats.effect.{ExitCode, IO, IOApp}

import dev.firework.algebras.scrappers.{MLScrapper, Scrapper}

import dev.firework.utils


object MLScrapperTest extends IOApp.Simple:

  val mlScrapper: Scrapper[IO] = MLScrapper.impl[IO]

  override def run: IO[Unit] =
    mlScrapper.getMatchedElement("RTX%203090%20TI").flatMap(result => IO.println(result))
    
end MLScrapperTest