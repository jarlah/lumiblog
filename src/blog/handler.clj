(ns blog.handler
  (:require [compojure.core :refer [defroutes]]
            [blog.routes.home :refer [home-routes]]
            [blog.routes.auth :refer [auth-routes]]
            [blog.routes.admin :refer [admin-routes]]
            [blog.middleware :as middleware]
            [blog.models.schema :as schema]
            [blog.models.db :as db]
            [noir.util.middleware :refer [app-handler]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [selmer.parser :as parser]
            [noir.session :as session]
            [environ.core :refer [env]]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/appender-fn})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "blog.log" :max-size (* 512 1024) :backlog 10})

  (try
    (schema/create-users-table)
    ;; encrypt a password with noir.util.crypt/encrypt
    ;; (db/create-user {:id "username" :name "fullname" :pass "encryptedpass"})
    (catch Exception ex
      (timbre/info "Tables users exists already" ex)))

  (try
    (schema/create-entries-table)
    (catch Exception ex
      (timbre/info "Table entries exists already" ex)))

  (if (env :dev) (parser/cache-off!))
  (timbre/info "blog started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "blog is shutting down..."))

(defn user-page [_]
  (session/get :user))

(def app (app-handler
           ;; add your application routes here
           [admin-routes auth-routes home-routes app-routes]
           ;; add custom middleware here
           :middleware [middleware/template-error-page
                        middleware/log-request]
           ;; add access rules here
           :access-rules [user-page]
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
