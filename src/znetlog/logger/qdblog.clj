(ns znetlog.logger.qdblog
	(:require [questdb.core :refer [put-docs! put-doc! uuids get-doc]]
						[znetlog.dbase :refer [make-couch]]
						[com.stuartsierra.component :as com]
						[com.ashafa.clutch :as cl]))

(defrecord ContentLog [dbname max-log-size]
	com/Lifecycle

	(start [this]
		(assoc this :db dbname
								:counter (atom 0)
								:max-log-size max-log-size))

	(stop [this]))

(defn make-log [dbname max-log-size]
	(->ContentLog dbname
								max-log-size))

(defonce db (make-couch :couch-local
										"config.edn"))

(defonce log-state (com/start (make-log "logdb" 1000)))

(declare add-post! persist!)

(defn post!
	[log-state form-map]
	(let [datum (merge form-map
										 {:type ctype :class "log"})
				{:keys [counter max-log-size]} log-state]
		(do (if (>= @counter max-log-size)
					(do (reset! counter 0)
							(persist! log-state db)))
				(add-post! counter datum))))



