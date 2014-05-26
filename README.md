# lumiblog

A blog written with the Luminus web framework. A users table with an auth handler and a entries table with an admin handler. And finally a home handler that displays the latest ten entries. Uses mysql and expects a database blog on localhost with granted access for username blog and password blog. The blog tables can be created by running the blog.sql script.

To enable logging in, you can either uncomment the register routes in auth.clj or go into lein repl and type the following command:

(noir.util.crypt/encrypt "yourpassword")

Copy the result of this and paste it into a new row in users along with userid and name.

TODOS:

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
