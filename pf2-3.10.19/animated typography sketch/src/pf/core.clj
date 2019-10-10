(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf.pf_helpers :as pf]))

(def WIDTH 700)
(def HEIGHT 700)
(def SAVE-FRAME? false)

(defn setup-sketch []
  (q/frame-rate 30)
  (q/background 50 100 100 100)
  (q/color-mode :hsb 100 100 100)
  ;; change to absolute name of font installed on you computer
  ;; find absolute name on mac os:
  ;; 1. open finder
  ;; 2. right click on desired font
  ;; 3. open in finder
  ;; 4. copy the absolute name of the font without the suffix(ttf, otf etc.)
  (let [font (q/create-font "FiraSans-SemiBold" 16 true)]
    (q/text-font font))
  (q/text-size 120)
  (q/fill 15 100 100)
  (q/text-align :center :center)
)

(defn update-sketch [state]

)

(defn draw-sketch [state]
  (q/fill 15 100 100 10)
  (q/rect 0 0 (q/width) (q/height))
  ;; (let construct let's you create a temporary named variable
  ;; (pf/pulse expects a value that you want to 'pulse'
  ;; and a number than will make the pulse slower or faster
  ;; values between 0.1 and 0.001 work the best
  (let [pulse-value (pf/pulse 2.0 0.003)]
    (q/text-size 120)
    ;; move the drawing canvas to where the mouse is
    (q/translate (q/mouse-x) (q/mouse-y))
    ;; rotate the whole canvas
    (q/rotate (* (q/millis) 0.01))
    ;; scale the canvas
    (q/scale pulse-value)
    (q/fill (q/random 0 5) 100 100)
    (q/text "PULSE" 50 50))
    (when SAVE-FRAME? (q/save-frame "pulse-[###].jpg"))
)

(q/defsketch pf
  :title "pointfugue meetup 2"
  :size [WIDTH HEIGHT]
  :setup setup-sketch
  :update update-sketch
  :draw draw-sketch
  :features [:keep-on-top]
  :middleware [m/fun-mode])
