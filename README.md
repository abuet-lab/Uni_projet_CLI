# Vendure CLI

[![CI](https://github.com/abuet-lab/Uni_projet_CLI/actions/workflows/gradle.yml/badge.svg)](https://github.com/abuet-lab/Uni_projet_CLI/actions)
![Java](https://img.shields.io/badge/Java-17+-orange)
![Gradle](https://img.shields.io/badge/build-Gradle-02303A)
![GraphQL](https://img.shields.io/badge/API-GraphQL-E10098)

A Java command-line interface (CLI) for [Vendure](https://github.com/vendurehq/vendure), an open-source *headless* e-commerce platform. The CLI talks to Vendure's GraphQL API to display the product catalog directly in the terminal, either as a formatted table or as JSON.

> Built as part of a software engineering course at the Department of Computer Science, University of Fribourg (2026).

---

## Preview

```text
$ cli list --format table
+----+----------------------------+------------+
| ID | Name                       | Price      |
+----+----------------------------+------------+
| 1  | Laptop                     | 1299.00    |
| 2  | Tablet                     |  329.00    |
| 3  | Wireless Earbuds           |   89.00    |
+----+----------------------------+------------+
```

```text
$ cli list --format json
[
  { "id": "1", "name": "Laptop", "price": 129900 },
  ...
]
```

## Features

- **Git-style subcommands** (`list`, `product`, …) powered by [picocli](https://picocli.info/).
- **Multiple output formats** for the product list: a readable table (`--format table`) or JSON (`--format json`).
- **Configurable server URL**, either through the global `--url` option or the `URL` environment variable (the option takes precedence).
- **Typed, extensible GraphQL client**: each GraphQL query is a Java class that builds its own query string and maps the JSON response to typed objects.
- **Test-Driven Development (TDD)**: unit tests were written and committed before the implementation.
- **Continuous integration** with GitHub Actions: tests and formatting checks run on every push.

## Architecture

The core of the project is a small framework for defining GraphQL queries as Java classes. Rather than being a general-purpose GraphQL client, each query knows how to:

1. build the GraphQL query (and its variables) to send;
2. deserialize the JSON response into an instance of the expected result type.

```text
┌──────────────┐     ┌────────────────────┐     ┌─────────────────────┐
│  CLI         │ ──► │  VendureService    │ ──► │  Vendure GraphQL    │
│  (picocli)   │     │  (HTTP POST JSON)  │     │  API  /shop-api     │
└──────────────┘     └────────────────────┘     └─────────────────────┘
        │                      ▲
        ▼                      │
┌──────────────┐     ┌────────────────────┐
│  Formatters  │     │  GraphQLQuery<T>   │
│  table/json  │     │  ├ ProductsQuery   │
└──────────────┘     │  └ ProductQuery    │
                     └────────────────────┘
```

Adding a new query only requires creating a class that extends the base query and defines its GraphQL text and result type; the service handles the HTTP call and the deserialization.

Implemented queries:

| Class           | GraphQL query                                         | Result            |
|-----------------|-------------------------------------------------------|-------------------|
| `ProductsQuery` | `products(options: ProductListOptions): ProductList!` | List of products  |
| `ProductQuery`  | `product(id: ID, slug: String): Product`              | Product details   |

## Tech stack

- **Java** + **Gradle**
- **picocli** for command-line argument parsing
- **GraphQL** (HTTP POST requests with a JSON payload)
- **JUnit** for unit testing
- **GitHub Actions** for continuous integration
- **google-java-format** for consistent code style (Google Java Style)

## Getting started

### Prerequisites

- Java 17 or later
- A local Vendure server (see the [Vendure installation guide](https://docs.vendure.io/current/core/getting-started/installation)), which requires Node.js

### Run Vendure

```bash
npx @vendure/create my-shop
cd my-shop
npm run dev
```

The shop API is then available at `http://localhost:3000/shop-api`.

### Build the CLI

```bash
git clone https://github.com/abuet-lab/Uni_projet_CLI.git
cd Uni_projet_CLI
./gradlew build
```

## Usage

```bash
# Display products as a table
cli --url http://localhost:3000/shop-api list --format table

# The --url option can also be placed after the subcommand
cli list --url http://localhost:3000/shop-api --format json

# Use the environment variable instead of --url
URL=http://localhost:3000/shop-api cli list

# Show help
cli --help
cli list --help
```

## Tests

```bash
./gradlew test
```

The tests cover the two-way mapping between GraphQL and the Java classes:

- each query class generates the correct GraphQL query;
- a sample JSON response is correctly converted into an instance of the matching class;
- CLI options (`--url`, environment variable, `--format`) are parsed correctly.

No running Vendure server is needed to run the tests.

## Code style

The code follows the [Google Java Style](https://google.github.io/styleguide/javaguide.html). The CI pipeline fails if any file is not properly formatted. Formatting can be applied locally with the google-java-format plugin for IntelliJ IDEA.

## What I learned

- Designing a small, extensible, type-safe abstraction on top of a GraphQL API
- Practicing TDD: defining expected behavior through tests before writing code
- Building an ergonomic CLI with subcommands and shared options
- Setting up a CI pipeline (tests + formatting check) with GitHub Actions
- Integrating an existing headless backend service without depending on its source code
