(ns blog.routes.admin
	(:require [compojure.core :refer :all]
			[blog.views.layout :as layout]
			[noir.session :as session]
			[blog.models.db :as db]
			[noir.util.route :refer [restricted]]
			[hiccup.form :refer :all]
			[noir.response :as resp]
			[noir.validation :as vali]))

(defn admin-overview []
	(layout/render "admin.html"))

(defn add-entry [& [title content]]
	(layout/render "edit-entry.html" {:title title :content content :errors (vali/get-errors (:title :content))}))

(defn edit-entry [entry]
	(layout/render "edit-entry.html" (merge entry {:errors (vali/get-errors (:title :content))})))

(defn edit-user [user]
	(layout/render "edit-user.html" user))

(defn confirm-delete-entry [entry]
	(layout/render "confirm-delete-entry.html" entry))

(defn confirm-delete-user [user]
	(layout/render "confirm-delete-user.html" user))

(defn list-entries []
	(layout/render "list-entries.html" {:entries (db/get-latest-entries 100)}))

(defn list-users []
	(layout/render "list-users.html" {:users (db/get-all-users)}))

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
		(db/create-entry {:title title :content content :authorid (:name (session/get :user))})
		(resp/redirect "/admin/entries"))
	(add-entry title content)))

(defn handle-delete-entry [id]
	(db/delete-entry id)
	(resp/redirect "/admin/entries"))

(defn handle-delete-user [id]
	(db/delete-user id)
	(resp/redirect "/admin/users"))

(defn handle-edit-entry [id title content]
	(if (valid-entry? title content)
	(do
		(db/update-entry id {:title title :content content})
		(resp/redirect "/admin/entries"))
	(edit-entry {:title title :content content})))

(defn handle-edit-user [id name active level]
  (db/update-user {:id id :name name :active active :level level})
  (resp/redirect "/admin/users"))

(defroutes admin-routes
	(GET "/admin" []
		 (restricted (admin-overview)))
	(GET "/admin/entries" []
		 (restricted (list-entries)))
	(GET "/admin/entry" []
		 (restricted (add-entry)))
	(POST "/admin/entry" [title content]
		 (restricted (handle-add-entry title content)))
	(GET "/admin/entry/:id" [id]
		 (restricted (edit-entry (db/get-entry id))))
	(POST "/admin/entry/:id" [id title content]
		 (restricted (handle-edit-entry id title content)))
	(GET "/admin/entry/:id/delete" [id]
		 (restricted (confirm-delete-entry (db/get-entry id))))
	(POST "/admin/entry/:id/delete" [id]
		 (restricted (handle-delete-entry id)))
	(GET "/admin/users" []
		 (restricted (list-users)))
  (GET "/admin/user/:id" [id]
		 (restricted (edit-user (db/get-user id))))
  (POST "/admin/user/:id" [id name active level]
		 (restricted (handle-edit-user id name active level)))
	(GET "/admin/user/:id/delete" [id]
		 (restricted (confirm-delete-user (db/get-user id))))
	(POST "/admin/user/:id/delete" [id]
		 (restricted (handle-delete-user id))))
