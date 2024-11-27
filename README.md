# Exchange Rates API Service

This is a Spring Boot application for managing currencies and fetching exchange rates from external sources (e.g., Fixer.io API). It provides RESTful endpoints for interacting with currencies and exchange rates, supports automatic updates, and includes Swagger documentation for API exploration.

---

## **Features**
- Add new currencies to the system.
- Fetch exchange rates for a specific currency.
- Scheduled task to automatically update exchange rates every minute.
- Store currencies and exchange rates in a PostgreSQL database.
- Manage database schema using Liquibase.
- Docker support for PostgreSQL.
- Swagger UI for API documentation.

---

## **Endpoints**

### **Currency Endpoints**
1. **Add a New Currency**
    - **Method:** `POST`
    - **URL:** `/api/currencies?currencyCode={code}`
    - **Description:** Add a new currency to the system.
    - **Example:**
      ```bash
      curl -X POST "http://localhost:8080/api/currencies?currencyCode=USD"
      ```

2. **Get All Currencies**
    - **Method:** `GET`
    - **URL:** `/api/currencies`
    - **Description:** Retrieve a list of all available currencies.
    - **Example:**
      ```bash
      curl -X GET "http://localhost:8080/api/currencies"
      ```

---

### **Exchange Rate Endpoints**
1. **Fetch Exchange Rates for a Currency**
    - **Method:** `GET`
    - **URL:** `/api/exchange-rates/{currencyId}`
    - **Description:** Get all exchange rates for a specific currency by its ID.
    - **Example:**
      ```bash
      curl -X GET "http://localhost:8080/api/exchange-rates/{currencyId}"
      ```

---

### **Swagger Documentation**
Access the Swagger UI for detailed API documentation and testing:
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

---

## **Database Configuration**

This project uses **PostgreSQL** as the database and manages the schema using **Liquibase**. The database schema is defined in the Liquibase changelog files.

### **Database Setup**
The application requires a running PostgreSQL instance. Use the provided `docker-compose.yml` file to set up PostgreSQL in a Docker container.

### **Starting PostgreSQL with Docker**
1. Ensure Docker is installed and running.
2. Start the PostgreSQL container:
   ```bash
   docker-compose up -d
The PostgreSQL database will be available at `localhost:5432` with the following credentials:
- **Username:** `user`
- **Password:** `password`
- **Database Name:** `exchange_db`

### **Liquibase Schema Management**
Liquibase ensures that the database schema is up-to-date during application startup. The schema changes are defined in:
- `src/main/resources/db/changelog/changelog-master.xml`

---

## **Scheduled Tasks**

### **Update Exchange Rates**
A scheduled task updates exchange rates for all currencies in the system every **minute**. You can adjust this interval by modifying the `@Scheduled` annotation in the `ExchangeRateService` class.

---

## **Setup and Running the Application**

### **Prerequisites**
- **Java 17**: Required to run the Spring Boot application.
- **Gradle** or **Maven**: For building and managing dependencies.
- **Docker**: To run PostgreSQL in a container.

### **Steps to Run**
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   
2. Build the Project
   To build the project, use the following command:
    ```bash
    ./gradlew build
3. Start the Application
   Once the project is built, you can start the application using the following command:
    ```bash
    ./gradlew bootRun


## Configuration Details

The configuration file contains the following sections:

### API Settings
```yaml
app:
  api:
    url: http://apilayer.net/api/live
    access-key: 0ccac8c3d5d02eb4014c5742aad82cd7
```
- **`url`**: The endpoint for the API. In this example, it points to the `http://apilayer.net/api/live` endpoint. Users can replace this with the desired API URL.
- **`access-key`**: The API access key required for authentication. Replace this with your valid API key.

### Scheduler Settings
```yaml
  scheduler:
    fixed-rate: 3600000 # 60 minutes
#    fixed-rate: 60000 # 60 seconds
```
- **`fixed-rate`**: This defines the interval (in milliseconds) for fetching data from the API.
    - Default value is `3600000`, which corresponds to 60 minutes.
    - Uncomment and modify the value as needed to adjust the interval. For example, `60000` corresponds to 60 seconds.

## Customization

1. **API Customization**:
    - Replace the `url` value with the endpoint of your preferred API.
    - Update the `access-key` with a valid key obtained from your API provider.

2. **Scheduler Customization**:
    - Adjust the `fixed-rate` value to set the frequency of API calls. This is particularly useful for scenarios where you need data updates more or less frequently.
    - Uncomment the alternative `fixed-rate` value for testing purposes (e.g., setting a short interval for rapid updates).

