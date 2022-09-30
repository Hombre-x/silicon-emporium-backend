package dev.firework.core

import cats.effect.{IO, IOApp, Sync}
import cats.syntax.applicativeError.*
import dev.firework.algebras.scrappers.*
import dev.firework.domain.scrapper.{ScrapperResult, UserQuery}
import eu.timepit.refined.types.string.NonEmptyString
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

trait AmazonScrapperTester[F[_]] extends Scrapper[F]

object AmazonScrapperTester


object JsoupTest extends IOApp.Simple:
  override def run: IO[Unit] =
    for
      _ <- IO.println("Hello this is working")
    yield ()

end JsoupTest
