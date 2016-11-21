(ns callcongress.call-rep-intent
  (:require [com.climate.boomhauer.intent-handler :refer [defintent]]
            [callcongress.sunlight :as sunlight]
            [callcongress.reps :as reps]
            [callcongress.dynfar :as dyn]
            )
  (:import [com.amazon.speech.speechlet SpeechletResponse]
           [com.amazon.speech.ui PlainTextOutputSpeech Reprompt]))

;;; This stuff should be in util library
(defn- mk-plain-speech [text]
  (doto (PlainTextOutputSpeech.) (.setText text)))

(defn- mk-plain-reprompt [text]
  (let [reprompt (Reprompt.)]
    (.setOutputSpeech reprompt (mk-plain-speech text))
    reprompt))

(defn set-session-slot [session slot value]
  (.setAttribute session slot value))

(defn user-id [session]
  (.getUserId (.getUser session)))

(def last-bill-slot "LAST_BILL")

(defn user-zip [user]
  (dyn/read-zip user))

;;; for demo
(def default-legislator
  {:contact_form "https://forms.house.gov/speier/webforms/email_jackie.shtml",
  :votesmart_id 8425,
  :party "D",
  :fax "202-347-0956",
  :first_name "Jackie",
  :in_office true,
  :bioguide_id "S001175",
  :youtube_id "jackiespeierca12",
  :govtrack_id "412259",
  :phone "202-225-3531",
  :oc_email "Rep.Speier@opencongress.org",
  :nickname nil,
  :chamber "house",
  :birthday "1950-05-14",
  :name_suffix nil,
  :twitter_id "RepSpeier",
  :state "CA",
  :middle_name nil,
  :facebook_id "99332606976",
  :title "Rep",
  :crp_id "N00029649",
  :office "2465 Rayburn House Office Building",
  :state_name "California",
  :icpsr_id 20762,
  :term_start "2015-01-06",
  :fec_ids ["H8CA12171"],
  :term_end "2017-01-03",
  :website "http://speier.house.gov",
  :last_name "Speier",
  :thomas_id "01890",
  :gender "F",
  :ocd_id "ocd-division/country:us/state:ca/cd:14",
  :district 14})


(defn representative-text [user chamber]
  (let [zip (user-zip user)
        legislators (filter #(= (:chamber %) chamber)
                            (sunlight/get-legislators zip))
        legislator (or (first legislators)
                       default-legislator)]
    (format "You can call %s %s at %s"
            (or (:nickname legislator)
                (:first_name legislator))
            (:last_name legislator)
            (:phone legislator))))

(defn call-rep [session session-map]
  (let [bill (sunlight/get-bill (get session-map (keyword last-bill-slot)))
        chamber (or (get session-map :Chamber)
                    (keyword (:chamber bill))
                    "house")
        user (user-id session)
        text (representative-text user chamber)
        speech (mk-plain-speech text)]
    (SpeechletResponse/newTellResponse speech)))

(defn call-senator [session session-map]
  (call-rep session (assoc session-map :Chamber "senate")))

(defintent :CallRepIntent call-rep)
(defintent :CallSenatorIntent call-senator)

