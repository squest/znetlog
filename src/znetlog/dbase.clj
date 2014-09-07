(ns znetlog.dbase
  (:require [cemerick.url :as curl]
            [com.ashafa.clutch :as cl]))

(defn get-couch
  [whichdb]
  (-> (slurp "config.edn")
      (read-string)
      (get whichdb)))

(defn current-couch
  []
  (get-couch :cloudant-development))

(def couch (assoc (curl/url (:url (current-couch)) (:dbname (current-couch)))
             :username (:username (current-couch))
             :password (:password (current-couch))))

(def quest "znet-logger")



