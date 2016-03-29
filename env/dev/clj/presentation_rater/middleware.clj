(ns presentation-rater.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defn logging [handler]
  (fn [request]
    (println "hello")
    (handler request)))

(defn wrap-middleware [handler]
  (-> handler
      wrap-json-response
      wrap-exceptions
      wrap-reload))
