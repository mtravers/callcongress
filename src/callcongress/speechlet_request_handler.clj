(ns callcongress.speechlet-request-handler
  (:gen-class
    :name CallCongress.SpeechletRequestHandler
    :extends com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler
    :init init
    :constructors {[] [com.amazon.speech.speechlet.Speechlet java.util.Set]})

  (:use [callcongress.get-bill-intent]
;        [callcongress.call-rep-intent]
                                       )

  (:import [com.climate.boomhauer BoomhauerSpeechlet]))

(defn -init []
  [[(BoomhauerSpeechlet.
      {:launch-message "Welcome to Call Congress"
       :card-title "Call Congress"}), #{}] nil])
