package dev.firework.algebras.scrappers

import dev.firework.domain.scrapper.*
import dev.firework.domain.search.Item


trait Scrapper[F[_]]:
  def getMatchedElement (userQuery: UserQuery): F[Item]

end Scrapper


