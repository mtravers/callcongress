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

(defn bill-ssml [bill]
  [:speak
   "The bill"
   ;; Wish there was a quote speech markup!
   [:s (:title bill)
    [:break {:strength "medium"}]
    [:say-as {:interpret-as "digits"} (:number bill)]]
   "is scheduled for consideration in the House this week."
   [:s "Would you like to call your representative?"]])   
    
(defn get-bills [session session-map]
  (let [bill (house/next-bill (u/safe-parse-integer (get session-map (keyword last-bill-slot))))
        speech (mk-ssml-speech (bill-ssml bill))
        reprompt (mk-plain-reprompt "Call your representative?")]
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
(defintent :AMAZON.CancelIntent stop)
  
