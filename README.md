# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Here is a diagram showing the server/client interaction: [click me](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDADIQDmSaF1hKd+QsWCk27CAFcwPKnwENhpAEopOAZzCUZc3IMYjmLJBu0h+uhU3hQUwTafP0hVgFIQuD+c4MwAIigQUe0pZMy99Ul87YABBEDM1NUwAE2iAI2A1FBhktMxMdihJbBgAYjRgKgBPGBV1MCg7JAg0MoB3AAskTUxEVFIAWgA+chDKAC4YAG0ABQB5MgAVAF0YAHoJLKgAHTQAb03KCoBbFAAaGFxEtuhki5Rj4CQEAF9MGUoYYdrVY00oByTGz1SgACkOUBO50umTUNygdxgDyeCAAlJg6n8tCE+F8RlEwLF4ihEpN2EEAKpbcFbKHoglEhJqPE5NKTMgAURYHLgixgEKhMAAZkVjvytpgGXEmV9vpiNNjeGZJmgJAgEBjfgqATizCypcTSTAQDY7CgqWCBcBThcrnDbvTotKSczvrlJgBJABynKUfKtNph11u90ezxg3sWc3FtHl-wc+qdhrUkxNtk0MSkHRpR2tKEdhOdiRZ7ojPo5fpjkLzF2AWcWEAA1ugy1GYHWwB1JUmZQM5Vr47qUJMOx0G820JqQTqldk+yMPlAR-Wm+h3mNYPP4Mh0GBJgAmAAMh92e1H4-Qb3QyXyhWKZWgInJrA4XHaXR6fV3LMXk1mCxWdYskSZo0FPANoTteFkjeRc8TEDwh0mBBXzQHNq0DKCHUwBDuCHRNC2TVNTQzLN0LpbtCN7EZS29X1-VpGt2xXCdW2jUdKMZF1ZRGXDAWYzsL0nPj8K3X8BLHVdJzgrcvwwA9j1Pc8pKvNAbwKIoJBKUpChQFsxEkZhSk6bp+Dk5hvnEqZfC5DlFg5VY1mAtRQN2ZSJ3XahPmGAypH4lDxCkUF3PQdFfOkUT8R7F0gQeCAaEzTtgpY0LOKLV0aLZWoOQAWTmAA1DkJKE4VRQktLkx4l9ODw2cVTVDURNnKrxNVdVPJoTdvnMhTD0wa9b007SbGSVg-hgABxPNmWMj8zJ3QYFw3P8Jrsxz2DzNyUukjd4L+fy-im041GSwSpLC-bIr8aKjXJMALSgU7JInAsuOLN0ss5bleSrQURQgMUOINXtviMEwkN+vMcMu5rhmBmKYDuo6SVBV70pLT7bJ+jbTg9XwLhMzQHqhC5djSBBQEbYmmJxlAvTzUqAcR6aKpB3iYdCYdmdx-GYEJ81GMDMmKZAKnBehWn6dOaHwdhyzlu5lA8YJubqaFtBycptWJbzKX+Bk7qFr3GAjxPfZaeVvnVfF0mNZFsXc0DSW81U9S7y0spsAkKBsECax02yZH31M3ojZ-BX-yWdbNoqbbT2d05YN24Y4BIlB+LTM1kaeoSLgT-NejThNvnh27KWpEK0DRyqPvZLGGMd7J-sBrNWe4+dU4D-iIMLrvItLlNjTT7P8+r6jWU9ctK3zvO8zxmAuEgRWklyHiB7JOffBgNv3pTouIYtrfe7NYvhnEw-t4NoYepNxTzc3l5t4GjT71KKoAhQtoYDcN8g9KTXRah36BZJaXklzTBmBSQCaxaZbTOhOU85k4AQBQlAWePMk5gLxD-WqnNJgACt3BoGzpXdBSteZIJQdAdEODi5RSogjO6D0c7nR3hlCe5B66Q1OIzFunY2E8VoRDHuQjYZDHXojIII9N5j3bplOu30+QX2borAR85RF4MVnjTAGjcRwxuoPYAyRkgNmkTzC4lDUEXAgrI968iyz0W3MA5BVjuHZEXhAZeajhi6OVDANqCBt6+LnGfBWATL7JxTkbXq+R+pqUGq-QowAaiIFsLAYA2BfaECaC0YOn4w7yzAX+Gy3J7KOVyB1by4iAhBHThDEAgQ8Co0lDU4IYjroMKNA0tJzSB4Y0mEoXKBUiq0ySH0+c-hAhtM0QEtR9jBl5UKlWbx1Spl1Lqv4hqcyOELOGeVcZcNWnrJmVsyZtTT5DFag1CJWCO7RNvn1AaQA)
