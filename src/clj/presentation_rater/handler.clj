(ns presentation-rater.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [presentation-rater.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [ring.middleware.json :refer [wrap-json-response]]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(def loading-page
  (html5
   [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))]
    [:body
     mount-target
     (include-js "/js/app.js")]))

(defonce votes
  (atom {:0 {:up 0 :down 0}}))

(defn json-response [body]
  {:body body
   :content-type "application/json"
   :status 200})

(defn increment-count [votes slide direction]
  (let [slide-key (keyword (str slide))]
    (if (get votes slide-key)
      (update-in votes [slide-key direction] inc)
      (let [votes-for-slide {:up 0 :down 0}]
        (assoc votes slide-key (update votes-for-slide direction inc))))))

(defn update-count [slide direction]
  (swap! votes increment-count slide direction))

(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/votes/:slide/" [slide]
    (json-response (get @votes (keyword slide) {:up 0 :down 0})))
  (POST "/votes/:slide/up" [slide]
    (update-count slide :up))
  (POST "/votes/:slide/down" [slide]
    (update-count slide :down))
  (resources "/")
  (not-found "Not Found"))

(def app
  (wrap-json-response (wrap-middleware #'routes) :pretty true))
