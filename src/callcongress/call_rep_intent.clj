(ns callcongress.call-rep-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.sunlight :as sunlight]
            [callcongress.reps :as reps]
            )
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

;;; This stuff should be in util library
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

(def last-bill-slot "LAST_BILL")

(defn call-rep [session session-map]
  (let [bill (sunlight/get-bill (get session-map (keyword last-bill-slot)))
        chamber (keyword (:chamber bill))
        user (user-id session)
        text (reps/representative-text user chamber)
        speech (mk-plain-speech text)]
    (SpeechletResponse/newTellResponse speech)))

(defintent :CallRepIntent call-rep)

