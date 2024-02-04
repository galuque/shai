(ns shai.cli
  (:require [clojure.string :as str]
            [clojure.tools.cli :as cli]
            [shai.api :refer [generate-content get-api-key]]
            [shai.prompt :refer [parse-command]]
            [shai.util :refer [sh]])
  (:gen-class))

(set! *warn-on-reflection* true)

;; CLI
(def cli-options
  [["-h" "--help" "Show this message"]])

(defn validate-args [args]
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args cli-options)]
    (if (or (:help options) (seq errors))
      (do
        (println "Usage: shai [options] query")
        (println "The GOOGLE_AI_STUDIO_API_KEY environment variable must be set")
        (println "Options:")
        (println summary)
        (System/exit 0))
      (str/join " " arguments))))

(defn banner []
  (println "   _____ _           _    _____ _      _____ 
  / ____| |         (_)  / ____| |    |_   _|
 | (___ | |__   __ _ _  | |    | |      | |  
  \\___ \\| '_ \\ / _` | | | |    | |      | |  
  ____) | | | | (_| | | | |____| |____ _| |_ 
 |_____/|_| |_|\\__,_|_|  \\_____|______|_____|
"))

(defn -main
  "Executes a query to the generative language model
   And asks the user if it should execute the command"
  [& args]
  (banner)
  (when (nil? (get-api-key))
    (println "The GOOGLE_AI_STUDIO_API_KEY environment variable must be set")
    (System/exit 1))
  (let [query (validate-args args)]
    (if (nil? query)
      (println "No query provided")
      (let [_        (println "Generating content...")
            response (generate-content query)
            command  (parse-command response)]
        (if (nil? command)
          (println "No command found")
          (do
            (println "Command found:\n")
            (prn command)
            (println "\nExecute command? (y/n)")
            (let [input (-> (read-line)
                            (str/trim)
                            (str/lower-case))]
              (if (= input "y")
                (do (println)
                    (-> (sh command)
                        :out
                        (println)))
                (println "\nCommand not executed\n"))
              (shutdown-agents)
              (System/exit 0))))))))
