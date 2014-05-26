(ns blog.models.db
	(:require [clojure.java.jdbc :as sql]))

(let [db-host "localhost"
      db-port 3306
      db-name "blog"]

  (def db {:classname "com.mysql.jdbc.Driver" ; must be in classpath
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user "root"
           :password ""}))

(defn create-user [user]
	(sql/with-connection db (sql/insert-record :users user)))

(defn get-user [id]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from users where id=?" id] (first res))))

(defn get-latest-entries [max]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from entries order by publishedDate desc limit ?" max] (doall res))))

(defn get-entry [id]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from entries where id=?" id] (first res))))

(defn create-entry [entry]
	(sql/with-connection db (sql/insert-record :entries entry)))

(defn update-entry [id entry]
	(sql/with-connection db (sql/update-values :entries ["id=?" id] entry)))

(defn delete-entry [id]
	(sql/with-connection db (sql/delete-rows :entries ["id=?" id])))
