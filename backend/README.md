# Spring Boot Backend

The backend follows Project Idea 4: Campus Event Booking & Ticketing.

## Database

PostgreSQL database:

`Campus_Event_Booking_&_Ticketing`

Configuration:

`src/main/resources/application.properties`

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

If port `3002` is already in use, stop the existing Spring process:

```powershell
Get-NetTCPConnection -LocalPort 3002 -State Listen
Stop-Process -Id <OwningProcess> -Force
.\mvnw.cmd spring-boot:run
```

API base:

`http://127.0.0.1:3002/api`

## Project Packages

- `model`: JPA entities and enums
- `repository`: Spring Data JPA repositories
- `security`: JWT and Spring Security configuration
- `controller`: REST API endpoints
- `dto`: authentication validation records
