(ns callcongress.sunlight
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [callcongress.utils :as u]
            )
  )

(def api-url "http://congress.api.sunlightfoundation.com/upcoming_bills?order=legislative_day")

;;; better
(def api-url "http://congress.api.sunlightfoundation.com/bills?order=last_action_at")

(defn get-upcoming []
  (let [resp (client/get api-url
                         {})]
    (when-not (= 200 (:status resp))
      (throw (Error. resp)))
    (:results (json/read-str (:body resp) :key-fn keyword))))

;;; Stopgap for proof of concept
(defn get-some-text []
  (:context (first (get-upcoming))))

(defn bill-text [bill]
  (:short_title bill))

(defn bill-id [bill]
  (:bill_id bill))

(defn get-bill [id]
  (u/cl-find id (get-upcoming) :key bill-id))

(defn next-bill [id]
  (let [raw (get-upcoming)]
    (if (nil? id)
      (first raw)
      (loop [rst raw]
        (if (= (bill-id (first rst)) id)
          (second rst)
          (recur (rest rst)))))))

