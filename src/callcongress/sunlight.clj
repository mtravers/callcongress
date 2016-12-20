(ns callcongress.sunlight
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [callcongress.utils :as u]
            )
  )

;;; TODO replaced by ProPublica? https://propublica.github.io/congress-api-docs
;;; TODO for some reason :query-params isn't working

(def api-base "http://congress.api.sunlightfoundation.com")

(defn get-upcoming []
  (let [resp (client/get (str api-base "/bills?history.active=true&order=last_action_at")
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

;;; Distrcts
;;; Can return >1

(defn get-districts [zip]
  (let [resp (client/get (str api-base "/districts/locate?zip=" zip))]
    (when-not (= 200 (:status resp))
      (throw (Error. resp)))
    (:results (json/read-str (:body resp) :key-fn keyword))))

(defn get-legislators [zip]
  (let [resp (client/get (str api-base "/legislators/locate?zip=" zip))]
    (when-not (= 200 (:status resp))
      (throw (Error. resp)))
    (:results (json/read-str (:body resp) :key-fn keyword))))

;;; TODO not always right, a zip can have >1 district so >1 rep.
(defn get-congressman [zip]
  (first
   (filter #(= (:chamber %) "house")
           (get-legislators zip))))


(defn say-legislator [legislator]
  (str
   (or (:nickname legislator)
       (:first_name legislator))
   " "
   (:last_name legislator)))
