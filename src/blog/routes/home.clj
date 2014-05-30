(ns blog.routes.home
	(:use compojure.core)
	(:require [blog.views.layout :as layout]
		[blog.models.db :as db]
		[blog.util :as util]
		[noir.session :as session]))

(defn home-page []
	(layout/render "home.html" {:entries (db/get-latest-entries 10)}))

(defn about-page []
	(layout/render "about.html"))

(defn post [id]
  (layout/render "entry.html" (db/get-entry id)))

(defroutes home-routes
	(GET "" []
		(home-page))
	(GET "/" []
		(home-page))
	(GET "/about" []
		(about-page))
  (GET "/post/:id" [id perma]
    (post id)))
