(ns callcongress.call-rep-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.sunlight :as sunlight]
            [callcongress.reps :as reps]
            [callcongress.dynfar :as dyn]
            [callcongress.set-zip-intent :as set-zip-intent]
            )
  (:use callcongress.alexa-utils)
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

(def last-bill-slot "LAST_BILL")

(defn user-zip [user]
  (dyn/read-zip user))

(defn representative-ssml [user chamber]
  (let [zip (user-zip user)
        legislators (filter #(= (:chamber %) chamber)
                            (sunlight/get-legislators zip))
        legislator (first legislators)]
    (if legislator
      [:speak
       "You can call"
       (or (:nickname legislator)
           (:first_name legislator))
       (:last_name legislator)
       [:say-as {:interpret-as "telephone"} (:phone legislator)]]
      [:speak "Sorry, I couldn't find a legislator"])))


(defn call-rep [session session-map]
  (let [bill (sunlight/get-bill (get session-map (keyword last-bill-slot)))
        chamber (or (get session-map :Chamber)
                    (keyword (:chamber bill))
                    "house")
        user (user-id session)
        zip (user-zip user)]
    (if zip
        (let [speech (mk-ssml-speech (representative-ssml user chamber))]
          (SpeechletResponse/newTellResponse speech))
        ;; no zip
        (set-zip-intent/prompt-for-zip session session-map))))
        
;;; TODO this hack apparently not working
(defn call-senator [session session-map]
  (call-rep session (assoc session-map :Chamber "senate")))

(defintent :CallRepIntent call-rep)
(defintent :CallSenatorIntent call-senator)

