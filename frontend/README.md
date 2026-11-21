# Campus Event Booking & Ticketing Frontend

The frontend is built with Nuxt 3 and TypeScript. Tailwind CSS provides the responsive interface styling.

## Requirements

- Node.js 20 or newer
- npm
- Spring Boot backend running at `http://127.0.0.1:3002`

## Setup

```powershell
npm install
npm run dev
```

Open:

`http://127.0.0.1:3000`

## Backend Connection

The frontend proxies API requests from `/api/**` to:

`http://127.0.0.1:3002/api/**`

The proxy is configured in `nuxt.config.ts`.

Start the backend from the `backend/` folder:

```powershell
.\mvnw.cmd spring-boot:run
```

## Pages

| Page         | Purpose                                               |
| ------------ | ----------------------------------------------------- |
| `/login`     | Sign in with email and password                         |
| `/register`  | Register with nickname, email, password, and role       |
| `/dashboard` | Role-based dashboard                                  |
| `/events`    | Published event listings                              |
| `/organizer` | Event Organizer workspace and ticket verification     |
| `/admin`     | Super Admin event approval and sales report workspace |

## Project Roles

- Attendee: browse published events and view/purchase ticket options.
- Event Organizer: create/manage events, manage ticket tiers, and verify ticket UUIDs.
- Super Admin: approve or reject events and view sales reports.

Public registration allows Attendee and Event Organizer roles. Super Admin registration is disabled.

Login uses email. The nickname is stored separately and shown on the dashboard.

## Input Validation

Frontend forms use the following username and password rules.

Username:

```text
^[A-Za-z0-9_]{3,20}$
```

Password:

```text
^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&#]{8,64}$
```

Example valid password: `Lolipopmania12`

## Build

```powershell
npm run build
```
