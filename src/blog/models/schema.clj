(ns blog.models.schema
	(:require
		[blog.models.db :refer :all]
		[clojure.java.jdbc :as sql]))

(defn create-users-table []
	(sql/with-connection
		db
		(sql/create-table
			:users
			[:id "VARCHAR(255)"]
      [:name "VARCHAR(255)"]
			[:pass "VARCHAR(255)"])))

(defn create-entries-table []
	(sql/with-connection
		db
		(sql/create-table
			:entries
			[:id "INT PRIMARY KEY AUTO_INCREMENT"]
			[:title "VARCHAR(255)"]
      [:content "LONGTEXT"]
      [:createdDate "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
      [:publishedDate "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
      [:authorid "VARCHAR(255)"])))
