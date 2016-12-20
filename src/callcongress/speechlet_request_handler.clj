(ns callcongress.speechlet-request-handler
  (:gen-class
    :name CallCongress.SpeechletRequestHandler
    :extends com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler
    :init init
    :constructors {[] [com.amazon.speech.speechlet.Speechlet java.util.Set]})
  (:require [callcongress.get-bill-intent]
            [callcongress.call-rep-intent]
            [callcongress.set-zip-intent]
            [callcongress.help-intent])
  (:import [com.climate.boomhauer BoomhauerSpeechlet]))

(defn -init []
  [[(BoomhauerSpeechlet.
      {:launch-message "Hello. Welcome to Call Congress. You can ask me for an upcoming bill or how to call your representative."
       :card-title "Call Congress"}), #{}] nil])


