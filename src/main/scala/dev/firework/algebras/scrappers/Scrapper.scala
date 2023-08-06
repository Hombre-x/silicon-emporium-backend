package dev.firework.algebras.scrappers

import dev.firework.domain.scrapper.*


trait Scrapper[F[_]]:
  def getMatchedElement (userQuery: UserQuery): F[ScrapperResult]

end Scrapper


