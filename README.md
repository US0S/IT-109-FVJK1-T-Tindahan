# Campus Event Booking & Ticketing

Full-stack implementation of Project Idea 4.

## Technology Stack

| Layer         | Technology                                        |
| ------------- | ------------------------------------------------- |
| Frontend      | Nuxt 3, TypeScript, Tailwind CSS                  |
| Backend       | Spring Boot (Java)                                |
| Database      | pgAdmin                                           |
| Security      | Spring Security, JWT HttpOnly cookie, BCrypt      |
| UI components | Shadcn-Vue-compatible Vue components and Tailwind |

## Structure

```text
frontend/  Nuxt 3 frontend
backend/   Spring Boot REST API
```

## Roles

- Attendee: browse published events and purchase/view tickets.
- Event Organizer: create events, manage ticket tiers/capacity, and verify tickets.
- Super Admin: approve/reject events and view sales reports.

## Core Modules

- Attendee profile and account management
- Campus event creation, approval, publication, editing, and cancellation
- Ticket tiers with capacity and price in Philippine centavos
- Ticket order creation and payment status
- Unique ticket UUID generation
- QR ticket verification and one-time `USED` status
- Sales and revenue summaries

## PostgreSQL Setup

Create this database in pgAdmin:

```text
Campus_Event_Booking_&_Ticketing
```

The Spring configuration is at `backend/src/main/resources/application.properties`.
Local credentials are development values only. Do not commit production secrets.

## Backend Setup

Requirements:

- Java 21 or newer
- Maven
- PostgreSQL 18 or compatible

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The backend runs at `http://127.0.0.1:3002/api`.

## Frontend Setup

```powershell
cd frontend
npm install
npm run dev
```

The frontend runs at `http://127.0.0.1:3000`.

Nuxt proxies `/api/**` to the Spring Boot backend.

## Seed Accounts

| Nickname          | Email                    | Password        | Role            |
| ----------------- | ------------------------ | --------------- | --------------- |
| `super_admin`     | `super.admin@campus.edu` | `CampusAdmin1!` | Super Admin     |
| `event_organizer` | `organizer@campus.edu`   | `Organizer1!`   | Event Organizer |
| `campus_attendee` | `attendee@campus.edu`    | `Attendee1!`    | Attendee        |

Login uses email. Username/nickname is displayed after sign-in.

Change seeded passwords before deployment.

## Validation

Username:

```text
^[A-Za-z0-9_]{3,20}$
```

Password:

```text
^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&#]{8,64}$
```

## Main API Routes

| Method | Route                              | Access                   |
| ------ | ---------------------------------- | ------------------------ |
| POST   | `/api/auth/register`               | Public                   |
| POST   | `/api/auth/login`                  | Public                   |
| POST   | `/api/auth/logout`                 | Authenticated            |
| GET    | `/api/me`                          | Authenticated            |
| GET    | `/api/campus-events/published`     | Public                   |
| POST   | `/api/campus-events/:id/approve`   | Super Admin              |
| POST   | `/api/campus-events/:id/reject`    | Super Admin              |
| GET    | `/api/campus-events/reports/sales` | Organizer or Super Admin |
| POST   | `/api/ticket-orders/verify`        | Organizer or Super Admin |

## Security

- Role checks run on Spring Security backend endpoints.
- Passwords are BCrypt hashed.
- JWT is stored in an HttpOnly session cookie.
- PostgreSQL queries use JPA repositories.
- Frontend input is validated before requests and backend DTOs validate again.

# IT-109
