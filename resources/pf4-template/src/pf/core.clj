(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf.pf_helpers :as pf]))

(def WIDTH 600)
(def HEIGHT 800)


(defn setup-sketch []

)

(defn update-sketch [state]


)

(defn draw-sketch [state]


)




(q/defsketch pf
  :title "pointfugue meetup template"
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup-sketch
  :update update-sketch
  :draw draw-sketch

  ; update-state is called on each iteration before draw-state.
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
