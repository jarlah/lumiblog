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

(defn valid? [id pass pass1]
	(vali/rule (vali/has-value? id)
		[:id "user id is required"])
	(vali/rule (vali/min-length? pass 5)
		[:pass "password must be at least 5 characters"])
	(vali/rule (= pass pass1)
		[:pass "entered passwords do not match"])
	(not (vali/errors? :id :pass :pass1)))

(defn error-item [[error]]
	[:div.error error])

(defn control [id label field]
	(list
		(vali/on-error id error-item)
		label field
		[:br]))

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

(defn tull []
  (crypt/encrypt "idi0t200"))

(defn handle-registration [id pass pass1]
	(if (valid? id pass pass1)
		(try
			(db/create-user {:id id :pass (crypt/encrypt pass)})
			(session/put! :user id)
			(resp/redirect "/")
			(catch Exception ex
				(vali/rule false [:id (format-error id ex)])
				(registration-page)))
		(registration-page id)))

(defn handle-login [id pass]
	(let [user (db/get-user id)]
		(if (and user (crypt/compare pass (:pass user)))
			(session/put! :user (:name user)))
	(resp/redirect "/")))

(defn handle-logout []
	(session/clear!)
	(resp/redirect "/"))

(defroutes auth-routes
  ;; enable these if you want to open the doors...
	;;(GET "/register" []
	;;	(registration-page))
	;;(POST "/register" [id pass pass1]
	;;	(handle-registration id pass pass1))
  (GET "/login" []
    (login-page))
	(POST "/login" [id pass]
		(handle-login id pass))
	(GET "/logout" []
		(handle-logout)))
