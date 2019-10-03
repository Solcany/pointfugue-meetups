(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf.pf_helpers :as pf]))

(def WIDTH 600)
(def HEIGHT 600)

(defn setup-sketch []
  (q/frame-rate 30)
  {:cat (q/load-image "cat.jpg")}
)

;; remove update-sketch code block

; (defn update-sketch [state]

; )

;; if you want to use this in the sketch we made today

(defn draw-sketch [state]
  (q/image (:cat state) (q/mouse-x) (q/mouse-y))
)



(q/defsketch pf
  :title "pointfugue meetup template"
  :size [WIDTH HEIGHT]
  :setup setup-sketch
  :draw draw-sketch
  ;; :update update-sketch ;; !! also remove it here !!
  :features [:keep-on-top]
  :middleware [m/fun-mode])
