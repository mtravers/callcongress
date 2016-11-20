(ns callcongress.dynfar
  (:require [taoensso.faraday :as far]))

;;; keys


(def client-opts
  {;;; For DDB Local just use some random strings here, otherwise include your
   ;;; production IAM keys:
   :access-key access-key
   :secret-key secret-key

   ;;; You may optionally override the default endpoint if you'd like to use DDB
   ;;; Local or a different AWS Region (Ref. http://goo.gl/YmV80o), etc.:
   ;; :endpoint "http://localhost:8000"                   ; For DDB Local
   ;; :endpoint "http://dynamodb.eu-west-1.amazonaws.com" ; For EU West 1 AWS region
   })

;;; hope this gets done...
;(println [:tables (far/list-tables client-ops)])

(defn write-zip [user zip]
  (far/put-item client-opts :congress_user_prefs
                {:user_id user :zip zip}))

(defn read-zip [user]
  (:zip (far/get-item client-opts :congress_user_prefs {:user_id user})))
