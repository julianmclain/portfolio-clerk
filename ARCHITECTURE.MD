# Architecture

TODO - https://matklad.github.io//2021/02/06/ARCHITECTURE.md.html

## Application layer

## Persistence layer

Layering:

Model implementations (case classes) <- TablesDefinition <- Repository <- Service  

Repositories are thin data access objects that provide queries against the database. Services provide an API to the 
application layer. Services encapsulate business logic that surrounds database access and hits the database by execute 
queries provided by repositories.