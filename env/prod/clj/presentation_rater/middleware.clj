(ns presentation-rater.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-json-response :pretty true)
      (wrap-defaults site-defaults)))
