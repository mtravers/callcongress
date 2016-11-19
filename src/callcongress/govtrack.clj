(ns callcongress.govtrack
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]
            )
  )

(def api-url "https://www.govtrack.us/api/v2/bill?order_by=-current_status_date")

;;; Fails due to some SSH problem
(defn get-bills []
  (let [resp (client/get api-url
                         {})]
    (when-not (= 200 (:status resp))
      (throw (Error. resp)))
    (json/read-str (:body resp) :key-fn keyword)))

