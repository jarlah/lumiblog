(ns blog.models.db
	(:require [clojure.java.jdbc :as sql]))

(defn db-url [host port database]
	(str "//" host ":" port "/" database))

(let [host "localhost" 
	  port 3306 
	  database "blog"
	  user "blog"
	  password "blog"]
	(def db {:classname "com.mysql.jdbc.Driver"
			:subprotocol "mysql"
			:subname (db-url host port database)
			:user user
			:password password}))

(defn create-user [user]
	(sql/with-connection db (sql/insert-record :users user)))

(defn create-comment [comment]
	(sql/with-connection db (sql/insert-record :comments comment)))

(defn get-all-users []
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from users"] (doall res))))

(defn get-user-count []
	(sql/with-connection db
		(sql/with-query-results
			res ["select count(id) as count from users"] (first res))))

(defn get-active-user [id]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from users where id=? and active=1" id] (first res))))

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

(defn get-comments [id]
	(sql/with-connection db
		(sql/with-query-results
			res ["select * from comments where entry=?" id] (doall res))))

(defn create-entry [entry]
	(sql/with-connection db (sql/insert-record :entries entry)))

(defn update-entry [id entry]
	(sql/with-connection db (sql/update-values :entries ["id=?" id] entry)))

(defn delete-entry [id]
	(sql/with-connection db (sql/delete-rows :entries ["id=?" id])))

(defn delete-user [id]
	(sql/with-connection db (sql/delete-rows :users ["id=?" id])))

(defn update-user [user]
	(sql/with-connection db (sql/update-values :users ["id=?" (:id user)] user)))
