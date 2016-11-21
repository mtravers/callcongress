(ns callcongress.set-zip-intent
  (:require [clojure.string :as str]
            [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.call-rep-intent :as call-rep-intent]
            [callcongress.dynfar :as dyn]
            )
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

(defn- mk-plain-speech [text]
  (doto (PlainTextOutputSpeech.) (.setText text)))

(defn- mk-plain-reprompt [text]
  (let [reprompt (Reprompt.)]
    (.setOutputSpeech reprompt (mk-plain-speech text))
    reprompt))

(defn set-session-slot [session slot value]
  (.setAttribute session slot value))

(defn user-id [session]
  (.getUserId (.getUser session)))

(defn say-zip [zip]
  (str/join " " (into [] zip)))

(defn numeric? [s]
  (every? #(and (>= (compare % \0) 0)
                (>= (compare \9 %) 0))
          s))

(defn clean-zip [zip]
  (cond (not (numeric? zip))
        nil
        (= 5 (count zip))
        zip
        ;; "to 90210" will get interpreted as "290210", this fixes
        (and (= 6 (count zip))
             (= \2 (first zip)))
        (subs zip 1)
        true false))

(defn set-zip [session session-map]
  (let [user (user-id session)
        zip (clean-zip (str (get session-map :Zip)))
        ;; TODO might as well return legislator
        speech (mk-plain-speech
                (if zip
                  (format "I have your zip as %s" (say-zip zip))
                  (format "Sorry I couldn't understand")))
        reprompt (mk-plain-reprompt
                  (if zip
                    "Call your congressman?"
                    "Try again"))]
    (dyn/write-zip user zip)
    (SpeechletResponse/newAskResponse speech reprompt)))

(defintent :SetZipIntent set-zip)


  
