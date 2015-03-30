# Dabbling with Scala

Basic Scala Server with a basic API

The goal of this app is to show come competency with PatternMatching and List Comprehensions

## Not completely working

So I did not realize how hard it was to Serialize complex datatypes into JSON in scala, so I am just returning my data in a basic string

## What the data looks like

The Data format looks like this:
```scala
Map[String, List[(String, Double)]]
```

So a data would look like:
"Company Name" -> ( ("UnitOfTime", Double), ("AnotherUnitOfTime", Double) )

The Idea was to pipe this date into a D3.js Graph, since they like data formatted like this.
Because I couldn't figure out JSON serialization in time I did not get that far, however.

The Idea is that this is a Weekday Ticker of some value, maybe profit, maybe expense. The 'UnitOfTime' being repeated.
This works best for a day of the week or a month.

So returning, for exameple, an average of every "June" expense would look interesting on a graph.\

I chose such a complex data type because I feel like it best represents something that could be retrieved quickly from say, a Redis instance.

I am so used to Python I did not realize the pain that it takes to Serialize nested data structures.

## API Calls

Current API Calls
/all    (Returns all of the Data)
/sum    (Returns the Sum of all of each "Company")
/avg    (Returns the Average of all of each "Company")
/groupavg (Returns the Average of each "Company"'s "Unit Of Time")