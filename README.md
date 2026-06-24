# CineSeat – Movie Ticket Booking Backend

Cineseat is a **concurrency-safe movie ticket booking backend** built with **Spring Boot, MySQL, and Razorpay**.  
The system prevents **double booking** using **pessimistic database locking** and implements a **seat hold mechanism** that temporarily reserves seats before payment.

This project demonstrates how real booking platforms handle **concurrent seat reservations, payment confirmation, and transactional consistency**.

---

# Features

## Seat Management
- View all available seats
- Real-time seat availability tracking
- Prevents booking already reserved seats

## Seat Hold Mechanism
- Seats are **temporarily held for 6 minutes**
- Prevents other users from booking the same seat
- Expired holds automatically become available again

## Concurrency Control
- Uses **pessimistic locking** to prevent race conditions
- Ensures only one user can hold a seat at a time

## Razorpay Payment Integration
- Creates Razorpay payment orders
- Secure **server-side signature verification**
- Confirms booking only after successful payment

## Transaction Management
- Uses `@Transactional` to ensure atomic operations
- Maintains database consistency during booking

---

# System Architecture

```
User
 │
 │ Select Seats
 ▼
Frontend (React)
 │
 │ POST /seats/hold
 ▼
Spring Boot Backend
 │
 │ Lock Seat (Pessimistic Lock)
 ▼
MySQL Database
 │
 │ Seat Status → HELD
 ▼
Create Razorpay Order
 │
 ▼
Razorpay Payment Gateway
 │
 │ Payment Success
 ▼
POST /payment/confirm
 │
 ▼
Seat Status → BOOKED
```

---

# Tech Stack

## Backend
- **Spring Boot**
- **Spring Data JPA**
- **Java**

## Database
- **MySQL**

## Payment
- **Razorpay API**

## Frontend (Demo UI)
- **React**
- **TypeScript**
- **Tailwind CSS**

---

# Project Structure

```
src/main/java/com/movie/movieticket

controller
 ├── SeatController
 └── PaymentController

service
 ├── BookingService
 └── PaymentService

entity
 ├── Seat
 ├── Hold
 ├── SeatStatus
 └── HoldStatus

repository
 ├── SeatRepository
 └── HoldRepository

dto
 ├── HoldSeatRequest
 ├── UserDetailsDTO
 └── CreateSeatsRequest
```

---

# Seat Booking Workflow

## 1. Select Seats
User selects available seats from the UI.

## 2. Hold Seats

```
POST /seats/hold
```

Backend:
- Locks seats using **pessimistic locking**
- Updates status → `HELD`
- Creates a **hold record with expiration**

---

## 3. Create Payment Order

```
POST /payment/create-order
```

Backend:
- Creates Razorpay order
- Returns order details to frontend

---

## 4. Complete Payment
User completes payment via Razorpay Checkout.

---

## 5. Confirm Booking

```
POST /payment/confirm
```

Backend:
- Verifies Razorpay signature
- Converts seat status from **HELD → BOOKED**

---

# Concurrency Handling

To prevent multiple users from booking the same seat simultaneously, the system uses:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

This ensures that once a seat is being reserved:

- Other transactions **must wait**
- Prevents **double booking**

---

# Seat Hold Expiration

Each seat hold contains:

```
createdAt
expireAt = createdAt + 6 minutes
```

If payment is not completed within 6 minutes:

```
Hold → EXPIRED
Seat → AVAILABLE
```

---

# API Endpoints

## Get All Seats

```
GET /seats
```

---

## Hold Seats

```
POST /seats/hold
```

Request

```json
{
  "seatIds": [1,2],
  "userDetails": {
    "name": "User Name",
    "email": "user@email.com",
    "phone": "9876543210"
  }
}
```

---

## Create Payment Order

```
POST /payment/create-order
```

---

## Confirm Payment

```
POST /payment/confirm
```

---

# Environment Variables

Add Razorpay credentials in `application.properties`.

```
razorpay.key=YOUR_KEY
razorpay.secret=YOUR_SECRET
```

---

# Running the Project

## 1. Clone Repository

```
git clone https://github.com/Darshan9742/CineSeat.git
```

---

## 2. Configure Database

Update `application.properties`

```
spring.datasource.url=jdbc:mysql://localhost:3306/movieticket
spring.datasource.username=root
spring.datasource.password=yourpassword
```

---

## 3. Run Application

```
mvn spring-boot:run
```

Server will start at:

```
http://localhost:8085
```

---

# Future Improvements

Possible enhancements:

- Automatic cleanup of expired seat holds
- Distributed locking using Redis
- Booking history for users
- Show and theatre management
- Microservice architecture
