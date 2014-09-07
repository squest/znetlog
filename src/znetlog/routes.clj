(ns znetlog.routes
  (:require [compojure.core :refer :all]
            [znetlog.layout :as page]
						[znetlog.logger.logger :as log]))

(defroutes home
  (GET "/" req
       "There's nothing to see here"))

(def api
	(context "/api/logger/" req
					 (GET "/video/:id/:username/:status"
								[id username status]
								(log/video id username status))))

