(ns mapquest_clj.core
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]))

(def mqurl "https://open.mapquestapi.com/geocoding/v1/address")
(def key-env-var "MAPQUEST_OPEN_KEY")
(def mqkey (System/getenv key-env-var))

(defn generate-get-options
  ([^String street-address
    ^String city
    ^String state
    ^String zip]
     (->> (generate-get-options street-address state zip)
          (#(assoc-in % [:query-params :city] city))))
  ([^String street-address
    ^String state
    ^String zip]
     (->> (generate-get-options street-address zip)
          (#(assoc-in % [:query-params :state] state))))
  ([^String street-address
    ^String zip]
     {:query-params {:key mqkey
                     :inFormat "kvp"
                     :outFormat "json"
                     :thumbMaps "false"
                     :street street-address
                     :postalCode zip}}))

(defn extract-lat-long [req]
  (let [r @req]
    (->> r
         (:body)
         (#(json/decode % true))
         (:results)
         (first)
         (:locations)
         (first)
         (:latLng))))

(defn query [^String street-address
             ^String zip]
  (let [opts (generate-get-options street-address zip)]
    (http/get mqurl opts)))

(defn print-and-error [^String msg]
  ((println msg)
   (System/exit 1)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (cond
   (or (nil? args)
       (not= 2 (count args)))
   (print-and-error
    (str "This program must be run with 2 arguments: mapquest_clj.core <street-address> <zip-code>"))
   (nil? (System/getenv key-env-var))
   (print-and-error
    (str "You must set an environment variable called: " key-env-var " with your mapquest open key."))
   (clojure.string/blank? (System/getenv key-env-var))
   (print-and-error
    (str "Your $" key-env-var " variable cannot be empty."))
   :else (let [street-address (first args)
               zip (second args)]
           (println
            @(query street-address zip)))))
