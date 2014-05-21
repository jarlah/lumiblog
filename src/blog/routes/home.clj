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

(defroutes home-routes
  (GET "" [] (home-page))
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
