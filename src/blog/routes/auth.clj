(ns blog.routes.auth
	(:require [hiccup.form :refer :all]
			  [compojure.core :refer :all]
			  [blog.routes.home :refer :all]
			  [blog.views.layout :as layout]
			  [noir.session :as session]
			  [noir.response :as resp]
			  [noir.validation :as vali]
			  [noir.util.crypt :as crypt]
			  [blog.models.db :as db])
	(:import java.sql.SQLException
			 java.io.File))

(defn valid? [id name pass pass1]
	(vali/rule (vali/has-value? id)
		[:id "user id is required"])
  (vali/rule (vali/has-value? name)
		[:name "name is required"])
	(vali/rule (vali/min-length? pass 5)
		[:pass "password must be at least 5 characters"])
	(vali/rule (= pass pass1)
		[:pass "entered passwords do not match"])
	(not (vali/errors? :id :name :pass :pass1)))

(defn registration-page [& [id]]
	(layout/render "register.html" {:id id :errors (vali/get-errors (:id :pass))}))

(defn login-page [& [id]]
	(layout/render "login.html"))

(defn format-error [id ex]
	(cond
		(and (instance? java.sql.SQLException ex) (= 0 (.getErrorCode ex)))
			(str "The user with id " id " already exists!")
		:else
			"An error has occured while processing the request"))

(defn get-active-flag []
  (if (= 0 (:count (db/get-user-count))) 1 0))

(defn get-level-flag []
  (if (= 0 (:count (db/get-user-count))) 0 1))

(defn handle-registration [id name pass pass1]
	(if (valid? id name pass pass1)
		(try
			(db/create-user {
        :id id
        :name name
        :pass (crypt/encrypt pass)
        :active (get-active-flag)
        :level (get-level-flag)})
			(resp/redirect "/login")
			(catch Exception ex
				(vali/rule false [:id (format-error id ex)])
				(registration-page)))
		(registration-page id name)))

(defn handle-login [id pass]
	(let [user (db/get-active-user id)]
		(if (and user (crypt/compare pass (:pass user)))
			(session/put! :user user))
	(resp/redirect "/")))

(defn handle-logout []
	(session/clear!)
	(resp/redirect "/"))

(defroutes auth-routes
	(GET "/register" []
		(registration-page))
  (POST "/register" [id name pass pass1]
		(handle-registration id name pass pass1))
  (GET "/login" []
    (login-page))
	(POST "/login" [id pass]
		(handle-login id pass))
	(GET "/logout" []
		(handle-logout)))
