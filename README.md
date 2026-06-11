# LoanLens — Full-Stack EMI Calculator

A professional, feature-rich EMI (Equated Monthly Installment) calculator with a Java Spring Boot backend and a modern HTML/CSS/JS frontend.

---

## Features

### Loan Inputs
- Property price with slider
- Annual interest rate
- Loan tenure with quick-select pills (1Y–30Y)
- Down payment (% slider or absolute ₹ amount)
- Trade-in / asset offset

### Advanced Cost Features
- Flat processing fee
- Percentage-based processing fee
- Insurance cost
- Other upfront costs

### Prepayment Options
- Extra monthly prepayment slider
- Annual lump-sum prepayment
- Interest saved + months saved calculation
- Visual with/without prepayment balance chart

### Loan Comparison
- Side-by-side tenure comparison (e.g. 20-year vs 30-year)
- EMI, total interest, total cost comparison
- Color-coded better/worse values

### Amortization Schedule
- Full monthly schedule
- Yearly rollup view
- Sortable columns
- Paginated (24 rows/page)
- Search/filter by year
- CSV export

### Affordability & Budgeting
- EMI-to-income ratio gauge
- GREEN / YELLOW / RED rating
- Maximum affordable loan/price calculation
- Monthly budget donut chart (EMI vs Expenses vs Remaining)

### Charts (4 panels)
1. Payment breakdown donut (Principal / Interest / Upfront)
2. Yearly principal vs interest stacked bar
3. Outstanding balance over time line chart
4. Cumulative payment area chart

---

## Project Structure

```
emi-calculator/
├── backend/                        # Java Spring Boot API
│   ├── pom.xml
│   └── src/main/java/com/emicalc/
│       ├── EmiCalculatorApplication.java
│       ├── controller/LoanController.java
│       ├── model/
│       │   ├── LoanRequest.java
│       │   ├── LoanResult.java
│       │   └── AmortizationRow.java
│       └── service/LoanCalculatorService.java
│
└── frontend/
    └── index.html                  # Single-file frontend (HTML + CSS + JS)
```

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Any modern browser

### Backend Setup

```bash
cd backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**

#### API Endpoints
| Method | Endpoint              | Description              |
|--------|-----------------------|--------------------------|
| POST   | /api/loan/calculate   | Full loan calculation     |
| GET    | /api/loan/health      | Health check             |

#### Sample Request
```json
POST /api/loan/calculate
{
  "propertyPrice": 5000000,
  "downPayment": 1000000,
  "tradeInValue": 0,
  "annualInterestRate": 8.5,
  "tenureMonths": 240,
  "processingFee": 0,
  "processingFeePercent": 0.5,
  "insuranceCost": 50000,
  "otherUpfrontCosts": 0,
  "monthlyPrepayment": 5000,
  "annualLumpSum": 0,
  "monthlyIncome": 150000,
  "monthlyExpenses": 50000,
  "maxEmiPercent": 40,
  "compareTenureMonths": 360
}
```

### Frontend Setup

Just open `frontend/index.html` in your browser:

```bash
# Option 1: Direct open
open frontend/index.html

# Option 2: Simple HTTP server
cd frontend
python3 -m http.server 3000
# Then visit http://localhost:3000
```

> **Offline fallback**: If the Java backend is unavailable, all calculations run in JavaScript in the browser with identical logic.

---

## Backend: Core Calculation Logic

### EMI Formula
```
EMI = P × r × (1+r)^n / ((1+r)^n - 1)

Where:
  P = Principal loan amount
  r = Monthly interest rate (annual rate / 12 / 100)
  n = Tenure in months
```

### Prepayment Simulation
The amortization engine reduces balance by both standard principal and extra prepayment each month. Annual lump sums apply at month % 12 == 0.

### Affordability Rating
| EMI/Income | Rating |
|-----------|--------|
| ≤ 75% of threshold | 🟢 GREEN — Comfortable |
| ≤ threshold         | 🟡 YELLOW — Borderline  |
| > threshold         | 🔴 RED — Stretched       |

---

## Tech Stack

| Layer     | Technology                       |
|-----------|----------------------------------|
| Backend   | Java 17, Spring Boot 3.2, Maven  |
| Frontend  | HTML5, CSS3, Vanilla JavaScript  |
| Charts    | Chart.js 4.4                     |
| Fonts     | Inter, Syne (Google Fonts)       |

---

## Building a JAR

```bash
cd backend
mvn clean package
java -jar target/emi-calculator-1.0.0.jar
```

---

## Extending the Project

### Add a database (H2 / MySQL)
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
Add a `LoanHistory` entity + repository to persist calculations.

### Add authentication (Spring Security + JWT)
- Add `spring-boot-starter-security` dependency
- Create `JwtAuthFilter` and `UserDetailsService`

### Deploy
- Backend: Package as JAR and deploy to any cloud (AWS Elastic Beanstalk, Railway, Render)
- Frontend: Deploy `index.html` to Vercel / Netlify / GitHub Pages
- Update the API URL in `index.html` from `localhost:8080` to your deployed URL
