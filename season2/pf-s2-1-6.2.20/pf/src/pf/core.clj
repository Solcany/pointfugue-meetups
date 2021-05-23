(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def WIDTH 600)
(def HEIGHT 900)
(def AMOUNT-OF-ELLIPSES 300)


(defn setup []
  (q/frame-rate 1)
  (q/color-mode :hsb 100 100 100 100)

)


(defn draw-state [state]
  (q/no-fill)

;;   (q/with-fill [0 0 10 80]
;;     (q/rect 0 0 WIDTH HEIGHT))
;;     (q/fill 100)
   (q/background 1 0 15) ; hue saturation brightness

    (let [ellipses (for [e (range AMOUNT-OF-ELLIPSES)]
                     [(q/random WIDTH) (q/random HEIGHT)
                      (q/random WIDTH) (q/random HEIGHT)
                      (q/random WIDTH) (q/random HEIGHT)])
      ]

      (doseq [ellipse ellipses]
        (let [radius (q/random 10 100)]
          (q/with-stroke [0 0 (q/random 80 90)]
          (apply q/triangle ellipse))
          (q/with-fill [0 0 0]
            (q/triangle 0 0 WIDTH 0 300 700))
            )))
       (q/fill 100 0 100)
       (q/text-size 138)
       (q/text "PARASITE" -15 98))





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
