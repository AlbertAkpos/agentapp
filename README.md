# Agent Mobile App V2

This app is a product of Youverify and used by Youverify and other Agents to carry out their verification activities while being checkmated.

## Setup
This project follows the Clean Architecture, is based on the MVVM model and is organized according to the following (feature based) packages: 

### core
This contains functional logic for results returned from API and DB calls, base classes and custom views used accross the entire application

### data layer:
## api
This contains logic concerning interaction to the server, including data classes (for holding serialized objects such as json) used for the client-server communication. 

## db
This contains any logic relating to creating, reading, writing, updating, and securing the database.

### domain layer:
This is a contract between the data layer and the presentation layer and contains all the use cases of the application and the repository

## repository
The repository servers as a single source of truth for accessing data resources. All app related data is exposed to the UI through the repository.

### presentation
This is where any logic related to the user interface is placed including android framework related classes eg. Activities, Fragments, Adapters, View Models etc

### dependecy injection
This is holds any logic or definition for dependency injection.


## Stack
Retrofit, Room, Hilt, Coroutines Flow|Channel, Android Jetpack
