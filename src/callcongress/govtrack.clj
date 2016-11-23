(ns callcongress.govtrack
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]
            )
  )

(def api-url "https://www.govtrack.us/api/v2/bill?order_by=-current_status_date")

(defn get-keystore []
  (let [ks (java.security.KeyStore/getInstance "JKS")
        stream (.getResourceAsStream
                (.getClassLoader CallCongress.SpeechletRequestHandler)
                "cc-keystore.jks")]
    (.load ks stream (char-array "arglebargle"))
    ks))

(defonce keystore (get-keystore))

;;; TODO package this keystore
(defn get-bills []
  (let [resp (client/get api-url
                         {:trust-store keystore})]
    (when-not (= 200 (:status resp))
      (throw (Error. resp)))
    (json/read-str (:body resp) :key-fn keyword)))

