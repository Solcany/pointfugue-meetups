(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def WIDTH 800)
(def HEIGHT 800)

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb 100 100 100)

)


(defn draw-state [state]
  (q/background 0)


)


(q/defsketch pf
  :title "pf empty template"
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
