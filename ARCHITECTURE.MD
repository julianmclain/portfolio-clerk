# Architecture

TODO - https://matklad.github.io//2021/02/06/ARCHITECTURE.md.html

## Application layer

## Persistence layer

Layering:

Model implementations (case classes) <- TablesDefinitions <- Repositories <- Services  

Repositories are thin data access objects that execute queries against the database. Services encapsulate 
business logic that hits database (which is encapsulated by the repository layer)