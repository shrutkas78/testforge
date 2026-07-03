# 🧪 TestForge — Full-Stack Test Automation Framework

> Production-grade UI + API test automation framework built with Java, Selenium, Playwright, and REST Assured — containerized with Docker and wired into CI/CD via GitHub Actions.

<!-- Badges: these start working once CI is set up. Replace USERNAME/REPO with yours. -->
![CI](https://github.com/USERNAME/REPO/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.x-green)
![Playwright](https://img.shields.io/badge/Playwright-Java-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

<!-- Demo GIF: record a 15-30s screen capture of tests running + Allure report opening.
     Use ScreenToGif (Windows) or Kap (Mac). Drop the file in docs/demo.gif -->
![Demo](docs/demo.gif)

---

## 📌 What this project demonstrates

This is not a tutorial project. It's a framework designed the way a real SDET team would build one:

- **Two UI automation stacks** (Selenium WebDriver + Playwright) against the same application, switchable via a config profile — showing I can evaluate and work across tools, not just one
- **Independent API test layer** (REST Assured) with JSON schema validation, auth flows, and negative testing
- **Page Object Model** with a clean separation: pages know locators, tests know behavior, utilities know plumbing
- **Data-driven testing** from external JSON/CSV — same tests, multiple data sets
- **Allure reporting** with screenshots on failure, environment info, and step-level logs
- **Dockerized execution** — the entire suite runs with one `docker run`, no local setup
- **CI/CD** — every push triggers the suite via GitHub Actions; nightly regression on a schedule

**Application under test:** [Sauce Demo](https://www.saucedemo.com) (UI) and [Restful Booker](https://restful-booker.herokuapp.com) (API) — public demo apps chosen so anyone can clone and run this immediately.

---

## 🏗️ Architecture

```
                    ┌─────────────────────────┐
                    │      Test Layer          │
                    │  (TestNG test classes)   │
                    └───────────┬─────────────┘
                                │
          ┌─────────────────────┼─────────────────────┐
          ▼                     ▼                     ▼
  ┌──────────────┐     ┌──────────────┐      ┌──────────────┐
  │ Selenium POM │     │ Playwright   │      │ REST Assured │
  │ (UI tests)   │     │ POM (UI)     │      │ (API tests)  │
  └──────┬───────┘     └──────┬───────┘      └──────┬───────┘
         │                    │                     │
         └────────────────────┼─────────────────────┘
                              ▼
              ┌───────────────────────────────┐
              │  Core: config, driver factory, │
              │  data providers, listeners,    │
              │  Allure reporting, utilities   │
              └───────────────────────────────┘
```

## 📁 Project structure

```
testforge/
├── .github/
│   └── workflows/
│       ├── ci.yml                  # Runs suite on every push/PR
│       └── nightly.yml             # Scheduled full regression (added in v2)
├── docs/
│   ├── demo.gif                    # Demo recording for README
│   └── framework-design.md         # Design decisions & rationale (interview gold)
├── src/
│   ├── main/java/com/testforge/
│   │   ├── config/
│   │   │   ├── ConfigManager.java        # Reads config.properties + env overrides
│   │   │   └── FrameworkConstants.java
│   │   ├── driver/
│   │   │   ├── DriverFactory.java        # Selenium WebDriver creation (thread-safe)
│   │   │   └── PlaywrightFactory.java    # Playwright browser/context creation
│   │   ├── pages/
│   │   │   ├── selenium/                 # Selenium Page Objects
│   │   │   │   ├── BasePage.java
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── ProductsPage.java
│   │   │   │   ├── CartPage.java
│   │   │   │   └── CheckoutPage.java
│   │   │   └── playwright/               # Playwright Page Objects (same pages)
│   │   │       ├── BasePage.java
│   │   │       ├── LoginPage.java
│   │   │       └── ...
│   │   ├── api/
│   │   │   ├── clients/
│   │   │   │   ├── BaseApiClient.java    # RestAssured spec builder
│   │   │   │   ├── AuthClient.java
│   │   │   │   └── BookingClient.java
│   │   │   └── models/                   # POJOs for request/response bodies
│   │   │       ├── Booking.java
│   │   │       └── AuthRequest.java
│   │   └── utils/
│   │       ├── DataReader.java           # JSON/CSV test data loader
│   │       ├── ScreenshotUtil.java
│   │       └── WaitUtil.java
│   └── test/
│       ├── java/com/testforge/tests/
│       │   ├── ui/
│       │   │   ├── selenium/
│       │   │   │   ├── LoginTests.java
│       │   │   │   ├── CartTests.java
│       │   │   │   └── CheckoutE2ETests.java
│       │   │   └── playwright/
│       │   │       └── LoginTests.java
│       │   ├── api/
│       │   │   ├── AuthTests.java
│       │   │   ├── BookingCrudTests.java
│       │   │   └── BookingNegativeTests.java
│       │   └── listeners/
│       │       ├── TestListener.java     # Screenshot-on-failure, Allure hooks
│       │       └── RetryAnalyzer.java
│       └── resources/
│           ├── config/
│           │   ├── config.properties     # Default env config
│           │   └── config-ci.properties  # CI overrides (headless, etc.)
│           ├── testdata/
│           │   ├── login-data.json
│           │   ├── checkout-data.csv
│           │   └── booking-payloads.json
│           ├── schemas/                  # JSON schemas for API validation
│           │   └── booking-schema.json
│           └── suites/
│               ├── smoke.xml             # TestNG suite: fast critical path
│               ├── regression.xml        # Full suite
│               └── api-only.xml
├── Dockerfile
├── docker-compose.yml                    # v2: Selenium Grid setup
├── pom.xml
├── .gitignore
└── README.md
```

---

## 🚀 Quick start

### Prerequisites
- Java 17+
- Maven 3.8+
- Chrome/Firefox installed (or use Docker — no browser needed)

### Run locally

```bash
# Clone
git clone https://github.com/USERNAME/testforge.git
cd testforge

# Run smoke suite (Selenium, headless)
mvn clean test -Psmoke

# Run full regression
mvn clean test -Pregression

# Run API tests only
mvn clean test -Papi

# Run with Playwright instead of Selenium
mvn clean test -Psmoke -Dui.engine=playwright

# Run against a different browser
mvn clean test -Psmoke -Dbrowser=firefox
```

### Run in Docker (zero local setup)

```bash
docker build -t testforge .
docker run --rm -v $(pwd)/allure-results:/app/allure-results testforge
```

### View the Allure report

```bash
mvn allure:serve
```

---

## 🧩 Key design decisions

*(Full rationale in [docs/framework-design.md](docs/framework-design.md))*

| Decision | Why |
|---|---|
| ThreadLocal driver factory | Enables safe parallel execution across test classes |
| Dual UI engines behind one interface | Tool-agnostic tests; framework survives a Selenium→Playwright migration |
| API layer fully decoupled from UI | API suite runs in ~30s; used as fast feedback gate in CI before UI tests |
| External test data (JSON/CSV) | Non-coders can add test cases; data changes don't touch code |
| Retry analyzer (max 1 retry, flagged) | Handles flakiness honestly — retried tests are marked in reports, not hidden |
| Config via properties + env overrides | Same code runs locally, in Docker, and in CI without edits |

---

## 📊 Test coverage

| Suite | Scenarios | Avg runtime | Trigger |
|---|---|---|---|
| Smoke (UI) | 8 | ~2 min | Every push |
| Regression (UI) | 25+ | ~10 min | PRs to main + nightly |
| API | 20+ | ~30 sec | Every push (runs first) |

---

## 🛣️ Roadmap

- [x] Selenium UI suite with POM
- [x] REST Assured API suite with schema validation
- [x] Allure reporting + screenshot on failure
- [x] Dockerfile
- [x] GitHub Actions CI
- [ ] Playwright parallel stack
- [ ] Selenium Grid via docker-compose (cross-browser parallel)
- [ ] Nightly scheduled pipeline with published reports (GitHub Pages)
- [ ] Performance smoke layer (JMeter)

---

## 👤 About me

QA Automation Engineer with 3 years of experience in manual and automation testing (Selenium, REST Assured, TestNG) across e-commerce and enterprise applications. Building in public as I deepen my SDET and DevOps skills.

- LinkedIn: [[my-linkedin](https://www.linkedin.com/in/shruti-kashyap55/)]
- Fiverr/Upwork: available for automation projects

## 📄 License

MIT — use anything here freely.
