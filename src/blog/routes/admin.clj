(ns blog.routes.admin
  (:require [compojure.core :refer :all]
            [blog.views.layout :as layout]
            [noir.session :as session]
            [blog.models.db :as db]
            [noir.util.route :refer [restricted]]
            [hiccup.form :refer :all]
            [noir.response :as resp]
            [noir.validation :as vali]))

(defn error-item [[error]]
	[:div.error error])

(defn display-add-entry [& [title content]]
  (layout/render "add-entry.html" {:title title :content content :errors (vali/get-errors (:title :content))}))

(defn valid-entry? [title content]
	(vali/rule (vali/has-value? title)
		[:title "title is required"])
  (vali/rule (vali/has-value? content)
		[:content "content is required"])
	(vali/rule (vali/min-length? title 5)
		[:title "title must be at least 5 characters"])
	(vali/rule (vali/min-length? content 5)
		[:content "content must be at least 5 characters"])
	(not (vali/errors? :title :content)))

(defn handle-add-entry [title content]
  (if (valid-entry? title content)
    (do
      (db/create-entry {:title title :content content :authorid (session/get :user)})
      (resp/redirect "/"))
    (display-add-entry title content)))

(defroutes admin-routes
  (GET  "/add-entry" [] (restricted (display-add-entry)))
  (POST  "/add-entry" [title content] (restricted (handle-add-entry title content))))
