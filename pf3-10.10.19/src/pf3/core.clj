(ns pf3.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf3.pf_helpers :as pf]))

(def WIDTH 600)
(def HEIGHT 600)

;; do you want to save frames of animation?
(def SAVE-FRAME? false)

(defn setup-sketch []
  (q/frame-rate 30)
  (q/color-mode :hsb 100 100 100)

)

(defn update-sketch [state]

)

(defn draw-sketch [state]
  (q/background 10 100 100)

  ;; define 2 layers that will store our drawings separately
  ;; one for drawing the other for a mask
  (let [drawing-layer (q/create-graphics WIDTH HEIGHT)
        mask-layer (q/create-graphics WIDTH HEIGHT)]

       ;; (q/with-graphics tells Quil that we want to draw
       ;; to a specific layer, in this case 'drawing-layer' layer
       (q/with-graphics drawing-layer
         ;; every layer needs to be set up separately
         ;; for example: we need to set color mode for every
         ;; layer individually
         (q/color-mode :hsb 100 100 100)
         (q/background 0 100 100)
         (q/fill 0)
         (q/text-align :center :center)
         (q/text-size 500)
         (q/text "@" (/ WIDTH 2) (/ HEIGHT 2)))

      ;; before we start drawing into 'mask-layer' layer
      ;; we define some variables that we will use to animate
      ;; pf/pulse oscillates between 0 and the first value we provide
      ;; the second value slows down the oscillation
      (let [pulsing-width (pf/pulse 200 0.005)
            pulsing-height (pf/pulse 300 0.003)
            pulsing-width2 (pf/pulse 300 0.003)
            pulsing-height2 (pf/pulse 200 0.005)
            pulsing-stroke-weight (pf/pulse 25 0.003)
            pulsing-stroke-weight2 (pf/pulse 35 0.001)]

      ;; now we start drawing into the mask layer
      (q/with-graphics mask-layer
         (q/color-mode :hsb 100 100 100 100)
         (q/stroke 15)
         (q/no-fill)
         (q/rect-mode :center)
         (q/stroke-weight pulsing-stroke-weight2)
         ;; move this layer's origin (x: 0, y: 0)
         ;; by WIDTH / 2 and HEIGHT / 2
         ;; reason for this is that we want to rotate the rects we draw later around their centers
         (q/translate (/ WIDTH 2) (/ HEIGHT 2))
         ;; (q/rotate expects value in radians (number between 0 and PI * 2)
         ;; in this case we give it milliseconds since the start of our program
         ;; divided by 1000
         (q/rotate ( / (q/millis) 1000))
         (q/rect 0 0 pulsing-width pulsing-height)
         (q/stroke-weight pulsing-stroke-weight)
         (q/rect 0 0 pulsing-width2 pulsing-height2)))

      ;; we want to use the 'mask-layer' as a mask for 'drawing-layer'
      ;; Whatever is drawn in mask-layer will make pixels in drawing-layer visible
      ;; Transparent(or empty) pixels of mask-layer will hide pixels of the drawing-layer
      (q/mask-image drawing-layer mask-layer)

      ;; We finished drawing into both layers and applied the mask.
      ;; but the drawing is only stored in the computer's memory, not drawn to the screen yet.
      ;; we draw it to the screen by (q/image function
      (q/image drawing-layer 0 0)

      (when SAVE-FRAME? (q/save-frame "frame-[###].png"))
    ))


(q/defsketch pf3
  :title "pointfugue meetup template"
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup-sketch
  ; update-state is called on each iteration before draw-state.
  :update update-sketch
  :draw draw-sketch
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
