# Live Football World Cup Score Board

## Overview

The **Live Football World Cup Score Board** is a simple in-memory Java library for managing live scores of ongoing
football matches. It allows users to start matches, update scores, finish matches, and retrieve a summary of all ongoing
matches in real time.

The scoreboard supports the following operations:

1. **Start a new match** between two teams, with an initial score of `0-0`
2. **Update the score** of an ongoing match with new values
3. **Finish a match** to remove it from the scoreboard
4. **Retrieve a summary** of all ongoing matches ordered by their total score. Matches with the same total score are
   ordered by the most recently started match

## Features

- **In-memory store**: matches are stored using an in-memory data structure
- **Multithreading support**: the library is designed to handle concurrent updates
- **Logging**: the library uses **SLF4J** integration to log various events

## Requirements

- **Java**: Version 17 or higher
- **Maven**: To manage dependencies and build the project
- **Lombok**: For boilerplate code reduction and logging
- **SLF4J**: For logging events

## Build

1. **Clone the repository**:

    ```sh
    git clone https://github.com/ykorobitsin/scoreboard.git
    cd scoreboard
    ```

2. **Build the project**:
    - Run the following Maven command to build the project:

      ```sh
      mvn clean install
      ```

    - The project will generate a **snapshot JAR** during the Maven build phase:

      ```
      target/scoreboard-1.0-SNAPSHOT.jar
      ```

3. **Running Tests**:
    - Run the tests using Maven:

      ```sh
      mvn test
      ```

## Usage

### Starting a match

```java
ScoreBoard scoreBoard = new ScoreBoard();
scoreBoard.startMatch("Poland", "Germany");
```

## Future improvements

- Normalize team names by trimming and converting them to lowercase
- Improve match lookup by ensuring consistent team name formatting
- Optimize match summary retrieval to reduce sorting when dealing with a large number of matches
  - Maintain sorted order of matches after each update or new addition
  - Use a sorted collection such as `TreeSet` if the frequency of match updates is significantly lower than that of summary retrievals