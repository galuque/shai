(ns shai.api
  (:require [babashka.http-client :as http]
            [babashka.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [shai.prompt :refer [get-system-prompt]]))

(set! *warn-on-reflection* true)

;; API key and URL

(defn get-api-key []
  (System/getenv "GOOGLE_AI_STUDIO_API_KEY"))

(def api-url "https://generativelanguage.googleapis.com/v1beta/")

;; API base helper functions

(defn- api-get [endpoint & headers]
  (let [headers (merge {"x-goog-api-key" (get-api-key)} headers)]
    (http/get (str api-url endpoint "/") {:headers headers})))


(defn- api-post
  ([endpoint data]
   (api-post endpoint data {:stream false}))
  ([endpoint data & {:keys [stream]}]
   (let [headers {"x-goog-api-key" (get-api-key)
                  "Content-Type" "application/json"}
         json    (json/write-str data)
         payload (if stream
                   {:headers headers
                    :body json
                    :as :stream}
                   {:headers headers
                    :body json})]
     (http/post (str api-url endpoint "/")
                payload))))

(defn get-models []
  (let [response (api-get "models")]
    (json/read-str (:body response) {:key-fn keyword})))

(def generation-config {:temperature 0.4
                        :maxOutputTokens 100
                        :topP 1
                        :topK 1})

(defn- get-text [response]
  (-> response :candidates (nth 0) :content :parts (nth 0) :text))

(defn generate-content [text]
  (let [endpoint "models/gemini-pro:generateContent"
        prompt   (get-system-prompt)
        payload  {:contents [{:parts [{:text (str prompt text)}]}]
                  :generationConfig generation-config}
        response (api-post endpoint payload)
        body     (json/read-str (:body response) {:key-fn keyword})]
    (get-text body)))

(defn stream-generate-content [text]
  (let [endpoint "models/gemini-pro:streamGenerateContent"
        prompt   (get-system-prompt)
        payload  {:contents [{:parts [{:text (str prompt text)}]}]
                  :generationConfig generation-config}
        response (api-post endpoint payload :stream true)]
    (with-open [rdr (-> (:body response)
                        (io/reader))]
      (str/join (doall (for [doc (json/read rdr {:key-fn keyword})]
                         (get-text doc)))))))
