(ns callcongress.call-rep-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.sunlight :as sunlight]
            [callcongress.reps :as reps]
            [callcongress.dynfar :as dyn]
            )
  (:use callcongress.alexa-utils)
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

(def last-bill-slot "LAST_BILL")

(defn user-zip [user]
  (dyn/read-zip user))

(defn representative-text [user chamber]
  (let [zip (user-zip user)
        legislators (filter #(= (:chamber %) chamber)
                            (sunlight/get-legislators zip))
        legislator(first legislators)]
    (if legislator
      (format "You can call %s %s at %s"
              (or (:nickname legislator)
                  (:first_name legislator))
              (:last_name legislator)
              (:phone legislator))
      "Sorry, I couldn't find a legislator")))

(defn call-rep [session session-map]
  (let [bill (sunlight/get-bill (get session-map (keyword last-bill-slot)))
        chamber (or (get session-map :Chamber)
                    (keyword (:chamber bill))
                    "house")
        user (user-id session)
        text (representative-text user chamber)
        speech (mk-plain-speech text)]
    (SpeechletResponse/newTellResponse speech)))

(defn call-senator [session session-map]
  (call-rep session (assoc session-map :Chamber "senate")))

(defintent :CallRepIntent call-rep)
(defintent :CallSenatorIntent call-senator)

