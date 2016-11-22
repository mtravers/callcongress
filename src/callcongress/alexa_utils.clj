(ns callcongress.alexa-utils
  (:require [clojure.data.xml :as xml])
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech SsmlOutputSpeech Reprompt]))

(defn mk-plain-speech [text]
  (doto (PlainTextOutputSpeech.) (.setText text)))

;;; Takes ssml in [:speak {} "text"] version of xml
(defn mk-ssml-speech [ssml]
  (doto (SsmlOutputSpeech.) (.setSsml (xml/emit-str (xml/sexp-as-element ssml)))))

(defn mk-plain-reprompt [text]
  (doto (Reprompt.) (.setOutputSpeech (mk-plain-speech text))))

(defn set-session-slot [session slot value]
  (.setAttribute session slot value))

(defn user-id [session]
  (.getUserId (.getUser session)))
