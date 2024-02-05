# Introduction

# SQL Quries

###### Table Setup (DDL)

-- Creates 'members' table
CREATE TABLE IF NOT EXISTS cd.members (
    memid INTEGER NOT NULL,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    "address" VARCHAR(300) NOT NULL,
    zipcode VARCHAR NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    recommendedby INTEGER,
    joindate timestamp NOT NULL,
    PRIMARY KEY (memid),
    CONSTRAINT fk_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid)
)

-- Creates 'bookings'
CREATE TABLE IF NOT EXISTS cd.bookings  (
    facid INTEGER NOT NULL,
    memid INTEGER NOT NULL,
    starttime timestamp NOT NULL,
    slots INTEGER NOT NULL,
    PRIMARY KEY (facid),
    CONSTRAINT fk_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
)

-- Creates 'facilities'
CREATE TABLE IF NOT EXISTS cd.facilities  (
    facid INTEGER NOT NULL,
    "name" VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initialoutlay NUMERIC NOT NULL,
    monthlymaintenance NUMERIC NOT NULL,
    PRIMARY KEY (facid)
)

###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members
```

###### Questions 2: Lorem ipsum...

```sql
SELECT blah blah 
```
