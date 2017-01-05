(ns callcongress.set-zip-intent
  (:require [clojure.string :as str]
            [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.sunlight :as sunlight]
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
  (let [speech (mk-ssml-speech [:speak "I need to know your zip code. Please say, set my zip code to " ])  ; this is confusing [:say-as {:interpret-as "digits"} "11111"]
        reprompt (mk-plain-reprompt "Sorry I couldn't understand.")]
    (SpeechletResponse/newAskResponse speech reprompt)))

(defn set-zip [session session-map]
  (let [user (user-id session)
        zip (clean-zip (str (get session-map :Zip)))
        rep (and zip (sunlight/get-congressman zip))]
    (if zip
      (let [speech (mk-ssml-speech [:speak
                                    [:s "I have your zip as " [:say-as {:interpret-as "digits"} zip]]
                                    (if rep
                                      [:s "Your congressperson is " (sunlight/say-legislator rep)]
                                      [:s "I could not find a congressperson for that zip"])])]
        (dyn/write-zip user zip)
        ;; Note: I wanted this to be an Ask response that would then prompt to call, but Amazon rejected that idea...
        (SpeechletResponse/newTellResponse speech))
      (SpeechletResponse/newAskResponse (mk-plain-speech "Sorry I couldn't understand.")
                                        (mk-plain-reprompt
                                         "Try again?")))))

(defintent :SetZipIntent set-zip)


  
