package algebras.scrappers

import cats.effect.{ExitCode, IO, IOApp}

import dev.firework.algebras.scrappers.{EbayScrapper, Scrapper}

import dev.firework.utils


object EbayScrapperTest extends IOApp.Simple:
  
  val ebayScrapper: Scrapper[IO] = EbayScrapper.impl[IO]
  
  override def run: IO[Unit] =
    ebayScrapper.getMatchedElement("RTX%203090%20TI").flatMap(result => IO.println(result))
  
end EbayScrapperTest