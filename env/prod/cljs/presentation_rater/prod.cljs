(ns presentation-rater.prod
  (:require [presentation-rater.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
