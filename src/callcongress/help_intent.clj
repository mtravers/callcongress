(ns callcongress.help-intent
  (:require [clojure.string :as str]
            [com.climate.boomhauer.intent-handler :refer [defintent]]
            )
  (:use callcongress.alexa-utils)
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt])  )

(defn help [session session-map]
  (let [speech (mk-plain-speech "You can ask me for an upcoming bill or how to call your representative.")
        reprompt (mk-plain-reprompt "Try again.")]
    (SpeechletResponse/newAskResponse speech reprompt)))

(defintent :HelpRepIntent help)
