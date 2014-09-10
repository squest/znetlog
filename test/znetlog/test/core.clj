(ns znetlog.test.core
	(:require [znetlog.logger.core :refer :all]
						[expectations :refer :all]
						[clojure.java.io :as io]
						[com.ashafa.clutch :as cl]
						[znetlog.dbase :refer [make-couch]]))

(def db (make-couch :couch-local
										"config.edn"))

(defn ctypes [] ["video-log" "content-log" "notes-log"])
(defn users [] ["dhika" "dodol" "kalfak"])
(defn statuses [] ["premium" "regular"])
(def crazy 140)
(defn ids [] (range 1001 (+ crazy 1001)))

(def last-couch (->> (cl/get-view db "log" "byId")
										 first
										 :value))























