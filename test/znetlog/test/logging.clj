(ns znetlog.test.logging
  (:require [znetlog.logger.logger :all]
            [expectations :refer :all]
            [clojure.java.io :as io]
            [com.ashafa.clutch :as cl]))

(defn users [] ["dhika" "dodol" "kalfak"])
(defn statuses [] ["premium" "regular"])
(def crazy 10)

(defn vids [] (range 1001 (+ crazy 1001)))

(def last-couch (->> (cl/get-view db "videoLog" "byId")
                     first
                     :value))

(expect nil
        (do (when (.exists (io/as-file (video-file 1)))
              (io/delete-file (video-file 1)))
            (when (.exists (io/as-file (video-file 2)))
              (io/delete-file (video-file 2)))
            (when (.exists (io/as-file (video-meta-file 1)))
              (io/delete-file (video-meta-file 1)))
            (when (.exists (io/as-file (video-meta-file 2)))
              (io/delete-file (video-meta-file 2)))))

(expect 0
        (reset! which-video? 0))

(defn save-all
  [vids users statuses]
  (let [seqs (for [v vids u users s statuses]
               (vector (str v) u s))]
    (doseq [[id user stat] seqs]
      (video id user stat))))

(expect (rem (* crazy  (count (users)) (count (statuses)))
             2000)
        (do (save-all (vids) (users) (statuses))
            (read-string (slurp (video-meta-file 1)))))

(expect (rem (* 2 crazy (count (users)) (count (statuses)))
             2000)
        (do (save-all (vids) (users) (statuses))
            (read-string (slurp (video-meta-file 1)))))

(expect (rem (* 3 crazy (count (users)) (count (statuses)))
             2000)
        (do (save-all (vids) (users) (statuses))
            (read-string (slurp (video-meta-file 1)))))

(expect true
        (do (io/delete-file (video-file 1))
            (io/delete-file (video-file 2))
            (io/delete-file (video-meta-file 1))
            (io/delete-file (video-meta-file 2))))

(expect 0
        (reset! which-video? 0))

(expect (+ last-couch (* 1000
                         (quot (* 3 crazy (count (users)) (count (statuses)))
                               1000)))
        (->> (cl/get-view db "videoLog" "byId")
             first
             :value))




