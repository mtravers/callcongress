(ns callcongress.sunlight
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]
            )
  )

(def api-url "http://congress.api.sunlightfoundation.com/upcoming_bills?order=legislative_day")

(defn get-upcoming []
  (let [resp (client/get api-url
                         {})]
    (when-not (= 200 (:status resp))
      (throw (Error. resp)))
    (:results (json/read-str (:body resp) :key-fn keyword))))

;;; Stopgap for proof of concept
(defn get-some-text []
  (:context (first (get-upcoming))))
