(ns callcongress.get-bill-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.call-rep-intent :as call-rep-intent]
            [callcongress.house :as house]
            [callcongress.utils :as u]
            )
  (:use callcongress.alexa-utils)
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

(def last-bill-slot "LAST_BILL")

(defn bill-text [bill]
  (format "%s %s is scheduled to before the house soon. Would you like to call your representative?"
          (:text bill)
          (:number bill)))

(defn get-bills [session session-map]
  (let [bill (house/next-bill (u/safe-parse-integer (get session-map (keyword last-bill-slot))))
        text (bill-text bill)
        speech (mk-plain-speech text)
        reprompt (mk-plain-reprompt "Call your congressman?")
        ]
    (set-session-slot session last-bill-slot (:n bill))
    (SpeechletResponse/newAskResponse speech reprompt)))

(defn stop [session session-map]
  (let [speech (mk-plain-speech "Thanks for being politically involved. Goodbye")]
    (SpeechletResponse/newTellResponse speech)))

(defintent :WhatBillsIntent get-bills)

(defintent :AMAZON.NextIntent get-bills)

(defintent :AMAZON.NoIntent get-bills)

;;; In response to call question
(defintent :AMAZON.YesIntent call-rep-intent/call-rep)

(defintent :AMAZON.StopIntent stop)
  
