(ns znetlog.dbase
	(:require [cemerick.url :as curl]
						[com.ashafa.clutch :as cl]
						[com.stuartsierra.component :as component]
						[clojure.java.io :as io]))

(defrecord CouchDB [whichdb dbconf]
	component/Lifecycle
	(start [this]
		(let [dbconfig (-> (slurp dbconf)
											 (read-string)
											 (get whichdb))
					couch (assoc (curl/url (:url dbconfig) (:dbname dbconfig))
									:username (:username dbconfig)
									:password (:password dbconfig))]
			(assoc this
				:db couch)))

	(stop [this]))

(defn make-couch [whichdb dbconf]
	(->CouchDB whichdb dbconf))

(defn persist!
	"Persist whatever in fname into whichdb which is an instance of CouchDB"
	[whichdb fname]
	(let [{db :db} (component/start whichdb)
				data (if (.exists (io/as-file fname))
							 (map #(assoc % :class "log")
										(read-string (slurp fname)))
							 nil)]
		(cl/bulk-update db data)))
