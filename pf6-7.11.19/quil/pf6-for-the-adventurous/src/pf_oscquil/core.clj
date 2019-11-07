(ns pf_oscquil.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [overtone.osc :as osc]))

(def WIDTH 800)
(def HEIGHT 800)

;; pointfugue meetup 6 7.11.19


(def AMOUNT-OF-RECTS 32)
(def margin-vertical 30)
(def margin-horizontal 30)

(def CONTAINER-HEIGHT (- HEIGHT (* 2 margin-vertical)))
(def CONTAINER-WIDTH (- WIDTH (* 2 margin-horizontal )))
(def HEIGHT-OF-BAR (/ CONTAINER-HEIGHT AMOUNT-OF-RECTS))
(def VERTICAL-GAP 4)


(defn average-of-collection
  [collection]
  (/ (reduce + collection) (count collection)))

(defn partition-spectrum
  [spectrum amount-of-partitions]
  (let
    [ spectrum-size (count spectrum)
      spectrum-partitioned (partition (/ spectrum-size amount-of-partitions) spectrum)
      spectrum-averages (map q/abs (map average-of-collection spectrum-partitioned))
      spectrum-indexed (map vector (range) spectrum-averages)
      ]
    spectrum-indexed))


;; define minimum and maxium value to distort a vertex's x and y values
(def VERTEX-DISTORTION-MINMAX
  {
    :min -25
    :max 25
  })

;; create a variable that will keep sound analysis data
(def p5-analysis-state (atom {}))

; start an OSC server that will handle data from Processing
(def server (osc/osc-server 7000))

; add sound spectrum data to the state variable once they are received
(osc/osc-handle server "/processing/spectrum" (fn [msg] (swap! p5-analysis-state assoc :spectrum (:args msg))))

; add sound volume data to the state variable once they are received
(osc/osc-handle server "/processing/volume" (fn [msg] (swap! p5-analysis-state assoc :volume (:args msg))))


; the sketch
(defn setup-sketch []
  (q/frame-rate 30)
  (q/color-mode :hsb 100 100 100 100)
)

(defn draw-sketch [state]
  (q/no-stroke)

  (q/with-fill [0 0 10 15]
    (q/rect 0 0 WIDTH HEIGHT))

  (when (not (nil? (get @p5-analysis-state :spectrum)))
    (let
      [spectrum (get @p5-analysis-state :spectrum)
       spectrum-partitioned (partition-spectrum spectrum AMOUNT-OF-RECTS)
       hues (for [index (range AMOUNT-OF-RECTS)]
              (q/map-range index 0 (- AMOUNT-OF-RECTS 1) 0 14))
       saturations (for [spec spectrum-partitioned]
                     (let [spec-value (* (last spec) 10)]
                        (q/constrain (* 100 spec-value) 70 100)))
       scaled-rectangles (for
                           [spec spectrum-partitioned]
                             (let [
                                   spec-index (first spec)
                                   spec-value (* (last spec) 10)
                                   center-x margin-horizontal
                                   center-y (+ margin-vertical (* HEIGHT-OF-BAR spec-index))
                                   width (* CONTAINER-WIDTH (q/abs (q/sin (q/map-range spec-index 0 (- AMOUNT-OF-RECTS 1) 0 3.14159))))
                                   height (* (- HEIGHT-OF-BAR VERTICAL-GAP) spec-value )
                                   height-constrained (q/constrain height 0.5 (- HEIGHT-OF-BAR VERTICAL-GAP))
                                   rect [center-x center-y width height-constrained]]
                                   rect))]

      (doseq [index (range AMOUNT-OF-RECTS)]
        (let [hue (nth hues index)
              saturation (nth saturations index)
              rect (nth scaled-rectangles index)]
              (q/with-fill [hue saturation 100]
              (apply q/rect rect)))))))









(q/defsketch pf_oscquil
  :title ""
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup-sketch
  ; update-state is called on each iteration before draw-state.
 ; :update update-sketch
  :draw draw-sketch
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
