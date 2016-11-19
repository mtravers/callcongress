(ns callcongress.get-bill-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.sunlight :as sunlight]
            )
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech]))

(defn- mk-plain-speech [text]
  (doto (PlainTextOutputSpeech.) (.setText text)))

(defn get-bills [session session-map]
  (let [speech (mk-plain-speech (sunlight/get-some-text))]
    (SpeechletResponse/newTellResponse speech)))

(defintent :WhatBillsIntent get-bills)
