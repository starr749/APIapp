# Dabbling with Scala

Basic Scala Server with a basic API

The goal of this app is to show come competency with PatternMatching and List Comprehensions

Also built with the idea that using a framework like play or spray would be cheating :P

## Now returning JSON

So it seems that spray wins the easiest way to serialize nested data type award.
Now data is returned using spray's toJson.prettyprint method.

## What the data looks like

The Data format looks like this:
```scala
Map[String, List[(String, Double)]]
```

So a data would look like:
"Company Name" -> ( ("UnitOfTime", Double), ("AnotherUnitOfTime", Double) )

The Idea was to pipe this date into a D3.js Graph, since they like data formatted like this.

The Idea is that this is a Weekday Ticker of some value, maybe profit, maybe expense. The 'UnitOfTime' being repeated.
This works best for a day of the week or a month.

So returning, for exameple, an average of every "June" expense would look interesting on a graph.

I chose such a complex data type because I feel like it best represents something that could be retrieved quickly from say, a Redis instance.

## API Calls

Current API Calls
/all    (Returns all of the Data)
/sum    (Returns the Sum of all of each "Company")
/avg    (Returns the Average of all of each "Company")
/groupavg (Returns the Average of each "Company"'s "Unit Of Time")