(ns callcongress.get-bill-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.call-rep-intent :as call-rep-intent]
            [callcongress.sunlight :as sunlight]
            )
  (:use callcongress.alexa-utils)
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

(def last-bill-slot "LAST_BILL")

(defn bill-text [bill]
  (format "In %s days the bill %s will go before the %s. Would you like to call your representative?"
          (+ 1 (rand-int 6))            ;cheating
          (sunlight/bill-text bill)
          (:chamber bill)))

(defn get-bills [session session-map]
  (let [bill (sunlight/next-bill (get session-map (keyword last-bill-slot)))
        text (bill-text bill)
        speech (mk-plain-speech text)
        reprompt (mk-plain-reprompt "Call your congressman?")
        ]
    (set-session-slot session last-bill-slot (sunlight/bill-id bill))
    (SpeechletResponse/newAskResponse speech reprompt)))

(defintent :WhatBillsIntent get-bills)

(defintent :AMAZON.NextIntent get-bills)

(defintent :AMAZON.NoIntent get-bills)

;;; In response to call question
(defintent :AMAZON.YesIntent call-rep-intent/call-rep)
  
