(ns znetlog.logger.core
	(:require [clojure.java.io :as io]
						[clj-time.local :as tn]
						[znetlog.dbase :as db]
						[com.ashafa.clutch :as cl]))

;; UTILITY FUNCTIONS FOR LOGGING CONTEXT

(def db db/couch)

(defn- now
	"To provide an instance of joda time and return a string type"
	[]
	(-> (tn/local-now)
			(str)
			(subs 0 10)))

(def current-log (atom {}))
(def fdir "resources/log-data/")

(defn- log-file
	"Returns the file-path name for a specific ctype and n as number 1/2
	which designated to which file is currently requested. if option
	supplied by a non-nil value, then instead of returning the log data
	file, it returns the counter-file which counting the number of log
	data in log-file number n."
	[ctype n & option]
	(if option
		(str fdir ctype "-count-" n ".edn")
		(str fdir ctype n ".edn")))

(defn- read-log-counter-file
	"Returns the content of log-counter-file"
	[ctype n]
	(read-string (slurp (log-file ctype n true))))

(defn- write-log-counter-file!
	"Write datum into the file, it will replace the old-content in the file."
	[ctype n datum]
	(spit (log-file ctype n true)
				datum))

(defn- read-log-file
	"Read the whole content of log-file for a certain ctype number n."
	[ctype n]
	(read-string (slurp (log-file ctype n))))

(defn- write-log-file!
	"Write data into log-gile ctype number n. IT WILL REPLACE THE WHOLE FILE CONTENT"
	[ctype n data]
	(spit (log-file ctype n)
				data))

(defn- switch-log!
	"Switch log atom for a ctype with the supplied n value."
	[ctype n]
	(reset! current-log
					(merge @current-log
								 {ctype n})))

(defn- init-log!
	"Initialise the new log ctype. It will assoc a newly created ctype
	to the log atom and creates a necessary log-files and
	log-counter-files for that ctype."
	[ctype]
	(do (switch-log! ctype 1)
			(write-log-file! ctype 1 [])
			(write-log-file! ctype 2 [])
			(write-log-counter-file! ctype 1 0)
			(write-log-counter-file! ctype 2 0)))

(defn- ctype-exists?
	"Returns true if a certain ctype exist and false otherwise"
	[ctype]
	(if (get @current-log ctype) true false))

(defn create!
	"Create a new ctype, it will initialise the ctype. If ctype alredy
	exists, it will log the existing content of files into database, and
	reset the condition as if it's a newly created ctype with the
	difference being the content already in the files would not be lost."
	[ctype]
	(do (if (ctype-exists? ctype)
				(persist! db ctype))
			(init-log! ctype)))

(defn post!
	[form-map]
	nil)




