# test-app

A blog written with the Luminus web framework. A users table with an auth handler and a entries table with an admin handler. And finally a home handler that displays the latest ten entries. Uses Postgresql and expects a database blog on localhost with username admin and password admin. The blog will automatically try to create the table when it starts up.

Possible areas for extension:

1. Create an individual administration page
   * Listing entries
   * Editing entries
   * Deleting entries
   * Updating entries
2. Paginating the entries on the front page
   * Enough with forward and back button
3. Archive page or widget
   * Browse per category, year, month or tags
4. Search page (that you land on when you search)
   * The home page should have a search field somewhere
5. Comment functionality on entries
   * Moderation in the administration page for accepting, deleting, replying to and updating comments

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2014 Jarl André Hübenthal
