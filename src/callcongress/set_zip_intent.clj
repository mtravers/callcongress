(ns callcongress.set-zip-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
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

(defn set-zip [session session-map]
  (let [user (user-id session)
        zip (str (get session-map :Zip)) ;TODO leading zeros
        ;; TODO might as well return legislator
        speech (mk-plain-speech (format "I have your zip as %s" zip))
        reprompt (mk-plain-reprompt "Call your congressman?")
        ]
    (dyn/write-zip user zip)
    (SpeechletResponse/newAskResponse speech reprompt)))

(defintent :SetZipIntent set-zip)


  
