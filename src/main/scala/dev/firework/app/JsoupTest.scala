package dev.firework.app

import cats.effect.{IO, IOApp}
import cats.syntax.parallel._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import dev.firework.algebras.Scrapper
import dev.firework.utils.IOUtils._

object JsoupTest extends IOApp.Simple:
  
  
  
  override def run: IO[Unit] =
    for
      scrap <- IO(Scrapper.impl[IO])
      titles <-
        List(
          "https://es.wikipkjhlkjhlkedia.org/wiki/Surtsey",
          "https://www.amazon.com/-/es/stores/GeForce/RTX3090_SERIEGEFORCERTX30/page/CFF83A4D-9DEC-4003-AC7E-96DF4170CED0",
          "https://articulo.mercadolibre.com.co/MCO-839377495-morovol-mesh-micro-atx-tower-computer-case-2pcs-argb-f-_JM?searchVariation=173982473528#searchVariation=173982473528&position=2&search_layout=stack&type=item&tracking_id=94f91d69-e1f8-422c-963b-6acd1303181a"
        ).parTraverse(url => scrap.getQuery(url).debug)
        
      _ <- IO.println("Total de consultas: ") >> IO(titles.foreach(println))
    yield ()
  
end JsoupTest