package dev.firework.domain

import cats.effect.Resource

import skunk.Session


object skunkTypes:
 
  type Pool[F[_]] = Resource[F, Session[F]]
  type SessionPool[F[_]] = Resource[F, Pool[F]]
  
end skunkTypes

