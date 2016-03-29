(ns presentation-rater.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [cljs-http.client :as http]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; Views

(defn submit-vote
  [slide direction]
  (let [url (str "/votes/" slide "/" (name direction))]
    (http/post url {})))

(defn update-slide
  [slide e]
  (reset! slide (.-target.value e))
  (println @slide))

(def home-page
  (let [slide (atom -1)]
    (fn []
      [:div [:h2 "Boom it's a presentation-rater"]
       [:form
        [:input {:type "text" :value @slide :on-change (partial update-slide slide)}]
        [:input {:type "button" :value "Up" :on-click #(submit-vote 1 :up)}]
        [:input {:type "button" :value "Down" :on-click #(submit-vote 1 :down)}]]
       [:div [:a {:href "/about"} "go to about page"]]])))

(defn about-page []
  [:div [:h2 "About presentation-rater"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
