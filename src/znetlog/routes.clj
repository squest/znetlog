(ns znetlog.routes
	(:require [compojure.core :refer :all]
						[znetlog.layout :as page]
						[znetlog.logger.core :as log]))


(defroutes home
					 (GET "/" req
								(apply str (map  #(str "<br>" %) req))))

(def api
	(context "/api/log/" req
					 (POST "/:ctype" req
								 (log/post! (:ctype (:route-params req))
														(:params req)))
					 (POST "/create-ctype" req
								 (let [{:keys [pass ctype]} (:params req)]
									 (if (= "abcdef1234" pass)
										 (log/create! ctype))))))


