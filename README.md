# Spring Boot Development - Book store

This repository contains codes for the Spring Boot Development course's
labs. The lab codes are in branches (not ideal, but hopefully easiest).
Most of the branches are solutions for the labs, but some may have a branch
that contains a project in a state that has some of the routines for lab
setup implemented.

The labs
	
1. CRUD Books
1. Error handling / Books
1. REST client for Datamuse / Books
1. Testing the REST
1. Book repository (JPA)
1. Book store (relationships)
1. Transactions



# Database setup

If you are using PostgreSQL, there are a few things that may help in getting started:
- Note that unless the PostgreSQL command line utilities are in the path (in Windows they typically
are not) you need to have a way to find them, so the examples here are run in Postgres' `bin` directory.
- Create the database before running the labs. Assuming the connection is to a DB named `booklabs`,
and you are using the default `postgres` user, then you can create the database:

```
	C:\Program Files\PostgreSQL\12\bin>createdb -p 5432 -U postgres booklabs
	Password:
	
	C:\Program Files\PostgreSQL\12\bin>	
```
- Accessing the database from command line is easiest with the `psql` command

```
	C:\Program Files\PostgreSQL\12\bin>psql -U postgres booklabs
	Password for user postgres:
	psql (12.9)
	Type "help" for help.
	
	booklabs=# \dt
	          List of relations
	 Schema |  Name   | Type  |  Owner   
	--------+---------+-------+----------
	 public | book | table | postgres
	(1 row)
	
	booklabs=# select count(*) from book;
	 count 
	-------
	     4
	(1 row)

	booklabs=# \q
	
	C:\Program Files\PostgreSQL\12\bin>		
```

### H2 console

If you are using the H2 as the database there is no need to create the database,
and you can just let Hibernate create the table(s) for you.

When the code is running you can access H2 console at http://localhost:8080/h2-console

```
  h2:
    console:
        enabled: true
```
