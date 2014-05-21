(ns blog.models.schema
	(:require
		[blog.models.db :refer :all]
		[clojure.java.jdbc :as sql]))

(defn create-users-table []
	(sql/with-connection
		db
		(sql/create-table
			:users
			[:id "TEXT PRIMARY KEY"]
      [:name "TEXT"]
			[:pass "TEXT"])))

(defn create-entries-table []
	(sql/with-connection
		db
		(sql/create-table
			:entries
			[:id "serial"]
			[:title "TEXT"]
      [:content "TEXT"]
      [:createdDate "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
      [:publishedDate "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
      [:authorid "TEXT"])))
