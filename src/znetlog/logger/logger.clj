(ns znetlog.logger.logger
	(:require [clojure.java.io :as io]
						[clj-time.local :as tn]
						[znetlog.dbase :as db]
						[com.ashafa.clutch :as cl]
						[com.stuartsierra.component :as component]))

(def lbd (db/make-couch :couch-local
												"config.edn"))

(def db (:db (component/start ldb)))

(defn- now
	"To provide an instance of joda time and return a string type"
	[]
	(-> (tn/local-now)
			(str)
			(subs 0 10)))

(def which-video? (atom 0))

(defn video-file
	"Returns the string path to the video logging file"
	[n]
	(str "resources/logdata/video" n ".edn"))

(defn video-meta-file
	"File name for the video meta data"
	[n]
	(str "resources/logdata/video-log-id" n ".edn"))

(defn- update-video-meta
	"Update the meta data for the video logging, usually happens after scheduler done"
	[n]
	(let [old (read-string (slurp (video-meta-file n)))]
		(if (>= old 1000)
			(do (spit (video-meta-file n) 0)
					(spit (video-meta-file (- 3 n)) 1))
			(spit (video-meta-file n) (inc old)))))

(defn- log-video
	"Add the datum to the video-file logging"
	[n datum]
	(let [data (read-string (slurp (video-file n)))
				final (conj data datum)]
		(spit (video-file n)
					final)))

(defn- init-log
	"Initialise the files"
	[]
	(do (spit (video-file 1) [])
			(spit (video-file 2) [])
			(spit (video-meta-file 1) 0)
			(spit (video-meta-file 2) 0)
			(reset! which-video? 1)))

(declare persist!)

(defn video
	"This is the logging process for each video"
	[id username status]
	(do (when (zero? @which-video?)
				(init-log))
			(let [datum {:type     "video-log"
									 :video-id (read-string id)
									 :user     username
									 :status   status
									 :time     (now)}
						count-video (read-string (slurp (video-meta-file @which-video?)))]
				(do (update-video-meta @which-video?)
						(when (>= count-video 1000)
							(let [cur @which-video?]
								(do (reset! which-video?
														(- 3 @which-video?))
										(persist! db (video-file cur)))))
						(log-video @which-video? datum)))))

(defn persist!
	"The process of logging into the central database, and reset the current file"
	[db fname]
	(let [data (read-string (slurp fname))]
		(do (cl/bulk-update db data)
				(spit fname []))))



 
