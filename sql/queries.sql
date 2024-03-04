-- Modifying Data
-- https://pgexercises.com/questions/updates/insert.html
INSERT INTO
    cd.facilities
VALUES
    (9, "Spa", 20, 30, 100000, 800);

-- https://pgexercises.com/questions/updates/insert3.html
INSERT INTO
    cd.facilities (
        facid,
        name,
        membercost,
        guestcost,
        initialoutlay,
        monthlymaintenance
    )
VALUES
    (
        (
            SELECT
                (MAX(facid) + 1)
            FROM
                cd.facilities
        ),
        'Spa',
        20,
        30,
        100000,
        800
    );

-- https://pgexercises.com/questions/updates/update.html
UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
    facid = 1;

-- https://pgexercises.com/questions/updates/updatecalculated.html
UPDATE
    cd.facilities
SET
    membercost = (
        SELECT
            (membercost * 1.1)
        FROM
            cd.facilities
        WHERE
            facid = 1
    ),
    guestcost = (
        SELECT
            (guestcost * 1.1)
        FROM
            cd.facilities
        WHERE
            facid = 1
    )
WHERE
    facid = 1;

-- https://pgexercises.com/questions/updates/delete.html
DELETE FROM
    cd.bookings;

-- https://pgexercises.com/questions/updates/deletewh.html
DELETE FROM
    cd.members
WHERE
    memid = 37;

-- Basics
-- https://pgexercises.com/questions/basic/where2.html
SELECT
    facid,
    "name",
    membercost,
    monthlymaintenance
FROM
    cd.facilities
WHERE
    membercost < (monthlymaintenance / 50)
    AND membercost > 0;

-- https://pgexercises.com/questions/basic/where3.html
SELECT
    *
FROM
    cd.facilities
WHERE
    "name" LIKE '%Tennis%';

-- https://pgexercises.com/questions/basic/where4.html
SELECT
    *
FROM
    cd.facilities
WHERE
    facid IN (1, 5);

-- https://pgexercises.com/questions/basic/date.html
SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-1';

-- https://pgexercises.com/questions/basic/union.html
SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities;

-- https://pgexercises.com/questions/joins/simplejoin.html
-- Both work
-- Option 1 using Subquery
SELECT
    starttime
FROM
    cd.bookings
WHERE
    (
        SELECT
            memid
        FROM
            cd.members
        WHERE
            firstname LIKE 'David'
            AND surname LIKE 'Farrell'
    ) = memid;

-- Option 2 using Join
SELECT
    starttime
FROM
    cd.bookings
    JOIN cd.members ON cd.members.memid = cd.bookings.memid
WHERE
    firstname LIKE 'David'
    AND surname LIKE 'Farrell';

-- https://pgexercises.com/questions/joins/simplejoin2.html
SELECT
    starttime,
    name
FROM
    cd.bookings
    JOIN cd.facilities ON cd.facilities.facid = cd.bookings.facid
WHERE
    "name" LIKE '%Tennis Court%'
    AND starttime >= date '2012-09-21'
    AND starttime < date '2012-09-22'
ORDER BY
    starttime;

-- https://pgexercises.com/questions/joins/self2.html
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

-- https://pgexercises.com/questions/joins/sub.html
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

-- https://pgexercises.com/questions/aggregates/count3.html
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

-- https://pgexercises.com/questions/aggregates/fachours.html
SELECT
    facid,
    SUM(slots) as total_slots
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid;

-- https://pgexercises.com/questions/aggregates/fachoursbymonth.html
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
    total_slots;

-- https://pgexercises.com/questions/aggregates/fachoursbymonth2.html
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
ORDER BY
    facid,
    "month";

-- https://pgexercises.com/questions/aggregates/members1.html
SELECT
    COUNT(distinct memid)
FROM
    cd.bookings;

-- https://pgexercises.com/questions/aggregates/nbooking.html
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
    members.memid;

-- https://pgexercises.com/questions/aggregates/countmembers.html
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
    joindate;

-- https://pgexercises.com/questions/aggregates/nummembers.html
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
    joindate;

-- https://pgexercises.com/questions/aggregates/fachours4.html
select
    facid,
    total
from
    (
        select
            facid,
            sum(slots) total,
            rank() over (
                order by
                    sum(slots) desc
            ) rank
        from
            cd.bookings
        group by
            facid
    ) as ranked
where
    rank = 1;

-- Format the names of members
SELECT
    CONCAT(surname, ', ', firstname) as name
FROM
    cd.members;

-- Find telephone numbers with parentheses
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone ~ '[()]';

-- Questions 28: Count the number of members whose surname starts with each letter of the alphabet
SELECT
    substring(surname, 1, 1) as letter,
    COUNT(*)
FROM
    cd.members
GROUP BY
    letter
ORDER BY
    letter;
