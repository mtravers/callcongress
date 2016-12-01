(ns callcongress.house
  (:require [clj-http.client :as client]
            [clojure.data.xml :as xml]
            [clojure.walk :as walk]
            [clojure.string :as str]
            [callcongress.utils :as u]
            ))

;;; The House actually publishes useful data about upcoming bills.

(def base "http://docs.house.gov")
(def house-floor-html-url "http://docs.house.gov/floor/")

(defn- house-contents [xml]
  (map (fn [elt]
         {:number (u/elt-sub-text :legis-num elt)
          :title (u/elt-sub-text :floor-text elt)})
       (u/find-elts #(= (:tag %) :floor-item) xml)))

(defn- get-bills-this-week []
  ;; the parse is very slow for some reason
  (let [html (:body (client/get house-floor-html-url))
        xml-url (second (re-matches #"(?s).*\"Download\.aspx\?file\=(.*?)\".*" html))
        xml-content (xml/parse-str (:body (client/get (str base xml-url))))]
    (house-contents xml-content)))

;;; A sequence of {:number .. :title ..} maps
;;; Note: this gets downloaded once and cached, under the assumption that the lifecycle of a lambda function server is very small compared how often this data changes.
(def bills-this-week (memoize get-bills-this-week))

(defn bill-n [n]
  (assoc (nth (bills-this-week) n) :n n))

(defn next-bill [n]
  (if (nil? n)
    (bill-n (rand-int (count (bills-this-week))))
    (bill-n (mod (+ 1 n) (count (bills-this-week))))))


  

