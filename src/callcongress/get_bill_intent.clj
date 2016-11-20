(ns callcongress.get-bill-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.call-rep-intent :as call-rep-intent]
            [callcongress.sunlight :as sunlight]
            )
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

(defn- mk-plain-speech [text]
  (doto (PlainTextOutputSpeech.) (.setText text)))

(defn- mk-plain-reprompt [text]
  (let [reprompt (Reprompt.)]
    (.setOutputSpeech reprompt (mk-plain-speech text))
    reprompt))

(def last-bill-slot "LAST_BILL")

(defn set-session-slot [session slot value]
  (.setAttribute session slot value))

(defn get-bills [session session-map]
  (let [bill (sunlight/next-bill (get session-map (keyword last-bill-slot)))
        speech (mk-plain-speech (sunlight/bill-text bill))
        reprompt (mk-plain-reprompt "Call your congressman?")
        ]
    (set-session-slot session last-bill-slot (sunlight/bill-id bill))
    (SpeechletResponse/newAskResponse speech reprompt)))

(defintent :WhatBillsIntent get-bills)

(defintent :AMAZON.NextIntent get-bills)

(defintent :AMAZON.NoIntent get-bills)

;;; In response to call question
(defintent :AMAZON.YesIntent call-rep-intent/call-rep)
  
