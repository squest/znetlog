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
                           (:form-params req)))
           (GET "/create-ctype/:ctype" [ctype]
                (log/create! ctype))))


