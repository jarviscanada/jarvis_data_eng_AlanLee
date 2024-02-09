# Introduction

This project focuses on gaining hands-on experience with PostgreSQL through a series of practical exercises. The objective is to understand and implement various database operations including CRUD, joins, as well as executing complexy query searches. The setup of the PostgreSQL database environment is facilitated using Docker, providing a streamlined and isolated environment for database management. A dataset named clubdata.sql is imported to simulate real-world data manipulation and querying tasks. Through this project, the aim is to build proficiency in managing databases with PostgreSQL and to apply these skills in data management and analysis scenarios.

# SQL Queries

###### Table Setup (DDL)

```sql
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
```

###### Question 1: Insert some data into a table

```sql
INSERT INTO cd.facilities
VALUES (9, 'Spa', 20, 30, 100000, 800)
```

###### Questions 2: Insert calculated data into a table

```sql
INSERT INTO cd.facilities
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES ((SELECT (MAX(facid) + 1 ) FROM cd.facilities),'Spa', 20, 30, 100000, 800);
```

###### Questions 3: Update some existing data

```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;
```

###### Questions 4: Update a row based on the contents of another row

```sql
UPDATE cd.facilities
SET membercost = (SELECT (membercost * 1.1) FROM cd.facilities WHERE facid = 1),
    guestcost = (SELECT (guestcost * 1.1) FROM cd.facilities WHERE facid = 1)
WHERE facid = 1;
```

###### Questions 5: Delete all bookings

```sql
DELETE FROM cd.bookings;
```

###### Questions 6: Delete a member from the cd.members table

```sql
DELETE FROM cd.members
WHERE memid = 37;
```

###### Questions 7: Control which rows are retrieved - part 2

```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < (monthlymaintenance/50)
AND membercost > 0
```

###### Questions 8: Basic string searches

```sql
SELECT *
FROM cd.facilities
WHERE "name" LIKE '%Tennis%';
```

###### Questions 9: Matching against multiple possible values

```sql
SELECT *
FROM cd.facilities
WHERE
    facid IN (1,5);
```

###### Questions 10: Working with dates

```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-1'
```

###### Questions 11: Combining results from multiple queries

```sql
SELECT surname
FROM cd.members
UNION
SELECT name
FROM cd.facilities;
```

###### Questions 12: Retrieve the start times of members' bookings

```sql
 SELECT
    starttime
FROM
    cd.bookings
    JOIN cd.members ON cd.members.memid = cd.bookings.memid
WHERE
    firstname LIKE 'David'
    AND surname LIKE 'Farrell';
```

###### Questions 13: Work out the start times of bookings for tennis courts

```sql
SELECT
    starttime, name
FROM
    cd.bookings
    JOIN cd.facilities ON cd.facilities.facid = cd.bookings.facid
WHERE
    "name" LIKE '%Tennis Court%'
    AND starttime >= date '2012-09-21'
    AND starttime < date '2012-09-22'
ORDER BY starttime;
```

###### Questions 14: Produce a list of all members, along with their recommender

```sql
SELECT
    main.firstname as fname,
    main.surname as lname,
    other.firstname as rfname,
    other.surname as rlname
FROM
    cd.members as main
    LEFT JOIN cd.members as other ON main.recommendedby = other.memid
ORDER BY
    main.surname,
    main.firstname;
```

###### Questions 15: Produce a list of all members who have recommended another member

```sql
SELECT DISTINCT s.firstname, s.surname
FROM cd.members f
JOIN cd.members s ON f.recommendedby = s.memid
ORDER BY surname, firstname
```

###### Questions 16: Produce a list of all members, along with their recommender, using no joins.

```sql
SELECT
    DISTINCT CONCAT(firstname, ' ', surname) as member,
    (
        SELECT
            CONCAT(firstname, ' ', surname) as recommender
        FROM
            cd.members
        WHERE
            memid = main.recommendedby
    )
FROM
    cd.members as main
ORDER BY
    member;
```

###### Questions 17: Count the number of recommendations each member makes.

```sql
SELECT
    recommendedby,
    COUNT(recommendedby)
FROM
    cd.members
WHERE
    recommendedby IS NOT NULL
GROUP BY
    recommendedby
ORDER BY
    recommendedby;
```

###### Questions 18: List the total slots booked per facility

```sql
SELECT
    facid,
    SUM(slots) as total_slots
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid;
```

###### Questions 19: List the total slots booked per facility in a given month

```sql
SELECT
    facid,
    SUM(slots) as total_slots
FROM
    cd.bookings
WHERE
    starttime >= '2012-09-01'
    AND starttime < '2012-10-01'
GROUP BY
    facid
ORDER BY
    total_slots
```

###### Questions 20: List the total slots booked per facility per month

```sql
 SELECT
    facid,
    EXTRACT(
        MONTH
        FROM
            starttime
    ) as "month",
    SUM(slots) as total_slots
FROM
    cd.bookings
WHERE
    starttime >= '2012-01-01'
    AND starttime < '2013-01-01'
GROUP BY
    facid,
    "month"
    ORDER BY facid, "month"
```

###### Questions 21: Find the count of members who have made at least one booking

```sql
SELECT
    COUNT( distinct memid)
FROM
    cd.bookings
```

###### Questions 22: List each member's first booking after September 1st 2012

```sql
SELECT
    members.surname,
    members.firstname,
    members.memid,
    min(bookings.starttime)
FROM
    cd.members as members
    LEFT JOIN cd.bookings as bookings ON members.memid = bookings.memid
WHERE
    starttime >= '2012-09-01'
GROUP BY
    members.surname,
    members.firstname,
    members.memid
ORDER BY
    members.memid
```

###### Questions 23: Produce a list of member names, with each row containing the total member count

```sql
SELECT
    (
        SELECT
            COUNT(distinct memid)
        FROM
            cd.members
    ),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate
```

###### Questions 24: Produce a numbered list of members

```sql
SELECT
    row_number() OVER (
        ORDER BY
            joindate
    ),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate
```

###### Questions 25: Output the facility id that has the highest number of slots booked, again

```sql
select facid, total from (
	select facid, sum(slots) total, rank() over (order by sum(slots) desc) rank
        	from cd.bookings
		group by facid
	) as ranked
	where rank = 1
```

###### Questions 26: Format the names of members

```sql
SELECT
    CONCAT(surname, ', ', firstname) as name
FROM
    cd.members;
```

###### Questions 27: Find telephone numbers with parentheses

```sql
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone ~ '[()]';
```

###### Questions 28: Count the number of members whose surname starts with each letter of the alphabet

```sql
SELECT
    substring(surname, 1, 1) as letter,
    COUNT(*)
FROM
    cd.members
GROUP BY
    letter
ORDER BY
    letter;
```

