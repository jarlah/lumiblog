(ns blog.models.db
	(:require [clojure.java.jdbc :as sql]))

(let [db-host "localhost"
      db-port 5432
      db-name "blog"]
  (def db {:classname "org.postgresql.Driver" ; must be in classpath
        :subprotocol "postgresql"
        :subname (str "//" db-host ":" db-port "/" db-name)
        :user "admin"
        :password "admin"}))

(defn create-user [user]
	(sql/with-connection db
		(sql/insert-record :users user)))

(defn get-user [id]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from users where id=?" id] (first res))))

(defn get-latest-entries [max]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from entries order by publishedDate desc limit ?" max] (doall res))))

(defn create-entry [entry]
	(sql/with-connection db
		(sql/insert-record :entries entry)))
