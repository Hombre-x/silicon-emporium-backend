package dev.firework.app

import cats.effect.{IO, IOApp, Sync}
import cats.syntax.applicativeError.*
import cats.syntax.all.*
import cats.instances.all.*
import dev.firework.algebras.scrappers.*
import dev.firework.domain.scrapper.{ScrapperResult, UserQuery}
import dev.firework.utils.IOUtils.*
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.auto.*
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import dev.firework.utils.IOUtils.debug
import cats.effect.implicits.parallelForGenSpawn

trait AmazonScrapperTester[F[_]] extends Scrapper[F]

object AmazonScrapperTester



object JsoupTest extends IOApp.Simple:
  override def run: IO[Unit] =
    for
      _ <- IO.println("Hello this is working")
    yield ()

end JsoupTest
