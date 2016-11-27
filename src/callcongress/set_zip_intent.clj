(ns callcongress.set-zip-intent
  (:require [clojure.string :as str]
            [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.dynfar :as dyn]
            )
  (:use callcongress.alexa-utils)
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

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

(defn prompt-for-zip [session session-map]
  (let [speech (mk-ssml-speech [:speak "I need to know your zip code. Please say, set my zip code to " [:say-as {:interpret-as "digits"} "11111"]])
        reprompt (mk-plain-reprompt "Sorry I couldn't understand")]
    (SpeechletResponse/newAskResponse speech reprompt)))

(defn set-zip [session session-map]
  (let [user (user-id session)
        zip (clean-zip (str (get session-map :Zip)))
        ;; TODO might as well return legislator
        speech (if zip
                 (mk-ssml-speech [:speak "I have your zip as " [:say-as {:interpret-as "digits"} zip]])
                 (mk-plain-speech "Sorry I couldn't understand"))
        reprompt (mk-plain-reprompt
                  (if zip
                    "Call your congressman?"
                    "Try again"))]
    (dyn/write-zip user zip)
    (SpeechletResponse/newAskResponse speech reprompt)))

(defintent :SetZipIntent set-zip)


  
